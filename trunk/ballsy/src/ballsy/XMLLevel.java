package ballsy;

import graphics.Background;
import graphics.TrackingCamera;

import java.util.ArrayList;

import menu.MenuButton;

import physics.PhysicsWorld;
import ballsy.ScreenLoader.Screens;
import bodies.AbstractBody;

public class XMLLevel extends AbstractLevel {
	private String _path, _nextLevelPath;
	private Background _background;
	private TrackingCamera _camera;
	private MenuButton _myButton;
	
	public XMLLevel(String path, MenuButton myButton) {
		_path = path;
		_myButton = myButton;
		// load in the level
		this.setInstance(); // set this level as the singleton
		XMLUtil.getInstance().readFile(this, path);
		_background = new Background();
		_camera = new TrackingCamera(_player);
	}
	
	public void setupWorld(float minX, float minY, float maxX, float maxY) {
		// set up the physics world && bodies
		_world = new PhysicsWorld(_window);
		_world.createWorld(minX, minY, maxX, maxY);
		_world.setGravity(_gravity.x, _gravity.y);
		_bodies = new ArrayList<AbstractBody>();
	}
	
	public void draw() {
		// draw window stuffs
		_window.background(_backgroundColor);
		_background.draw();
		_window.stroke(ballsy.GeneralConstants.DEFAULT_LINE_WIDTH);
		_window.noCursor();
		// step physics world
		if (!_paused && !_won) {
			_world.step();
		}
		_camera.update();
		// display all objects
		for (AbstractBody body : _bodies) { body.display(); }
		// handle keypresses
		if (_window.keyPressed) {
			// check for WASD, move accordingly
			switch (_window.key){
			case 'a': // left
				_player.moveLeft();
				break;
			case 'd': // right
				_player.moveRight();
				break;
			case 's': // extend
				if (_player.isGrappled()) {
					_player.extendGrapple();
				}
				break;
			case 'w': // retract
				if (_player.isGrappled()) {
					_player.retractGrapple();
				}
				break;	
			}
		}
		if (_paused) {
			_pauseScreen.draw();
		}
		if (_won) {
			_winScreen.draw();
			_player.setCrosshairVisible(false);
		}
	}
	
	public void nextLevel() {
		_myButton = _myButton.getNextLevel();
		if (_myButton != null) {
			_path =_myButton.getLevelPath();
			_won = false;
			this.reload();
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
		_window.fadeOutAndIn();
		XMLUtil.getInstance().readFile(this, _path);
//		_background = new Background(); causes a little bug... how do we fix this?
		_camera = new TrackingCamera(_player);
	}
	
	// necessary empty overrides
	public void setup() { }
}
