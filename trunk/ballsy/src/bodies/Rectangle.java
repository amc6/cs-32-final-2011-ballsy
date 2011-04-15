package bodies;

import physics.PathDef;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;

public class Rectangle extends AbstractBody {

	// default attributes
	public static final float DEFAULT_CENTER_X = 5;
	public static final float DEFAULT_CENTER_Y = 5;
	public static final float DEFAULT_WIDTH = 4;
	public static final float DEFAULT_HEIGHT = 4;
	public static final float DEFAULT_DENSITY = 1.0f;
	public static final float DEFAULT_FRICTION = 0.3f;
	public static final float DEFAULT_BOUNCINESS = 0.5f;
	public static final int DEFAULT_COLOR = Window.getInstance().color(100, 200, 100);
	
	public Rectangle(AbstractLevel level, PhysicsWorld world){
		super(level, world, new physics.RectangleDef(world, DEFAULT_CENTER_X, DEFAULT_CENTER_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_BOUNCINESS, true), new graphical.RectangleDef(DEFAULT_COLOR));
	}
	
	public Rectangle(AbstractLevel level, PhysicsWorld world, float centerX, float centerY){
		super(level, world, new physics.RectangleDef(world, centerX, centerY, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_BOUNCINESS, true), new graphical.RectangleDef(DEFAULT_COLOR));
	}
	
	public Rectangle(AbstractLevel level, PhysicsWorld world, float centerX, float centerY, float width, float height, boolean mobile){
		super(level, world, new physics.RectangleDef(world, centerX, centerY, width, height, DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_BOUNCINESS, mobile), new graphical.RectangleDef(DEFAULT_COLOR));
	}
	
}
