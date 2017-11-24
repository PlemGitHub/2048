package Threads;

import java.util.ArrayList;

import Mech.Constants;
import Mech.Logic;
import Tiles.TileToMove;
import Visual.Screen;

public class TileMovement extends Thread implements Constants {
	
	public boolean allMovementsDone;
	private int dX, dY;
	private ArrayList<TileToMove> arrToMove;
	private Screen scr;
	private Logic lgc;
	
	public TileMovement(Screen scr, Logic lgc, int dX, int dY, ArrayList<TileToMove> arrToMove) {
		this.scr = scr;
		this.lgc = lgc;
		this.dX = dX;
		this.dY = dY;
		this.arrToMove = arrToMove;
	}
	@Override
	public void run() {
			lgc.scr.fr.removeKeyListener(lgc);
			allMovementsDone = false;

			do {
				if (arrToMove.size() == 0) break;		// if arrToMove = 0 - there is nothing to move;
				
				for (int i = 0; i < arrToMove.size(); i++) {
					TileToMove ttm = arrToMove.get(i);
					int oldX = ttm.tile.getX();
					int oldY = ttm.tile.getY();
					
					if (oldX != ttm.destPoint.getX() | oldY != ttm.destPoint.getY()){
						ttm.tile.setXY(oldX+dX*dN, oldY+dY*dN);
						}
					else{
						ttm.movementDone = true;
						if (ttm.removeTile == true)
							scr.mp.remove(ttm.tile);
					}
				}
				
				for (int i = 0; i < arrToMove.size(); i++) {
					if (arrToMove.get(i).movementDone == false){
						allMovementsDone = false;
						break;
					}else
						allMovementsDone = true;
				}
				
				scr.mp.repaint();
				try {
					Thread.sleep(tSpeed/5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!allMovementsDone);
			
			if (allMovementsDone)		// if any movements done - allMovementsDone is True, and turn is over
				lgc.turnDone = true;
			
			arrToMove.clear();

			lgc.scr.fr.addKeyListener(lgc);
		}
}
