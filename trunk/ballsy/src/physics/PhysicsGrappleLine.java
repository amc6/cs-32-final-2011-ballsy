package physics;

import static bodies.BodyConstants.CROSSHAIR_RANGE;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

/**
 * Represents the grappling hook in the physics world.
 */
public class PhysicsGrappleLine extends PhysicsGrapple {
	
	private DistanceJoint _joint;

	// Grapple line properties
	private static final float FREQ = 1f;
	private static final float DAMPING = .9f;

	public PhysicsGrappleLine(bodies.UserBall ball) {
		super(ball);
	}
	
	/**
	 * Generates the joint for the grappling hook.
	 */
	public void grapple() {
		// If the ball has been set to currently using its
		// grapple, create the joint in the physics world.
		if (_ball.isGrappled()) {		
			bodies.AbstractBody grappledBody = _ball.getGrappleObject();
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(_ball.getPhysicsDef().getBody(), grappledBody.getPhysicsDef().getBody(), _ball.getWorldPosition(), _ball.getWorldGrapplePointVec());
			jointDef.collideConnected = true;
			jointDef.frequencyHz = FREQ;
			jointDef.dampingRatio = DAMPING;
			_joint = (DistanceJoint) _world.createJoint(jointDef);
			_joint.m_length -= 3;
		}
	}
	
	/**
	 * Remove the joint in the physics world, releasing the grapple.
	 */
	public void releaseGrapple() {
		if (_joint != null) {
			_world.destroyJoint(_joint);
		}
	}
	
	/**
	 * Extend the grapple by extending its length.
	 */
	public void extendGrapple() {
		if (_joint.m_length < CROSSHAIR_RANGE) {
			_joint.m_length = _joint.m_length + .5F;
		}
	}
	
	/**
	 * Retract the grapple by decreasing the joint length.
	 */
	public void retractGrapple() {
		// Don't let the grapple get too short
		if (_joint.m_length > ((PhysicsBall) _ball.getPhysicsDef()).getRadius()){
			_joint.m_length = _joint.m_length - .5F;
		}
	}
	
	/**
	 * Returns the point in the physics world to which the ball is grappled.
	 */
	public Vec2 getGrapplePoint(){
		return _joint.getAnchor2();
	}
}
