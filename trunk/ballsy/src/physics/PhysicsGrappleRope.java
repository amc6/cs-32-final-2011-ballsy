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
import bodies.Link;
import bodies.Rope;
import bodies.UserBall;
import static ballsy.GeneralConstants.*;
import static bodies.BodyConstants.*;


public class PhysicsGrappleRope extends PhysicsGrapple {
	
	private Vector<Link> _links;
	private PhysicsWorld _world;
	private Window _window;
	private DistanceJoint _firstJoint, _secondJoint, _lastJoint;
	private int _numLinks = 40;
	private float _maxLenth = CROSSHAIR_RANGE;

	public PhysicsGrappleRope(bodies.UserBall ball) {
		super(ball); // Super class PhysicsDef requires this...how can we refactor this better?
		_window = Window.getInstance();
		_world = PhysicsWorld.getInstance();
		_links = new Vector<Link>();
	}
	
	public Vector<Link> getLinks() {
		return _links;
	}
	
	public DistanceJoint getFirstJoint() {
		return _firstJoint; //connects ball to rope
	}
	
	public DistanceJoint getLastJoint() {
		return _lastJoint; //connects rope to object
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
			//System.out.println("numlinks: " + _numLinks);
			float dx = dist.x/_numLinks;
			float dy = dist.y/_numLinks;
			
			float x = pos1.x + dx;
			float y = pos1.y + dy;
				
			//connect body1 to first link
			Link link = new Link(x, y);
			_links.add(link);
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(body1, link.getPhysicsDef().getBody(), pos1, link.getWorldPosition());
			jointDef.collideConnected = false;
			_firstJoint = (DistanceJoint) _world.createJoint(jointDef);
			link.setPrev(_ball, _firstJoint); //set link refs
			Link lastLink = link;
			
			//second joint
			x = x+dx;
			y = y+dy;
			link = new Link(x, y);
			_links.add(link);
			jointDef = new DistanceJointDef();
			jointDef.initialize(lastLink.getPhysicsDef().getBody(), link.getPhysicsDef().getBody(), lastLink.getWorldPosition(), link.getWorldPosition());
			jointDef.collideConnected = false;
			_secondJoint = (DistanceJoint) _world.createJoint(jointDef);
			lastLink.setNext(link, _secondJoint); //set link refs
			link.setPrev(lastLink, _secondJoint); //set link refs
			lastLink = link;
			
			//build chain
			for (int i=1; i<_numLinks-2; i++) {
				x = x+dx;
				y = y+dy;
				link = new Link(x, y);
				_links.add(link);
				jointDef = new DistanceJointDef();
				jointDef.initialize(lastLink.getPhysicsDef().getBody(), link.getPhysicsDef().getBody(), lastLink.getWorldPosition(), link.getWorldPosition());
				jointDef.collideConnected = false;
				DistanceJoint joint = (DistanceJoint) _world.createJoint(jointDef);
				lastLink.setNext(link, joint); //set link refs
				link.setPrev(lastLink, joint); //set link refs
				lastLink = link;
			}
			
			//last link to body2
			jointDef = new DistanceJointDef();
			jointDef.initialize(lastLink.getPhysicsDef().getBody(), body2, lastLink.getWorldPosition(), pos2);
			jointDef.collideConnected = false;
			_lastJoint = (DistanceJoint) _world.createJoint(jointDef);
			lastLink.setNext(_ball.getGrappleObject(), _lastJoint); //set last link ref
		}
	}
	
	public void releaseGrapple() {
		_world.destroyJoint(_firstJoint);
		_world.destroyJoint(_lastJoint);
	
		for (int i=0; i<_links.size()-1; i++) {
			_world.destroyBody(_links.get(i).getPhysicsDef().getBody());
		}
		_links = new Vector<Link>();
	}
	
	public void extendGrapple() {
		Body ballBody = _ball.getPhysicsDef().getBody();
		Vec2 ballAnchor = _ball.getWorldPosition();
		
		Link firstLink = _links.get(0);
		Vec2 firstLinkAnchor = firstLink.getWorldPosition();
		
		_world.destroyJoint(_firstJoint);
		
		float dist = ballAnchor.sub(firstLinkAnchor).length();
		Vec2 midpt = ballAnchor.add(firstLinkAnchor).mul(.5F);
		
		Link linkToInsert = new Link(midpt.x, midpt.y);
		_links.add(0,linkToInsert); //preserve order
		
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.initialize(ballBody, linkToInsert.getPhysicsDef().getBody(), ballAnchor, linkToInsert.getWorldPosition());
		jointDef.collideConnected = false;
		_firstJoint = (DistanceJoint) _world.createJoint(jointDef);
		_firstJoint.m_length = LINK_DIST;
		linkToInsert.setPrev(_ball, _firstJoint);
		
		jointDef = new DistanceJointDef();
		jointDef.initialize(linkToInsert.getPhysicsDef().getBody(), firstLink.getPhysicsDef().getBody(), linkToInsert.getWorldPosition(), firstLinkAnchor);
		jointDef.collideConnected = false;
		DistanceJoint joint2 = (DistanceJoint) _world.createJoint(jointDef);
		joint2.m_length = LINK_DIST;	
		linkToInsert.setNext(firstLink, joint2);
		firstLink.setPrev(linkToInsert, joint2);

	}
	
	public void retractGrapple() {
		if (_links.size() > 1) {
			Link linkToRemove = _links.get(0);
			_links.remove(0);//remove from vector
			
			_world.destroyJoint(linkToRemove.getPrevJoint());
			_world.destroyJoint(linkToRemove.getNextJoint());
			
			Link nextLink = (Link) linkToRemove.getNext();
	
			Body ballBody = _ball.getPhysicsDef().getBody(); //ball
			Vec2 ballAnchor = _ball.getWorldPosition();
			
			Vec2 direction = nextLink.getWorldPosition().sub(ballAnchor).mul(1.2f); //give ball a lil push
			_ball.getPhysicsDef().applyImpulse(direction);
			
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.initialize(ballBody, nextLink.getPhysicsDef().getBody(), ballAnchor, nextLink.getWorldPosition());
			jointDef.collideConnected = false;
			_firstJoint = (DistanceJoint) _world.createJoint(jointDef);
			_firstJoint.m_length = LINK_DIST;
			
			nextLink.setPrev(_ball, _firstJoint);
	
			_world.destroyBody(linkToRemove.getPhysicsDef().getBody());
		}
	}

	// Required by PhysicsDef. How can we refactor this better?
	protected void createBody() {
	}

	@Override
	public Vec2 getGrapplePoint() {
		// TODO Auto-generated method stub
		return null;
	}
}
