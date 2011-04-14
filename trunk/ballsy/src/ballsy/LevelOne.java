package ballsy;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import processing.core.PConstants;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.Rectangle;

public class LevelOne extends AbstractLevel {

	private ArrayList<AbstractBody> _bodies;
	private PhysicsWorld _world;
	private Rectangle _playerBox;
	
	@Override
	public void setup() {
		
	
		// Initialize Box2D physics and set custom gravity
		_world = new PhysicsWorld(_window);
		_world.createWorld();
		_world.setGravity(0, -20); // otherwise defaults to -10f

		_bodies = new ArrayList<AbstractBody>();
		_playerBox = new Rectangle(this, _world, _world.getCenterX(), 0);

		// Add a bunch of fixed boundaries
		float worldWidth = _world.getWidth();
		float worldHeight = _world.getHeight();
		
		System.out.println(worldHeight);
		
		System.out.println(_world.getCenterY());
		
		Rectangle top = new Rectangle(this, _world, _world.getCenterX(), 30, worldWidth - 100, 2, false);
		Rectangle bottom = new Rectangle(this, _world, _world.getCenterX(), - 30, worldWidth - 100, 2, false);
//		Rectangle left = new Rectangle()
		_bodies.add(top);
		_bodies.add(bottom);
		_bodies.add(_playerBox);
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
		
		if (_window.keyPressed){
			System.out.println("here");
			switch (_window.keyCode){
			case PConstants.LEFT:
				System.out.println("hre");
				_playerBox.applyForce(new Vec2(-35,0));
				break;
			case PConstants.RIGHT:
				_playerBox.applyForce(new Vec2(35,0));
				break;
			case PConstants.DOWN:
				_playerBox.applyForce(new Vec2(0,-35));
				break;
			case PConstants.UP:
				_playerBox.applyForce(new Vec2(0,35));
				break;				
			}
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
		_window.text("framerate: " + (int)_window.frameRate,12,46);
		
	}

	@Override
	public void remove(AbstractBody object) {
		_bodies.remove(object);		
	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		
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
