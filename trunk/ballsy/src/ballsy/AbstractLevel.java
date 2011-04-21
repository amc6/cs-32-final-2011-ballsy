package ballsy;

import java.util.ArrayList;

import physics.PhysicsWorld;

import bodies.AbstractBody;
import bodies.UserBall;

public abstract class AbstractLevel extends Screen {
	protected ArrayList<AbstractBody> _bodies; // an array of the bodies in a given level
	protected UserBall _player; // the player instance, also contained in bodies
	protected PhysicsWorld _world; // reference to the world of the level
	public static int DEFAULT_WEIGHT = 0; // the default drawing weight (zero means no edges)
	public abstract void setup();
	public abstract void draw();
	public void remove(AbstractBody object) { _bodies.remove(object); }
	
	// accessors/mutators...
	public ArrayList<AbstractBody> getBodies() { return _bodies; }
	public void setBodies(ArrayList<AbstractBody> bodies) { _bodies = bodies; }
	public UserBall getPlayer() { return _player; }
	public void setPlayer(UserBall b) { _player = b; }
	public PhysicsWorld getWorld() { return _world; }
}
