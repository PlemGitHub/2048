package Mech;


public interface KeyDictionary {
	
	public static char keyTranslation(char c){
		char cc=' ';
		
			switch (c) {
			case 'w':
			case 'W':
			case 'ц':
			case 'Ц':
				cc = 'w';
				break;
	
			case 'a':
			case 'A':
			case 'ф':
			case 'Ф':
				cc = 'a';
				break;
				
			case 's':
			case 'S':
			case 'ы':
			case 'Ы':
				cc = 's';
				break;
				
			case 'd':
			case 'D':
			case 'в':
			case 'В':
				cc = 'd';
				break;
			}
			
		return cc;

	}
}
