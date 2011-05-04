package editor;

import static bodies.BodyConstants.USER_RADIUS;

import java.util.ArrayList;
import java.util.Collections;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsBall;
import physics.PhysicsDef;
import physics.PhysicsPolygon;
import physics.PhysicsRectangle;
import physics.PhysicsRegularPolygon;
import physics.PhysicsWorld;
import processing.core.PConstants;
import ballsy.AbstractLevel;
import ballsy.XMLUtil;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.BodyConstants;
import bodies.EndPoint;
import bodies.IrregularPolygon;
import bodies.Rectangle;
import bodies.RegularPolygon;
import bodies.UserBall;
import graphics.TrackingCamera;

public class EditorLevel extends AbstractLevel {
	private final int SELECTED_COLOR = _window.color(100, 100, 100);
	private final float MINIMUM_SIZE = 1; // radius or width-or-height/2
	private boolean _running = false;
	private float _minX, _minY, _maxX, _maxY;
	private float _lastMouseX, _lastMouseY;
	private Element _savedState;
	private AbstractBody _selectedBody;
	private boolean _placeMode = false; // either placing or modifying (or running, I guess)
	private BodyFactory _factory;
	private ArrayList<Vec2> _selectedPoints; // WORLD positions of selected points
	private boolean _selectingPoints = false;
	private int _previousColor;
	private TrackingCamera _camera;
	private boolean _rotating = false;
	private Vec2 _rotationCenter;
	private boolean _resizing = false;
	
	public EditorLevel() {
		// load in the level
		_factory = new BodyFactory();
		this.setInstance(); // set this level as the singleton
		this.setup(); //run setup
	}
	
	public void setup() {
		setupWorld(-100, -100, 100, 100);
		// make a player
		_player = new UserBall(0, 0, USER_RADIUS);
		_bodies.add(_player);
		_player.setInPlay(false);
		_player.getGraphicsDef().setSmoke(null);
		_paused = true;
		// make an endpoint offset to the right of the body
		EndPoint ep = new EndPoint(BodyConstants.USER_RADIUS * 5, 0);
		_bodies.add(ep);
	}
	
	/**
	 * Play the current level from the beginning (saved state).
	 */
	public void play() {
		// clear points if we're setting
		_selectedPoints = null;
		_selectingPoints = false;
		// reset selected object
		this.resetSelected(null);
		// save the state of the level (temporarily)
		_savedState = XMLUtil.getInstance().genXML(this);
		// set the game and player to running
		_running = true;
		this.togglePaused();
		// set up camera
		_camera = new TrackingCamera(_player);
	}
	
	/**
	 * Stop the level and restore the saved beginning state.
	 */
	public void stop() {
		// load in saved state
		XMLUtil.getInstance().restoreXML(this, _savedState);
		// stop the running
		_running = false;
		this.togglePaused();
		_camera = null;
	}
	
	public void setupWorld(float minX, float minY, float maxX, float maxY) {
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		// set up the physics world && bodies
		_world = new PhysicsWorld(_window);
		_world.createWorld(minX, minY, maxX, maxY);
		_world.setGravity(_gravity.x, _gravity.y);
		_bodies = new ArrayList<AbstractBody>();
	}
	
	public void draw() {
		// draw window stuffs
		_window.background(_backgroundColor);
		// draw a line around the world
		_window.stroke(0);
		_window.strokeWeight(1);
		_window.line(_world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_minY), _world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_minY)); //bottom
		_window.line(_world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_minY), _world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_maxY)); //left
		_window.line(_world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_maxY), _world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_maxY)); //top
		_window.line(_world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_minY), _world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_maxY)); //right		
		// reset line width
		_window.stroke(ballsy.GeneralConstants.DEFAULT_LINE_WIDTH);
		// display all objects
		for (AbstractBody body : _bodies) { 
			// if not running and not selecting points, display the path behind the selected body body
			if (!_running && body.getPath() != null && !_selectingPoints && _selectedBody == body) {
				_window.stroke(200);
				_window.strokeWeight(2);
				ArrayList<Vec2> pathPoints = body.getPath().getWorldPoints();
				if (pathPoints.size() < 2) continue; // exit this iteration of the loop if the path only has 1 or less points
				Vec2 last = pathPoints.get(pathPoints.size()-1);
				for (Vec2 p : pathPoints) {
					_window.line(_world.worldXtoPixelX(last.x), _world.worldYtoPixelY(last.y), _world.worldXtoPixelX(p.x), _world.worldYtoPixelY(p.y));
					last = p;
				}
			}
			body.display(); // display the body itself
		}
		// do the running things if it's running
		if (_running) {
			_window.noCursor();
			// step physics world and camera
			_world.step();
			_camera.update();
			// handle keypresses
			this.applyInput();
		} else {
			_window.cursor();
		}
		// draw the points in teh point array
		_window.fill(50);
		_window.stroke(50);
		_window.strokeWeight(2);
		if (_selectedPoints != null && _selectedPoints.size() > 0) {
			Vec2 lastPoint = null; 
			for (Vec2 v : _selectedPoints) {
				_window.ellipse(_world.worldXtoPixelX(v.x), _world.worldYtoPixelY(v.y), 10, 10);
				if (lastPoint != null)
					_window.line(_world.worldXtoPixelX(lastPoint.x), _world.worldYtoPixelY(lastPoint.y), _world.worldXtoPixelX(v.x), _world.worldYtoPixelY(v.y));
				lastPoint = v;
			}
		}
		// if rotating, draw a line of rotation...
		_window.fill(80);
		_window.stroke(80);
		if (_rotating && _rotationCenter != null) _window.line(_world.worldXtoPixelX(_rotationCenter.x), _world.worldYtoPixelY(_rotationCenter.y), _window.mouseX, _window.mouseY);
	}
	
	public void mousePressed() {
		if (_running) super.mousePressed();
		else {
			_lastMouseX = _window.mouseX;
			_lastMouseY = _window.mouseY;
			if (_selectingPoints) {
				// put a new point at the location clicked
				Vec2 newPoint = new Vec2(_world.pixelXtoWorldX(_window.mouseX), _world.pixelYtoWorldY(_window.mouseY));
				if (_world.contains(newPoint))
					_selectedPoints.add(newPoint);
			} else if (_placeMode) {
				// we're placing something, make call to placeObject
				Vec2 newPos = new Vec2(_world.pixelXtoWorldX(_lastMouseX), _world.pixelYtoWorldY(_lastMouseY));
				if (_world.contains(newPos)) _selectedBody = this.placeBody(newPos);
			} else {
				// we're selecting stuff or moving
				// set selected object to object under mousepress, or null if none
				this.resetSelected(getBody(new Vec2(_world.pixelXtoWorldX(_window.mouseX), _world.pixelYtoWorldY(_window.mouseY))));
				if (_rotating && _selectedBody != null) {
					_rotationCenter = _selectedBody.getWorldPosition();
				}
			}
		}
	}
	
	public void mouseReleased() {
		if (_running) {
			super.mouseReleased();
		} else {
			// if we're placing, we don't want anything to be selected on mouseup
			if (_placeMode) this.resetSelected(null);
			// the rotation center should be null now
			_rotationCenter = null;
		}
	}
	
	public void mouseDragged() {
		if (_running) super.mouseDragged();
		else {
			// it's not running, so perform editing stuff
			float distX = - _window.mouseX + _lastMouseX;
			float distY = _window.mouseY - _lastMouseY;
			if (_selectedBody != null && !_selectingPoints && !_rotating && !_resizing) {
				// we're dragging a body around
				PhysicsDef physDef = _selectedBody.getPhysicsDef();
				float xNew = physDef.getBody().getXForm().position.x - _world.scalarPixelsToWorld(distX);
				float yNew = physDef.getBody().getXForm().position.y - _world.scalarPixelsToWorld(distY);
				// make sure that, if there is a path, it's contained in the bounds of the world.
				boolean pathContained = true;
				ArrayList<Vec2> newPathPoints = new ArrayList<Vec2>();
				if (_selectedBody.getPath() != null) {
					for (Vec2 p : _selectedBody.getPath().getWorldPoints()) {
						Vec2 newPP = new Vec2(p.x - _world.scalarPixelsToWorld(distX), p.y - _world.scalarPixelsToWorld(distY));
						if (!_world.contains(newPP)) {
							pathContained = false;
							break;
						} else {
							newPathPoints.add(newPP);
						}
					}
				}
				// if teh world contains the new point and all the new path points
				if (_world.contains(new Vec2(xNew, yNew)) && pathContained) {
					physDef.getBody().setXForm(new Vec2(xNew, yNew), physDef.getBody().getAngle());
					if (_selectedBody.getPath() != null) _selectedBody.setPath(PhysicsPolygon.getOffsets(newPathPoints, new Vec2(xNew, yNew)));
				}
				// else notify?
			} else if (!_placeMode && !_rotating && !_resizing){
				// we're not, move the camera
				_world.moveCamera(distX, distY, true);
			} else if (_selectedBody != null && _rotating && _rotationCenter != null) {
				// we're rotating the object. Calculate the angle...
				float angleNow = (float) Math.atan2(_world.pixelYtoWorldY(_window.mouseY) - _rotationCenter.y, (_world.pixelXtoWorldX(_window.mouseX) - _rotationCenter.x));
				float angleBefore = (float) Math.atan2(_world.pixelYtoWorldY(_lastMouseY) - _rotationCenter.y, (_world.pixelXtoWorldX(_lastMouseX) - _rotationCenter.x));
				float angleDiff = angleNow - angleBefore;
				// set angle
				float angle = _selectedBody.getPhysicsDef().getBody().getAngle();
				_selectedBody.getPhysicsDef().setRotation(angle + angleDiff);
			} else if (_selectedBody != null && _resizing) {
				// resizing object. Respond depending on what it is.
				float distXW = - _world.scalarPixelsToWorld(distX);
				float distYW = - _world.scalarPixelsToWorld(distY);
				float distTotal = (float) Math.sqrt(distXW * distXW + distYW * distYW); // for use with reg. polygons and circles
				if (distXW < 0) distTotal = -distTotal; // to compensate for always positive sqrt
				if (_selectedBody instanceof RegularPolygon) {
					PhysicsRegularPolygon polyPhysDef = (PhysicsRegularPolygon) _selectedBody.getPhysicsDef();
					if (polyPhysDef.getRadius() + distTotal > MINIMUM_SIZE)
						polyPhysDef.resizeBy(distTotal);
				} else if (_selectedBody instanceof Ball) {
					PhysicsBall ballPhysDef = (PhysicsBall) _selectedBody.getPhysicsDef();
					if (ballPhysDef.getRadius() + distTotal > MINIMUM_SIZE)
						ballPhysDef.setRadius(ballPhysDef.getRadius() + distTotal);
				} else if (_selectedBody instanceof Rectangle) {
					PhysicsRectangle rectPhysDef = (PhysicsRectangle) _selectedBody.getPhysicsDef();
					if (rectPhysDef.getHeight() + distYW * 2 > MINIMUM_SIZE)
						rectPhysDef.setHeight(rectPhysDef.getHeight() + distYW * 2);
					if (rectPhysDef.getWidth() + distXW * 2 > MINIMUM_SIZE)
						rectPhysDef.setWidth(rectPhysDef.getWidth() + distXW * 2);
				} else if (_selectedBody instanceof IrregularPolygon) {
					float distCX = _world.pixelXtoWorldX(_lastMouseX) - _selectedBody.getWorldPosition().x;
					float distCY = _world.pixelYtoWorldY(_lastMouseY) - _selectedBody.getWorldPosition().y;
					float distLast = (float) Math.sqrt(distCX * distCX + distCY * distCY);
					float ratio = (distLast + distTotal) / distLast;
					PhysicsPolygon polyPhysDef = (PhysicsPolygon) _selectedBody.getPhysicsDef();
					polyPhysDef.scalePoints(ratio, MINIMUM_SIZE); // will check for size and not execute if it'll be too small
				}
			}
			// reset mouse shits
			_lastMouseX = _window.mouseX;
			_lastMouseY = _window.mouseY;
		}
	}
	
	private AbstractBody placeBody(Vec2 pos) {
		AbstractBody newShape = _factory.getBody(pos);
		_bodies.add(newShape);
		_previousColor = newShape.getGraphicsDef().getColor(); // because we're selecting it while it's placed
		return newShape;
	}
	
	/**
	 * Helper method to select a new body, as passed in.
	 * if passed null, then simply deselects currently selected body
	 * @param newSelected
	 */
	private void resetSelected(AbstractBody newSelected) {
		if (!_selectingPoints) {
			// if we're not making a path
			if (_selectedBody != null && _selectedBody != newSelected) 
				_selectedBody.getGraphicsDef().setColor(_previousColor);
			if (newSelected != null && _selectedBody != newSelected) {
				_previousColor = newSelected.getGraphicsDef().getColor();
				newSelected.getGraphicsDef().setColor(SELECTED_COLOR);
			}
			_selectedBody = newSelected;
			// if the new body exists, move it to the end of the array, so it's drawn last.
			// swaps incrementally to keep objects in reverse order of last touched
			if (_selectedBody != null) {
				int index = _bodies.indexOf(newSelected);
				for (int i = index; i < _bodies.size() - 1; i++) {
					Collections.swap(_bodies, i, i + 1);
				}
			}
		}
	}
	
	/**
	 * Helper method to find which object contains a given point. Returns null if no containment.
	 * @param point
	 * @return
	 */
	private AbstractBody getBody(Vec2 point) {
		if (point == null) return null; // if passed a null point, output should be null
		ArrayList<AbstractBody> list = this.getBodies();
		for (int i = list.size() - 1; i>=0; i--) {
			// iterate through all bodies backwards, checking for containment of point
			// backwards, because that's how they're drawn
			AbstractBody b = list.get(i);
			if (b.getPhysicsDef().getBody().getShapeList().testPoint(b.getPhysicsDef().getBody().getXForm(), point)) {
				return b;
			}
		}
		return null; // it didn't find an object, return null
	}
	
	public void keyPressed() {
		if (_running) {
			// act as normal, unless they press r or escape
			if (_window.key == 'r' || _window.key == 27) { 
				_window.key = 0;
				this.stop();
			} else super.keyPressed();
		} else {
			switch (_window.key) {
			case 'r': 
				// run it if not running
				this.resetSelected(null);
				this.play();
				break;
			case 't':
				// toggle placing
				this.resetSelected(null);
				_placeMode = !_placeMode;
				break;
			case 'y':
				// toggle static (for body and path)
				if (!_placeMode && _selectedBody != null && !(_selectedBody instanceof UserBall)) {
					_selectedBody.getPhysicsDef().setMobile(!_selectedBody.getPhysicsDef().getMobile());
					if (_selectedBody.getPath() != null) _selectedBody.getPath().setStatic(!_selectedBody.getPhysicsDef().getMobile());
				} else if (_placeMode) {
					_factory.fixed = !_factory.fixed;
				}
			case 'b':
				// set rectangle
				_factory.setBody(BodyFactory.RECT);
				break;
			case 'n':
				// set ball
				_factory.setBody(BodyFactory.BALL);
				break;
			case 'm':
				// set reg poly
				_factory.setBody(BodyFactory.RPOLY);
				break;
			case ',':
				// start or finish a chain of points
				if (_placeMode) {
					// start or complete an irregular polygon
					if (!_selectingPoints) {
						// start a polygon
						_selectedPoints = new ArrayList<Vec2>();
						_selectingPoints = true;
					} else {
						// finish/create the polygon
						_selectingPoints = false;
						Vec2 center = PointMath.getCenter(_selectedPoints);
						if (_world.contains(center) && _selectedPoints.size() > 2) {
							_factory.polyPoints = PhysicsPolygon.getOffsets(_selectedPoints, center);
							_factory.setBody(BodyFactory.IPOLY);
							_selectedBody = this.placeBody(center);
						}
						_selectedPoints = null;
					}
				} else if (_selectedBody != null && !(_selectedBody instanceof UserBall)){
					// make a path if an object is selected
					if (!_selectingPoints) {
						// start selecting poitns
						_selectingPoints = true;
						_selectedPoints = new ArrayList<Vec2>();
						_selectedPoints.add(_selectedBody.getWorldPosition()); // add the center of the body to start
					} else {
						// stop selecting points, apply selected
						_selectingPoints = false;
						_selectedBody.setPath(PhysicsPolygon.getOffsets(_selectedPoints, _selectedBody.getWorldPosition()));
						_selectedBody.getPath().setStatic(!_selectedBody.getPhysicsDef().getMobile());
						_selectedPoints = null;
					}
				}
				break;
			case '.':
				// remove path if selected, not place mode, and path exists
				if (!_placeMode && _selectedBody != null && _selectedBody.getPath() != null) {
					_selectedBody.clearPath();
				}
				break;
			case 'q':
				System.exit(0);
			}
			// other shit (backspace, and other non character keys)
			if (_window.keyCode == PConstants.BACKSPACE || _window.keyCode == PConstants.DELETE) {
				// handle deletion of object (as long as it's not player or endpoint or null)
				if (_selectedBody != null && !_placeMode && 
						!(_selectedBody instanceof UserBall) && !(_selectedBody instanceof EndPoint)) 
					_selectedBody.killBody();
			} else if (_window.key == 27) {
				_window.key = 0;
				// clear points if we're setting if they press esc
				_selectedPoints = null;
				_selectingPoints = false;
			} else if (_window.keyCode == PConstants.SHIFT) {
				// user has pressed shift
				_rotating = true;
			} else if (_window.key == 'z') {
				// user pressed z to resize
				_resizing = true;
			}
		}
	}
	
	public void keyReleased() {
		if (_running) super.keyReleased();
		else if (_window.keyCode == PConstants.SHIFT) {
			// user released shift
			_rotating = false;
		} else if (_window.key == 'z') {
			// user released z
			_resizing = false;
		}
	}
}
