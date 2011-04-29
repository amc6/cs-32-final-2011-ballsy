package ballsy;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.DebugDraw;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import physics.PhysicsWorld;
import processing.core.PConstants;
import bodies.AbstractBody;
import bodies.GrappleLine;
import bodies.Rectangle;
import bodies.Rope;
import bodies.UserBall;

public class TestGrappleLevel extends AbstractLevel {

	private PhysicsWorld _world;
	//private Rectangle _playerBox;
	private UserBall _player;
	private GrappleLine _grapple;
	private Rope _rope;
	
	
	@Override
	public void setup() {
		this.setInstance(); // set this level as the singleton
		
	
		// Initialize Box2D physics and set custom gravity
		_world = new PhysicsWorld(_window);
		_world.createWorld();
		_world.setGravity(0, -20); // otherwise defaults to -10f

		_bodies = new ArrayList<AbstractBody>();
		//_playerBox = new Rectangle(_world.getCenterX(), 0);
		
		// make a moving box (demo pathing)
		Rectangle movingBox = new Rectangle(_world.getCenterX(),20, 4, 4);
		Vector<Point2D.Float> path = new Vector<Point2D.Float>();
		path.add(new Point2D.Float(20, 0));
		path.add(new Point2D.Float(20, -20));
		path.add(new Point2D.Float(-20, 0));
		movingBox.setPath(path);
		movingBox.setGrappleable(true);
		
		// make a user ball
		Point2D.Float startingPoint = new Point2D.Float(0, 0);
		_player = new UserBall(startingPoint.x, startingPoint.y, bodies.BodyConstants.USER_RADIUS);
		
		// Add a bunch of fixed boundaries
		float worldWidth = _world.getWidth();
		float worldHeight = _world.getHeight();
		
		Rectangle top = new Rectangle(_world.getCenterX(), 30, worldWidth - 100, 2);
		top.getPhysicsDef().setMobile(false);
		Rectangle bottom = new Rectangle(_world.getCenterX(), - 30, worldWidth - 100, 2);
		bottom.getPhysicsDef().setMobile(false);
		Rectangle left = new Rectangle(50, _world.getCenterY(), 2, worldHeight - 100);
		left.getPhysicsDef().setMobile(false);
		Rectangle right = new Rectangle(-50, _world.getCenterY(), 2, worldHeight - 100);
		right.getPhysicsDef().setMobile(false);
		top.setGrappleable(true);
		right.setGrappleable(true);
		left.setGrappleable(true);
		
		
		_player.getGraphicsDef().setColor(_window.color(100,100,200));

		
		//test rope
//		_rope = new Rope(_player, movingBox, _player.getWorldPosition(), movingBox.getWorldPosition());

		_bodies.add(top);
		_bodies.add(bottom);
		_bodies.add(left);
		_bodies.add(right);
		_bodies.add(_player);
		_bodies.add(movingBox);
		
	}
	
	@Override
	public void draw() {
		_window.background(255);
		
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
		
//		_rope.display();
				
		// Just drawing the framerate to see how many particles it can handle
		_window.fill(0);
		_window.textSize(20);
		_window.text((int)_window.frameRate + " FPS",12,46);
		
		// detect keypresses
		if (_window.keyPressed) {
			switch (_window.key){
			case 'a':
				_player.moveLeft();
				break;
			case 'd':
				_player.moveRight();
				break;
			case 's':
				if (_player.isGrappled()) {
					_player.extendGrapple();
				}
				//_player.moveDown();
				break;
			case 'w':
				if (_player.isGrappled()) {
					_player.retractGrapple();
				}
				//_player.moveUp();
				break;		
			}
		}
		
	}

	@Override
	public void remove(AbstractBody object) {
		_bodies.remove(object);		
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		if (!_player.isGrappled()) _player.fireGrapple();
		else _player.releaseGrapple();
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub

	}

}
