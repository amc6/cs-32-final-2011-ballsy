package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class WelcomeScreen extends Screen {
	
	String _newGame;
	String _pickLevel;
	
	public void setup() {

		
		_newGame = "res/new_game.png";
		_pickLevel = "res/pick_level.png";
	}
	
	
	public void draw() {
		
		Window window = Window.getInstance();
		
		window.background(255,255,255);
		
		
		//new game hover detect
		int x_left = window.width/2-150;
		int x_right = window.width/2+150;
		int y_top = 400;
		int y_bottom = 500;
		if (window.mouseX > x_left && window.mouseX < x_right && window.mouseY > y_top && window.mouseY < y_bottom) {
			_newGame = "res/new_game_hover.png";			
		}
		else {
			_newGame = "res/new_game.png";
		}
		
		
		
		//pick level hover detect
		x_left = window.width/2-150;
		x_right = window.width/2+150;
		y_top = 500;
		y_bottom = 600;
		if (window.mouseX > x_left && window.mouseX < x_right && window.mouseY > y_top && window.mouseY < y_bottom) {
			_pickLevel = "res/pick_level_hover.png";
		}
		else {
			_pickLevel = "res/pick_level.png";
		}
		
		
		

		window.imageMode(window.CENTER);
		
		//title
		PImage title = window.loadImage("res/ballsy_title.png");
		window.image(title,window.width/2,300);
		
		//new game
		PImage newGame = window.loadImage(_newGame);
		window.image(newGame,window.width/2,450);
		
		//pick level
		PImage pickLevel = window.loadImage(_pickLevel);
		window.image(pickLevel,window.width/2,550);
		
	}
	
	public void mousePressed() {
		
		Window window = Window.getInstance();
		
		//new game click detect
		int x_left = window.width/2-150;
		int x_right = window.width/2+150;
		int y_top = 400;
		int y_bottom = 500;
		if (window.mouseX > x_left && window.mouseX < x_right && window.mouseY > y_top && window.mouseY < y_bottom) {
					
		}
		
		//pick level click detect
		x_left = window.width/2-150;
		x_right = window.width/2+150;
		y_top = 500;
		y_bottom = 600;
		if (window.mouseX > x_left && window.mouseX < x_right && window.mouseY > y_top && window.mouseY < y_bottom) {
			window.setScreen(new LevelMenu1());
		}
		
		
		
	}
	
	public void mouseReleased() {

	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		
	}

}










