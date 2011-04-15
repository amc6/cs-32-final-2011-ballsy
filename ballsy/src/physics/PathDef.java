package physics;

/**
 * Defines the path of an object. This is essentially a wrapper for a vector of Point2Ds,
 * with a "step" method which updates the velocity of the physical object to move it towards
 * the current target point. If it gets near enough to the target point, the current target
 * is updated to the next entry of the vector.
 */

import java.awt.geom.Point2D;
import java.util.Vector;
import org.jbox2d.common.Vec2;

public class PathDef {
	private Vector<Point2D.Float> _pathPoints;
	private PhysicsDef _physDef;
	private int _currTarget = 0;
	private Point2D.Float _initPos;
	private float _velCoeff = 10; // equivalent to the speed of the object
	
	public PathDef(PhysicsDef p) {
		_physDef = p;
		_pathPoints = new Vector<Point2D.Float>();
		_initPos = new Point2D.Float(p.getBody().getWorldCenter().x, p.getBody().getWorldCenter().y);
	}
	
	public PathDef(PhysicsDef p, Vector<Point2D.Float> v) {
		_physDef = p;
		_pathPoints = v;
		_initPos = new Point2D.Float(p.getBody().getWorldCenter().x, p.getBody().getWorldCenter().y);
	}
	
	public void step() {
		if (_pathPoints.size() == 0) return; // don't step if there're no points to step through
		// set up some variables
		Point2D.Float position = new Point2D.Float(_physDef.getBody().getWorldCenter().x, _physDef.getBody().getWorldCenter().y);
		// check if it's near to the target point
		if (position.distance(getAdjusted(_currTarget)) <= 2) { // distance value arbitrarily chosen...
			// it is! increment the point value, loop around if necessary
			if (_currTarget < _pathPoints.size() - 1) { _currTarget++;
			} else { _currTarget = 0; }
		}
		Point2D.Float destination = getAdjusted(_currTarget);
		// find the angle towards the next point, create velocity in that direction
		double angle = Math.atan2((destination.getY() - position.getY()), (destination.getX() - position.getX()));
		Vec2 vel = new Vec2((float)(_velCoeff * Math.cos(angle)), (float)(_velCoeff * Math.sin(angle)));
		// set velocity towards target point
		_physDef.setLinearVelocity(vel);
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
}
