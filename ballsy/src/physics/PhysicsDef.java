package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class PhysicsDef {
	
	protected PhysicsWorld _world;
	protected Body _body;
	protected float _density, _friction, _bounciness, _x, _y, _width, _height;
	protected boolean _mobile = true;
	
	public PhysicsDef(float x, float y, float density, float friction, float bounciness){
		_x = x;
		_y = y;
		_density = density;
		_friction = friction;
		_bounciness = bounciness;
	}
	
	public abstract void createBody();
	
	public Body getBody(){
		return _body;
	}
	
	public void setWorld(PhysicsWorld world){
		_world = world;
	}
	
	public void setMobile(boolean mobile){
		_mobile = mobile;
	}
	
	/**
	 * Applies a force given by the parameter to the object's center of mass.
	 * @param vector
	 */
	public void applyForce(Vec2 vector){
		_body.applyForce(vector, _body.getWorldCenter());
	}
	
	public void setLinearVelocity(Vec2 vec){
		_body.setLinearVelocity(vec);
	}
	
	public void setAngularVelocity(float vel){
		_body.setAngularVelocity(vel);
	}
		
	public abstract float getWidth();
	public abstract float getHeight();
	public abstract float getRadius();
}
