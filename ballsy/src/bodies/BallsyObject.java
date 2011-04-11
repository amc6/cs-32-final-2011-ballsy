package bodies;

import graphical.AbstractLevel;
import graphical.GraphicalDef;

import org.jbox2d.common.Vec2;


import physics.PathDef;
import physics.PhysicsDef;
import physics.PhysicsWorld;

public abstract class BallsyObject {

	private PhysicsWorld _world;
	private AbstractLevel _level;
	private PhysicsDef _physicsDef;
	private GraphicalDef _graphicalDef;
	private PathDef _pathDef;
	private boolean _massless;
	
	public BallsyObject(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean massless){
		_level = level;
		_world = world;
		_physicsDef = physicsDef;
		_physicsDef.setWorld(_world);
		_graphicalDef = graphicalDef;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);
		_massless = massless;
		_pathDef = null; // obviously, not in signature
	}
	
	public BallsyObject(AbstractLevel level, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean massless, PathDef pathDef){
		_level = level;
		_world = world;
		_physicsDef = physicsDef;
		_physicsDef.setWorld(_world);
		_graphicalDef = graphicalDef;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);
		_massless = massless;
		_pathDef = pathDef; // obviously, not in signature
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
	
}
