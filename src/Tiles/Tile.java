package Tiles;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Mech.*;

public class Tile extends JPanel implements Constants{
	private static final long serialVersionUID = 1L;
	
	private int score;
	private Color color;
	private JLabel scoreLabel = new JLabel();
	
	public Tile() {
		setName("Tile");
		setLayout(null);
		add(scoreLabel);
		generateStartScore();
		doColor(255, 0, 0);
		scoreLabel.setBounds(0, 0, tSize, tSize);
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreLabel.setVerticalAlignment(JLabel.CENTER);
		scoreLabel.setFont(fontTile);
		
	}
	
	public void doColor(int red, int green, int blue) {
		color = new Color(red, green, blue);
		this.setBackground(color);
	}

	/**
	 * 91% chance to start with 2 score on Tile, 9% - with 4
	 */
	private void generateStartScore() {
		Random rnd = new Random();
		int k = rnd.nextInt(100)+1;
		score = k>=91? 4:2;
	}
	
	/**
	 * Sets up new X and Y for Tile
	 * @param newX, newY
	 */
	public void setXY(Point point){
		this.setBounds(point.x, point.y, tSize, tSize);
	}
	/**
	 * Sets up new X and Y for Tile
	 * @param newX, newY
	 */
	public void setXY(int x, int y){
		this.setBounds(x, y, tSize, tSize);
	}
	
	/**
	 * Sets up new score and rewrite it on the Tile
	 * @param newScore
	 */
	public void setScore(int newScore){
		score = newScore;
		scoreLabel.setText(Integer.toString(newScore));
//		scoreLabel.repaint();
		int log2 = (int)(Math.log(score)/Math.log(2));	// logarithm on base 2
		int blue = (int) log2*40/(5+log2)*8;			// hand-made formula to quick change color at start, and slower further
		doColor(255, blue, 0);
		repaint();
	}
		public int getScore(){
			return score;
		}
}
