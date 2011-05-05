package ballsy;

import editor.EditorLevel;
import editor.LevelEditor;

public class ScreenLoader implements Runnable {
	
	public enum Screens {
		WELCOME_SCREEN,
		LEVEL_EDITOR,
		EDITOR_LEVEL,
		LEVEL_MENU,
		LEVEL_ONE
	}
	
	private Screens _screenType;
	
	public ScreenLoader(Screens s) {
		_screenType = s;
	}

	@Override
	public void run() {
		Screen screen = null;
		switch (_screenType) {
		case WELCOME_SCREEN:
			screen = new WelcomeScreen();
			break;
		case LEVEL_MENU:
			screen = new LevelMenu1();
			break;
		case LEVEL_ONE:
			screen = new LevelOne();
			break;
		case LEVEL_EDITOR:
			screen = new LevelEditor();
			break;
		case EDITOR_LEVEL:
			screen = new EditorLevel();
			break;
		}
		
		screen.setup();
		Window.getInstance().setScreen(screen);
	}

}