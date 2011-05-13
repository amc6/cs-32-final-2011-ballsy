package ballsy;

/** 
 * Simplifies the screen loading process.
 */

import menu.Menu;
import menu.MenuButton;
import editor.LevelEditor;

public class ScreenLoader implements Runnable {
	
	public enum Screens {
		WELCOME_SCREEN,
		LEVEL_EDITOR,
		LEVEL_MENU,
		LEVEL_ONE,
		XML_LEVEL
	}
	
	private Screens _screenType;
	private String _fileName;
	private MenuButton _current;
	
	/**
	 * Constructor
	 * @param s - the screen type (as in enum)
	 * @param fileName for XMLevel
	 * @param current menubutton pressed, if selected from select level
	 */
	public ScreenLoader(Screens s, String fileName, MenuButton current) {
		_screenType = s;
		_fileName = fileName;
		_current = current;
	}

	public void run() {
		Screen screen = null;
		switch (_screenType) {
		case WELCOME_SCREEN:
			screen = new WelcomeScreen();
			break;
		case LEVEL_MENU:
			screen = new Menu();
			break;
		case XML_LEVEL:
			screen = new XMLLevel(_fileName,_current);
			break;	
		case LEVEL_EDITOR:
			screen = new LevelEditor();
			break;
		}
		
		screen.setup();
		Window.getInstance().setScreen(screen);
	}

}
