package ballsy;

import graphics.HoverImage;
import graphics.Image;

import java.util.Vector;

public class PauseScreen {
	
	private Image _resume, _pause, _exit, _menu;
	private Window _window;
	//private AbstractLevel _level = AbstractLevel.getInstance();
	private AbstractLevel _level;
	
	public PauseScreen(AbstractLevel level) {
		_level = level;
		_window = Window.getInstance();
		_resume = new Image(_window, "res/resume.png", 257,64, _window.width/2, _window.height/2);
		_pause = new Image(_window, "res/pause.png",(int)(257*1.5),(int)(64*1.5),_window.width/2,_window.height/2-200);
		_menu = new Image(_window, "res/exit.png", 257,64,_window.width/2,_window.height/2+100);
		//TODO Jessica needs to make a menu pic, for the menu button
		_exit = new Image(_window, "res/exit.png", 257,64,_window.width/2, _window.height/2+200);
	}

	public void draw() {
		_window.cursor();
		_window.fill(50,200,200,100);
		_window.rectMode(_window.CORNER);
		_window.rect(0, 0, _window.width, _window.height);
		_resume.draw();
		_pause.draw();
		_exit.draw();
		_menu.draw();
	}

	public void mousePressed() {
		if (_resume.mouseOver()) {
			_level.togglePaused();
		}
		else if (_exit.mouseOver()) {
			System.exit(0);
		}
		else if (_menu.mouseOver()) {
			_window.setScreen(new WelcomeScreen());
		}
	}

}
