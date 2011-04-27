package physics;

/**
 * Defines the path of an object. This is essentially a wrapper for a vector of Point2Ds,
 * with a "step" method which updates the velocity of the physical object to move it towards
 * the current target point. If it gets near enough to the target point, the current target
 * is updated to the next entry of the vector.
 */

import java.awt.geom.Point2D;
import java.util.Vector;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;

public class PhysicsPath {
	private Vector<Point2D.Float> _pathPoints;
	private PhysicsDef _physDef;
	private int _currTarget = 0;
	private Point2D.Float _initPos;
	private float _velCoeff = 10; // equivalent to the speed of the object
	private boolean _static = false;
	private float _stepRotation = 0f;
	
	public PhysicsPath(PhysicsDef p) {
		_physDef = p;
		_pathPoints = new Vector<Point2D.Float>();
		_initPos = new Point2D.Float(p.getBodyWorldCenter().x, p.getBodyWorldCenter().y);
	}
	
	public PhysicsPath(PhysicsDef p, Vector<Point2D.Float> v) {
		_physDef = p;
		_pathPoints = v;
		_initPos = new Point2D.Float(p.getBodyWorldCenter().x, p.getBodyWorldCenter().y);
	}
	
	/**
	 * Set the static property of the path. If a path is "static", the object will be
	 * immobile, meaning that nothing can interact with it (beyond colliding with it). 
	 * @param s
	 */
	public void setStatic(boolean s) {
		_static = s;
		_physDef.setMobile(!s); // if the path is "static" the object must not be affected by gravity, etc.
	}
	
	/**
	 * Sets the constant rotation applied by the path. This rotation will be applied at
	 * every step, and the object need not have a set of points in its path to rotate
	 * (i.e. it can rotate without traveling otherwise).
	 * @param r
	 */
	public void setRotation(float r) {
		_stepRotation = r;
	}
	
	/**
	 * Apply all parameters indicated by the path. Moves the body between points; if
	 * the body is mobile, it simply applies a velocity towards the next point. 
	 * If it's static, the path will work by directly changing the position, instead
	 * of applying a velocity. Rotation is always applied.
	 */
	public void step() {
		// rotate by _stepRotation
		_physDef.getBody().setXForm(_physDef.getBody().getXForm().position, _physDef.getBody().getAngle() + _stepRotation);
		// don't step further if there're no points to step through
		if (_pathPoints.size() == 0) return;
		// set up some variables
		Point2D.Float position = new Point2D.Float(_physDef.getBodyWorldCenter().x, _physDef.getBodyWorldCenter().y);
		// check if it's near to the target point
		if (position.distance(getAdjusted(_currTarget)) <= 2) { // distance value arbitrarily chosen...
			// it is! increment the point value, loop around if necessary
			if (_currTarget < _pathPoints.size() - 1) { _currTarget++;
			} else { _currTarget = 0; }
		}
		Point2D.Float destination = getAdjusted(_currTarget);
		// find the angle towards the next point
		double angle = Math.atan2((destination.getY() - position.getY()), (destination.getX() - position.getX()));
		if (!_static) {
			// if it's not static, create velocity in that direction
			Vec2 vel = new Vec2((float)(_velCoeff * Math.cos(angle)), (float)(_velCoeff * Math.sin(angle)));
			// set velocity towards target point
			_physDef.setLinearVelocity(vel);
		} else {
			// if it is, simply change the position...
			float unitsToMove = PhysicsWorld.getInstance().scalarPixelsToWorld(_velCoeff)/4; // figure out how many units to move it, off of the position
			float xNew = _physDef.getBody().getXForm().position.x + (float) (unitsToMove * Math.cos(angle));
			float yNew = _physDef.getBody().getXForm().position.y + (float) (unitsToMove * Math.sin(angle));
			_physDef.getBody().setXForm(new Vec2(xNew, yNew), _physDef.getBody().getAngle());
		}
	}
	
	/**
	 * Returns the offset position, for relative shits
	 * @param position
	 * @return
	 */
	private Point2D.Float getAdjusted(int position) {
		return new Point2D.Float(_initPos.x + _pathPoints.get(position).x, 
				_initPos.y + _pathPoints.get(position).y);
	}
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file.
	 * @return
	 */
	public Element writeXML() {
		// create the new element for the path
		Element newEl = DocumentHelper.createElement("PATH_DEF");
		// populate it's attributes accordingly
		newEl.addAttribute("CURRENT_TARGET", Integer.toString(_currTarget));
		newEl.addAttribute("VEL_COEFF", Float.toString(_velCoeff));
		newEl.addAttribute("INITIAL_X", Float.toString(_initPos.x));
		newEl.addAttribute("INITIAL_Y", Float.toString(_initPos.y));
		newEl.addAttribute("STATIC", Boolean.toString(_static));
		newEl.addAttribute("ROTATION", Float.toString(_stepRotation));
		// make the points as sub-elements
		for (Point2D.Float p : _pathPoints) {
			Element pointEl = DocumentHelper.createElement("PATH_POINT");
			pointEl.addAttribute("X", Float.toString(p.x));
			pointEl.addAttribute("Y", Float.toString(p.y));
			newEl.add(pointEl);
		}
		return newEl;
	}
	
	// mutators for use in restoring a saved path
	public void setCurrTarget(int t) { _currTarget = t; }
	public void setInitialPoint(Point2D.Float p) { _initPos = p; }
	public void setVelCoeff(float v) { _velCoeff = v; }
}
