package ballsy;

//import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import static bodies.BodyConstants.DEFAULT_BALL_RADIUS;
import static bodies.BodyConstants.DEFAULT_RECTANGLE_HEIGHT;
import static bodies.BodyConstants.DEFAULT_RECTANGLE_WIDTH;
import static bodies.BodyConstants.USER_RADIUS;

import graphics.Background;
import graphics.Image;
import graphics.Text;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.xml.ws.Endpoint;

import org.jbox2d.common.Vec2;

import physics.PhysicsPath;
import physics.PhysicsVertexSurface;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.EndPoint;
import bodies.IrregularPolygon;
import bodies.Rectangle;
import bodies.RegularPolygon;
import bodies.UserBall;
import bodies.VertexSurface;

public class LevelOne extends AbstractLevel {
	
	private Image _background;
	private Background _bg;
	private Text _text;
	
	@Override
	public void setup() {
		this.setInstance(); // set this level as the singleton
		
		_background = new Image(_window, "res/background1.jpg", _window.width, _window.height);
		_background.setImageMode(_window.CORNER);
		
		_bg = new Background();
		_text = new Text("THIS IS SOME TEXT DUDEEEEE", 150, 100);
		
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
		PhysicsPath pathObject = new PhysicsPath(movingBox.getPhysicsDef(), path);
		movingBox.setPath(pathObject);
		movingBox.getGraphicsDef().setColor(200, 100, 200);
		
		// make a moving box on a static path (more pathing demo)
		Rectangle movingBoxAGAIN = new Rectangle(_world.getCenterX() - 20,20,DEFAULT_RECTANGLE_WIDTH * 3,DEFAULT_RECTANGLE_HEIGHT);
		Vector<Point2D.Float> path2 = new Vector<Point2D.Float>();
		path2.add(new Point2D.Float(0, -30));
		path2.add(new Point2D.Float(0, 0));
		PhysicsPath pathObject2 = new PhysicsPath(movingBoxAGAIN.getPhysicsDef(), path2);
		// this shit is new: setStatic makes it non-interactwithable, setRotation gives it a constant rotation at every step
		pathObject2.setStatic(true);
		pathObject2.setRotation(0.05f);
		movingBoxAGAIN.setPath(pathObject2);
		movingBoxAGAIN.getGraphicsDef().setColor(100, 200, 50);

		// Add a bunch of fixed boundaries
		float worldWidth = _world.getWidth();
		float worldHeight = _world.getHeight();
		
		//Rectangle top = new Rectangle(this, _world, _world.getCenterX(), 30, worldWidth - 100, 2, false);
		Rectangle bottom = new Rectangle( _world.getCenterX(), - 30, worldWidth - 100, 2);
		bottom.getPhysicsDef().setMobile(false);
		bottom.setGrappleable(false);
//		bottom.getPhysicsDef().setFriction(100f);
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
		_bodies.add(movingBoxAGAIN);
		
		// add an endpoint
		EndPoint ep = new EndPoint(40, 0);
		_bodies.add(ep);
		
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
//		polygon.setDeadly(true);
		_bodies.add(polygon);
		
//		Working on this stuff with the vertex surface...
//		VertexSurface surface = new VertexSurface(-30,-20,PhysicsVertexSurface.generateLine(20, 1));
//		_bodies.add(surface);
		
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
	//	_window.noCursor();
		
		//background image
		_background.draw();
		
//		_bg.draw();
		_text.draw();
		
		
		if (!_paused) {
			// Step the physics world
			_world.step();
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
		
		this.applyInput();
		
		// detect keypresses
		if (_window.keyPressed) {
			// check for shits
			switch (_window.key){
			case ' ': // check for space, make stuff if appropriate
				float x = _world.pixelXtoWorldX(_window.mouseX);
				float y = _world.pixelYtoWorldY(_window.mouseY);
				Random r = new Random();
				int numSides = r.nextInt(15);
				numSides += 2;
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
			case 'q':
				System.exit(0);
				break;
			}
		}
		
		if (_paused) {
			_window.fill(100, 100);
			_window.rectMode(_window.CORNER);
			_window.rect(0, 0, _window.width, _window.height);
		}
	}

}
