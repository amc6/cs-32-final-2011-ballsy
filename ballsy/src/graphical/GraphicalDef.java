package graphical;

import ballsy.Window;
import physics.PhysicsDef;
import physics.PhysicsWorld;

public abstract class GraphicalDef {

	protected PhysicsDef _physicsDef;
	protected PhysicsWorld _world;
	protected int _color = 175;
	
	public GraphicalDef(int color) {
		_color = color; 
	}
	
	public void setPhysicsDef(PhysicsDef physicsDef){
		_physicsDef = physicsDef;
	}
	
	public void setWorld(PhysicsWorld world){
		_world = world;
	}
	
	public abstract void display();
	
	public void setColor(int r, int g, int b) {
		Window w = Window.getInstance();
		_color = w.color(r, g, b);
	}
	
	public void setColor(int val){
		Window w = Window.getInstance();
		_color = w.color(val);
	}
}
