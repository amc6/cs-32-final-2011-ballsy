package physics;

import java.util.Vector;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import ballsy.Window;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.Rope;
import bodies.UserBall;

public class PhysicsGrappleRope extends PhysicsGrapple {
	
	private Vector<AbstractBody> _links;
	private PhysicsWorld _world;
	private Window _window;
	private DistanceJoint _joint1, _joint2;
	private int _numLinks = 40;

	public PhysicsGrappleRope(bodies.UserBall ball) {
		super(ball); // Super class PhysicsDef requires this...how can we refactor this better?
		_window = Window.getInstance();
		_world = PhysicsWorld.getInstance();
		_links = new Vector<AbstractBody>();
	}
	
	public Vector<AbstractBody> getLinks() {
		return _links;
	}
	
	public DistanceJoint getFirstJoint() {
		return _joint1;
	}
	
	public DistanceJoint getLastJoint() {
		return _joint2;
	}
	
	public void grapple() {
		if (_ball.isGrappled()) {
			//grapple
			Body body1 = _ball.getPhysicsDef().getBody();
			Body body2 = _ball.getGrappleObject().getPhysicsDef().getBody();
			Vec2 pos1 = _ball.getWorldPosition();
			Vec2 pos2 = _ball.getWorldGrapplePointVec();
	
			Vec2 dist = pos2.sub(pos1);
			float magDist = dist.length();
			_numLinks = (int) (magDist*2);
			float dx = dist.x/_numLinks;
			float dy = dist.y/_numLinks;
			
			float x = pos1.x + dx;
			float y = pos1.y + dy;
				
			//connect body1 to first link
			Ball ball = new Ball(x, y, .5F);
			_links.add(ball);
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(body1, ball.getPhysicsDef().getBody(), pos1, ball.getWorldPosition());
			jointDef.collideConnected = false;
			_joint1 = (DistanceJoint) _world.createJoint(jointDef);
			Ball lastLink = ball;

			//build chain
			for (int i=0; i<_numLinks-2; i++) {
				x = x+dx;
				y = y+dy;
				ball = new Ball(x, y, .5F);
				_links.add(ball);
				jointDef = new DistanceJointDef();
				jointDef.initialize(lastLink.getPhysicsDef().getBody(), ball.getPhysicsDef().getBody(), lastLink.getWorldPosition(), ball.getWorldPosition());
				jointDef.collideConnected = false;
				_world.createJoint(jointDef);
				lastLink = ball;
			}
			
			//last link to body2
			jointDef = new DistanceJointDef();
			jointDef.initialize(lastLink.getPhysicsDef().getBody(), body2, lastLink.getWorldPosition(), pos2);
			jointDef.collideConnected = false;
			_joint2 = (DistanceJoint) _world.createJoint(jointDef);

		}
	}
	
	public void releaseGrapple() {
		_world.destroyJoint(_joint1);
		_world.destroyJoint(_joint2);
	
		for (int i=0; i<_numLinks-2; i++) {
			_world.destroyBody(_links.get(i).getPhysicsDef().getBody());
		}
		_links = new Vector<AbstractBody>();
	}
	
	public void extendGrapple() {
	}
	
	public void retractGrapple() {
	}

	// Required by PhysicsDef. How can we refactor this better?
	protected void createBody() {
	}
}
