package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsGrappleLine;

import org.dom4j.Element;

import physics.PhysicsGrappleLine;


public class GrappleLine extends AbstractBody {

	public GrappleLine(UserBall ball) {
		PhysicsGrappleLine physics = new PhysicsGrappleLine(ball);
		GraphicsGrappleLine graphics = new GraphicsGrappleLine(DEFAULT_BODY_COLOR, ball);
		this.setPhysicsAndGraphics(physics, graphics);
	}

	@Override
	public Element writeXML() {
		return null;
	}
	
	public void grapple() {
		((physics.PhysicsGrappleLine) this.getPhysicsDef()).grapple();
	}
	
	public void releaseGrapple() {
		((physics.PhysicsGrappleLine) this.getPhysicsDef()).releaseGrapple();
	}

	
	public void extendGrapple() {
		((physics.PhysicsGrappleLine) this.getPhysicsDef()).extendGrapple();
	}
	
	public void retractGrapple() {
		((physics.PhysicsGrappleLine) this.getPhysicsDef()).retractGrapple();
	}
}
