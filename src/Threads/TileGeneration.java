package Threads;
import java.awt.Point;

import Mech.*;
import Tiles.*;

public class TileGeneration extends Thread implements Constants{
	
	public boolean creationDone = false;
	private Logic lgc;
	private Tile tile;
	private Point point;
	
	public TileGeneration(Logic lgc, Tile tile, Point point) {
		this.lgc = lgc;
		this.tile = tile;
		this.point = point;
	}
	
	@Override
	public void run() {
		lgc.scr.newGameBtn.removeKeyListener(lgc);
		tile.setBounds(point.x+25, point.y+25, 50, 50);
		lgc.scr.mp.add(tile);
		
		for (int i = 0; i <= 5; i++) {
			tile.setBounds(point.x+25-5*i, point.y+25-5*i, 50+10*i, 50+10*i);

			lgc.scr.mp.repaint();
			try {
				Thread.sleep(tSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tile.setScore(tile.getScore());
		lgc.scr.newGameBtn.addKeyListener(lgc);
		creationDone = true;
	}
}
