package bodies;

import static bodies.BodyConstants.*;
import graphics.GraphicsPolygon;

import org.dom4j.Element;

import physics.PhysicsRegularPolygon;

public class EndPoint extends AbstractBody{
	public EndPoint(float x, float y) {
		PhysicsRegularPolygon physics = new PhysicsRegularPolygon(x, y, 8, ENDPOINT_SIZE);
		GraphicsPolygon graphics = new GraphicsPolygon(ENDPOINT_COLOR);
		this.setPhysicsAndGraphics(physics, graphics);
		this.setEndpoint(true);
	}
	
	public Element writeXML() {
		return super.writeXML("regular_polygon");
	}
}
