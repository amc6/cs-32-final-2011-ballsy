package ballsy;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import processing.core.PConstants;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.Rectangle;
import bodies.UserBall;

public class LevelOne extends AbstractLevel {

	private ArrayList<AbstractBody> _bodies;
	private PhysicsWorld _world;
	//private Rectangle _playerBox;
	private UserBall _player;
	
	@Override
	public void setup() {
		
	
		// Initialize Box2D physics and set custom gravity
		_world = new PhysicsWorld(_window);
		_world.createWorld();
		_world.setGravity(0, -20); // otherwise defaults to -10f

		_bodies = new ArrayList<AbstractBody>();
		//_playerBox = new Rectangle(this, _world, _world.getCenterX(), 0);
		
		// make a moving box (demo pathing)
		Rectangle movingBox = new Rectangle(this, _world, _world.getCenterX(),20);
		Vector<Point2D.Float> path = new Vector<Point2D.Float>();
		path.add(new Point2D.Float(20, 0));
		path.add(new Point2D.Float(20, -20));
		path.add(new Point2D.Float(-20, 0));
		movingBox.setPath(path);
		
		// make a user ball
		Point2D.Float startingPoint = new Point2D.Float(0, 0);
		_player = new UserBall(this, _world, startingPoint.x, startingPoint.y);

		// Add a bunch of fixed boundaries
		float worldWidth = _world.getWidth();
		float worldHeight = _world.getHeight();
		
		Rectangle top = new Rectangle(this, _world, _world.getCenterX(), 30, worldWidth - 100, 2, false);
		Rectangle bottom = new Rectangle(this, _world, _world.getCenterX(), - 30, worldWidth - 100, 2, false);
		Rectangle left = new Rectangle(this, _world, 50, _world.getCenterY(), 2, worldHeight - 100, false);
		Rectangle right = new Rectangle(this, _world, -50, _world.getCenterY(), 2, worldHeight - 100, false);
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

		// When the mouse is clicked, add a new Box object
		if (_window.mousePressed) {
			float x = _world.pixelXtoWorldX(_window.mouseX);
			float y = _world.pixelYtoWorldY(_window.mouseY);
			//Rectangle newRect = new Rectangle(this, _world, x, y);
			Ball newBall = new Ball(this, _world, x, y);
			// display the line on this guy
			((graphical.BallDef) newBall.getGraphicalDef()).setLine(true);
			_bodies.add(newBall);
		}
		
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
		_window.text((int)_window.frameRate + " FPS",12,46);
		
		// detect keypresses
		if (_window.keyPressed) {
			switch (_window.keyCode){
			case PConstants.LEFT:
				_player.moveLeft();
				break;
			case PConstants.RIGHT:
				_player.moveRight();
				break;
			case PConstants.DOWN:
				_player.moveDown();
				break;
			case PConstants.UP:
				_player.moveUp();
				break;				
			}
		}
		
	}

	@Override
	public void remove(AbstractBody object) {
		_bodies.remove(object);		
	}

	@Override
	public void keyPressed() {
		
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}

}
