package ballsy;

import oldshit.Level;
import processing.core.PImage;

public class WelcomeScreen extends Screen {
	Window _window;
	PImage _newGraphic, _newGraphicHover, _levelsGraphic, _levelsGraphicHover;
	
	public void setup() {
		_window = Window.getInstance();
		
		// preload all images, for rollover speed
		_newGraphic = _window.loadImage("res/new_game.png");
		_newGraphicHover = _window.loadImage("res/new_game_hover.png");
		_levelsGraphic = _window.loadImage("res/pick_level.png");
		_levelsGraphicHover = _window.loadImage("res/pick_level_hover.png");
	}
	
	public void draw() {
		_window.background(255,255,255);
		
		// draw title
		PImage title = _window.loadImage("res/ballsy_title.png");
		_window.image(title,_window.width/2,300);
		
		//new game hover detect
		int x_left = _window.width/2-150;
		int x_right = _window.width/2+150;
		int y_top = 400;
		int y_bottom = 500;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			_window.image(_newGraphicHover,_window.width/2,450);
		}
		else {
			_window.image(_newGraphic,_window.width/2,450);
		}
		
		//pick level hover detect
		x_left = _window.width/2-150;
		x_right = _window.width/2+150;
		y_top = 500;
		y_bottom = 600;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			_window.image(_levelsGraphicHover,_window.width/2,550);
		}
		else {
			_window.image(_levelsGraphic,_window.width/2,550);
		}
		
		// put stuff in the middle
		_window.imageMode(_window.CENTER);
	}
	
	public void mousePressed() {
		//new game click detect
		int x_left = _window.width/2-150;
		int x_right = _window.width/2+150;
		int y_top = 400;
		int y_bottom = 500;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			// temporary... Matt's gonna do some shit with TestObjects.
			// set to Level for now
			_window.setScreen(new Level());
		}
		
		//pick level click detect
		x_left = _window.width/2-150;
		x_right = _window.width/2+150;
		y_top = 500;
		y_bottom = 600;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			_window.setScreen(new LevelMenu1());
		}
	}
	
	public void mouseReleased() {

	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
	}
}