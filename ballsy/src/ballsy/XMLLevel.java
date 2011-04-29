package ballsy;

import java.util.ArrayList;

import org.jbox2d.dynamics.Body;

import physics.PhysicsWorld;
import bodies.AbstractBody;

public class XMLLevel extends AbstractLevel {
	private String _path;
	public XMLLevel(String path) {
		_path = path;
		// load in the level
		this.setInstance(); // set this level as the singleton
		XMLUtil.getInstance().readFile(this, path);
		
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
		_window.stroke(ballsy.GeneralConstants.DEFAULT_LINE_WIDTH);
		_window.noCursor();
		// step physics world
		_world.step();
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
			case 's': // up
				_player.moveDown();
				break;
			case 'w': // down
				_player.moveUp();
				break;	
			}
		}
	}

	/**
	 * Detects mousepressed (override).
	 * Fires the grapple if it's unfired, releases it if it's fired.
	 */
	public void mousePressed() {
		if (!_player.isGrappled()) _player.fireGrapple();
		else _player.releaseGrapple();
	}

	// necessary empty overrides
	public void setup() { }
}
