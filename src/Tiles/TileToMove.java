package Tiles;

import java.awt.Point;

public class TileToMove{
	public Tile tile;
	public Point destPoint = new Point();	// destination position
	public int destI;
	public int destJ;
	public boolean removeTile = false;
	public boolean movementDone = false;
	public boolean selfCollision = false;
}
