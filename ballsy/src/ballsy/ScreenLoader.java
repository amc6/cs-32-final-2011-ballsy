package ballsy;

import menu.Menu;
import menu.MenuButton;
import editor.EditorLevel;
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
	
	public ScreenLoader(Screens s, String fileName, MenuButton current) {
		_screenType = s;
		_fileName = fileName;
		_current = current;
	}

	@Override
	public void run() {
		Screen screen = null;
		switch (_screenType) {
		case WELCOME_SCREEN:
			screen = new WelcomeScreen();
			break;
		case LEVEL_MENU:
			screen = new Menu();
			break;
		case LEVEL_ONE:
			screen = new LevelOne();
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
