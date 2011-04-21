package physics;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class GrappleDef extends PhysicsDef {
	
	private bodies.UserBall _ball;
	private PhysicsWorld _world;
	private DistanceJoint _joint;

	public GrappleDef(PhysicsWorld world, boolean mobile, bodies.UserBall ball) {
		super(world, mobile);
		_ball = ball;
		_world = world;
	}
	
	public void grapple() {
		if (_ball.isGrappled()) {
			bodies.AbstractBody grappledBody = _ball.getGrappleObject();
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(_ball.getBody(), grappledBody.getBody(), _ball.getWorldPosition(), _ball.getWorldGrapplePointVec());
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

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Vec2 getGrapplePoint(){
		return _joint.getAnchor2();
	}

	public Element writeXML() {
		return null; // never write XML for a grapple!
	}
}
