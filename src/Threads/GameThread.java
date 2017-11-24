package Threads;

import Mech.*;

public class GameThread extends Thread implements Constants{
	
	private Logic lgc;
	
	public GameThread(Logic lgc) {
		this.lgc = lgc;
	}

	public void run() {
		lgc.setGameScore(0);
		do{
			lgc.turnDone = false;
			lgc.createNewTile();
			
				do
					Thread.yield();
				while (!lgc.turnDone);
		}while (true);
		
	}
}
