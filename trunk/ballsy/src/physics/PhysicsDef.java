package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class PhysicsDef {
	
	protected PhysicsWorld _world;
	protected Body _body;
	protected boolean _mobile;
	
	public PhysicsDef(PhysicsWorld world, boolean mobile){
		_world = world;
		_mobile = mobile;
	}
	
	public Body getBody(){
		return _body;
	}
		
	public void setMobile(boolean mobile){
		_mobile = mobile;
	}
	
	/**
	 * Applies a force given by the parameter to the object's center of mass.
	 * @param vector
	 */
	public void applyForce(Vec2 vector){
		_body.applyImpulse(vector, _body.getWorldCenter());
	}
	
	public void setLinearVelocity(Vec2 vec){
		_body.setLinearVelocity(vec);
	}
	
	public void setAngularVelocity(float vel){
		_body.setAngularVelocity(vel);
	}
	
	public PhysicsWorld getWorld() {
		return _world;
	}
		
	public abstract float getWidth();
	public abstract float getHeight();
	public abstract float getRadius();
}
