package Visual;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Mech.*;

public class Screen implements Constants{
	public JPanel mp;
	public JButton arrTileBtn;
	public JButton scoreBtn;
	public JButton newGameBtn;
	public JButton surrendBtn;
	public JButton upBtn, leftBtn, downBtn, rightBtn;
	public JButton okHSBtn;
	public JButton undoBtn;
	public JButton logBackUp;
	public JButton clearHSBtn;
	public JLabel scoreLbl;
	private JLabel helpLbl;
	public JFrame fr;
	public JTextField[] highScoreNames = new JTextField[10];
	public JLabel[] highScoreValues = new JLabel[10];
	
	public Screen() {
		mp = new JPanel();
		mp.setBackground(Color.white);
		mp.setLayout(null);
		
		fr = new JFrame("2048");
		fr.setContentPane(mp);
		fr.setExtendedState(Frame.MAXIMIZED_BOTH);
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		newGameBtn = new JButton("New Game");
		mp.add(newGameBtn);
		newGameBtn.setFont(fontScore);
		newGameBtn.requestFocus();
		
		surrendBtn = new JButton("Surrend");
		mp.add(surrendBtn);
		surrendBtn.setFont(fontScore);
		surrendBtn.setFocusable(false);
		surrendBtn.setEnabled(false);
		
		undoBtn = new JButton("Undo");
		mp.add(undoBtn);
		undoBtn.setFont(fontScore);
		undoBtn.setFocusable(false);
		undoBtn.setEnabled(false);
		
		logBackUp = new JButton("Save log");
		mp.add(logBackUp);
		logBackUp.setFont(fontScore);
		logBackUp.setFocusable(false);
		logBackUp.setEnabled(false);
		
		upBtn = new JButton("W");
		upBtn.setFocusable(false);
		upBtn.setEnabled(false);
		leftBtn = new JButton("A");
		leftBtn.setFocusable(false);
		leftBtn.setEnabled(false);
		downBtn = new JButton("S");
		downBtn.setFocusable(false);
		downBtn.setEnabled(false);
		rightBtn = new JButton("D");
		rightBtn.setFocusable(false);
		rightBtn.setEnabled(false);
		upBtn.setFont(fontWASD);
		leftBtn.setFont(fontWASD);
		downBtn.setFont(fontWASD);
		rightBtn.setFont(fontWASD);
		mp.add(upBtn);
		mp.add(leftBtn);
		mp.add(downBtn);
		mp.add(rightBtn);
		
		scoreLbl = new JLabel("");
		scoreLbl.setFocusable(false);
		scoreLbl.setHorizontalAlignment(JLabel.CENTER);
		scoreLbl.setVerticalAlignment(JLabel.CENTER);
		scoreLbl.setOpaque(true);
		scoreLbl.setBackground(Color.LIGHT_GRAY);
		scoreLbl.setFont(fontScore);
		mp.add(scoreLbl);
		
		helpLbl = new JLabel("<html> W A S D - вверх, влево, вниз, вправо.<br> "
							+ "Esc - выход.<br> "
							+ "<br> "
							+ "Смахивайте плитки в разные стороны. <br> "
							+ "Плитки с одинаковым номиналом соединятся между собой.<br> "
							+ "Номинал получившейся плитки прибавится к Вашим очкам.</html>");
		helpLbl.setFocusable(false);
		helpLbl.setHorizontalAlignment(JLabel.LEFT);
		helpLbl.setVerticalAlignment(JLabel.TOP);
		helpLbl.setOpaque(true);
		helpLbl.setBackground(Color.white);
		helpLbl.setFont(fontScore);
		mp.add(helpLbl);
		
		Logic lgc = new Logic(this);
		newGameBtn.addActionListener(lgc);
		surrendBtn.addActionListener(lgc);
		undoBtn.addActionListener(lgc);
		logBackUp.addActionListener(lgc);
		newGameBtn.addKeyListener(lgc);
		upBtn.addActionListener(lgc);
		leftBtn.addActionListener(lgc);
		downBtn.addActionListener(lgc);
		rightBtn.addActionListener(lgc);
		newGameBtn.setBounds(arrPoint[1][0].x+tSize/2, arrPoint[2][0].y-80, tSize+tGap, 50);
		surrendBtn.setBounds(arrPoint[3][0].x, arrPoint[2][0].y-80, tSize, 50);
		undoBtn.setBounds(arrPoint[0][0].x, arrPoint[2][0].y-80, tSize, 50);
		scoreLbl.setBounds(arrPoint[1][3].x, arrPoint[2][3].y+tSize+20, tSize*2+tGap, 50);	
		helpLbl.setBounds(arrPoint[3][0].x+helpDX, arrPoint[3][0].y, tSize*3+tGap, tSize*2+tGap);
			upBtn.setBounds(arrPoint[3][0].x+helpDX+wasdDXY, arrPoint[3][2].y, wasdSize, wasdSize);
			leftBtn.setBounds(arrPoint[3][0].x+helpDX, arrPoint[3][2].y+wasdDXY, wasdSize, wasdSize);
			downBtn.setBounds(arrPoint[3][0].x+helpDX+wasdDXY, arrPoint[3][2].y+wasdDXY, wasdSize, wasdSize);
			rightBtn.setBounds(arrPoint[3][0].x+helpDX+wasdDXY*2, arrPoint[3][2].y+wasdDXY, wasdSize, wasdSize);
		logBackUp.setBounds(leftBtn.getX(), leftBtn.getY()+leftBtn.getHeight()+tGap, wasdSize*3+tGap*2, 50);
			
		int highScoresNamesX = helpLbl.getX()+helpLbl.getWidth()+tGap*2;
		int highScoresValuesX = highScoresNamesX + tSize*2 + tGap;
		for (int i = 0; i < 10; i++) {
			highScoreNames[i] = new JTextField();
			highScoreNames[i].setHorizontalAlignment(JTextField.RIGHT);
			highScoreNames[i].setBounds(highScoresNamesX, arrPoint[0][0].x+25*i, tSize*2, 20);
			highScoreNames[i].setFocusable(false);
			highScoreNames[i].addKeyListener(lgc.hsw);
			mp.add(highScoreNames[i]);
			
			highScoreValues[i] = new JLabel();
			highScoreValues[i].setHorizontalAlignment(JTextField.CENTER);
			highScoreValues[i].setBounds(highScoresValuesX, arrPoint[0][0].x+25*i, tSize, 20);
			highScoreValues[i].setOpaque(true);
			highScoreValues[i].setBackground(Color.LIGHT_GRAY);
			mp.add(highScoreValues[i]);
		}
		okHSBtn = new JButton("ok");
		okHSBtn.setHorizontalAlignment(JButton.CENTER);
		okHSBtn.setVerticalAlignment(JButton.CENTER);
		okHSBtn.setFocusable(false);
		okHSBtn.setBounds(highScoresValuesX+highScoreValues[0].getWidth()+tGap, highScoreValues[0].getY(), 50, 20);
		okHSBtn.setVisible(false);
		okHSBtn.addActionListener(lgc.hsw);
		mp.add(okHSBtn);
		
		clearHSBtn = new JButton("Clear high scores");
		clearHSBtn.setHorizontalAlignment(JButton.CENTER);
		clearHSBtn.setVerticalAlignment(JButton.CENTER);
		clearHSBtn.setFocusable(false);
		clearHSBtn.setBounds(highScoresNamesX, highScoreValues[0].getY()-25, tSize*3+tGap, 20);
		clearHSBtn.addActionListener(lgc.hsw);
		mp.add(clearHSBtn);
		
		fr.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				lgc.doOnClose();
			}
			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
		lgc.hsw.displayHSatStart();
		mp.repaint();
	}
	
	public static void main(String[] args) {
	@SuppressWarnings("unused")
	Screen scr = new Screen();
}
}