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
import processing.core.PImage;
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
import graphics.Background;
import graphics.TrackingCamera;

public class EditorLevel extends AbstractLevel {
	private final int SELECTED_COLOR = _window.color(100, 100, 100);
	private final float MINIMUM_SIZE = 1; // radius or width-or-height/2
	private Background _background;
	private boolean _running = false;
	private float _minX, _minY, _maxX, _maxY;
	private float _lastMouseX, _lastMouseY;
	private Element _savedState;
	//private int _savedTransX, _savedTransY;
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
	private LevelEditor _editor;
	private float _savedViewX, _savedViewY;
	private String _savefile, _loadfile;
	
	public EditorLevel(LevelEditor editor, BodyFactory factory) {
		_editor = editor;
		// load in the level
		_factory = factory;
		this.setInstance(); // set this level as the singleton
		this.setup(); //run setup
	}
	
	public void setup() {
		//setupWorld(-100, -100, 100, 100);
		setupWorld(0,0,200,200);
		_background = new Background();
		// make a player
//		_player = new UserBall(100, 50, USER_RADIUS);
//		_bodies.add(_player);
//		_player.setInPlay(false);
//		_player.getGraphicsDef().setSmoke(null);
		_paused = true;
		// make an endpoint offset to the right of the body
		//EndPoint ep = new EndPoint(BodyConstants.USER_RADIUS * 5, 0);
//		EndPoint ep = new EndPoint(120,50);
//		_bodies.add(ep);
//		Vec2 pos = _player.getWorldPosition();
//		_world.centerCameraOn(pos.x,pos.y);
	}
	
	/**
	 * Play the current level from the beginning (saved state).
	 */
	public void play() {
		//save view for restoring
		_savedViewX = _world.pixelXtoWorldX(_window.width/2);
		_savedViewY = _world.pixelYtoWorldY(_window.height/2);
		// clear points if we're setting
		_selectedPoints = null;
		if (_selectingPoints && _placeMode){ // so that when we return from playing we create a new polygon
			this.startPoints();
		} else {
			_selectingPoints = false;
		}
		
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
		_world.centerCameraOn(_savedViewX, _savedViewY);
		_editor.updateFieldValues(); // make sure they're correct per XML
	}
	
	/**
	 * recenters camera
	 */
	public void centerCamera() {
		_world.centerCameraOn(_player.getWorldPosition().x, _player.getWorldPosition().y);
	}
	
	public void setupWorld(float minX, float minY, float maxX, float maxY) {
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		// set up the physics world && bodies
//		_world = new PhysicsWorld(_window); //DON'T CALL THIS!!!!
		_world.createWorld(minX, minY, maxX, maxY);
		_world.setGravity(_gravity.x, _gravity.y);
		_bodies = new ArrayList<AbstractBody>();
	}
	
	public void draw() {
		// draw window stuffs
//		_window.background(_backgroundColor);
		_background.draw();
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
			if (!_running && body.getPath() != null && !_selectingPoints && _selectedBody == body && body.getPath().getWorldPoints().size() > 1) {
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
			_camera.update();
			_world.step();
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
			Vec2 lastPoint = _selectedPoints.get(_selectedPoints.size() - 1); 
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
		if (_rotating && _rotationCenter != null && !_resizing) _window.line(_world.worldXtoPixelX(_rotationCenter.x), _world.worldYtoPixelY(_rotationCenter.y), _window.mouseX, _window.mouseY);

		if (_savefile != null){
			this.save(_savefile);
		}
		
		if (_loadfile != null){
			this.load(_loadfile);
		}
	}
	
	public void mousePressed() {
		if (_running) super.mousePressed();
		else {
			if (_window.mouseButton == PConstants.RIGHT && _selectingPoints){
				this.handleRightClick();
			}else if (_window.mouseButton == PConstants.LEFT){
	
				
				_lastMouseX = _window.mouseX;
				_lastMouseY = _window.mouseY;
				if (_selectingPoints) {
					Vec2 newPoint = new Vec2(_world.pixelXtoWorldX(_window.mouseX), _world.pixelYtoWorldY(_window.mouseY));
					//System.out.println("clicked point: " + newPoint);
					if (_placeMode && _selectedPoints.size() > 1) {
						// we're placing a polygon, so points need to be counterclockwise and convex
						ArrayList<Vec2> newPoints = (ArrayList<Vec2>) _selectedPoints.clone();
						newPoints.add(newPoint);
						newPoints = PointMath.sortCCW(newPoints, PointMath.getCenter(newPoints));
											
						if (PointMath.isConvex(newPoints) && _world.contains(newPoint)) {
							// we're good to go
							_selectedPoints = newPoints;
						} else {
							// notify...
							return;
						}
					} else {
						// we're drawing a path, just place dat shit
						if (_world.contains(newPoint))
							_selectedPoints.add(newPoint);
					}
				} else if (_placeMode) {
					// we're placing something, make call to placeObject
					Vec2 newPos = new Vec2(_world.pixelXtoWorldX(_lastMouseX), _world.pixelYtoWorldY(_lastMouseY));
					if (_world.contains(newPos)) this.placeBody(newPos); // don't select it anymore
				} else {
					// we're selecting stuff or moving
					// set selected object to object under mousepress, or null if none
					this.resetSelected(getBody(new Vec2(_world.pixelXtoWorldX(_window.mouseX), _world.pixelYtoWorldY(_window.mouseY))));
					if (_rotating && _selectedBody != null) {
						_rotationCenter = _selectedBody.getWorldPosition();
					}
				}
			}
			// update the gui
			_editor.updateFieldValues();
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
			// update the gui
			_editor.updateFieldValues();
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
				_world.moveCamera(distX, distY);
			} else if (_selectedBody != null && _rotating && _rotationCenter != null && !_resizing) {
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
				float distCX = _world.pixelXtoWorldX(_lastMouseX) - _selectedBody.getWorldPosition().x; //before from center
				float distCY = _world.pixelYtoWorldY(_lastMouseY) - _selectedBody.getWorldPosition().y; //before
				float distCXN = _world.pixelXtoWorldX(_window.mouseX) - _selectedBody.getWorldPosition().x; //new from center
				float distCYN = _world.pixelYtoWorldY(_window.mouseY) - _selectedBody.getWorldPosition().y; //new
				if (_selectedBody instanceof Ball) {
					PhysicsBall ballPhysDef = (PhysicsBall) _selectedBody.getPhysicsDef();
					if (ballPhysDef.getRadius() + distTotal > MINIMUM_SIZE)
						ballPhysDef.setRadius(ballPhysDef.getRadius() + distTotal);
				} else if (_selectedBody instanceof Rectangle) {
					PhysicsRectangle rectPhysDef = (PhysicsRectangle) _selectedBody.getPhysicsDef();
					// get the x & y change along rotation of object
					float rectRotAng = - rectPhysDef.getBody().m_sweep.a;
					// get the rotated distance dragged from center
					float rdx = (float) ((distCXN-distCX) * Math.cos(rectRotAng) - (distCYN-distCY) * Math.sin(rectRotAng));
					float rdy = (float) ((distCXN-distCX) * Math.sin(rectRotAng) + (distCYN-distCY) * Math.cos(rectRotAng));
					// and the rotated point on the shape...
					float dcxn = (float) (distCXN * Math.cos(rectRotAng) - distCYN * Math.sin(rectRotAng));
					float dcxy = (float) (distCXN * Math.sin(rectRotAng) + distCYN * Math.cos(rectRotAng));
					// and now if the point is at the right place on the shape, adjust
					if (dcxn < 0) rdx = -rdx;
					if (dcxy < 0) rdy = -rdy;
					// FINALLY actually do the resize
					if (/*_rotating*/ false) { //remove false if code is fixed
						Vec2 bodyPixelCenter = _selectedBody.getPixelPosition();
						float mouseX = _window.mouseX;
						float mouseY = _window.mouseY;
						float mouseAngle = (float) -Math.atan2(mouseY-bodyPixelCenter.y, mouseX-bodyPixelCenter.x);
						float rotatedMouseAngle = (float) mouseAngle + rectRotAng;
						rotatedMouseAngle = (float) (((rotatedMouseAngle % (Math.PI*2)) + (Math.PI*2)) % (Math.PI*2));
						System.out.println("angle:" + Math.toDegrees(mouseAngle));
						System.out.println("rotation" + Math.toDegrees(rectRotAng));
						System.out.println("rotated angle:" + Math.toDegrees(rotatedMouseAngle));
						float xAngInc = 0, yAngInc = 0;
						if (0<rotatedMouseAngle && rotatedMouseAngle<Math.PI/2) {
							//quadrant 1
							xAngInc = 0;
							yAngInc = (float) (Math.PI/2);
						}
						else if (Math.PI/2<rotatedMouseAngle && rotatedMouseAngle<Math.PI) {
							//quadrant 2
							xAngInc = (float) (Math.PI/2);
							yAngInc = (float) (Math.PI);
						}
						else if (Math.PI<rotatedMouseAngle && rotatedMouseAngle<3*Math.PI/2) {
							//quadrant 3
							xAngInc = (float) (Math.PI);
							yAngInc = (float) (3*Math.PI/3);
						}
						else if (3*Math.PI/2<rotatedMouseAngle && rotatedMouseAngle<2*Math.PI) {
							//quadrant 4
							xAngInc = (float) (3*Math.PI/3);
							yAngInc = (float)  (2*Math.PI);
						}
						
						// if they're also pressing shift, we'll only resize in the direction primarily pulled
						if (Math.abs(rdy) > Math.abs(rdx) && rectPhysDef.getHeight() + rdy * 2 > MINIMUM_SIZE) { // resize height
							rectPhysDef.setHeight(rectPhysDef.getHeight() + rdy);// move it in the direction of the cursor
							rectPhysDef.setBodyWorldCenter(new Vec2(rectPhysDef.getBodyWorldCenter().x + (float) (rdy/2 * Math.cos(rectRotAng+xAngInc)), rectPhysDef.getBodyWorldCenter().y + (float) (rdy/2 * Math.sin(rectRotAng+yAngInc))));
						} else if (rectPhysDef.getWidth() + rdx * 2 > MINIMUM_SIZE) { // resize width
							rectPhysDef.setWidth(rectPhysDef.getWidth() + rdx);// move it in the direction of the cursor
							rectPhysDef.setBodyWorldCenter(new Vec2(rectPhysDef.getBodyWorldCenter().x + (float) (rdx/2 * Math.cos(rectRotAng+xAngInc)), rectPhysDef.getBodyWorldCenter().y + (float) (rdx/2 * Math.sin(rectRotAng+yAngInc))));
						}
					} else {
						// just resize as expected
						if (rectPhysDef.getHeight() + rdy * 2 > MINIMUM_SIZE)
							rectPhysDef.setHeight(rectPhysDef.getHeight() + rdy);
						if (rectPhysDef.getWidth() + rdx * 2 > MINIMUM_SIZE)
							rectPhysDef.setWidth(rectPhysDef.getWidth() + rdx);// move it in the direction of the cursor
						rectPhysDef.setBodyWorldCenter(new Vec2(rectPhysDef.getBodyWorldCenter().x + (distCXN-distCX)/2, rectPhysDef.getBodyWorldCenter().y + (distCYN-distCY)/2));
					}
				} else if (_selectedBody.getPhysicsDef() instanceof PhysicsPolygon) {
					float distLast = (float) Math.sqrt(distCX * distCX + distCY * distCY);
					float ratio = Math.abs((distLast + distTotal) / distLast); // so no mirroring with a negative ratio
					PhysicsPolygon polyPhysDef = (PhysicsPolygon) _selectedBody.getPhysicsDef();
					polyPhysDef.scalePoints(ratio); // will check for size and not execute if it'll be too small
				}
			}
			// update the gui
			_editor.updateFieldValues();
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
			if (_selectedBody != null && _selectedBody != newSelected) {
				_selectedBody.getGraphicsDef().setSelected(false);
			}
			if (newSelected != null && _selectedBody != newSelected) {
				newSelected.getGraphicsDef().setSelected(true);
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
		_editor.updateFieldValues();
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
			super.keyPressed();
		} else {
			switch (_window.key) {
			case 'q':
				System.exit(0);
				break;
			case 'd':
				if (_selectedBody != null && !_placeMode && 
						!(_selectedBody instanceof UserBall) && !(_selectedBody instanceof EndPoint)) {
					_selectedBody.killBody();
					this.resetSelected(null);
				}
				break;
			case 'x':
				// snap shape to nearest rotation
				if (_selectedBody != null && _selectedBody instanceof Rectangle) {
					float offset = (float) (_selectedBody.getPhysicsDef().getBody().m_sweep.a % (Math.PI/2));
					if (Math.abs(offset) < Math.PI/4) {
						// subtract
						_selectedBody.getPhysicsDef().setRotation(_selectedBody.getPhysicsDef().getRotation() - offset);
					} else {
						// add
						_selectedBody.getPhysicsDef().setRotation((float) (_selectedBody.getPhysicsDef().getRotation() + (Math.PI/2 - offset)));
					}
				}
				break;
			case 'z':
				// user pressed z to resize
				_resizing = true;
			}
			// other shit (backspace, and other non character keys)
			if (_window.key == 27) {
				// clear points if we're setting if they press esc
				_window.key = 0;
				this.clearPoints(); // abandonded click creation prematurely!
			} else if (_window.keyCode == PConstants.SHIFT) {
				// user has pressed shift
				_rotating = true;
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
	
	public boolean isRunning(){
		return _running;
	}
	
	public void setPlacemode(boolean placemode){
		this.resetSelected(null);
		_placeMode = placemode;
	}

	public void handleRightClick(){
		
		if (_placeMode) {
			// finish/create the polygon
			_selectingPoints = false;
			Vec2 center = PointMath.getCenter(_selectedPoints);
			if (_world.contains(center) && _selectedPoints.size() > 2) {
				_factory.polyPoints = PhysicsPolygon.getOffsets(_selectedPoints, center);
				_factory.setBody(BodyFactory.IPOLY);
				_selectedBody = this.placeBody(center);
			}
			this.resetSelected(null);			
			this.startPoints(); // allow the user to start a new polygon
		} else if (_selectedBody != null && !(_selectedBody instanceof UserBall)){
			// stop selecting points, apply selected for pathing
			_selectingPoints = false;
			_selectedBody.setPath(PhysicsPolygon.getOffsets(_selectedPoints, _selectedBody.getWorldPosition()));
			_selectedBody.getPath().setStatic(!_selectedBody.getPhysicsDef().getMobile());
			_selectedPoints = null;
			_editor.updateFieldValues();
		}

	}
	
	public void startPoints(){
		// start or finish a chain of points
		if (_placeMode) {
			// start or complete an irregular polygon

			// start a polygon
			_selectedPoints = new ArrayList<Vec2>();
			_selectingPoints = true;

		} else if (_selectedBody != null && !(_selectedBody instanceof UserBall)){
			// make a path if an object is selected

			// start selecting poitns
			_selectingPoints = true;
			_selectedPoints = new ArrayList<Vec2>();
			_selectedPoints.add(_selectedBody.getWorldPosition()); // add the center of the body to start

		}
	}
	
	public void clearPoints(){
		_selectedPoints = null;
		_selectingPoints = false;
	}
	
	public boolean selectingPoints() {
		return _selectingPoints;
	}

	public AbstractBody getSelected(){
		return _selectedBody;
	}
	
	public void makeThumbnail(String path) {
		System.out.println("makeThumbnail called");
		//PhysicsWorld world = PhysicsWorld.getInstance();
		this.play();
//		_player.setCrosshairVisible(false);
		this.draw();
//		_player.setCrosshairVisible(true);
		this.stop();
		_window.save("levels/thumbs/tempThumb.png");
		PImage img = _window.loadImage("levels/thumbs/tempThumb.png");
		int captureSize = 500;
		int xMin = img.width/2-captureSize/2;
		int yMin = img.height/2-captureSize/2;
		PImage thumb = img.get(xMin, yMin, captureSize, captureSize);
		thumb.resize(menu.MenuConstants.THUMBNAIL_SIZE, menu.MenuConstants.THUMBNAIL_SIZE);
		thumb.save(path);
	}
	
	public void updateWorldDimensions(float width, float height){
		_world.setBounds(width, height);
		_savedState = XMLUtil.getInstance().genXML(this);
		// load in saved state
		XMLUtil.getInstance().restoreXML(this, _savedState);
		// make extra sure we're not running
		_running = false;
		_paused = false; // just so we toggle off of that
		this.togglePaused();
		_background = new Background();
		
		
	}
	
	public void setSave(String name){
		_savefile = name;
	}
	
	public void save(String name) {
		_savefile = null;
		this.resetSelected(null);
		String pathname = "levels/" + name;
		String thumbPathname = "levels/thumbs/" + name.substring(0, name.indexOf('.')) + ".png";
		XMLUtil.getInstance().writeFile(this, pathname);
		this.makeThumbnail(thumbPathname);
		XMLUtil.getInstance().addMenuButton(pathname, thumbPathname);
	}
	
	public void setLoad(String name){
		_loadfile = name;
	}
	
	public void load(String name) {
		_loadfile = null;
		
		XMLUtil.getInstance().readFile(this, name);
		_background = new Background();
		this.play();
		this.stop(); //lol this gets rid of crosshair and smoke. we should probs do it more directly iunno.
		this.centerCamera();
		
	}
	
}
