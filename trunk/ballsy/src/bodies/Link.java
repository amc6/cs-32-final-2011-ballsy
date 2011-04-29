package bodies;

import static bodies.BodyConstants.*;

import org.jbox2d.dynamics.joints.DistanceJoint;

public class Link extends Ball {
	
	private AbstractBody _prev, _next;
	private DistanceJoint _prevJoint, _nextJoint;

	public Link(float centerX, float centerY) {
		super(centerX, centerY, LINK_RADIUS);
		this.getPhysicsDef().setDensity(DEFAULT_BODY_DENSITY*2);
	}

	public void setPrev(AbstractBody prev, DistanceJoint prevJoint) {
		_prev = prev;
		_prevJoint = prevJoint;
	}
	
	public void setNext(AbstractBody next, DistanceJoint nextJoint) {
		_next = next;
		_nextJoint = nextJoint;
	}
	
	public AbstractBody getPrev() {
		return _prev;
	}
	
	public AbstractBody getNext() {
		return _next;
	}
	
	public DistanceJoint getPrevJoint() {
		return _prevJoint;
	}
	
	public DistanceJoint getNextJoint() {
		return _nextJoint;
	}
}
