package ballsy;

import graphical.HoverImage;
import graphical.Image;
import oldshit.Level;
import processing.core.PConstants;
import processing.core.PImage;

public class WelcomeScreen extends Screen {
	Window _window;
	Image _titleGraphic;
	HoverImage _newGraphic, _newGraphicHover, _levelsGraphic, _levelsGraphicHover;
	
	public void setup() {
		_window = Window.getInstance();
		
		_newGraphic = new HoverImage(_window, "res/new_game.png", "res/new_game_hover.png", 300, 100, _window.width/2, 450);
		_levelsGraphic = new HoverImage(_window, "res/pick_level.png", "res/pick_level_hover.png", 300, 100, _window.width/2, 550);
		_titleGraphic = new Image(_window, "res/ballsy_title.png", 661, 309, _window.width/2, 300);
	}
	
	public void draw() {
		_window.background(255,255,255);
		
		// put stuff in the middle
		_window.imageMode(PConstants.CENTER);
		
		// draw graphics
		_titleGraphic.draw();
		_levelsGraphic.draw();
		_newGraphic.draw();
		
	}
	
	public void mousePressed() {

		if (_newGraphic.mouseOver()) {
			// temporary... Matt's gonna do some shit with TestObjects.
			// set to Level for now
			_window.setScreen(new TestGrappleLevel());
		}
		
		if (_levelsGraphic.mouseOver()) {
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