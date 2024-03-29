package bodies;

/**
 * Superclass for all bodies, manages physics and graphics defs, and contains lots of
 * getters and setters for important stuff.
 */

import graphics.GraphicsDef;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsDef;
import physics.PhysicsPath;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;

import static bodies.BodyConstants.*;

public abstract class AbstractBody {

	protected PhysicsWorld _world = PhysicsWorld.getInstance();
	protected AbstractLevel _level = AbstractLevel.getInstance();
	protected Window _window = Window.getInstance();
	private PhysicsDef _physicsDef;
	private GraphicsDef _graphicsDef;
	private PhysicsPath _pathDef;
	private boolean _grappleable = true;
	private boolean _isEndpoint = false;
	private boolean _deadly = false;
	private double SELECTED_OPACITY = .8;
	
	/**
	 * Should check to see if the object can be removed from the world. 
	 * This implementation causes the object to dissappear if it falls below y = 0
	 * in world coordinates.
	 */
	public boolean done() {
		return false;
	}
	
	/**
	 * Display the object using processing graphics.
	 */
	public void display(){
		_graphicsDef.displayEffects();
		if (_grappleable) {
			if (!_graphicsDef.getSelected()) _graphicsDef.setStrokeWeightAndColor(GRAPPLEABLE_STROKE_WEIGHT,GRAPPLEABLE_BORDER_COLOR);
			else _graphicsDef.setStrokeWeightAndColor(GRAPPLEABLE_STROKE_WEIGHT,GRAPPLEABLE_BORDER_COLOR, SELECTED_OPACITY);
		}
		else {
			if (!_graphicsDef.getSelected()) _graphicsDef.setStrokeWeightAndColor(UNGRAPPLEABLE_STROKE_WEIGHT,UNGRAPPLEABLE_BORDER_COLOR);
			else _graphicsDef.setStrokeWeightAndColor(UNGRAPPLEABLE_STROKE_WEIGHT,UNGRAPPLEABLE_BORDER_COLOR, SELECTED_OPACITY);
		}
		if (_deadly) {
			if (!_graphicsDef.getSelected()) _graphicsDef.setColor(DEADLY_FILL_COLOR);
			else _graphicsDef.setColor(DEADLY_FILL_COLOR, SELECTED_OPACITY);
		}
		else {
			if (!_graphicsDef.getSelected()) _graphicsDef.setColor(NOT_DEADLY_FILL_COLOR);
			else _graphicsDef.setColor(NOT_DEADLY_FILL_COLOR, SELECTED_OPACITY);
		}
		// take care of graphical only stuff
		if (_physicsDef.getGraphicalOnly()) {
			_graphicsDef.setColor(GRAPHICAL_ONLY_FILL_COLOR);
			_graphicsDef.setStrokeWeightAndColor(GRAPHICAL_ONLY_STROKE_WEIGHT, GRAPHICAL_ONLY_STROKE_COLOR);
		}
			
		_graphicsDef.display();
	}
	
	/**
	 * setter for both physics and graphics defs
	 * @param physics
	 * @param graphics
	 */
	public void setPhysicsAndGraphics(PhysicsDef physics, GraphicsDef graphics){
		_physicsDef = physics;
		this.setGraphics(graphics);
	}
	
	/**
	 * Setter for just graphics def
	 * @param graphics
	 */
	public void setGraphics(GraphicsDef graphics) {
		_graphicsDef = graphics;
		_graphicsDef.setPhysicsDef(_physicsDef);
	}
	
	/**
	 * Accessor for graphical def.
	 * @return
	 */
	public GraphicsDef getGraphicsDef() {
		return _graphicsDef;
	}
	
	/**
	 * Accessor for physics def.
	 * @return
	 */
	public PhysicsDef getPhysicsDef() {
		return _physicsDef;
	}
	
	/**
	 * Sets the position of the object and its path, if object and all path points are
	 * still inside the world
	 * @param pos
	 */
	public void setPosition(Vec2 pos) {
		if (!PhysicsWorld.getInstance().contains(pos)) return; // don't put it there if it's out of this world
		if (this.getPath() != null) {
			Vec2 currPos = _physicsDef.getBodyWorldCenter();
			Vec2 offset = new Vec2(pos.x - currPos.x, pos.y - currPos.y);
			if (!this.getPath().moveBy(offset)) return; // if the path isn't contained, return
		}
		_physicsDef.setBodyWorldCenter(pos);
	}
	
	/**
	 * Should remove the BallsyObject entirely.
	 */
	public void killBody(){
		_world.unregisterPostStep(_pathDef);
		_world.destroyBody(_physicsDef.getBody()); // remove physics object from physics world
		_level.remove(this); // remove so as to not be called in draw method
	}

	/**
	 * Mutator for the path.
	 * @param p
	 */
	public void setPath(Vector<Point2D.Float> p) {
		this.setPath(new PhysicsPath(this.getPhysicsDef(), p));
	}
	
	/**
	 * Another mutator, because Matt was dumb earlier and used Point2Ds for the
	 * paths... and is currently too lazy to change it.
	 * @param p
	 */
	public void setPath(ArrayList<Vec2> p) {
		Vector<Point2D.Float> vec = new Vector <Point2D.Float>();
		for (Vec2 v : p) {
			vec.add(new Point2D.Float(v.x, v.y));
		}
		this.setPath(vec);
	}
	
	/**
	 * and another for if you have a path already...
	 * @param p
	 */
	public void setPath(PhysicsPath p) {
		_pathDef = p;
		_world.registerPostStep(_pathDef);
	}
	
	/**
	 * path accessor
	 */
	public PhysicsPath getPath() {
		return _pathDef;
	}
	
	/**
	 * Clears the path of the current object and set its velocity to nothing.
	 */
	public void clearPath() {
		_world.unregisterPostStep(_pathDef);
		_pathDef = null;
		_physicsDef.setLinearVelocity(new Vec2(0, 0));
	}
	
	/**
	 * 
	 * @return world position as Vec2
	 */
	public Vec2 getWorldPosition() {
		return _physicsDef.getBodyWorldCenter();
	}
	
	
	/**
	 * @return pixel pos as Vec2
	 */
	public Vec2 getPixelPosition() {
		return _world.coordWorldToPixels(getWorldPosition());
	}
		
	/**
	 * accessor for grappleability of object
	 * @return
	 */
	public boolean isGrappleable() {
		return _grappleable;
	}

	/**
	 * mutator for grappleability of object
	 * @param g
	 */
	public void setGrappleable(boolean g) {
		_grappleable = g;
	}
	
	/**
	 * mutator for endpointness
	 * @param b
	 */
	public void setEndpoint(boolean b) {
		_isEndpoint = b;
		if (b) _grappleable = false;
	}
	
	public boolean isEndpoint() {
		return _isEndpoint;
	}
	
	/**
	 * Accessor/mutator for deadliness..
	 */
	public void setDeadly(boolean b) { _deadly = b;	}
	public boolean isDeadly() { return _deadly;	}
	
	/**
	 * Handle collision of this body with "other" (outside of physics)
	 * @param other
	 */
	public void handleCollision(AbstractBody other, float velocity) {
		// by default, do nothing. Intended to be overridden for special stuff (mostly by UserBall)
	}
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file.
	 * @return
	 */
	public abstract Element writeXML();
	
	/**
	 * General method for XML representation of an AbstractBody. When passed a type kind,
	 * by the subclass, produces proper XML.
	 * @param type
	 * @return
	 */
	public Element writeXML(String type) {
		// create a new Body element
		Element newEl = DocumentHelper.createElement("BODY");
		newEl.addAttribute("TYPE", type);
		newEl.addAttribute("ENDPOINT", Boolean.toString(_isEndpoint));
		newEl.addAttribute("GRAPPLEABLE", Boolean.toString(_grappleable));
		newEl.addAttribute("DEADLY", Boolean.toString(_deadly));
		// add the representations of the physics def, graphical def, and path def (if appropirate)
		newEl.add(_physicsDef.writeXML());
		newEl.add(_graphicsDef.writeXML());
		if (_pathDef != null) {
			newEl.add(_pathDef.writeXML());
		}
		return newEl;
	}
}
