package bodies;

import graphics.GraphicsImage;

import org.dom4j.Element;

import physics.PhysicsRectangle;

import static bodies.BodyConstants.*;

public class EndPoint extends AbstractBody{
	public EndPoint(float x, float y) {
		this(x,y,ENDPOINT_WIDTH,ENDPOINT_HEIGHT);
	}
	
	public EndPoint(float x, float y, float width, float height) {
		//PhysicsRegularPolygon physics = new PhysicsRegularPolygon(x, y, 8, ENDPOINT_SIZE);
		PhysicsRectangle physics = new PhysicsRectangle(x,y,width,height);
		//GraphicsPolygon graphics = new GraphicsPolygon(ENDPOINT_COLOR);
		GraphicsImage graphics = new GraphicsImage(ENDPOINT_IMAGE);
		this.setPhysicsAndGraphics(physics, graphics);
		this.setEndpoint(true);
	}
	
	public Element writeXML() {
		return super.writeXML("rectangle");
	}
}
