package ballsy;

/**
 * Screen to notify a user of winning a level, and to give them the
 * option to proceed to a new level. Very similar to pause screen.
 */

import processing.core.PConstants;
import graphics.Text;
import graphics.TextButton;
import ballsy.ScreenLoader.Screens;

public class WinScreen {
	
	private Text _won;
	private TextButton _next, _exit, _menu;
	private Window _window;
	private AbstractLevel _level;
	
	/**
	 * Set up with proper buttons
	 * @param level
	 */
	public WinScreen(AbstractLevel level) {
		_level = level;
		_window = Window.getInstance();
		_won = new Text("You Won!", _window.width/2, _window.height/2-50);	
		_won.setSize(72);
		_won.setColor(255);
		_next = new TextButton("Next Level", _window.width/2, _window.height/2+70);
		_menu = new TextButton("Menu", _window.width/2, _window.height/2+140);
		_exit = new TextButton("Exit ", _window.width/2, _window.height/2+210);	
	
	}

	/**
	 * Draw everything related to this level
	 */
	public void draw() {
		_window.fill(0,150);
		_window.rectMode(PConstants.CORNER);
		_window.rect(0, 0, _window.width, _window.height);
		_next.draw();
		_won.draw();
		_exit.draw();
		_menu.draw();
	}

	/**
	 * Handle mouse press (take correct action)
	 */
	public void mousePressed() {
		// go to the next level
		if (_next.mouseOver()) {
			((XMLLevel)_level).nextLevel();
		}
		// exit Ballsy
		else if (_exit.mouseOver()) {
			System.exit(0);
		}
		// go back to main menu
		else if (_menu.mouseOver()) {
			_window.loadScreen(Screens.WELCOME_SCREEN);
		}
	}

}
