package bodies;

import graphical.GraphicalDef;

import java.awt.geom.Point2D;
import java.util.Vector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ballsy.AbstractLevel;
import ballsy.Window;
import physics.PathDef;
import physics.PhysicsDef;
import physics.PhysicsWorld;

public abstract class AbstractBody {

	private PhysicsWorld _world;
	private AbstractLevel _level;
	protected PhysicsDef _physicsDef;
	private GraphicalDef _graphicalDef;
	private PathDef _pathDef;
	private boolean _grappleable = true;
	
	public AbstractBody(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef){
		this.constructorHelper(level, world, physicsDef, graphicalDef);
	}
	
	public AbstractBody(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, PathDef pathDef){
		this.constructorHelper(level, world, physicsDef, graphicalDef);
		_pathDef = pathDef; 
	}	
	
	private void constructorHelper(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef){
		_level = level;
		_world = world;
		_physicsDef = physicsDef;
		_graphicalDef = graphicalDef;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);	
	}
		
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
		_graphicalDef.display();
	}
	
	public void setGraphicalDef(GraphicalDef gd) {
		_graphicalDef = gd;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);
	}
	
	/**
	 * Should remove the BallsyObject entirely.
	 */
	public void killBody(){
		_world.destroyBody(_physicsDef.getBody()); // remove physics object from physics world
		_level.remove(this); // remove so as to not be called in draw method
	}
	
	/**
	 * Applies a force given by the parameter to the object's 
	 * center of mass.
	 * @param vector
	 */
	public void applyForce(Vec2 vector){
		_physicsDef.applyForce(vector);
	}
	
	/**
	 * Accessor for graphical def.
	 * @return
	 */
	public GraphicalDef getGraphicalDef() {
		return _graphicalDef;
	}
	
	/**
	 * Accessor for physics def.
	 * @return
	 */
	public PhysicsDef getPhysicsDef() {
		return _physicsDef;
	}
	
	/**
	 * Mutator for color of object
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(int r, int g, int b) {
		_graphicalDef.setColor(r, g, b);
	}
	
	public void setColor(int val) {
		_graphicalDef.setColor(val);
	}
	
	
	
	/**
	 * Mutator for the path.
	 * @param p
	 */
	public void setPath(Vector<Point2D.Float> p) {
		_pathDef = new PathDef(this.getPhysicsDef(), p);
	}
	
	/**
	 * 
	 * @return world x of object
	 */
	public float getWorldX() {
		return this.getPhysicsDef().getBody().getPosition().x;
	}
	
	/**
	 * 
	 * @return world y of object
	 */
	public float getWorldY() {
		return this.getPhysicsDef().getBody().getPosition().y;
	}
	
	/**
	 * 
	 * @return pixel x of object
	 */
	public int getPixelX() {
		return (int) _world.worldXtoPixelX(this.getWorldX());
	}
	
	/**
	 * 
	 * @return pixel y of object
	 */
	public int getPixelY() {
		return (int) _world.worldYtoPixelY(this.getWorldY());
	}
	
	/**
	 * 
	 * @return world radius of object
	 */
	public float getWorldRadius() {
		return this.getPhysicsDef().getRadius();
	}
	
	/**
	 * 
	 * @return pixel radius of object
	 */
	public int getPixelRadius() {
		return (int) _world.scalarWorldToPixels(this.getWorldRadius());
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
	
}
