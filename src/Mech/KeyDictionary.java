package Mech;

import java.awt.event.KeyEvent;

public interface KeyDictionary {
	
	public static char keyTranslation(char c){
		char cc=' ';
		
			switch (c) {
			case 'w':
			case 'W':
			case 'ц':
			case 'Ц':
			case KeyEvent.VK_UP:
				cc = 'w';
				break;
	
			case 'a':
			case 'A':
			case 'ф':
			case 'Ф':
			case KeyEvent.VK_LEFT:
				cc = 'a';
				break;
				
			case 's':
			case 'S':
			case 'ы':
			case 'Ы':
			case KeyEvent.VK_DOWN:
				cc = 's';
				break;
				
			case 'd':
			case 'D':
			case 'в':
			case 'В':
			case KeyEvent.VK_RIGHT:
				cc = 'd';
				break;
			}
			
		return cc;

	}
}
