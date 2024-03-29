package ballsy;

/**
 * Superclass for all levels. Handles the Singleton, pausing, collisions, control input, and more.
 */

import graphics.Smoke;
import graphics.Text;

import java.awt.event.KeyEvent;
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
	protected ArrayList<Checkpoint> _checkpoints; // array of the checkpoints in a given level.
	protected UserBall _player; // the player instance, also contained in bodies
	protected int _backgroundColor = 255; // color of the background, defaults to white
	protected Vec2 _gravity = new Vec2(0, -30);
	protected boolean _paused = false;
	protected boolean _won = false;
	private boolean[] _keys = {false, false, false, false};
	private static int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	protected PauseScreen _pauseScreen = new PauseScreen(this);
	protected WinScreen _winScreen = new WinScreen(this);
	private Checkpoint _currCP = null; // so we don't have to iterate every time.
	protected static Text _debug = new Text("",100,100); // for println to screen

	/** sets this as the current instance of level in the singleton **/
	public void setInstance(){
		LEVEL = this;
	}
	
	/** returns singleton **/
	public static AbstractLevel getInstance(){
		return LEVEL;
	}
	
	public abstract void setup();
	public abstract void draw();
	public void remove(AbstractBody object) { _bodies.remove(object); }
	
	// methods for use in XML reading
	public ArrayList<AbstractBody> getBodies() { return _bodies; }
	public ArrayList<Checkpoint> getCheckpoints() { return _checkpoints; }
	public void setBodies(ArrayList<AbstractBody> bodies) { _bodies = bodies; }
	public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {_checkpoints = checkpoints; }
	public UserBall getPlayer() { return _player; }
	public void setPlayer(UserBall b) { _player = b; }
	public void setBGColor(int c) { _backgroundColor = c; }
	public int getBGColor() { return _backgroundColor; }
	public void setupWorld(float minX, float minY, float maxX, float maxY) {}; // to be overridden in XMLLevel
	public Vec2[] getWorldBounds() { return _world.getBounds(); }
	public float getWorldWidth() { return _world.getWidth(); }
	public float getWorldHeight() { return _world.getHeight(); }
	public void setGravity(Vec2 g) {
		_gravity = g;
		_world.setGravity(g.x, g.y);
	}
	public Vec2 getGravity() { return _gravity; }
	
	/**
	 * Override: handle the collision of body 1 and body 2
	 * velocity provided for use with magnitude of... sound or some shit
	 */
	public void handleCollision(Body b1, Body b2, float velocity) { 
		AbstractBody body1 = getAbstractBody(b1);
		AbstractBody body2 = getAbstractBody(b2);
		// delegate collision handling to bodies
		if (body1 != null && body2 != null) {
			body1.handleCollision(body2, velocity);
			body2.handleCollision(body1, velocity);
		}
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
	 * Switch paused variables in level and player, turn smoke on/off
	 */
	public void togglePaused() {
		_paused = !_paused;
		if (_paused) {
			_window.cursor();
			_player.setInPlay(false);
			_player.getGraphicsDef().setSmoke(null);
		}
		else {
			_window.noCursor();
			_player.setInPlay(true);
			_player.getGraphicsDef().setSmoke(new Smoke(_player));
		}
		
		// Reset keys!
		for (int i = 0; i < _keys.length; i++){
			_keys[i] = false;
		}
		
	}
	
	/** handle winning, partially overridden for unlocking **/
	public void setWon() {
		_window.cursor();
		_won = true;
	}
	
	/**
	 * Handle keypresses, of both arrow keys and WASD
	 * Overrides escape keypress, and handles sets the boolean array when control keys are pressed
	 * 
	 */
	public void keyPressed(){
		
		// handle esc keypress
		if(_window.key == 27) {
			_window.key = 0;
			if (!_won) this.togglePaused();
		}
		// handle control keypresses
		switch (_window.keyCode) {
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			// left
			_keys[LEFT] = true;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			// right
			_keys[RIGHT] = true;
			break;
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			// up
			_keys[UP] = true;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			// down
			_keys[DOWN] = true;
			break;
		}
	}
	
	/**
	 * Handle keyreleases, and set boolean array
	 */
	public void keyReleased() {
		// handle control keyreleases
		switch (_window.keyCode) {
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			// left
			_keys[LEFT] = false;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			// right
			_keys[RIGHT] = false;
			break;
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			// up
			_keys[UP] = false;
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			// down
			_keys[DOWN] = false;
			break;
		}
	}
	
	/**
	 * apply values stored in boolean array
	 * so, if two keys are pressed at the time, act according to the press of each
	 */
	protected void applyInput() {
		if (!_paused) {
			// deal with keys
			if (_keys[UP] && _player.isGrappled()) _player.retractGrapple();
			if (_keys[DOWN] && _player.isGrappled()) _player.extendGrapple();
			if (_keys[LEFT]) _player.moveLeft();
			if (_keys[RIGHT]) _player.moveRight();
		}
	}
	
	/**
	 * Fire the grapple if the mouse is dragged. For some reason, a drag isn't a press,
	 * so we need to do this for both.
	 */
	public void mouseDragged() {
		if (!_paused && !_player.isGrappled()) _player.fireGrapple();
	}
	
	/**
	 * Fire the grapple upon a mouse press.
	 */
	public void mousePressed() {
		if (_paused) {
			_pauseScreen.mousePressed();
		}
		else if (!_player.isGrappled()) _player.fireGrapple();
	}
	
	/**
	 * release the grapple upon a mouse release
	 */
	public void mouseReleased() {
		if (!_paused && _player.isGrappled()) _player.releaseGrapple();
	}
	
	/**
	 * Accessor for pausedness.
	 * @return
	 */
	public boolean isPaused() {
		return _paused;
	}
	
	/**
	 * Sets all checkpoints to active:false, to precede activation of a new one
	 */
	public void setActiveCheckpoint(Checkpoint c) {
		if (c != _currCP) {
			_currCP = c;
			for (Checkpoint curr : _checkpoints) { curr.setActive(false); }
			c.setActive(true);
		}
	}
	
	
	public void setActiveCheckpoint(Vec2 position) {
		for (Checkpoint curr : _checkpoints) {
			if (curr.contains(position)) { this.setActiveCheckpoint(curr); }
		}
	}
	
	public Checkpoint getActiveCheckpoint() {
		return _currCP;
	}
	
	/**
	 * For debugging purposes, print a line to the visable level.
	 * @param m
	 */
	public static void println(String m) {
		System.out.println("FROM SCREEN: " + m);
		String s = _debug.getText() + m;
		int newlines = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\n') newlines++;
		}
		if (newlines > 8) {
			s = s.substring(s.indexOf('\n')+1);
		}
		s+='\n';
		_debug = new Text(s,100,100,100,Window.CORNER);
		_debug.setSize(30);
	}

}
