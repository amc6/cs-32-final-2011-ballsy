package ballsy;

/**
 * Makes a transparent "Paused" screen for when the user presses escape.
 * Provides options for return to menu, exiting Ballsy, or continuing play.
 */

import processing.core.PConstants;
import graphics.Text;
import graphics.TextButton;
import ballsy.ScreenLoader.Screens;

public class PauseScreen {
	
	private Text _pause;
	private TextButton _resume, _exit, _menu;
	private Window _window;
	private AbstractLevel _level;
	
	/**
	 * Construct with proper text buttons.
	 * @param level
	 */
	public PauseScreen(AbstractLevel level) {
		_level = level;
		_window = Window.getInstance();
		_pause = new Text("Paused", _window.width/2, _window.height/2-50);	
		_pause.setSize(72);
		_pause.setColor(255);
		_resume = new TextButton("Resume", _window.width/2, _window.height/2+70);
		_menu = new TextButton("Menu", _window.width/2, _window.height/2+140);
		_exit = new TextButton("Exit ", _window.width/2, _window.height/2+210);	
	
	}

	/**
	 * Draw everything associated with this screen
	 */
	public void draw() {
		_window.fill(0,150);
		_window.rectMode(PConstants.CORNER);
		_window.rect(0, 0, _window.width, _window.height);
		_resume.draw();
		_pause.draw();
		_exit.draw();
		_menu.draw();
	}

	/**
	 * handle the mouse click...
	 * figure out where it is, and either continue, exit, or load welcome screen
	 */
	public void mousePressed() {
		if (_resume.mouseOver()) {
			_level.togglePaused();
		}
		else if (_exit.mouseOver()) {
			System.exit(0);
		}
		else if (_menu.mouseOver()) {
			_window.loadScreen(Screens.WELCOME_SCREEN);
		}
	}

}
