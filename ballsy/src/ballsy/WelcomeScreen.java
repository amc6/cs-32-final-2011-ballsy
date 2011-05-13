package ballsy;

/** 
 * The screen which the user sees first, allowing a "new game", selection of level,
 * access of the level editor, and exit of ballsy. Has awesome graphical background, 
 * with clouds and whatnot
 */

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
	// constants pertaining to this screen specifically
	public static final int DEFAULT_TEXT_COLOR = 245;
	public static final int ACTIVE_LINK_COLOR = Window.getInstance().color(245,184,0);
	public static final int LINK_COLOR = Window.getInstance().color(252,237,152);

	/**
	 * prepare the screen
	 */
	public void setup() {
		_window = Window.getInstance();
		_window.cursor(); 
		_titleGraphic = _window.loadImage("res/ballsy_title2.png");
		// setup the buttons
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
	}
	
	/**
	 * handle drawing stuff, including background and menu buttons
	 */
	public void draw() {
		_window.cursor();
		_window.background(50,200,200);
		_background.draw();
		
		// put stuff in the middle
		_window.imageMode(PConstants.CENTER);
		
		_window.image(_titleGraphic, _window.width/2, _window.height/2-70);
		_newGame.draw();
		_level.draw();
		_levelEditor.draw();
		_exit.draw();
		
	}
	
	/**
	 * Manage mouse press, taking th eproper action
	 */
	public void mousePressed() {
		// load a new game
		if (_newGame.mouseOver()) {
			Vector<MenuButton> buttons = XMLUtil.getInstance().loadMenuButtons(false);
			MenuButton first = buttons.firstElement();
			_window.loadScreen(Screens.XML_LEVEL, first.getLevelPath(), first);
		}
		// open the level viewer
		if (_level.mouseOver()) {
			_window.loadScreen(Screens.LEVEL_MENU);
		}
		// open the level editor
		if (_levelEditor.mouseOver()) {
			_window.loadScreen(Screens.LEVEL_EDITOR);
		}
		// exit Ballsy
		if (_exit.mouseOver()) {
			System.exit(0);
		}
	}
	
	public void mouseReleased() { }

	@Override
	public void keyPressed() { }

	@Override
	public void keyReleased() { }

	@Override
	public void mouseDragged() { }
}