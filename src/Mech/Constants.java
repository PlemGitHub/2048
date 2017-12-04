package Mech;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;

public interface Constants {
	public int tSize = 100;
	public int tGap = tSize/10;			// tSize/10 is space between tiles
	public int tCells = 4;				// field size. tCells x tCells
	public int tSpeed = 10;				// lesser the number, faster the movement speed
	public int dN = 5;					// dX and dY to move Tiles on board
	public Font fontTile = new Font("Monofur", Font.BOLD, 25);
	public Font fontScore = new Font("Monofur", Font.BOLD, 15);
	public Font fontWASD = new Font("Monofur", Font.BOLD, 15);
	public Point[][] arrPoint = new Point[tCells][tCells];
	public int helpDX = tSize*2;
	public int wasdDXY = tSize/2+tGap;
	public int wasdSize = tSize/2;
	public Color lightRed = new Color(255, 200, 200);
	public File companyFolder = new File(System.getenv("APPDATA")+"/PlemCo");
	public File gameFolder = new File(System.getenv("APPDATA")+"/PlemCo/2048");
}