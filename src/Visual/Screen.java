package Visual;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Mech.*;

public class Screen implements Constants{
	public JPanel mp;
	public JButton arrTileBtn;
	public JButton scoreBtn;
	public JButton newGameBtn;
	public JLabel scoreLbl;
	public JFrame fr;
	
	public Screen() {
		mp = new JPanel();
		mp.setBackground(Color.white);
		mp.setLayout(null);
		
		fr = new JFrame("2048");
		fr.setContentPane(mp);
		fr.setExtendedState(Frame.MAXIMIZED_BOTH);
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		newGameBtn = new JButton("New Game");
		newGameBtn.setFocusable(false);
		mp.add(newGameBtn);
		
		scoreLbl = new JLabel("");
		scoreLbl.setFocusable(false);
		scoreLbl.setHorizontalAlignment(JLabel.CENTER);
		scoreLbl.setVerticalAlignment(JLabel.CENTER);
		scoreLbl.setOpaque(true);
		scoreLbl.setBackground(Color.LIGHT_GRAY);
		scoreLbl.setFont(fontScore);
		mp.add(scoreLbl);
		
		Logic lg = new Logic(this);
		fr.addKeyListener(lg);
		newGameBtn.addActionListener(lg);
		newGameBtn.setBounds(arrPoint[1][0].x+tSize/2, arrPoint[2][0].y-80, tSize+tGap, 50);
		scoreLbl.setBounds(arrPoint[1][3].x, arrPoint[2][3].y+tSize+20, tSize*2+tGap, 50);
		
//		arrTileBtn = new JButton("arrTile");
//		arrTileBtn.setBounds(lg.test.testPoint[0][0].x, lg.test.testPoint[0][0].y-80, 100, 50);
//		arrTileBtn.setFocusable(false);
//		mp.add(arrTileBtn);
//		arrTileBtn.addActionListener(lg.test);
//		
//		scoreBtn = new JButton("score");
//		scoreBtn.setBounds(lg.test.testPoint[1][0].x, lg.test.testPoint[0][0].y-80, 100, 50);
//		scoreBtn.setFocusable(false);
//		mp.add(scoreBtn);
//		scoreBtn.addActionListener(lg.test);
		
	}
	
	
	
	public static void main(String[] args) {
	@SuppressWarnings("unused")
	Screen scr = new Screen();
}
}