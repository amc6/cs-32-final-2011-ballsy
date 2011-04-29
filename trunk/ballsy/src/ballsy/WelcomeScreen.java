package ballsy;

import graphics.HoverImage;
import graphics.Image;
import processing.core.PConstants;

public class WelcomeScreen extends Screen {
	Window _window;
	Image _titleGraphic;
	HoverImage _newGraphic, _newGraphicHover, _levelsGraphic, _levelsGraphicHover;
	
	public void setup() {
		_window = Window.getInstance();
		_window.cursor(); // make the cursor visible
		_newGraphic = new HoverImage(_window, "res/new_game.png", "res/new_game_hover.png", 289, 56, _window.width/2, 450);
		_levelsGraphic = new HoverImage(_window, "res/pick_level.png", "res/pick_level_hover.png", 289, 53, _window.width/2, 520);
		_titleGraphic = new Image(_window, "res/ballsy_title.png", 661, 309, _window.width/2, 300);

		//so jessica can listen to her own music... we need a mute feature
//		AudioClip clip = new AudioClip("res/titlebg.wav");
//		clip.start();
	}
	
	public void draw() {
		_window.background(50,200,200);
		
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
			_window.setScreen(new LevelOne());
//			_window.loadLevel("default.xml");
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

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged() {
		// TODO Auto-generated method stub
		
	}
}