package graphical;

/**
 * Class to model the graphical interface for the grappling hook (where it will shoot, etc)
 * and also to identify where it will impact what object.
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.jbox2d.collision.Segment;
import org.jbox2d.collision.SegmentCollide;
import org.jbox2d.common.RaycastResult;
import org.jbox2d.common.Vec2;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;
import bodies.AbstractBody;
import bodies.UserBall;

public class Crosshair {
	private PhysicsWorld _world;
	private UserBall _player;
	private boolean _hidden = false;
	float _pointerDistCoeff = 1.2f, _range = 70f;
	int _weight = 2, _size = 12, _pointerSize = 50, _fillColor = 255, _fillOpacity = 150, 
		_activeDrawColor = Window.getInstance().color(0, 255, 0), _inactiveDrawColor = 200, _drawColor = _inactiveDrawColor;
	
	public Crosshair(PhysicsWorld w, UserBall b) {
		_world = w;
		_player = b;
	}
	
	/**
	 * Display the crosshair. If "hidden", only displays the cursor thing (looks like an actual crosshair),
	 * otherwise draws a short line outside the ball to indicate where the grapple will shoot.
	 */
	public void display() {
		Window window = Window.getInstance();
		
		// display cursor crosshair
		int x = window.mouseX;
		int y = window.mouseY;
		window.strokeWeight(_weight);
		if (_hidden) window.stroke(_inactiveDrawColor);
		else window.stroke(_drawColor);
		window.fill(_fillColor, _fillOpacity);
		window.ellipse(x, y, _size, _size);
		window.line(x-_size, y, x+_size, y);
		window.line(x, y-_size, x, y+_size);
		
		if (!_hidden) {
			// display line from ball
			int ballX = (int) _world.worldXtoPixelX(_player.getPhysicsDef().getBody().getPosition().x);
			int ballY = (int) _world.worldYtoPixelY(_player.getPhysicsDef().getBody().getPosition().y);
			double angle = Math.atan2((y - ballY), (x - ballX)); // angle to cursor
			int startX = (int) (ballX + _world.scalarWorldToPixels(_player.getPhysicsDef().getRadius() * _pointerDistCoeff) * Math.cos(angle));
			int startY = (int) (ballY + _world.scalarWorldToPixels(_player.getPhysicsDef().getRadius() * _pointerDistCoeff) * Math.sin(angle));
			int endX = startX + (int) (_pointerSize * Math.cos(angle));
			int endY = startY + (int) (_pointerSize * Math.sin(angle));
			window.line(startX, startY, endX, endY);
		}
		
		// reset window stroke weight
		window.strokeWeight(AbstractLevel.DEFAULT_WEIGHT);
	}
	
	/**
	 * Gets the point of intersection of the ray in the direction of the cursor up to the range of the grapple
	 * with any object provided in the list of bodies.
	 * Returns null if no grappleable object is available within range, or if the only grappleable object in range
	 * is behind an ungrappleable object.
	 * @param bodies
	 * @return
	 */
	public Point2D.Float getGrapplePoint(ArrayList<AbstractBody> bodies) {
		Window window = Window.getInstance();
		// get graphical XY of ball for proper angle, and get angle
		int ballGX = (int) _world.worldXtoPixelX(_player.getPhysicsDef().getBody().getPosition().x);
		int ballGY = (int) _world.worldYtoPixelY(_player.getPhysicsDef().getBody().getPosition().y);
		double angle = -Math.atan2((window.mouseY - ballGY), (window.mouseX - ballGX)); // angle to cursor
		// get the coords of the ball in physics world (not graphical position)
		float ballX = _player.getPhysicsDef().getBody().getPosition().x;
		float ballY = _player.getPhysicsDef().getBody().getPosition().y;
		// get the coords of the maximum point away from the ball
		float maxX = (float) (ballX + _range * Math.cos(angle)); 
		float maxY = (float) (ballY + _range * Math.sin(angle)); 
		
		/////// UNCOMMENT this to see a graphical representation of the range of the grapple
		//// window.stroke(200);
		//// window.line(_world.worldXtoPixelX(ballX), _world.worldYtoPixelY(ballY), _world.worldXtoPixelX(maxX), _world.worldYtoPixelY(maxY));
		//// window.strokeWeight(AbstractLevel.DEFAULT_WEIGHT);
		////////
		
		// iterate through bodies
		Point2D.Float grapplePoint = null;
		float minDist = _range + 1; // set minDist beyond the range
		for (AbstractBody body : bodies) {
			// get the intersection of the ray between the ball and the end of the range in the direction of the cursor
			RaycastResult out = new RaycastResult();
			Segment segment = new Segment();
			segment.p1.set(new Vec2(ballX, ballY));
			segment.p2.set(new Vec2(maxX, maxY));
			SegmentCollide hit = body.getPhysicsDef().getBody().getShapeList().testSegment(body.getPhysicsDef().getBody().getXForm(), out, segment, 1);
			// check if there is an intersection
			if (hit == SegmentCollide.HIT_COLLIDE) {
				// there is! calculate the point of intersection: alpha is percentage of segment length (range) at which intersection occurs
				Point2D.Float currPoint = new Point2D.Float((float) (ballX + _range * out.lambda * Math.cos(angle)),
						(float) (ballY + _range * out.lambda * Math.sin(angle)));
				float currDist = (float) currPoint.distance(new Point2D.Float(ballX, ballY));
				// check for distance, to make sure we return the closest intersection
				if (currDist < minDist) {
					minDist = currDist;
					if ( body.isGrappleable()) grapplePoint = currPoint;
					else grapplePoint = null; // if the body isn't grappleable, and is closer (i.e. in the way), we can't have access to the former body
				}
			}
		}
		// draw the results: a small circle at the point of intersection, for now
		if (grapplePoint != null) {
			if (!_hidden) {
				_drawColor = _activeDrawColor;
				window.strokeWeight(_weight);
				window.ellipse(_world.worldXtoPixelX(grapplePoint.x), _world.worldYtoPixelY(grapplePoint.y), 10, 10);
				window.strokeWeight(AbstractLevel.DEFAULT_WEIGHT);
			}
		} else { _drawColor = _inactiveDrawColor; }
		return grapplePoint;
	}
	
	/**
	 * Hide the crosshair.
	 */
	public void hide() {
		_hidden = true;
	}
	
	/**
	 * Show the crosshair.
	 */
	public void show() {
		_hidden = false;
	}
}
