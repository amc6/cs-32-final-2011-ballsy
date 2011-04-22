package ballsy;

//import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.IrregularPolygon;
import bodies.Rectangle;
import bodies.RegularPolygon;
import bodies.UserBall;
import bodies.VertexSurface;

import static bodies.BodyConstants.*;

public class LevelOne extends AbstractLevel {
	
	@Override
	public void setup() {
		this.setInstance(); // set this level as the singleton
		
		// Initialize Box2D physics and set custom gravity

		_world.createWorld();
		_world.setGravity(0, -20); // otherwise defaults to -10f

		_bodies = new ArrayList<AbstractBody>();
		
		// make a moving box (demo pathing)
		Rectangle movingBox = new Rectangle(_world.getCenterX(),20,DEFAULT_RECTANGLE_WIDTH,DEFAULT_RECTANGLE_HEIGHT);
		Vector<Point2D.Float> path = new Vector<Point2D.Float>();
		path.add(new Point2D.Float(20, 0));
		path.add(new Point2D.Float(20, -20));
		path.add(new Point2D.Float(-20, 0));
		movingBox.setPath(path);
		movingBox.getGraphicsDef().setColor(200, 100, 200);

		// Add a bunch of fixed boundaries
		float worldWidth = _world.getWidth();
		float worldHeight = _world.getHeight();
		
		//Rectangle top = new Rectangle(this, _world, _world.getCenterX(), 30, worldWidth - 100, 2, false);
		Rectangle bottom = new Rectangle( _world.getCenterX(), - 30, worldWidth - 100, 2);
		bottom.getPhysicsDef().setMobile(false);
		bottom.setGrappleable(false);
		Rectangle subBottom = new Rectangle(_world.getCenterX(), -35, 4, 3); // this is grappleable, but not through the floor!
		subBottom.getPhysicsDef().setMobile(false);
		subBottom.getGraphicsDef().setColor(0, 255, 255);
		Rectangle left = new Rectangle(50, _world.getCenterY(), 2, worldHeight - 100);
		left.getPhysicsDef().setMobile(false);
		Rectangle right = new Rectangle(-50, _world.getCenterY(), 2, worldHeight - 100);
		right.getPhysicsDef().setMobile(false);
		bottom.getGraphicsDef().setColor(200);
		subBottom.getGraphicsDef().setColor(200);
		left.getGraphicsDef().setColor(200);
		right.getGraphicsDef().setColor(200);
		//_bodies.add(top);
		_bodies.add(bottom);
		_bodies.add(subBottom);
		_bodies.add(left);
		_bodies.add(right);
		_bodies.add(movingBox);
		
		ArrayList<Vec2> worldPoints = new ArrayList<Vec2>();
		worldPoints.add(new Vec2(0,-10));
		worldPoints.add(new Vec2(5,-5));
		worldPoints.add(new Vec2(7,0));
		worldPoints.add(new Vec2(5,5));
		worldPoints.add(new Vec2(0,10));
		worldPoints.add(new Vec2(-5,5));
		worldPoints.add(new Vec2(-5,-5));		
		IrregularPolygon polygon = new IrregularPolygon(30, 20, worldPoints);
		polygon.getGraphicsDef().setColor(200, 200, 100);
		_bodies.add(polygon);
		
//		pretty ghetto implementation of the surface right now...
//		ArrayList<Vec2> surfacePoints = new ArrayList<Vec2>();
//		
//		surfacePoints.add(new Vec2(-20,10));
//		surfacePoints.add(new Vec2(-25,5));
//		surfacePoints.add(new Vec2(-25,-5));
//		surfacePoints.add(new Vec2(-15,-5));
//		surfacePoints.add(new Vec2(-15,5));
//		
//		//VertexSurface surface = new VertexSurface(this, _world, surfacePoints);
//		//_bodies.add(surface);
		
		// make a user ball
		Vec2 startingPoint = new Vec2(0, 0);
		_player = new UserBall(startingPoint.x, startingPoint.y, USER_RADIUS);
		_player.getGraphicsDef().setColor(100, 200, 200);
		_bodies.add(_player);
	}
	
	@Override
	public void draw() {
		_window.background(255);
		_window.stroke(0);
//		_window.strokeWeight(DEFAULT_LINE_WIDTH);
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
		_window.text((int)_window.frameRate + " FPS",12,46);
		
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
			case ' ': // check for space, make stuff if appropriate
				float x = _world.pixelXtoWorldX(_window.mouseX);
				float y = _world.pixelYtoWorldY(_window.mouseY);
				Random r = new Random();
				int numSides = r.nextInt(15);
				numSides += 2;
				numSides = 2;
				if (numSides == 2) {
					Ball newBall = new Ball(x, y, DEFAULT_BALL_RADIUS);
					((graphics.GraphicsBall) newBall.getGraphicsDef()).setLine(true);
					newBall.getGraphicsDef().setColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
					_bodies.add(newBall);
				} else if (numSides > 2) {
					RegularPolygon newPoly = new RegularPolygon(x, y, numSides, 2.5f);
					newPoly.getGraphicsDef().setColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
					_bodies.add(newPoly);
				}
				break;
			case 'o': // save an xml file of the current state
				XMLUtil.getInstance().writeFile(this, "default.xml");
				System.out.println("Level state saved to default.xml");
				break;
			case 'i': // restore state from xml file
				XMLUtil.getInstance().readFile(this, "default.xml");
				System.out.println("Level state restored from default.xml");
				break;
			}
		}
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
