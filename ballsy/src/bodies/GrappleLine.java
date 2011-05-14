package bodies;

/**
 * The GrappleLine, which is ultimately what we use for the game.
 * This class mostly manages the physics def of the grapple.
 */

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsGrappleLine;

import org.dom4j.Element;

import physics.PhysicsGrapple;
import physics.PhysicsGrappleLine;

public class GrappleLine extends AbstractBody {
	
	/**
	 * Construct in association with the userball
	 * @param ball
	 */
	public GrappleLine(UserBall ball) {
		PhysicsGrapple physics = new PhysicsGrappleLine(ball);
		GraphicsGrappleLine graphics = new GraphicsGrappleLine(DEFAULT_BODY_COLOR, ball);
		this.setPhysicsAndGraphics(physics, graphics);
	}

	@Override
	public Element writeXML() {
		return null;
	}
	
	@Override
	public PhysicsGrapple getPhysicsDef() {
		return (PhysicsGrapple) super.getPhysicsDef();
	}
	
	public void grapple() {
		this.getPhysicsDef().grapple();
	}
	
	public void releaseGrapple() {
		this.getPhysicsDef().releaseGrapple();
	}

	
	public void extendGrapple() {
		this.getPhysicsDef().extendGrapple();
	}
	
	public void retractGrapple() {
		this.getPhysicsDef().retractGrapple();
	}
}
