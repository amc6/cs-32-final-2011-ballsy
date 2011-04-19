package bodies;

/**
 * Class to model the ball controlled by the user. Manages movement, crosshair goings-on, 
 * and all other things specific to the user's ball.
 */

import graphical.Crosshair;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;

public class UserBall extends Ball {
	private float _moveCoeff = 20; // essentially the speed
	private float _maxVel = 100;
	private Crosshair _crosshair;
	private AbstractLevel _level;
	private AbstractBody _grappleObject;
	private boolean _grappled = false;
	
	public UserBall(AbstractLevel level, PhysicsWorld world, float centerX, float centerY) {
		super(level, world, centerX, centerY, 3, true);
		this.setColor(0, 0, 255);
		_crosshair = new Crosshair(world, this);
		_level = level;
	}

	// begin move control helper methods (just make things simpler, you know?)
	
	public void moveLeft() {
		if (_physicsDef.getBody().getLinearVelocity().x > -_maxVel)
			_physicsDef.applyImpulse(new Vec2(-_moveCoeff, 0));
	}
	
	public void moveRight() {
		if (_physicsDef.getBody().getLinearVelocity().x < _maxVel)
			_physicsDef.applyImpulse(new Vec2(_moveCoeff, 0));
	}
	
	public void moveDown() {
		if (_physicsDef.getBody().getLinearVelocity().y > -_maxVel)
			_physicsDef.applyImpulse(new Vec2(0, -_moveCoeff));
	}
	
	public void moveUp() {
		if (_physicsDef.getBody().getLinearVelocity().y < _maxVel)
			_physicsDef.applyImpulse(new Vec2(0, _moveCoeff));
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
		if (!_grappled) _grappleObject = getBody(_crosshair.getGrapplePoint(_level.getBodies()));
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
}
