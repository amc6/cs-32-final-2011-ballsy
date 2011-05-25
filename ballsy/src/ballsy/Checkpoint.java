package ballsy;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import bodies.AbstractBody;
import bodies.BodyConstants;

/**
 * Models a checkpoint in a level
 * @author matt
 *
 */

public class Checkpoint {
	private PhysicsWorld _world = PhysicsWorld.getInstance();
	private Window _window = Window.getInstance();
	private AbstractLevel _level = AbstractLevel.getInstance();
	private boolean _active = false, _selected = false, _first = false; // if active, user will be restored here
	// world coords
	private float _x, _y;
	private int _activeColor = _window.color(59, 196, 14),
		_inactiveColor = _window.color(232, 130, 16),
		_selectedColor = _window.color(232, 72, 16),
		_displayWeight = 2; 
	public static final float DEFAULT_SIZE = 5;
	
	public Checkpoint(float x, float y) {
		_x = x;
		_y = y;
	}
	
	public void display() {
		// check for containment
		if (this.contains(_level.getPlayer()) && (_level instanceof XMLLevel || !_level.isPaused())) {_level.setActiveCheckpoint(this);}
		// determine proper color
		_window.strokeWeight(_displayWeight);
		if (_selected) {
			_window.stroke(_selectedColor, 200);
			_window.fill(_selectedColor, 100);
		}
		else if (_active && (_level instanceof XMLLevel || !_level.isPaused())) {
			_window.stroke(_activeColor, 200);
			_window.fill(_activeColor, 100);
		}
		else { 
			_window.stroke(_inactiveColor, 200);
			_window.fill(_inactiveColor, 100);
		}
		// display circle
		_window.ellipse(_world.worldXtoPixelX(_x), _world.worldYtoPixelY(_y), _world.scalarWorldToPixels(DEFAULT_SIZE*2), _world.scalarWorldToPixels(DEFAULT_SIZE*2));
	}
	
	public void setActive(boolean b) {
		_active = b;
	}
	
	public void setSelected(boolean s) {
		_selected = s;
	}
	
	public void setFirst(boolean f) {
		_first = f;
	}
	
	public boolean isFirst() {
		return _first;
	}
		
	public boolean contains(AbstractBody b) {
		Vec2 bPos = b.getPhysicsDef().getBodyWorldCenter();
		return this.contains(bPos);
	}
	
	// passed in world coords
	public boolean contains(Vec2 pos) {
		if (Math.sqrt((pos.x-_x)*(pos.x-_x) + (pos.y-_y)*(pos.y-_y)) - BodyConstants.USER_RADIUS <= DEFAULT_SIZE) return true;
		return false;
	}
	
	// same as above, not considering user ball radius (for level editor)
	public boolean containsClick(Vec2 pos) {
		if (Math.sqrt((pos.x-_x)*(pos.x-_x) + (pos.y-_y)*(pos.y-_y)) <= DEFAULT_SIZE) return true;
		return false;
	}
	
	public Vec2 center() {
		return new Vec2(_x, _y);
	}
	
	public void setPos(Float x, Float y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file, 
	 * provided the proper type.
	 * @return
	 */
	public Element writeXML() {
		// create the graphical def element
		Element newEl = DocumentHelper.createElement("CHECKPOINT");
		// add proper attributes
		newEl.addAttribute("X", Float.toString(_x));
		newEl.addAttribute("Y", Float.toString(_y));
		return newEl;
		
	}
}
