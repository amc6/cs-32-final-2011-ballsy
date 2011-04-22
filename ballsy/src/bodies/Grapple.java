package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsGrapple;

import org.dom4j.Element;

import physics.PhysicsGrapple;


public class Grapple extends AbstractBody {

	public Grapple(UserBall ball) {
		PhysicsGrapple physics = new PhysicsGrapple(ball);
		GraphicsGrapple graphics = new GraphicsGrapple(DEFAULT_BODY_COLOR, ball);
		this.setPhysicsAndGraphics(physics, graphics);
	}

	@Override
	public Element writeXML() {
		return null;
	}
	
	public void grapple() {
		((physics.PhysicsGrapple) this.getPhysicsDef()).grapple();
	}
	
	public void releaseGrapple() {
		((physics.PhysicsGrapple) this.getPhysicsDef()).releaseGrapple();
	}

	
	public void extendGrapple() {
		((physics.PhysicsGrapple) this.getPhysicsDef()).extendGrapple();
	}
	
	public void retractGrapple() {
		((physics.PhysicsGrapple) this.getPhysicsDef()).retractGrapple();
	}
}
