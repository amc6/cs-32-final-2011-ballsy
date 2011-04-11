package graphical;

import physics.PhysicsDef;
import physics.PhysicsWorld;

public abstract class GraphicalDef {

	protected PhysicsDef _physicsDef;
	protected PhysicsWorld _world;
	
	public void setPhysicsDef(PhysicsDef physicsDef){
		_physicsDef = physicsDef;
	}
	
	public void setWorld(PhysicsWorld world){
		_world = world;
	}
	
	public abstract void display();
}
