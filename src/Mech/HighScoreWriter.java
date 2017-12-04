package Mech;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class HighScoreWriter extends Thread implements ActionListener, Constants, Runnable, KeyListener {

	private Logic lgc;
	private File hsFile;
	public ArrayList<String> strings = new ArrayList<>();
	public ArrayList<String> names = new ArrayList<>();
	private ArrayList<Integer> highScores = new ArrayList<>();
    private FileWriter outHS;
    
    private class NewPlace{
    	boolean isNewHS = false;
    	int index = -1;
    }
    private NewPlace newPlace = new NewPlace();
	
	public HighScoreWriter(Logic lgc) {
		this.lgc = lgc;
		initializeHSFile();
	}
	
	private void initializeHSFile(){
		if (!companyFolder.exists())
			companyFolder.mkdir();
		if (!gameFolder.exists())
			gameFolder.mkdir();
		hsFile = new File(gameFolder+"/2048hs.txt");

		try {
			outHS = new FileWriter(hsFile, true);
			FileReader in = new FileReader(hsFile);
			Scanner scan = new Scanner(in);

			if (!scan.hasNextLine())
	        	for (int i = 0; i < 10; i++) {
					outHS.write("---: 0");
					outHS.write(System.lineSeparator());
				}
	        scan.close();
			in.close();
			outHS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void displayHSatStart(){
		try {
			extractHighScoreFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 10; i++) {
			if (highScores.get(i) != null)
				lgc.scr.highScoreNames[i].setText(names.get(i));
				lgc.scr.highScoreValues[i].setText(Integer.toString(highScores.get(i)));
		}
	}
	
	@Override
	public void run() {
		int gameScore = lgc.gameScore;
		lgc.scr.newGameBtn.setEnabled(false);
		lgc.scr.surrendBtn.setEnabled(false);
		lgc.scr.undoBtn.setEnabled(false);
		lgc.scr.logBackUp.setEnabled(false);
		lgc.scr.upBtn.setEnabled(false);
		lgc.scr.leftBtn.setEnabled(false);
		lgc.scr.downBtn.setEnabled(false);
		lgc.scr.rightBtn.setEnabled(false);
		if (availableToAddToHSList(gameScore).isNewHS){
			// shift all names and values before new high score
			for (int i = 9; i > newPlace.index; i--) {
				lgc.scr.highScoreNames[i].setText(lgc.scr.highScoreNames[i-1].getText());
				lgc.scr.highScoreValues[i].setText(lgc.scr.highScoreValues[i-1].getText());
			}
			
			highScores.add(newPlace.index, gameScore);
			
			JLabel newHSValueField = lgc.scr.highScoreValues[newPlace.index];
			newHSValueField.setText(Integer.toString(gameScore));
			newHSValueField.setBackground(lightRed);

			JTextField newHSNameField = lgc.scr.highScoreNames[newPlace.index];
			newHSNameField.setBackground(lightRed);
			newHSNameField.setText("");
			newHSNameField.setFocusable(true);
			newHSNameField.requestFocus();
			lgc.scr.okHSBtn.setVisible(true);
			lgc.scr.okHSBtn.setLocation(lgc.scr.okHSBtn.getX(), lgc.scr.highScoreNames[newPlace.index].getY());
    		do {
				Thread.yield();
			} while (lgc.scr.okHSBtn.isVisible());
    		
    		String newHSName = nameWithoutColons(newHSNameField.getText());
			newHSNameField.setText(newHSName);
    		names.add(newPlace.index, newHSName);
    		if (names.get(newPlace.index).equals(""))
    			names.set(newPlace.index, " ");
			newHSNameField.setBackground(Color.WHITE);
			newHSNameField.setFocusable(false);
			newHSValueField.setBackground(Color.LIGHT_GRAY);
			lgc.logger.writeNewHighScore(newHSName, gameScore, newPlace.index+1);
			writeHSToFile();
		}
		lgc.scr.newGameBtn.setEnabled(true);
		lgc.scr.newGameBtn.requestFocus();
		lgc.scr.logBackUp.setEnabled(true);
	}
	
	private String nameWithoutColons(String name) {
		for (int i = 0; i <= name.length()-1; i++) {
			if (name.charAt(i) == ':'){
				name = name.substring(0, i) + name.substring(i+1);
				i--;
			}
		}
		return name;
	}

	private void writeHSToFile() {
		try {
			outHS = new FileWriter(hsFile);

        	for (int i = 0; i < 10; i++) {
        		String name = names.get(i);
        		String score = Integer.toString(highScores.get(i));
				outHS.write(name+": "+score);
				outHS.write(System.lineSeparator());
			}

			outHS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private NewPlace availableToAddToHSList(int gameScore){
		int lastIndex = highScores.size()-1;
		for (int i = 0; i <= lastIndex; i++) {
			if (gameScore > highScores.get(i)){
				newPlace.isNewHS = true;
				newPlace.index = i;
				return newPlace;
			}
		}
		newPlace.isNewHS = false;
		newPlace.index = -1;		
		return newPlace;
	}
	
	/**
	 * Extracts strings from file. Cut off player names before ":" and writes high score into int array highScores.
	 * @throws IOException
	 */
	private void extractHighScoreFromFile() throws IOException{
		FileReader in = new FileReader(hsFile);
		Scanner scan = new Scanner(in);
		String line;
		while (scan.hasNextLine()){
			line = scan.nextLine();
			strings.add(line);
		}

		String str;
		for (int i = 0; i < 10; i++)
			if ((str = strings.get(i)) != null){
				int colonPos = str.indexOf(":");
				names.add(str.substring(0, colonPos));
				String numberInString = str.substring(colonPos+2);
				highScores.add(Integer.parseInt(numberInString));
			}else
				break;
		scan.close();
		in.close();
	}

	/**
	 * Clears all high scores and displays it immediately
	 */
	public void clearHighScores() {
		try {
			outHS = new FileWriter(hsFile);
	        	for (int i = 0; i < 10; i++) {
					outHS.write("---: 0");
					outHS.write(System.lineSeparator());
				}
			outHS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		strings.clear();
		names.clear();
		highScores.clear();
		displayHSatStart();
		lgc.scr.mp.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * COMMIT NEW HI SCORE's NAME
		 */
		if (e.getSource().equals(lgc.scr.okHSBtn)){
			lgc.scr.okHSBtn.setVisible(false);
		}
		
		/**
		 * CLEAR HIGH SCORES
		 */
		if (e.getSource().equals(lgc.scr.clearHSBtn)){
			int d = JOptionPane.showConfirmDialog(lgc.scr.fr, "Вы уверены, что хотите удалить лучшие результаты?", "Удаление лучших результатов", JOptionPane.YES_NO_OPTION);
			if (d == JOptionPane.OK_OPTION){
				JOptionPane.showMessageDialog(lgc.scr.fr, "Рекорды удалены!");
				clearHighScores();
				lgc.logger.writeHighScoresCleared();
			}
		}
	}

	public void addHighScoresToFile(int gameScore) {
		Thread hsw = new Thread(this);
		hsw.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar()=='\n')
			lgc.scr.okHSBtn.doClick();
	}
}
