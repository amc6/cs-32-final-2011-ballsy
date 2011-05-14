package bodies;

/**
 * Endpoint definition. Most important for its constructor, which sets up the
 * physics and graphical defs.
 */

import graphics.GraphicsImage;

import org.dom4j.Element;

import physics.PhysicsRectangle;

import static bodies.BodyConstants.*;

public class EndPoint extends AbstractBody{
	public EndPoint(float x, float y) {
		this(x,y,ENDPOINT_WIDTH,ENDPOINT_HEIGHT);
	}
	
	/**
	 * Instantiate an endpoint, and set related properties in physics and graphics.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public EndPoint(float x, float y, float width, float height) {
		PhysicsRectangle physics = new PhysicsRectangle(x,y,width,height);
		GraphicsImage graphics = new GraphicsImage(ENDPOINT_IMAGE);
		this.setPhysicsAndGraphics(physics, graphics);
		this.setEndpoint(true);
		this.getPhysicsDef().setMobile(false);
	}
	
	public Element writeXML() {
		return super.writeXML("rectangle");
	}
}
