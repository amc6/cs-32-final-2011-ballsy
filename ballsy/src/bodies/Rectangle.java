package bodies;

import graphical.AbstractLevel;
import physics.PhysicsWorld;

public class Rectangle extends BallsyObject {

	private static final float X_POS = 5;
	private static final float Y_POS = 5;
	private static final float WIDTH = 50;
	private static final float HEIGHT = 50;
	private static final float DENSITY = 1.0f;
	private static final float FRICTION = 0.3f;
	private static final float BOUNCINESS = 0.5f;
	
	public Rectangle(AbstractLevel level, PhysicsWorld world){
		super(level, world, new physics.RectangleDef(X_POS, Y_POS, WIDTH, HEIGHT, DENSITY, FRICTION, BOUNCINESS), new graphical.RectangleDef(), false);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
