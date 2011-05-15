package ballsy;

/** 
 * Generic level for playing a level stored in XML Data. Is used to play all
 * saved levels. Extends from AbstractLevel, and relies on much of its functionality
 * (input and whatnot, for example)
 */

import graphics.Background;
import graphics.Text;
import graphics.TrackingCamera;

import java.util.ArrayList;

import menu.MenuButton;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import processing.core.PConstants;
import ballsy.ScreenLoader.Screens;
import bodies.AbstractBody;
import bodies.BodyConstants;

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
		
		// If first level, display controls as a tutorial
		if (_path.contains("builtin_levels/LevelOne-Final.ball")){
			
			Vec2 point = new Vec2(112,70);
			Text msg1 = new Text("Ballsy Controls", (int) _world.coordWorldToPixels(point).x, (int) _world.coordWorldToPixels(point).y);
			msg1.setColor(_window.color(0));
			msg1.setSize(38);
			
			point.addLocal(0, -5);
			Text msg2 = new Text("A/LEFT: Roll or Sway Left", (int) _world.coordWorldToPixels(point).x, (int) _world.coordWorldToPixels(point).y);
			msg2.setColor(_window.color(255));
			msg2.setSize(30);
						
			point.addLocal(0, -4);
			Text msg3 = new Text("D/RIGHT: Roll or Sway Right", (int) _world.coordWorldToPixels(point).x, (int) _world.coordWorldToPixels(point).y);
			msg3.setColor(_window.color(255));
			msg3.setSize(30);
			
			point.addLocal(0, -4);
			Text msg6 = new Text("CLICK: Fire Grapple", (int) _world.coordWorldToPixels(point).x, (int) _world.coordWorldToPixels(point).y);
			msg6.setColor(_window.color(255));
			msg6.setSize(30);
			
			point.addLocal(0, -4);
			Text msg4 = new Text("W/UP: Retract Grapple", (int) _world.coordWorldToPixels(point).x, (int) _world.coordWorldToPixels(point).y);
			msg4.setColor(_window.color(255));
			msg4.setSize(30);
			
			point.addLocal(0, -4);
			Text msg5 = new Text("S/DOWN: Extend Grapple", (int) _world.coordWorldToPixels(point).x, (int) _world.coordWorldToPixels(point).y);
			msg5.setColor(_window.color(255));
			msg5.setSize(30);
			
			msg1.draw();
			msg2.draw();
			msg3.draw();
			msg6.draw();
			msg4.draw();
			msg5.draw();
			
		}
		
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
