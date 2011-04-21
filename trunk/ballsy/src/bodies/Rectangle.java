package bodies;

import physics.PathDef;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;
import static bodies.BodyConstants.*;

public class Rectangle extends AbstractBody {

	public Rectangle(AbstractLevel level, PhysicsWorld world, float centerX, float centerY){
		super(level, world, new physics.PhysicsRectangle(world, centerX, centerY, DEFAULT_RECTANGLE_WIDTH, DEFAULT_RECTANGLE_HEIGHT, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS, true), new graphical.RectangleDef(DEFAULT_BODY_COLOR));
	}	
	
	public Rectangle(AbstractLevel level, PhysicsWorld world, float centerX, float centerY, float width, float height, boolean mobile){
		super(level, world, new physics.PhysicsRectangle(world, centerX, centerY, width, height, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS, mobile), new graphical.RectangleDef(DEFAULT_BODY_COLOR));
	}
	
}
