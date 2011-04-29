package ballsy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsWorld;
import bodies.AbstractBody;
import bodies.UserBall;

public abstract class AbstractLevel extends Screen {
	protected PhysicsWorld _world = new PhysicsWorld(Window.getInstance()); // reference to the world of the level
	private static AbstractLevel LEVEL;
	protected ArrayList<AbstractBody> _bodies; // an array of the bodies in a given level
	protected UserBall _player; // the player instance, also contained in bodies

	protected int _backgroundColor = 255; // color of the background, defaults to white
	public Point2D.Float _gravity = new Point2D.Float(0, -20);
	
	public void setInstance(){
		LEVEL = this;
	}
	
	public static AbstractLevel getInstance(){
		return LEVEL;
	}
	
	public abstract void setup();
	public abstract void draw();
	public void remove(AbstractBody object) { _bodies.remove(object); }
	
	// methods for use in XML reading
	public ArrayList<AbstractBody> getBodies() { return _bodies; }
	public void setBodies(ArrayList<AbstractBody> bodies) { _bodies = bodies; }
	public UserBall getPlayer() { return _player; }
	public void setPlayer(UserBall b) { _player = b; }
	public void setBGColor(int c) { _backgroundColor = c; }
	public int getBGColor() { return _backgroundColor; }
	public void setupWorld(float minX, float minY, float maxX, float maxY) {}; // to be overridden in XMLLevel
	public Vec2[] getWorldBounds() { return _world.getBounds(); }
	public void setGravity(Point2D.Float g) {
		_gravity = g;
		_world.setGravity(g.x, g.y);
	}
	public Point2D.Float getGravity() { return _gravity; }
	
	/**
	 * Override: handle the collision of body 1 and body 2
	 * velocity provided for use with magnitude of... sound or some shit
	 */
	public void handleCollision(Body b1, Body b2, float velocity) { 
		AbstractBody body1 = getAbstractBody(b1);
		AbstractBody body2 = getAbstractBody(b2);
		// delegate collision handling to bodies
		body1.handleCollision(body2);
		body2.handleCollision(body1);
		// make a fun sound!
		//AudioClip clip = new AudioClip("res/thump.wav");
		//clip.start(1);
	}
	
	/**
	 * Method to get the AbstractBody for which Body b is representative of
	 * in the physics world.
	 * @param b
	 * @return
	 */
	private AbstractBody getAbstractBody(Body b) {
		AbstractBody returnBody = null;
		for (AbstractBody bod : _bodies) { if (bod.getPhysicsDef().getBody() == b) returnBody = bod; }
		return returnBody;
	}
	
	/**
	 * Handle keypresses (through Processing).
	 * Most importantly to catch escape keypress.
	 */
	public void keyPressed(){
		// handle esc keypress
		if(_window.key==27) {
			_window.key=0;
			Window.getInstance().setScreen(new WelcomeScreen());
			_window.cursor();
		}
	}
}
