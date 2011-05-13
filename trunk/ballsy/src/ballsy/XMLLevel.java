package ballsy;

/** 
 * Generic level for playing a level stored in XML Data. Is used to play all
 * saved levels. Extends from AbstractLevel, and relies on much of its functionality
 * (input and whatnot, for example)
 */

import graphics.Background;
import graphics.TrackingCamera;

import java.util.ArrayList;

import menu.MenuButton;

import physics.PhysicsWorld;
import ballsy.ScreenLoader.Screens;
import bodies.AbstractBody;

public class XMLLevel extends AbstractLevel {
	private String _path;
	private Background _background;
	private TrackingCamera _camera;
	private MenuButton _myButton;
	
	/**
	 * Instantiated with a path to load, and the button it is loaded from, if it's loaded
	 * from the choose level screen.
	 * @param path
	 * @param myButton
	 */
	public XMLLevel(String path, MenuButton myButton) {
		_path = path;
		_myButton = myButton;
		// load in the level
		this.setInstance(); // set this level as the singleton
		XMLUtil.getInstance().readFile(this, path);
		_background = new Background();
		_camera = new TrackingCamera(_player);
		
	}
	
	/**
	 * Set up the physics world & bodies with the provided bounds
	 */
	public void setupWorld(float minX, float minY, float maxX, float maxY) {
		_world = new PhysicsWorld(_window);
		_world.createWorld(minX, minY, maxX, maxY);
		_world.setGravity(_gravity.x, _gravity.y);
		_bodies = new ArrayList<AbstractBody>();
	}
	
	/**
	 * Draw loop: draw everything in the level!
	 */
	public void draw() {
		// draw window stuffs
		_window.background(_backgroundColor);
		_background.draw();
		_window.stroke(ballsy.GeneralConstants.DEFAULT_LINE_WIDTH);
		// step physics world
		if (!_paused && !_won) {
			_world.step();
		}
		// and update camera
		_camera.update();
		// display all objects
		for (AbstractBody body : _bodies) { body.display(); }
		// and apply the input (stored in boolean array)
		this.applyInput();
		// handle pausedness and wonness
		if (_paused) {
			_pauseScreen.draw();
		}else if (_won) {
			_winScreen.draw();
			_player.setCrosshairVisible(false);
			_player.setInPlay(false);
		}
	}
	
	/**
	 * Loads the level following the currently loaded one, or the level menu if this was the last.
	 */
	public void nextLevel() {
		_myButton = _myButton.getNextLevel();
		if (_myButton != null) {
			_path =_myButton.getLevelPath();
			_window.loadScreen(Screens.XML_LEVEL, _path, _myButton);
		}
		else {
			_window.loadScreen(Screens.LEVEL_MENU);
		}
	}

	/**
	 * Detects mousepressed (override).
	 * Fires the grapple if it's unfired, releases it if it's fired.
	 */
	public void mousePressed() {
		if (_paused) {
			_pauseScreen.mousePressed();
		}
		if (_won) {
			_winScreen.mousePressed();
		}
		if (!_player.isGrappled()) _player.fireGrapple();
		else _player.releaseGrapple();
	}

	/**
	 * load the level from the file again - its starting place
	 */
	public void reload() {
		this.setInstance(); // set this level as the singleton
		_window.fadeIn();
		XMLUtil.getInstance().readFile(this, _path);
		_camera = new TrackingCamera(_player);
	}
	
	/**
	 * Set this level as won - once it's won. Called from UserBall
	 */
	public void setWon() {
		super.setWon();
		_myButton.getNextLevel().setLocked(false);
		XMLUtil.getInstance().addMenuButton(_myButton.getNextLevel());
	}
	
	// necessary empty override
	public void setup() { }
}
