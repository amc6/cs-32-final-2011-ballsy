package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public abstract class PhysicsDef {
	
	protected PhysicsWorld _world;
	protected Body _body;
	protected float _density, _friction, _bounciness, _x, _y, _width, _height;
	
	public PhysicsDef(float x, float y, float density, float friction, float bounciness){
		_x = x;
		_y = y;
		_density = density;
		_friction = friction;
		_bounciness = bounciness;
	}
	
	public Body getBody(){
		return _body;
	}
	
	public void setWorld(PhysicsWorld world){
		_world = world;
	}
	
	/**
	 * Applies a force given by the parameter to the object's 
	 * center of mass.
	 * @param vector
	 */
	public void applyForce(Vec2 vector){
		_body.applyForce(vector, _body.getWorldCenter());
	}
	
	public abstract float getWidth();
	public abstract float getHeight();
	public abstract float getRadius();
}
