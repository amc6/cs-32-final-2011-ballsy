package physics;

import static bodies.BodyConstants.LINK_DIST;

import java.util.Vector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import bodies.Link;


/**
 * Represents a slack, rope-like grappling hook unseen in the
 * first release of Ballsy. Ultimately the ease of controlling the 
 * line grapple was better than the rope, and so we cut this 
 * feature.
 */
public class PhysicsGrappleRope extends PhysicsGrapple {
	
	private Vector<Link> _links;
	private DistanceJoint _firstJoint, _secondJoint, _lastJoint;
	private int _numLinks = 40;
	
	private static final float LINK_PER_DISTANCE = 2f;
	private static final float FREQ = 0f;
	private static final float DAMPING = 1f;

	
	/**
	 * Associates the rope grapple with a ball and instantiates
	 * a list of physics links.
	 */
	public PhysicsGrappleRope(bodies.UserBall ball) {
		super(ball);
		_links = new Vector<Link>();
	}
	
	/**
	 * @return the list of links of this grapple
	 */
	public Vector<Link> getLinks() {
		return _links;
	}
	
	/**
	 * @return the first joint connecting the ball to the rope
	 */
	public DistanceJoint getFirstJoint() {
		return _firstJoint; //connects ball to rope
	}
	
	/**
	 * @return the last joint connecting the rope to the object
	 */
	public DistanceJoint getLastJoint() {
		return _lastJoint; //connects rope to object
	}
	
	/**
	 * Establishes the chain between the ball and its opposite object.
	 */
	public void grapple() {
		if (_ball.isGrappled()) {
			//grapple
			Body body1 = _ball.getPhysicsDef().getBody();
			Body body2 = _ball.getGrappleObject().getPhysicsDef().getBody();
			Vec2 pos1 = _ball.getWorldPosition();
			Vec2 pos2 = _ball.getWorldGrapplePointVec();
	
			Vec2 dist = pos2.sub(pos1);
			float magDist = dist.length();
			_numLinks = (int) (magDist*LINK_PER_DISTANCE);
			float dx = dist.x/_numLinks;
			float dy = dist.y/_numLinks;
			
			float x = pos1.x + dx;
			float y = pos1.y + dy;
				
			//connect body1 to first link
			Link link = new Link(x, y);
			_links.add(link);
			DistanceJointDef jointDef = new DistanceJointDef();
			jointDef.frequencyHz = FREQ;
			jointDef.dampingRatio = DAMPING;
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
			jointDef.frequencyHz = FREQ;
			jointDef.dampingRatio = DAMPING;
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
				jointDef.frequencyHz = FREQ;
				jointDef.dampingRatio = DAMPING;
				DistanceJoint joint = (DistanceJoint) _world.createJoint(jointDef);
				lastLink.setNext(link, joint); //set link refs
				link.setPrev(lastLink, joint); //set link refs
				lastLink = link;
			}
			
			//last link to body2
			jointDef = new DistanceJointDef();
			jointDef.initialize(lastLink.getPhysicsDef().getBody(), body2, lastLink.getWorldPosition(), pos2);
			jointDef.collideConnected = false;
			jointDef.frequencyHz = FREQ;
			jointDef.dampingRatio = DAMPING;
			_lastJoint = (DistanceJoint) _world.createJoint(jointDef);
			lastLink.setNext(_ball.getGrappleObject(), _lastJoint); //set last link ref
		}
	}
	
	/**
	 * Releases the grapple by destroying the links and joints in the world.
	 */
	public void releaseGrapple() {
		_world.destroyJoint(_firstJoint);
		_world.destroyJoint(_lastJoint);
	
		for (int i=0; i<_links.size()-1; i++) {
			_world.destroyBody(_links.get(i).getPhysicsDef().getBody());
		}
		_links = new Vector<Link>();
	}
	
	/**
	 * Extends the grapple by adding links in the world.
	 */
	public void extendGrapple() {
		Body ballBody = _ball.getPhysicsDef().getBody();
		Vec2 ballAnchor = _ball.getWorldPosition();
		
		Link firstLink = _links.get(0);
		Vec2 firstLinkAnchor = firstLink.getWorldPosition();
		
		_world.destroyJoint(_firstJoint);
		
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
	
	/**
	 * Retracts the grapple by removing links from the world.
	 */
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

	// There is no physics body to be created
	protected void createBody() {
	}
	
	// No grapple point to return
	@Override
	public Vec2 getGrapplePoint() {
		return null;
	}
}
