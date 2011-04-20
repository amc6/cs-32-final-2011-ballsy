package bodies;

import physics.PathDef;
import physics.PhysicsPolygonDef;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;
import physics.*;

public class Polygon extends AbstractBody {

	// default attributes
	public static final float DEFAULT_CENTER_X = 20;
	public static final float DEFAULT_CENTER_Y = 20;
	public static final float DEFAULT_SIDE_LENGTH = 8;
	public static final int DEFAULT_NUM_SIDES = 7;
	public static final float DEFAULT_DENSITY = 1.0f;
	public static final float DEFAULT_FRICTION = 0.3f;
	public static final float DEFAULT_BOUNCINESS = 0.9f;
	public static final int DEFAULT_COLOR = Window.getInstance().color(0, 0, 0);
	
	public Polygon(AbstractLevel level, PhysicsWorld world){
		super(level, world, new physics.PhysicsPolygonDef(world, DEFAULT_CENTER_X, DEFAULT_CENTER_Y, DEFAULT_NUM_SIDES, DEFAULT_SIDE_LENGTH, DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_BOUNCINESS, true), new graphical.PolygonDef(DEFAULT_COLOR));
	}
	
	public Polygon(AbstractLevel level, PhysicsWorld world, float x, float y, int numSides, float sideLen){
		super(level, world, new physics.PhysicsPolygonDef(world, x, y, numSides, sideLen, DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_BOUNCINESS, true), new graphical.PolygonDef(DEFAULT_COLOR));
	}
}
