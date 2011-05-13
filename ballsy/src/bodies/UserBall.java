package bodies;

/**
 * Class to model the ball controlled by the user. Manages movement, crosshair goings-on, 
 * and all other things specific to the user's ball.
 */

import static bodies.BodyConstants.*;
import static bodies.BodyConstants.USER_MAX_VELOCITY;
import static bodies.BodyConstants.USER_MOVE_COEFFICIENT;
import graphics.Crosshair;
import graphics.Smoke;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import ballsy.AbstractLevel;
import ballsy.XMLLevel;
import ballsy.ScreenLoader.Screens;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import editor.EditorLevel;

public class UserBall extends AbstractBody {

	private Crosshair _crosshair;
	private AbstractBody _grappleObject;
	private Point2D.Float _grapplePoint;
	private boolean _grappled = false, _inPlay = true;
	private GrappleLine _grapple;
	
	/* no sound for now
		// sound shits.
		private Minim _minim = new Minim(_window);
		private AudioSample _sound = _minim.loadSample("res/boop.wav", 2048);
		private float _maxVelocity = 50; // potential max velocity!?
	*/
	
	public UserBall(float centerX, float centerY, float radius) {
		this.setPhysicsAndGraphics(new physics.PhysicsBall(centerX, centerY, radius), new graphics.GraphicsUserBall());
		_crosshair = new Crosshair(this);
		_grapple = new GrappleLine(this);
		this.getPhysicsDef().setDensity(DEFAULT_BODY_DENSITY*3);
		this.getPhysicsDef().setFriction(200f); // to make rolling work well
		this.getGraphicsDef().setSmoke(new Smoke(this)); // make the trail
	}

	// begin move control helper methods (just make things simpler, you know?)
	
	public void moveLeft() {
		if (_grappled){
			if (this.getPhysicsDef().getBody().getLinearVelocity().length() > - USER_MAX_VELOCITY)
				this.getPhysicsDef().applyImpulse(new Vec2(-USER_MOVE_COEFFICIENT, 0));			
		}else{
			this.getPhysicsDef().getBody().wakeUp();
			this.getPhysicsDef().setAngularVelocity(USER_ANGULAR_VELOCITY);
		}
	}
	
	public void moveRight() {
		if (_grappled){
			if (this.getPhysicsDef().getBody().getLinearVelocity().length() < USER_MAX_VELOCITY)
				this.getPhysicsDef().applyImpulse(new Vec2(USER_MOVE_COEFFICIENT, 0));
		}else{
			this.getPhysicsDef().getBody().wakeUp();
			this.getPhysicsDef().setAngularVelocity(-USER_ANGULAR_VELOCITY);	
		}
	}
	
	public void moveDown() {
		if (this.getPhysicsDef().getBody().getLinearVelocity().y > -USER_MAX_VELOCITY)
			this.getPhysicsDef().applyImpulse(new Vec2(0, -USER_MOVE_COEFFICIENT));
	}
	
	public void moveUp() {
		if (this.getPhysicsDef().getBody().getLinearVelocity().y < USER_MAX_VELOCITY)
			this.getPhysicsDef().applyImpulse(new Vec2(0, USER_MOVE_COEFFICIENT));
	}
	
	// end move control methods
	
	/**
	 * Partially overrides the display of the superclass (Ball).
	 * Adds the functionality of the crosshair, causing it to display (display())
	 * and check for grappleability of object, if any is in range of the grapple gun
	 * (getGrapplePoint()), and setting that to _grappleObject.
	 */
	public void display() {
		if (_grapple != null) {
			_grapple.display(); // do this first so it appears behind
		}
		super.display();
		if (_inPlay) {
			_crosshair.display();
			if (!_grappled) {
				_grapplePoint = _crosshair.getGrapplePoint(_level.getBodies());
				_grappleObject = this.getBody(_grapplePoint);
			}	
		}
	}
	
	public void setCrosshairVisible(boolean visible) {
		if (visible) _crosshair.show();
		else _crosshair.hide();
	}
	
	public void setGrapple(GrappleLine grapple) {
		_grapple = grapple;
	}

	public void setInPlay(boolean b) {
		_inPlay = b;
	}
	
	public void fireGrapple() {
		if (_grappleObject != null) {
			_crosshair.hide();
			_grappled = true;
			_grapple.grapple();
		}
	}
	
	public void releaseGrapple() {
		_crosshair.show();
		_grappled = false;
		_grapple.releaseGrapple();
	}
	
	public void extendGrapple() {
		_grapple.extendGrapple();
	}
	
	public void retractGrapple() {
		_grapple.retractGrapple();
	}
	
	public boolean isGrappled() {
		return _grappled;
	}
	
	/**
	 * 
	 * @return current grappling point
	 */
	public Point2D.Float getWorldGrapplePoint() {
		return _grapplePoint;
	}
	
	/**
	 * 
	 * @return current grappling point
	 */
	public Vec2 getWorldGrapplePointVec() {
		return new Vec2(_grapplePoint.x, _grapplePoint.y);
	}
	
	/**
	 * 
	 * @return current grappling point
	 */
	public Point2D.Float getPixelGrapplePoint() {
		return new Point2D.Float(_world.worldXtoPixelX(_grapplePoint.x), _world.worldYtoPixelY(_grapplePoint.y));
	}
	
	/**
	 * 
	 * @return object we've just grappled
	 */
	public AbstractBody getGrappleObject() {
		return _grappleObject;
	}
	
	/**
	 * Helper method to find which object contains a given point. Returns null if no containment.
	 * @param point
	 * @return
	 */
	private AbstractBody getBody(Point2D.Float point) {
		if (point == null) return null; // if passed a null point, output should be null
		ArrayList<AbstractBody> list = _level.getBodies();
		for (AbstractBody b : list) {
			// iterate through all bodies, checking for containment of point
			if (b.getPhysicsDef().getBody().getShapeList().testPoint(b.getPhysicsDef().getBody().getXForm(), new Vec2(point.x, point.y))) {
				return b;
			}
		}
		return null; // it didn't find an object, return null
	}
	
	/**
	 * handle UserBall collisions specially!
	 */
	public void handleCollision(AbstractBody other, float velocity) {
		/* sound is out of the picture for now... maybe in a future version
			// make a fun sound!
			_sound.stop();
			// scale the volume
			float min = -40;
			float max = 12f;
			if (velocity > _maxVelocity) velocity = _maxVelocity;
			float percent = velocity / _maxVelocity;
			float gain = min + (max - min)*percent;
			System.out.println(gain);
			_sound.setGain(gain);
			_sound.trigger();
		*/
		
		if (other.isEndpoint()) {
			// ahoy! We've reached an endpoint.
			if (AbstractLevel.getInstance() instanceof EditorLevel) {
				// this is running in the level editor, so just stop it.
				((EditorLevel) AbstractLevel.getInstance()).stop();
			} else {
				// it's a real level. make shit happen
//				_window.loadScreen(Screens.WELCOME_SCREEN);
				AbstractLevel.getInstance().setWon();

			}
		}
		if (other.isDeadly()) {
			// UserBall fucked up. DEATH ENSUES
			if (AbstractLevel.getInstance() instanceof EditorLevel) {
				// this is running in the level editor, so just stop it.
				((EditorLevel) AbstractLevel.getInstance()).stop();
			} else if (AbstractLevel.getInstance() instanceof XMLLevel){
				// it's a real level. make shit happen
				_window.fadeOut();
			}
		}
	}
	
	
	/**
	 * partially override the writeXML() method of super to identify this as a USER ball
	 */
	public Element writeXML() {
		return super.writeXML("user_ball");
	}
}
