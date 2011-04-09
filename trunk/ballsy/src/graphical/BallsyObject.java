package graphical;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PathDef;
import physics.PhysicsDef;
import physics.PhysicsWorld;

public abstract class BallsyObject {

	private PhysicsWorld _world;
	private Main _window;
	private PhysicsDef _physicsDef;
	private GraphicalDef _graphicalDef;
	private PathDef _pathDef;
	private boolean _massless;
	
	public BallsyObject(Main window, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean massless){
		_window = window;
		_world = world;
		_physicsDef = physicsDef;
		_physicsDef.setWorld(_world);
		_graphicalDef = graphicalDef;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);
		_graphicalDef.setWindow(_window);
		_massless = massless;
		_pathDef = null; // obviously, not in signature
	}
	
	public BallsyObject(Main window, PhysicsWorld world, PhysicsDef physicsDef, GraphicalDef graphicalDef, boolean massless, PathDef pathDef){
		_window = window;
		_world = world;
		_physicsDef = physicsDef;
		_physicsDef.setWorld(_world);
		_graphicalDef = graphicalDef;
		_graphicalDef.setWorld(_world);
		_graphicalDef.setPhysicsDef(_physicsDef);
		_graphicalDef.setWindow(_window);
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
		//_window.destroy(this); // remove so as to not be called in draw method
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
