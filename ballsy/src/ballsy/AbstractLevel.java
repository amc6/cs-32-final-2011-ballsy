package ballsy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import bodies.AbstractBody;
import bodies.UserBall;

public abstract class AbstractLevel extends Screen {
	protected ArrayList<AbstractBody> _bodies; // an array of the bodies in a given level
	protected UserBall _player; // the player instance, also contained in bodies
	protected PhysicsWorld _world; // reference to the world of the level
	public static int DEFAULT_WEIGHT = 0; // the default drawing weight (zero means no edges)
	protected int _backgroundColor = 255; // color of the background, defaults to white
	public Point2D.Float _gravity = new Point2D.Float(0, -20);
	
	public abstract void setup();
	public abstract void draw();
	public void remove(AbstractBody object) { _bodies.remove(object); }
	
	// methods for use in XML reading
	public ArrayList<AbstractBody> getBodies() { return _bodies; }
	public void setBodies(ArrayList<AbstractBody> bodies) { _bodies = bodies; }
	public UserBall getPlayer() { return _player; }
	public void setPlayer(UserBall b) { _player = b; }
	public PhysicsWorld getWorld() { return _world; }
	public void setBGColor(int c) { _backgroundColor = c; }
	public int getBGColor() { return _backgroundColor; }
	public void setupWorld(float minX, float minY, float maxX, float maxY) {}; // to be overridden in XMLLevel
	public Vec2[] getWorldBounds() { return _world.getBounds(); }
	public void setGravity(Point2D.Float g) {
		_gravity = g;
		_world.setGravity(g.x, g.y);
	}
	public Point2D.Float getGravity() { return _gravity; }
}
