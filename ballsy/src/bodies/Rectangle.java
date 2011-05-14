package bodies;

/**
 * Body wrapper for physics and graphics defs of the rectangle.
 */

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsRectangle;
import org.dom4j.Element;
import physics.PhysicsRectangle;

public class Rectangle extends AbstractBody {

	public Rectangle(float centerX, float centerY, float width, float height){
		PhysicsRectangle physics = new PhysicsRectangle(centerX, centerY, width, height);
		GraphicsRectangle graphics = new GraphicsRectangle(DEFAULT_BODY_COLOR);
		this.setPhysicsAndGraphics(physics, graphics);
	}	

	@Override
	public Element writeXML() {
		return super.writeXML("rectangle");
	}
	
}
