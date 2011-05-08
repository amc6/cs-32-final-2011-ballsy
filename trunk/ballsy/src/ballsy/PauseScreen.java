package ballsy;

import graphics.Text;
import graphics.TextButton;
import ballsy.ScreenLoader.Screens;

public class PauseScreen {
	
	private Text _pause;
	private TextButton _resume, _exit, _menu;
	private Window _window;
	//private AbstractLevel _level = AbstractLevel.getInstance();
	private AbstractLevel _level;
	
	public PauseScreen(AbstractLevel level) {
		_level = level;
		_window = Window.getInstance();
//		_resume = new Image(_window, "res/resume.png", 257,64, _window.width/2, _window.height/2);
//		_pause = new Image(_window, "res/pause.png",(int)(257*1.5),(int)(64*1.5),_window.width/2,_window.height/2-200);
//		_menu = new Image(_window, "res/exit.png", 257,64,_window.width/2,_window.height/2+100);
		//TODO Jessica needs to make a menu pic, for the menu button
//		_exit = new Image(_window, "res/exit.png", 257,64,_window.width/2, _window.height/2+200);
		_pause = new Text("Paused", _window.width/2, _window.height/2-50);	
		_pause.setSize(72);
		_pause.setColor(255);
		_resume = new TextButton("Resume", _window.width/2, _window.height/2+70);
		_menu = new TextButton("Menu", _window.width/2, _window.height/2+140);
		_exit = new TextButton("Exit ", _window.width/2, _window.height/2+210);	
	
	}

	public void draw() {
		_window.cursor();
//		_window.fill(50,200,200,100);
		_window.fill(0,150);
		_window.rectMode(_window.CORNER);
		_window.rect(0, 0, _window.width, _window.height);
		_resume.draw();
		_pause.draw();
		_exit.draw();
		_menu.draw();
	}

	public void mousePressed() {
		System.out.println("clicked pause screen");
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
