package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_BOUNCINESS;
import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import static bodies.BodyConstants.DEFAULT_BODY_DENSITY;
import static bodies.BodyConstants.DEFAULT_BODY_FRICTION;
import static bodies.BodyConstants.DEFAULT_RECTANGLE_HEIGHT;
import static bodies.BodyConstants.DEFAULT_RECTANGLE_WIDTH;
import graphics.GraphicsRectangle;

import org.dom4j.Element;

import physics.PhysicsRectangle;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;

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
