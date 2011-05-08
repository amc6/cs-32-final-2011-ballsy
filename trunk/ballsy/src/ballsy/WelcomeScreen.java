package ballsy;

import graphics.HoverImage;
import graphics.Image;
import graphics.TextButton;
import processing.core.PConstants;
import ballsy.ScreenLoader.Screens;

public class WelcomeScreen extends Screen {
	Window _window;
	Image _titleGraphic;
	HoverImage _newGraphic, _newGraphicHover, _levelsGraphic, _levelsGraphicHover;
	TextButton _newGame, _level, _exit;
	
	public static final int DEFAULT_TEXT_COLOR = 245;
	
	public void setup() {
		_window = Window.getInstance();
		_window.cursor(); // make the cursor visible
		//_newGraphic = new HoverImage(_window, "res/new_game.png", "res/new_game_hover.png", 289, 56, _window.width/2, 450);
		//_levelsGraphic = new HoverImage(_window, "res/pick_level.png", "res/pick_level_hover.png", 289, 53, _window.width/2, 520);
		_titleGraphic = new Image(_window, "res/ballsy_title.png", 661, 309, _window.width/2, 300);
		_newGame = new TextButton("New Game", _window.width/2, 450, DEFAULT_TEXT_COLOR, Window.CENTER);
		_level = new TextButton("Pick Level", _window.width/2, 520, DEFAULT_TEXT_COLOR, Window.CENTER);
		_exit = new TextButton("Exit", _window.width/2, 585, 235, Window.CENTER);

		//so jessica can listen to her own music... we need a mute feature
//		AudioClip clip = new AudioClip("res/titlebg.wav");
//		clip.start();
	}
	
	public void draw() {
		_window.cursor();
		_window.background(50,200,200);
		
		// put stuff in the middle
		_window.imageMode(PConstants.CENTER);
		
		// draw graphics
		_titleGraphic.draw();
		_newGame.draw();
		_level.draw();
		//_levelsGraphic.draw();
		//_newGraphic.draw();
		_exit.draw();
		
	}
	
	public void mousePressed() {

		//if (_newGraphic.mouseOver()) {
		if (_newGame.mouseOver()) {
			// temporary... Matt's gonna do some shit with TestObjects.
			// set to Level for now
			//_window.setScreen(new LevelOne());
			_window.loadScreen(Screens.LEVEL_ONE);
//			_window.loadLevel("default.xml");
		}
		
		//if (_levelsGraphic.mouseOver()) {
		if (_level.mouseOver()) {
			_window.setScreenAndSetup(new menu.Menu());

		}
		if (_exit.mouseOver()) {
			System.exit(0);
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