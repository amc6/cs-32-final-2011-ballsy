package bodies;

import ballsy.AbstractLevel;
import ballsy.Window;
import physics.PhysicsWorld;

public class Rectangle extends AbstractBody {

	// default attributes
	private static final float CENTER_X = 5;
	private static final float CENTER_Y = 5;
	private static final float WIDTH = 4;
	private static final float HEIGHT = 4;
	private static final float DENSITY = 1.0f;
	private static final float FRICTION = 0.3f;
	private static final float BOUNCINESS = 0.5f;
	private static final int COLOR = Window.getInstance().color(100, 200, 100);
	
	public Rectangle(AbstractLevel level, PhysicsWorld world){
		super(level, world, new physics.RectangleDef(world, CENTER_X, CENTER_Y, WIDTH, HEIGHT, DENSITY, FRICTION, BOUNCINESS, true), new graphical.RectangleDef(COLOR));
	}
	
	public Rectangle(AbstractLevel level, PhysicsWorld world, float centerX, float centerY){
		super(level, world, new physics.RectangleDef(world, centerX, centerY, WIDTH, HEIGHT, DENSITY, FRICTION, BOUNCINESS, true), new graphical.RectangleDef(COLOR));
	}
	
	public Rectangle(AbstractLevel level, PhysicsWorld world, float centerX, float centerY, float width, float height, boolean mobile){
		super(level, world, new physics.RectangleDef(world, centerX, centerY, width, height, DENSITY, FRICTION, BOUNCINESS, mobile), new graphical.RectangleDef(COLOR));
	}

}
