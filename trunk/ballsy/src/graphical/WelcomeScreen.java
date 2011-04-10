package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class WelcomeScreen extends Screen {
	
	String _newGame;
	String _pickLevel;
	
	public WelcomeScreen(Main window) {
		super(window);
	}
	
	public void setup() {

		
		_newGame = "res/new_game.png";
		_pickLevel = "res/pick_level.png";
	}
	
	
	public void draw() {
		_window.background(255,255,255);
		
		
		//new game hover detect
		int x_left = _window.width/2-150;
		int x_right = _window.width/2+150;
		int y_top = 400;
		int y_bottom = 500;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			_newGame = "res/new_game_hover.png";			
		}
		else {
			_newGame = "res/new_game.png";
		}
		
		
		
		//pick level hover detect
		x_left = _window.width/2-150;
		x_right = _window.width/2+150;
		y_top = 500;
		y_bottom = 600;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			_pickLevel = "res/pick_level_hover.png";
		}
		else {
			_pickLevel = "res/pick_level.png";
		}
		
		
		

		_window.imageMode(_window.CENTER);
		
		//title
		PImage title = _window.loadImage("res/ballsy_title.png");
		_window.image(title,_window.width/2,300);
		
		//new game
		PImage newGame = _window.loadImage(_newGame);
		_window.image(newGame,_window.width/2,450);
		
		//pick level
		PImage pickLevel = _window.loadImage(_pickLevel);
		_window.image(pickLevel,_window.width/2,550);
		
	}
	
	public void mousePressed() {
		
		//new game click detect
		int x_left = _window.width/2-150;
		int x_right = _window.width/2+150;
		int y_top = 400;
		int y_bottom = 500;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
					
		}
		
		//pick level click detect
		x_left = _window.width/2-150;
		x_right = _window.width/2+150;
		y_top = 500;
		y_bottom = 600;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
			_window.setScreen(new LevelMenu1(_window));
		}
		
		
		
	}
	
	public void mouseReleased() {

	}

}










