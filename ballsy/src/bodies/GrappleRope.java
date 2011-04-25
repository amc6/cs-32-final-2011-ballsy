package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsGrappleLine;
import graphics.GraphicsGrappleRope;

import org.dom4j.Element;

import physics.PhysicsGrappleLine;
import physics.PhysicsGrappleRope;


public class GrappleRope extends AbstractBody {

	public GrappleRope(UserBall ball) {
		PhysicsGrappleRope physics = new PhysicsGrappleRope(ball);
		GraphicsGrappleRope graphics = new GraphicsGrappleRope(DEFAULT_BODY_COLOR, ball);
		this.setPhysicsAndGraphics(physics, graphics);
	}

	@Override
	public Element writeXML() {
		return null;
	}
	
	public void grapple() {
		((physics.PhysicsGrappleRope) this.getPhysicsDef()).grapple();
	}
	
	public void releaseGrapple() {
		((physics.PhysicsGrappleRope) this.getPhysicsDef()).releaseGrapple();
	}

	
	public void extendGrapple() {
		((physics.PhysicsGrappleRope) this.getPhysicsDef()).extendGrapple();
	}
	
	public void retractGrapple() {
		((physics.PhysicsGrappleRope) this.getPhysicsDef()).retractGrapple();
	}
}
