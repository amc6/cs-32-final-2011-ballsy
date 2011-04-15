package bodies;

import ballsy.AbstractLevel;
import ballsy.Window;
import physics.PhysicsWorld;

public class Ball extends AbstractBody {

	// default attributes
	private static final float CENTER_X = 5;
	private static final float CENTER_Y = 5;
	private static final float RADIUS = 2;
	private static final float DENSITY = 0.2f;
	private static final float FRICTION = 0.3f;
	private static final float BOUNCINESS = 0.5f;
	private static final int COLOR = Window.getInstance().color(200, 100, 100);
	
	public Ball(AbstractLevel level, PhysicsWorld world){
		super(level, world, new physics.BallDef(world, CENTER_X, CENTER_Y, RADIUS, DENSITY, FRICTION, BOUNCINESS, true), new graphical.BallDef(COLOR));
	}
	
	public Ball(AbstractLevel level, PhysicsWorld world, float centerX, float centerY){
		super(level, world, new physics.BallDef(world, centerX, centerY, RADIUS, DENSITY, FRICTION, BOUNCINESS, true), new graphical.BallDef(COLOR));
	}
	
	public Ball(AbstractLevel level, PhysicsWorld world, float centerX, float centerY, float radius, boolean mobile){
		super(level, world, new physics.BallDef(world, centerX, centerY, radius, DENSITY, FRICTION, BOUNCINESS, mobile), new graphical.BallDef(COLOR));
	}

	
}
