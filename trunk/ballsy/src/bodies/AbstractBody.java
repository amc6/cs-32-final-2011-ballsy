package bodies;

import graphical.GraphicalDef;

import org.jbox2d.common.Vec2;

import ballsy.AbstractLevel;


import physics.PathDef;
import physics.PhysicsDef;
import physics.PhysicsWorld;

public abstract class AbstractBody {

	private PhysicsWorld _world;
	private AbstractLevel _level;
	private PhysicsDef _physicsDef;
	private GraphicalDef _graphicalDef;
	private PathDef _pathDef;
	
	public AbstractBody(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean mobile){
		this.constructorHelper(level, world, physicsDef, graphicalDef, mobile);
	}
	
	public AbstractBody(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean mobile, PathDef pathDef){
		this.constructorHelper(level, world, physicsDef, graphicalDef, mobile);
		_pathDef = pathDef; 
	}	
	
	private void constructorHelper(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean mobile){
		_level = level;
		_world = world;
		_physicsDef = physicsDef;
		_physicsDef.setMobile(mobile);
		_physicsDef.setWorld(_world);
		_physicsDef.createBody();
		_graphicalDef = graphicalDef;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);	
	}
		
	/**
	 * Should check to see if the object can be removed from the world.
	 */
	public abstract boolean done();
	
	/**
	 * Display the object using processing graphics.
	 */
	public void display(){
		_graphicalDef.display();
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
}
