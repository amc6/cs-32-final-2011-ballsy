package physics;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class PhysicsGrapple extends PhysicsDef {
	
	private bodies.UserBall _ball;
	private DistanceJoint _joint;

	public PhysicsGrapple(bodies.UserBall ball) {
		super(0,0); // Super class PhysicsDef requires this...how can we refactor this better?
		_ball = ball;
	}
	
	public void grapple() {
		if (_ball.isGrappled()) {
			bodies.AbstractBody grappledBody = _ball.getGrappleObject();
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(_ball.getPhysicsDef().getBody(), grappledBody.getPhysicsDef().getBody(), _ball.getWorldPosition(), _ball.getWorldGrapplePointVec());
			jointDef.collideConnected = true;
			_joint = (DistanceJoint) _world.createJoint(jointDef);
		}
	}
	
	public void releaseGrapple() {
		System.out.println("joint: " + _joint);

		if (_joint != null) {
			_world.destroyJoint(_joint);
		}
	}
	
	public void extendGrapple() {
		_joint.m_length = _joint.m_length + .5F;
	}
	
	public void retractGrapple() {
		_joint.m_length = _joint.m_length - .5F;
	}
	
	public Vec2 getGrapplePoint(){
		return _joint.getAnchor2();
	}

	public Element writeXML() {
		return null; // never write XML for a grapple!
	}

	// Required by PhysicsDef. How can we refactor this better?
	protected void createBody() {
	}
}
