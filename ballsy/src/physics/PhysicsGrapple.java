package physics;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import bodies.UserBall;

public abstract class PhysicsGrapple extends PhysicsDef {
	
	protected UserBall _ball;

	public PhysicsGrapple(bodies.UserBall ball) {
		super(0,0); //FIX THIS!!1AODIFJAOPIFJAOPEIFJOAIEJFOAIJEF
		// TODO Auto-generated constructor stub
		_ball = ball;
	}
	
	
	public abstract void grapple();
	
	public abstract void releaseGrapple();
	
	public abstract void extendGrapple();
	
	public abstract void retractGrapple();
	
	public Element writeXML() {
		return null;
	}

	// Required by PhysicsDef. How can we refactor this better?
	protected void createBody() { }
}
