package bodies;

import graphics.GraphicsDef;

import java.awt.geom.Point2D;
import java.util.Vector;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsPath;
import physics.PhysicsDef;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;

public abstract class AbstractBody {

	protected PhysicsWorld _world = PhysicsWorld.getInstance();
	protected AbstractLevel _level = AbstractLevel.getInstance();
	private PhysicsDef _physicsDef;
	private GraphicsDef _graphicsDef;
	private PhysicsPath _pathDef;
	protected boolean _grappleable = true;
	
	/**
	 * Should check to see if the object can be removed from the world. 
	 * This implementation causes the object to dissapear if it falls below y = 0
	 * in world coordinates.
	 */
	public boolean done() {
//		Vec2 pos = _physicsDef.getBodyWorldCenter();
//		if (pos.y  < 0) {
//			return true;
//		}
		return false;
	}
	
	/**
	 * Display the object using processing graphics.
	 */
	public void display(){
		if (_pathDef != null) _pathDef.step();
		_graphicsDef.display();
	}
	
	public void setPhysicsAndGraphics(PhysicsDef physics, GraphicsDef graphics){
		_physicsDef = physics;
		_graphicsDef = graphics;
		_graphicsDef.setPhysicsDef(physics);
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
	 * Should remove the BallsyObject entirely.
	 */
	public void killBody(){
		_world.destroyBody(_physicsDef.getBody()); // remove physics object from physics world
		_level.remove(this); // remove so as to not be called in draw method
	}

	/**
	 * Mutator for the path.
	 * @param p
	 */
	public void setPath(Vector<Point2D.Float> p) {
		_pathDef = new PhysicsPath(this.getPhysicsDef(), p);
	}
	
	/**
	 * and another for if you have a path already...
	 * @param p
	 */
	public void setPath(PhysicsPath p) {
		_pathDef = p;
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
		// add the representations of the physics def, graphical def, and path def (if appropirate)
		newEl.add(_physicsDef.writeXML());
		newEl.add(_graphicsDef.writeXML());
		if (_pathDef != null) {
			newEl.add(_pathDef.writeXML());
		}
		return newEl;
	}
}
