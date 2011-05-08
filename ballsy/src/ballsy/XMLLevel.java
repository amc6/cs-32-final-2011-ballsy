package ballsy;

import graphics.Background;
import graphics.TrackingCamera;

import java.util.ArrayList;

import physics.PhysicsWorld;
import bodies.AbstractBody;

public class XMLLevel extends AbstractLevel {
	private String _path;
	private Background _background;
	private TrackingCamera _camera;
	
	public XMLLevel(String path) {
		_path = path;
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
		if (!_paused) {
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
	}

	/**
	 * Detects mousepressed (override).
	 * Fires the grapple if it's unfired, releases it if it's fired.
	 */
	public void mousePressed() {
		if (_paused) {
			_pauseScreen.mousePressed();
		}
		if (!_player.isGrappled()) _player.fireGrapple();
		else _player.releaseGrapple();
	}

	/**
	 * load the level from the file again - its starting place
	 */
	public void reload() {
		this.setInstance(); // set this level as the singleton
		XMLUtil.getInstance().readFile(this, _path);
		_background = new Background();
		_camera = new TrackingCamera(_player);
	}
	
	// necessary empty overrides
	public void setup() { }
}
