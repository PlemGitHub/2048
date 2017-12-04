package Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Mech.Constants;
import Mech.Logic;

public class Logger implements Constants {

	private Logic lgc;
	private File logFile;
    public FileWriter out;
	
	public Logger(Logic lgc) {
		this.lgc = lgc;
		initializeLogFile();
	}
	
	private void initializeLogFile(){
		if (!gameFolder.exists())
			gameFolder.mkdir();
		logFile = new File(gameFolder+"/2048log.txt");
		try {
			out = new FileWriter(logFile, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeNewGame(){
		try {
			out.write("============================================================");
			out.write(System.lineSeparator());
			out.write("New game started!");
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeSurrend(){
		try {
			out.write("==============================");
			out.write(System.lineSeparator());
			out.write("Game ended with surrend!");
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeNewHighScore(String name, int gamescore, int pos){
		try {
			out.write("==============================");
			out.write(System.lineSeparator());
			out.write("New high score achieved: "+pos+". "+name+" - "+gamescore+" pts.");
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeUndoClicked(){
		try {
			out.write("Undo button is clicked!");
			out.write(System.lineSeparator());
			writeTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeNewTile(int i, int j, int score){
		try {
			out.write("New tile generated: "+i+" "+j+" "+score);
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeHighScoresCleared(){
		try {
			out.write("High scores were cleared!");
			out.write(System.lineSeparator());
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeKey(char c) {
		String side="";
		switch (c) {
			case 'w': side = " up"; break;
			case 'a': side = " left"; break;
			case 's': side = " down"; break;
			case 'd': side = " right"; break;
		}
		try {
			out.write("Key pressed: \"" + c + "\""+side);
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeNoMoveAfterKeyPressed(char c) {
		String side="";
		switch (c) {
			case 'w': side = " up "; break;
			case 'a': side = " left "; break;
			case 's': side = " down "; break;
			case 'd': side = " right "; break;
		}
		
		try {
			out.write("Can't move tiles in"+side+"direction.");
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeTiles(){
		try {
			out.write("Tiles: ");
			for (int j = 0; j < tCells; j++){
				out.write(System.lineSeparator());
				for (int i = 0; i < tCells; i++){
					if (lgc.arrTile[i][j] == null)
						out.write("0 ");
					else 
						out.write(lgc.arrTile[i][j].getScore()+" ");
				}
			}
			out.write(System.lineSeparator());		
			out.write("Gamescore: "+lgc.gameScore);
			out.write(System.lineSeparator());				
			out.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void doLogBackUp(){
	    FileReader in;
	    Scanner scan;
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		out.close();
		
				Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		String logBackUpName = "/"+sdf.format(date)+"-log.txt";
		File fileBackUp = new File(gameFolder+logBackUpName);
			out = new FileWriter(fileBackUp, false);
			in = new FileReader(logFile);
			scan = new Scanner(in); 
			
		String line;
		while (scan.hasNextLine()){
			line = scan.nextLine();
			out.write(line);
			out.write(System.lineSeparator());
		}
		out.close();
		scan.close();
		in.close();

		out = new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
