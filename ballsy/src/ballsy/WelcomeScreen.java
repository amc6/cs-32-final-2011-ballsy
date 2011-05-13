package ballsy;

import graphics.HoverImage;
import graphics.ScreenBackground;
import graphics.TextButton;

import java.util.Vector;

import menu.MenuButton;
import processing.core.PConstants;
import processing.core.PImage;
import ballsy.ScreenLoader.Screens;

public class WelcomeScreen extends Screen {
	Window _window;
	PImage _titleGraphic;
	HoverImage _newGraphic, _newGraphicHover, _levelsGraphic, _levelsGraphicHover;
	TextButton _newGame, _level, _levelEditor, _exit;
	private ScreenBackground _background;
	
	public static final int DEFAULT_TEXT_COLOR = 245;
	
	public static final int ACTIVE_LINK_COLOR = Window.getInstance().color(245,184,0);
	public static final int LINK_COLOR = Window.getInstance().color(252,237,152);

	
	public void setup() {
		_window = Window.getInstance();
		_window.cursor(); // make the cursor visible
		//_newGraphic = new HoverImage(_window, "res/new_game.png", "res/new_game_hover.png", 289, 56, _window.width/2, 450);
		//_levelsGraphic = new HoverImage(_window, "res/pick_level.png", "res/pick_level_hover.png", 289, 53, _window.width/2, 520);
//		_titleGraphic = new Image(_window, "res/ballsy_title.png", 661, 309, _window.width/2, 300);
		_titleGraphic = _window.loadImage("res/ballsy_title2.png");

		_newGame = new TextButton("New Game", _window.width/2, _window.height/2 + 50, DEFAULT_TEXT_COLOR, Window.CENTER);
		_level = new TextButton("Pick Level", _window.width/2, _window.height/2 + 120, DEFAULT_TEXT_COLOR, Window.CENTER);
		_levelEditor = new TextButton("Level Editor", _window.width/2, _window.height/2 + 190, DEFAULT_TEXT_COLOR, Window.CENTER);
		_exit = new TextButton("Exit", _window.width/2, _window.height/2 + 260, DEFAULT_TEXT_COLOR, Window.CENTER);
		_newGame.setColor(LINK_COLOR);
		_level.setColor(LINK_COLOR);
		_levelEditor.setColor(LINK_COLOR);
		_exit.setColor(LINK_COLOR);
		
		_newGame.setActiveColor(ACTIVE_LINK_COLOR);
		_level.setActiveColor(ACTIVE_LINK_COLOR);
		_levelEditor.setActiveColor(ACTIVE_LINK_COLOR);
		_exit.setActiveColor(ACTIVE_LINK_COLOR);
		

		_background = new ScreenBackground();
		//so jessica can listen to her own music... we need a mute feature
//		AudioClip clip = new AudioClip("res/titlebg.wav");
//		clip.start();
	}
	
	public void draw() {
		_window.cursor();
		_window.background(50,200,200);
		_background.draw();
		
		// put stuff in the middle
		_window.imageMode(PConstants.CENTER);
		
		// draw graphics
//		_titleGraphic.draw();
		
		
		_window.image(_titleGraphic, _window.width/2, _window.height/2-70);
		_newGame.draw();
		_level.draw();
		_levelEditor.draw();
		_exit.draw();
		
	}
	
	public void mousePressed() {

		//if (_newGraphic.mouseOver()) {
		if (_newGame.mouseOver()) {
			// temporary... Matt's gonna do some shit with TestObjects.
			// set to Level for now
			//_window.setScreen(new LevelOne());
//			_window.loadScreen(Screens.LEVEL_ONE);
//			_window.loadLevel("default.xml");
			
			Vector<MenuButton> buttons = XMLUtil.getInstance().loadMenuButtons(false);
			MenuButton first = buttons.firstElement();
			_window.loadScreen(Screens.XML_LEVEL, first.getLevelPath(), first);
			
		}
		
		if (_level.mouseOver()) {
			_window.loadScreen(Screens.LEVEL_MENU);
		}
		if (_levelEditor.mouseOver()) {
			_window.loadScreen(Screens.LEVEL_EDITOR);
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