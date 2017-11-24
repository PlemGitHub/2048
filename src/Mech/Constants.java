package Mech;

import java.awt.Font;
import java.awt.Point;

public interface Constants {
	public int tSize = 100;
	public int tGap = tSize/10;			// tSize/10 is space between tiles
	public int tCells = 4;				// field size. tCells x tCells
	public int tSpeed = 10;				// lesser the number, faster the movement speed
	public int dN = 5;					// dX and dY to move Tiles on board
	public Font fontScore = new Font("Monofur", Font.BOLD, 15);
	
	public Point[][] arrPoint = new Point[tCells][tCells];
	
}