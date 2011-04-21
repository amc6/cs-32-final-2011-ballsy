package ballsy;

import graphical.TrackingCamera;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import physics.PhysicsWorld;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.Rectangle;
import bodies.RegularPolygon;
import bodies.UserBall;

public class LevelTwo extends AbstractLevel {
	private PhysicsWorld _world;
	private UserBall _player;
	private TrackingCamera _camera;
	
	@Override
	public void setup() {
		// Initialize Box2D physics and set custom gravity
		_world = new PhysicsWorld(_window, 10, 0,0);//last two coords specify what world coordinates
													//you would like to initially appear at the bottem left
													//of the screen
		_world.createWorld(100, 100);//sets the size of the world. Bottem left is always 0,0
		_world.setGravity(0, -20); // otherwise defaults to -10f

		_bodies = new ArrayList<AbstractBody>();

		// Add a bunch of fixed boundaries
		//Ball center = new Ball(this, _world, _world.getCenterX(), _world.getCenterY(), 10, false);//don't set position like this anymore. centerX and centerY could change
																							//if we decided to expand the level. Hardcode values! (I'll explain more in person)
		//center.setColor(255, 0, 0);
		//Rectangle top = new Rectangle(this, _world, _world.getCenterX(), 30, worldWidth - 100, 2, false);
		Rectangle bottom = new Rectangle(this, _world, 50, 0, 100, 2, false);
		bottom.setGrappleable(false);
		Rectangle top = new Rectangle(this, _world, 50, 100, 100, 2, false);
		//Rectangle subBottom = new Rectangle(this, _world, _world.getCenterX(), -35, 4, 3, false); // this is grappleable, but not through the floor!
		//subBottom.setColor(0, 255, 255);
		Rectangle left = new Rectangle(this, _world, 0, 50, 2, 100, false);
		Rectangle right = new Rectangle(this, _world, 100, 50, 2, 100, false);
		_bodies.add(bottom);
		_bodies.add(top);
		//_bodies.add(subBottom);
		_bodies.add(left);
		_bodies.add(right);
		//_bodies.add(center);

		// make a user ball
		Point2D.Float startingPoint = new Point2D.Float(50,50);
		_player = new UserBall(this, _world, startingPoint.x, startingPoint.y);
		_bodies.add(_player);
		_camera = new TrackingCamera(_world,_player);
	}
	
	@Override
	public void draw() {
		_window.background(255);
		_window.stroke(0);
		_window.strokeWeight(ballsy.GeneralConstants.DEFAULT_LINE_WIDTH);
		_window.noCursor();
		
		// Step the physics world
		_world.step();
		
		// Display all the objects
		for (AbstractBody body : _bodies) {
			body.display();
		}

		// Remove objects that leave the screen
		for (int i = _bodies.size() - 1; i >= 0; i--) {
			AbstractBody body = _bodies.get(i);
			if (body.done()) {
				body.killBody(); // removes from physics and graphical world
			}
		}
		
		// Just drawing the framerate to see how many particles it can handle
		_window.fill(0);
		_window.textSize(20);
		//_window.text((int)_window.frameRate + " FPS",12,46);
		_window.text((int)_world.transX+", " +(int)_world.transY, 12,46);
		
		// detect keypresses
		if (_window.keyPressed) {
			// check for WASD, move accordingly
			switch (_window.key){
			case 'a':
				_player.moveLeft();
				break;
			case 'd':
				_player.moveRight();
				break;
			case 's':
				_player.moveDown();
				break;
			case 'w':
				_player.moveUp();
				break;
			
			case ' ': // check for space, make stuff if appropriate
				float x = _world.pixelXtoWorldX(_window.mouseX);
				float y = _world.pixelYtoWorldY(_window.mouseY);
				Random r = new Random();
				int numSides = r.nextInt(15);
				numSides += 2;
				if (numSides == 2) {
					Ball newBall = new Ball(this, _world, x, y);
					((graphical.BallDef) newBall.getGraphicalDef()).setLine(true);
					newBall.setColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
					_bodies.add(newBall);
				} else if (numSides > 2) {
					RegularPolygon newPoly = new RegularPolygon(this, _world, x, y, numSides, 2.5f);
					newPoly.setColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
					_bodies.add(newPoly);
				}
			}
		}
		_camera.update();
	}

	@Override
	public void remove(AbstractBody object) {
		_bodies.remove(object);		
	}

	@Override
	public void keyPressed() {

	}

	/**
	 * Detects mousepressed (override).
	 * Fires the grapple if it's unfired, releases it if it's fired.
	 */
	public void mousePressed() {
		if (!_player.isGrappled()) _player.fireGrapple();
		else _player.releaseGrapple();
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}

}
