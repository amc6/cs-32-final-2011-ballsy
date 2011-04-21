package bodies;

/**
 * Class to model the ball controlled by the user. Manages movement, crosshair goings-on, 
 * and all other things specific to the user's ball.
 */

import static bodies.BodyConstants.USER_COLOR;
import static bodies.BodyConstants.USER_MAX_VELOCITY;
import static bodies.BodyConstants.USER_MOVE_COEFFICIENT;
import static bodies.BodyConstants.USER_RADIUS;
import graphical.Crosshair;
//import graphical.UserBallGraphical;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import ballsy.AbstractLevel;

public class UserBall extends Ball {

	private Crosshair _crosshair;
	private AbstractLevel _level;
	private AbstractBody _grappleObject;
	private Point2D.Float _grapplePoint;
	private boolean _grappled = false;
	private PhysicsWorld _world;
	
	public UserBall(AbstractLevel level, PhysicsWorld world, float centerX, float centerY) {
		super(level, world, centerX, centerY, USER_RADIUS, true);
//		this.setGraphicalDef(new UserBallGraphical());
//		this.setColor(USER_COLOR);
		_crosshair = new Crosshair(world, this);
		_level = level;
		_world = world;
	}

	// begin move control helper methods (just make things simpler, you know?)
	
	public void moveLeft() {
		if (_physicsDef.getBody().getLinearVelocity().x > - USER_MAX_VELOCITY)
			_physicsDef.applyImpulse(new Vec2(-USER_MOVE_COEFFICIENT, 0));
	}
	
	public void moveRight() {
		if (_physicsDef.getBody().getLinearVelocity().x < USER_MAX_VELOCITY)
			_physicsDef.applyImpulse(new Vec2(USER_MOVE_COEFFICIENT, 0));
	}
	
	public void moveDown() {
		if (_physicsDef.getBody().getLinearVelocity().y > -USER_MAX_VELOCITY)
			_physicsDef.applyImpulse(new Vec2(0, -USER_MOVE_COEFFICIENT));
	}
	
	public void moveUp() {
		if (_physicsDef.getBody().getLinearVelocity().y < USER_MAX_VELOCITY)
			_physicsDef.applyImpulse(new Vec2(0, USER_MOVE_COEFFICIENT));
	}
	
	// end move control methods
	
	/**
	 * Partially overrides the display of the superclass (Ball).
	 * Adds the functionality of the crosshair, causing it to display (display())
	 * and check for grappleability of object, if any is in range of the grapple gun
	 * (getGrapplePoint()), and setting that to _grappleObject.
	 */
	public void display() {
		super.display();
		_crosshair.display();
		if (!_grappled) {
			_grapplePoint = _crosshair.getGrapplePoint(_level.getBodies());
			_grappleObject = getBody(_grapplePoint);
		}
	}
	
	
	public void fireGrapple() {
		if (_grappleObject != null) {
			_crosshair.hide();
			_grappled = true;
			System.out.println("Grappling hook coming soon!");
		}
	}
	
	public void releaseGrapple() {
		_crosshair.show();
		_grappled = false;
	}
	
	public boolean isGrappled() {
		return _grappled;
	}
	
	/**
	 * 
	 * @return current grappling point
	 */
	public Point2D.Float getWorldGrapplePoint() {
		return _grapplePoint;
	}
	
	/**
	 * 
	 * @return current grappling point
	 */
	public Point2D.Float getPixelGrapplePoint() {
		return new Point2D.Float(_world.worldXtoPixelX(_grapplePoint.x), _world.worldYtoPixelY(_grapplePoint.y));
	}
	
	/**
	 * Helper method to find which object contains a given point. Returns null if no containment.
	 * @param point
	 * @return
	 */
	private AbstractBody getBody(Point2D.Float point) {
		if (point == null) return null; // if passed a null point, output should be null
		ArrayList<AbstractBody> list = _level.getBodies();
		for (AbstractBody b : list) {
			// iterate through all bodies, checking for containment of point
			if (b.getPhysicsDef().getBody().getShapeList().testPoint(b.getPhysicsDef().getBody().getXForm(), new Vec2(point.x, point.y))) {
				return b;
			}
		}
		return null; // it didn't find an object, return null
	}
	
	/**
	 * partially override the writeXML() method of super to identify this as a USER ball
	 */
	public Element writeXML() {
		return super.writeXML("user_ball");
	}
}
