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
		tile.setBounds(point.x+25, point.y+25, 50, 50);
		lgc.scr.mp.add(tile);
		
		for (int i = 0; i <= 10; i++) {
			tile.setBounds(point.x+25-5*i/2, point.y+25-5*i/2, 50+5*i, 50+5*i);

			lgc.scr.mp.repaint();
			try {
				Thread.sleep(tSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tile.setScore(tile.getScore());
		creationDone = true;
	}
}
