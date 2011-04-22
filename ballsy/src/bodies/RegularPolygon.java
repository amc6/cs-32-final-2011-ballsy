package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsPolygon;

import org.dom4j.Element;

import physics.PhysicsRegularPolygon;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;

public class RegularPolygon extends AbstractBody {
		
	public RegularPolygon(float x, float y, int numSides, float sideLen){
		PhysicsRegularPolygon physics = new PhysicsRegularPolygon(x, y, numSides, sideLen);
		GraphicsPolygon graphics = new GraphicsPolygon(DEFAULT_BODY_COLOR);
		this.setPhysicsAndGraphics(physics, graphics);
	}
	
	public Element writeXML() {
		return super.writeXML("regular_polygon");
	}
}
