package ballsy;

import graphics.Text;
import graphics.TextButton;
import ballsy.ScreenLoader.Screens;

public class WinScreen {
	
	private Text _won;
	private TextButton _next, _exit, _menu;
	private Window _window;
	//private AbstractLevel _level = AbstractLevel.getInstance();
	private AbstractLevel _level;
	
	public WinScreen(AbstractLevel level) {
		_level = level;
		_window = Window.getInstance();
//		_resume = new Image(_window, "res/resume.png", 257,64, _window.width/2, _window.height/2);
//		_pause = new Image(_window, "res/pause.png",(int)(257*1.5),(int)(64*1.5),_window.width/2,_window.height/2-200);
//		_menu = new Image(_window, "res/exit.png", 257,64,_window.width/2,_window.height/2+100);
		//TODO Jessica needs to make a menu pic, for the menu button
//		_exit = new Image(_window, "res/exit.png", 257,64,_window.width/2, _window.height/2+200);
		_won = new Text("You Won!", _window.width/2, _window.height/2-50);	
		_won.setSize(72);
		_won.setColor(255);
		_next = new TextButton("Next Level", _window.width/2, _window.height/2+70);
		_menu = new TextButton("Menu", _window.width/2, _window.height/2+140);
		_exit = new TextButton("Exit ", _window.width/2, _window.height/2+210);	
	
	}

	
	public void draw() {
//		_window.fill(50,200,200,100);
		_window.fill(0,150);
		_window.rectMode(_window.CORNER);
		_window.rect(0, 0, _window.width, _window.height);
		_next.draw();
		_won.draw();
		_exit.draw();
		_menu.draw();
	}

	public void mousePressed() {
		System.out.println("clicked pause screen");
		if (_next.mouseOver()) {
			((XMLLevel)_level).nextLevel();
		}
		else if (_exit.mouseOver()) {
			System.exit(0);
		}
		else if (_menu.mouseOver()) {
			_window.loadScreen(Screens.WELCOME_SCREEN);
		}
	}

}
