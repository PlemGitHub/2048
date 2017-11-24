package Mech;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import Visual.*;
import Tiles.*;
import Threads.*;

public class Logic implements Constants, KeyListener, KeyDictionary, ActionListener{
	
	public Screen scr;
	public Tile[][] arrTile = new Tile[tCells][tCells];
	public boolean turnDone;
	private ArrayList<TileToMove> arrToMove = new ArrayList<>();
	private TileGeneration tGen;
	private TileMovement tMove;
	public int gameScore;
	private GameThread gameThread;
	
	
	public Logic(Screen scr) {
		this.scr = scr;
		generateArrPoints();
		gameThread = new GameThread(this);
		gameThread.start();
	}
	
	/**
	 * Method generates points which are representing tiles possible positions
	 */
	private void generateArrPoints() {
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++){
				arrPoint[i][j] = new Point();
				arrPoint[i][j].setLocation(tSize+(tSize+tGap)*i, tSize+(tSize+tGap)*j);				
			}
	}

	/**
	 * Method creates new Tile on game field
	 */
	public void createNewTile() {
		boolean creationDone = false;
		
		if (checkEmptySpace())
			do {
				Random rnd = new Random();
				int i = rnd.nextInt(tCells);
				int j = rnd.nextInt(tCells);
				if (arrTile[i][j] == null){
					arrTile[i][j] = new Tile();
					tGen = new TileGeneration(this, arrTile[i][j], arrPoint[i][j]);
					tGen.start();
					do {
						Thread.yield();
					} while (!tGen.creationDone);
					
					creationDone = true;
				}
			} while (!creationDone);
	}

	/**
	 * Method checks array of Tiles to find empty space for createNewTile()
	 * @return True - at least one null in arrTile[][]. 
	 * <br> False - no nulls in arrTile[][]
	 */
	private boolean checkEmptySpace() {
		for (int i = 0; i < tCells; i++) {
			for (int j = 0; j < tCells; j++) {
				if (arrTile[i][j] == null)
					return true;
			}
		}
		return false;
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent arg0) {}
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar()==27)
			System.exit(0);
		char c = KeyDictionary.keyTranslation(e.getKeyChar());
		switch (c) {
		case 'w': goUp(); break;
		case 'a': goLeft(); break;
		case 's': goDown(); break;
		case 'd': goRight(); break;
		}
	}

	private void goUp() {
		for (int j = 0; j < tCells; j++)
			for (int i = 0; i < tCells; i++)
				go(i, j, 0, -1);

		tMove = new TileMovement(scr, this, 0, -1, arrToMove);
		tMove.start();
	}

	private void goLeft() {
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++)
				go(i, j, -1, 0);

		tMove = new TileMovement(scr, this, -1, 0, arrToMove);
		tMove.start();
	}

	private void goDown() {
		for (int j = tCells-1; j >=0; j--)
			for (int i = 0; i < tCells; i++) 
				go(i, j, 0, 1);
		tMove = new TileMovement(scr, this, 0, 1, arrToMove);
		tMove.start();
	}

	private void goRight() {
		for (int i = tCells-1; i >=0; i--)
			for (int j = 0; j < tCells; j++) 
				go(i, j, 1, 0);
		tMove = new TileMovement(scr, this, 1, 0, arrToMove);
		tMove.start();
	}
	
	private void go(int i, int j, int dX, int dY){
		if (arrTile[i][j] != null){
			TileToMove tileToMove = new TileToMove();
			tileToMove.tile = arrTile[i][j];
			
			tileToMove = checkCollision(tileToMove, i, j, dX, dY);
			
			if (tileToMove.removeTile){
				Tile toDouble = arrTile[tileToMove.destI][tileToMove.destJ];
				int newScore = toDouble.getScore()*2;
				toDouble.setScore(newScore);
				setGameScore(Integer.parseInt(scr.scoreLbl.getText())+newScore);
			}
			
			if (!tileToMove.selfCollision){				// if no selfCollision - move Tile in arrTile
				arrToMove.add(tileToMove);
				if (!tileToMove.removeTile)
					arrTile[tileToMove.destI][tileToMove.destJ] = arrTile[i][j];
				arrTile[i][j] = null;
			}
		}	
	}
	
	private TileToMove checkCollision(TileToMove tileToMove, int i, int j, int dI, int dJ){
		int oldI = i;
		int oldJ = j;
		tileToMove.selfCollision = false;
		tileToMove.removeTile = false;
		for (int k = 0; k < 3; k++){			// check row or column to see tiles' collision
				i = i+dI;
				i = (i < 0)|(i > tCells-1)? i-dI:i;
				j = j+dJ;
				j = (j < 0)|(j > tCells-1)? j-dJ:j;
				
				if (arrTile[i][j] != null){
					if (arrTile[i][j] != tileToMove.tile){	//// if collision with another Tile
						if (arrTile[i][j].getScore() != tileToMove.tile.getScore()){	// if different scores
							i-=dI;	// return previous
							j-=dJ;	// indexes values
							if (i == oldI && j == oldJ)
								tileToMove.selfCollision = true;;
						}else{															// if scores equal
							tileToMove.removeTile = true;
						}
					}else{									//// if "self-collision"
						tileToMove.selfCollision = true;
					}
				break;
				}
			}

		tileToMove.destI = i;		// need to modify
		tileToMove.destJ = j;		// arrTile array
		tileToMove.destPoint = arrPoint[i][j];
		return tileToMove;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(scr.newGameBtn)){
			gameThread.interrupt();
			arrToMove.clear();
			setGameScore(0);
			
			for (Component t : scr.mp.getComponents())
				if (t.getName() != null && t.getName().equals("Tile"))
					scr.mp.remove(t);

			for (int i = 0; i < tCells; i++)
				for (int j = 0; j < tCells; j++)
					arrTile[i][j] = null;
			
			gameThread = new GameThread(this);
			gameThread.start();
			scr.mp.repaint();
		}
	}

	public void setGameScore(int i) {
		gameScore = i;
		scr.scoreLbl.setText(Integer.toString(gameScore));
	}
}