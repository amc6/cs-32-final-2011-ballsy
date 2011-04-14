package bodies;

import ballsy.AbstractLevel;
import physics.PhysicsWorld;

public class Ball extends AbstractBody {

	// default attributes
	private static final float CENTER_X = 5;
	private static final float CENTER_Y = 5;
	private static final float RADIUS = 2;
	private static final float DENSITY = 1.0f;
	private static final float FRICTION = 0.3f;
	private static final float BOUNCINESS = 0.5f;
	
	public Ball(AbstractLevel level, PhysicsWorld world){
		super(level, world, new physics.BallDef(CENTER_X, CENTER_Y, RADIUS, DENSITY, FRICTION, BOUNCINESS), new graphical.BallDef(), true);
	}
	
	public Ball(AbstractLevel level, PhysicsWorld world, float centerX, float centerY){
		super(level, world, new physics.BallDef(centerX, centerY, RADIUS, DENSITY, FRICTION, BOUNCINESS), new graphical.BallDef(), true);
	}
	
	public Ball(AbstractLevel level, PhysicsWorld world, float centerX, float centerY, float radius, boolean mobile){
		super(level, world, new physics.BallDef(centerX, centerY, radius, DENSITY, FRICTION, BOUNCINESS), new graphical.BallDef(), mobile);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
