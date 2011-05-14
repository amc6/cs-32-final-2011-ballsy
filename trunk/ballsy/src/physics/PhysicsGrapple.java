package physics;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import bodies.UserBall;

/**
 * Superclass representing the two types of grapples developed
 * for Ballsy.
 */
public abstract class PhysicsGrapple extends PhysicsDef {
	
	protected UserBall _ball;

	// Create the physics representation of a grapple with
	// a reference to the associated ball.
	public PhysicsGrapple(bodies.UserBall ball) {
		super(0,0); // Grapple has no required initial position
		_ball = ball;
	}	
	
	public abstract void grapple();
	
	public abstract void releaseGrapple();
	
	public abstract void extendGrapple();
	
	public abstract void retractGrapple();
	
	public abstract Vec2 getGrapplePoint();
	
	// Grapple information is not stored in the XML
	public Element writeXML() {
		return null;
	}

	// Grapple never needs to be created
	protected void createBody() { }
}
