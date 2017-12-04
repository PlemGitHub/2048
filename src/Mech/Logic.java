package Mech;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import Logger.Logger;
import Visual.*;
import Tiles.*;
import Threads.*;

public class Logic implements Constants, KeyListener, KeyDictionary, ActionListener{
	
	public Screen scr;
	public HighScoreWriter hsw;
	private GameThread gameThread;
	private TileGeneration tGen;
	private TileMovement tMove;
	public Logger logger;
	public Tile[][] arrTile = new Tile[tCells][tCells];
	private ArrayList<TileToMove> arrToMove = new ArrayList<>();
	public boolean turnDone;
	public int gameScore;
	private int undoGameScore;
	private int undoDX = 0, undoDY = 0;
	public boolean undoSameSideMovement;
	private int undoNewTileI, undoNewTileJ;
	private int undoNewTileScore;
	private Tile[][] undoArrTile = new Tile[tCells][tCells];
	private int[][] undoArrTileScores = new int[tCells][tCells];
	/**
	 * undoQuantity decreases by 1 each turn from 2 (after Undo button click) to 0. <br>
	 * If it comes to 0 - button enables again.
	 */
	private int undoQuantity;
	
	public Logic(Screen scr) {
		this.scr = scr;
		generateArrPoints();
		hsw = new HighScoreWriter(this);
		gameThread = new GameThread(this);
		logger = new Logger(this);
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
		scr.surrendBtn.setEnabled(true);
		
		if (checkEmptySpace())
			do {
				Random rnd = new Random();
				int i = rnd.nextInt(tCells);
				int j = rnd.nextInt(tCells);
				if (arrTile[i][j] == null){
					arrTile[i][j] = new Tile();
					undoNewTileI = i;
					undoNewTileJ = j;
					undoNewTileScore = arrTile[i][j].getScore();
					tGen = new TileGeneration(this, arrTile[i][j], arrPoint[i][j]);
					tGen.start();
					do {
						Thread.yield();
					} while (!tGen.creationDone);
					
					creationDone = true;
					logger.writeNewTile(i, j, arrTile[i][j].getScore());
					logger.writeTiles();
				}
			} while (!creationDone);
		
		if (!checkEmptySpace() && noMoreMoves()){
			JOptionPane.showMessageDialog(scr.fr, "Нет больше ходов! Конец игры.");
			hsw.addHighScoresToFile(gameScore);
		}
	}
	
	/**
	 * Method creates new Tile on the same place if direction equals one before Undo btn pressed
	 * @param newTileI - saved index
	 * @param newTileJ - saved index
	 */
	public void createNewTileAtTheSamePlace() {
		scr.surrendBtn.setEnabled(true);
		int i = undoNewTileI;
		int j = undoNewTileJ;
			arrTile[i][j] = new Tile();
			arrTile[i][j].setScore(undoNewTileScore);
			tGen = new TileGeneration(this, arrTile[i][j], arrPoint[i][j]);
			tGen.start();
			do {
				Thread.yield();
			} while (!tGen.creationDone);
			undoSameSideMovement = false;
			logger.writeNewTile(i, j, arrTile[i][j].getScore());
			logger.writeTiles();
	}

	private boolean noMoreMoves() {
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells-1; j++)
				if (arrTile[i][j].getScore() == arrTile[i][j+1].getScore())
						return false;
		for (int i = 0; i < tCells-1; i++)
			for (int j = 0; j < tCells; j++)
				if (arrTile[i][j].getScore() == arrTile[i+1][j].getScore())
						return false;
	return true;
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
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		char c = ' ';
		switch (code) {
			case KeyEvent.VK_UP: c = 'w'; break;
			case KeyEvent.VK_LEFT: c = 'a'; break;
			case KeyEvent.VK_DOWN: c = 's'; break;
			case KeyEvent.VK_RIGHT: c = 'd'; break;
		}
		logger.writeKey(c);
		switch (c) {
			case 'w': if (scr.upBtn.isEnabled())
						if (checkSideMove(0, -1))	goUp(0, -1);
						else logger.writeNoMoveAfterKeyPressed(c); break;
			case 'a': if (scr.upBtn.isEnabled())
						if (checkSideMove(-1, 0))	goLeft(-1, 0);
						else logger.writeNoMoveAfterKeyPressed(c); break;
			case 's': if (scr.upBtn.isEnabled())
						if (checkSideMove(0, 1))	goDown(0, 1);
						else logger.writeNoMoveAfterKeyPressed(c); break;
			case 'd': if (scr.upBtn.isEnabled())
						if (checkSideMove(1, 0))	goRight(1, 0);
						else logger.writeNoMoveAfterKeyPressed(c); break;
		}
	}
	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 27)
			doOnClose();
		char c = KeyDictionary.keyTranslation(e.getKeyChar());
		logger.writeKey(c);		
		switch (c) {
			case 'w': if (scr.upBtn.isEnabled())
						if (checkSideMove(0, -1))	goUp(0, -1);
						else logger.writeNoMoveAfterKeyPressed(c); break;
			case 'a': if (scr.upBtn.isEnabled())
						if (checkSideMove(-1, 0))	goLeft(-1, 0);
						else logger.writeNoMoveAfterKeyPressed(c); break;
			case 's': if (scr.upBtn.isEnabled())
						if (checkSideMove(0, 1))	goDown(0, 1);
						else logger.writeNoMoveAfterKeyPressed(c); break;
			case 'd': if (scr.upBtn.isEnabled())
						if (checkSideMove(1, 0))	goRight(1, 0);
						else logger.writeNoMoveAfterKeyPressed(c); break;
		}
	}

	/**
	 * Checks if specific side movement is available
	 * @param dI
	 * @param dJ
	 * @return
	 */
	private boolean checkSideMove(int dI, int dJ) {
		
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++){
				int nextI = i+dI;
				int nextJ = j+dJ;
				if (nextI < 0 || nextI > tCells-1 || nextJ < 0 || nextJ > tCells-1)
					continue;
				Tile baseTile = arrTile[i][j];
				
				// if cell is empty - there is no Tile. Looking for more.
				if (baseTile == null)
					continue;
				
				// if next Tile is empty - u can move
				if (arrTile[nextI][nextJ] == null)
					return true;
				
				// if next Tile isn't empty and scores are equal
				if (arrTile[nextI][nextJ].getScore() == baseTile.getScore())
					return true;
			}
		return false;
	}

	private void goUp(int dX, int dY) {
		doSnapshot(dX, dY);
		for (int j = 0; j < tCells; j++)
			for (int i = 0; i < tCells; i++)
				go(i, j, dX, dY);

		tMove = new TileMovement(scr, this, dX, dY, arrToMove);
		tMove.start();
	}

	private void goLeft(int dX, int dY) {
		doSnapshot(dX, dY);
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++)
				go(i, j, dX, dY);

		tMove = new TileMovement(scr, this, dX, dY, arrToMove);
		tMove.start();
	}

	private void goDown(int dX, int dY) {
		doSnapshot(dX, dY);
		for (int j = tCells-1; j >=0; j--)
			for (int i = 0; i < tCells; i++) 
				go(i, j, dX, dY);
		tMove = new TileMovement(scr, this, dX, dY, arrToMove);
		tMove.start();
	}

	private void goRight(int dX, int dY) {
		doSnapshot(dX, dY);
		for (int i = tCells-1; i >=0; i--)
			for (int j = 0; j < tCells; j++) 
				go(i, j, dX, dY);
		tMove = new TileMovement(scr, this, dX, dY, arrToMove);
		tMove.start();
	}
	
	/**
	 * Main movement calculation method
	 * @param i - Tile's indexes
	 * @param j - Tile's indexes
	 * @param dX - defines direction (0, 1)
	 * @param dY - defines direction (0, 1)
	 */
	private void go(int i, int j, int dX, int dY){
		if (undoQuantity == 0)
			scr.undoBtn.setEnabled(true);
		
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
				if (!tileToMove.removeTile){
					arrTile[tileToMove.destI][tileToMove.destJ] = arrTile[i][j];
				}
				arrTile[i][j] = null;
			}
		}	
	}
	
	/**
	 * Universal method to check Tiles collision in specific direction
	 * @param tileToMove
	 * @param i - Tile's indexes
	 * @param j - Tile's indexes
	 * @param dI - defines direction (0, 1)
	 * @param dJ - defines direction (0, 1)
	 * @return tileToMove object with upgraded "selfCollision", "removeTile" and "destPoint" fields
	 */
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
				
				Tile tileToCheck = arrTile[i][j];
				if (tileToCheck != null){
					if (tileToCheck != tileToMove.tile){	//// if collision with another Tile
						if (tileToCheck.getScore() != tileToMove.tile.getScore()){	// if different scores
							i-=dI;	// return previous
							j-=dJ;	// indexes values
							if (i == oldI && j == oldJ)
								tileToMove.selfCollision = true;
						}else{														// if scores equal
							tileToMove.removeTile = true;
						}
					}else{									//// if "self-collision"
						tileToMove.selfCollision = true;
					}
				break;
				}
			}

		tileToMove.destI = i;		// need to modify arrTile array
		tileToMove.destJ = j;		// in go() method when scores 
		tileToMove.destPoint = arrPoint[i][j];
		return tileToMove;
	}
	
	/**
	 * Saves game field, game score, movement side and NewTile generation point to do "undo" action
	 * @param dX - defines side move direction (0, 1)
	 * @param dY - defines side move direction (0, 1)
	 */
	private void doSnapshot(int dX, int dY) {
		// game field
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++){
				undoArrTile[i][j] = null;
				if (arrTile[i][j] != null){
					undoArrTile[i][j] = arrTile[i][j];
					undoArrTileScores[i][j] = arrTile[i][j].getScore();
				}
			}
		
		// game score
		undoGameScore = gameScore;
		
		// movement side and NewTile generation point
		if (undoQuantity == 2)	// if Undo button just pressed
			if (undoDX == dX && undoDY == dY){	// if side move after Undo action is the same
				undoSameSideMovement = true;
			}
		undoDX = dX;
		undoDY = dY;

		undoQuantity = undoQuantity > 0? undoQuantity-1:undoQuantity;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * NEW GAME
		 */
		if (e.getSource().equals(scr.newGameBtn)){
			arrToMove.clear();
			setGameScore(0);
			undoDX = 0;
			undoDY = 0;
			undoGameScore = 0;
			
			clearTilesFromField();
			scr.upBtn.setEnabled(true);
			scr.leftBtn.setEnabled(true);
			scr.downBtn.setEnabled(true);
			scr.rightBtn.setEnabled(true);
			scr.surrendBtn.setEnabled(true);
			scr.undoBtn.setEnabled(false);
			scr.logBackUp.setEnabled(true);
			
			turnDone = true;
			scr.mp.validate();
			scr.mp.repaint();
			logger.writeNewGame();
			if (!gameThread.isAlive())
				gameThread.start();
		}
		
		/**
		 * SURREND
		 */
		if (e.getSource().equals(scr.surrendBtn)){
			int d = JOptionPane.showConfirmDialog(scr.fr, "Вы уверены, что хотите сдаться?", "Подтверждение сдачи", JOptionPane.YES_NO_OPTION);
			if (d == JOptionPane.OK_OPTION){
				logger.writeSurrend();
				JOptionPane.showMessageDialog(scr.fr, "Вы сдались! Конец игры.");
				hsw.addHighScoresToFile(gameScore);
			}
		}
		
		/**
		 * UNDO
		 */
		if (e.getSource().equals(scr.undoBtn)){
			scr.undoBtn.setEnabled(false);
			undoQuantity = 2;
			setGameScore(undoGameScore);
			clearTilesFromField();
			for (int i = 0; i < tCells; i++)
				for (int j = 0; j < tCells; j++){
					arrTile[i][j] = undoArrTile[i][j];
					if (arrTile[i][j] != null){
						scr.mp.add(arrTile[i][j]);
						arrTile[i][j].setLocation(arrPoint[i][j].x, arrPoint[i][j].y);
						arrTile[i][j].setScore(undoArrTileScores[i][j]);
					}
				}
			scr.mp.validate();
			scr.mp.repaint();
			logger.writeUndoClicked();
		}
		
		if (e.getSource().equals(scr.logBackUp)){
			logger.doLogBackUp();
		}
		
		if (e.getSource().equals(scr.upBtn)){
			if (scr.upBtn.isEnabled())
				goUp(0, -1);
//			generate14Tiles();
		}
		if (e.getSource().equals(scr.leftBtn)){
			if (scr.leftBtn.isEnabled())
			goLeft(-1, 0);
		}
		if (e.getSource().equals(scr.downBtn)){
			if (scr.downBtn.isEnabled())
			goDown(0, 1);
		}
		if (e.getSource().equals(scr.rightBtn)){
			if (scr.rightBtn.isEnabled())
			goRight(1, 0);
		}
	}
	
	private void clearTilesFromField() {
		for (Component t : scr.mp.getComponents())
			if (t.getName() != null && t.getName().equals("Tile"))
				scr.mp.remove(t);
		clearArrTile(arrTile);
	}
	
	public void clearArrTile(Tile[][] arrTile){
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++)
				arrTile[i][j] = null;
	}

	public void generate14Tiles(){
		for (int i = 0; i < 14; i++) {
			createNewTile();
		}
		for (int i = 0; i < tCells; i++)
			for (int j = 0; j < tCells; j++) {
				if (arrTile[i][j] != null)
					arrTile[i][j].setScore(i*j*3+2*i);
			}
	}

	public void setGameScore(int newScore) {
		gameScore = newScore;
		scr.scoreLbl.setText(Integer.toString(gameScore));
	}

	public void doOnClose() {
		try {
			logger.out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}
}