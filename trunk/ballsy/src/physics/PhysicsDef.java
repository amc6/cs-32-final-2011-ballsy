package physics;

import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

public abstract class PhysicsDef {
	
	protected PhysicsWorld _world;
	protected Body _body;
	protected boolean _mobile;
	
	public PhysicsDef(PhysicsWorld world, boolean mobile){
		_world = world;
		_mobile = mobile;
	}
	
	/**
	 * Must be called in the subclasses constructor to properly create the body
	 */
	public void createBody(ShapeDef shape, float d, float f, float b, float x, float y) {
		shape.density = d;
		shape.friction = f;
		shape.restitution = b;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vec2(x, y));

		
		_body = _world.createBody(bodyDef);
		_body.createShape(shape);
			
		// Shape does not move if immobile
		if (_mobile) {
			_body.setMassFromShapes();
		}
		else {
			MassData md = new MassData();
			md.mass = 0f;
			_body.setMass(md);
		}
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
		_body.applyForce(vector, _body.getWorldCenter());
	}
	
	/**
	 * Applies an impulse given by the parameter to the object's center of mass.
	 * @param vector
	 */
	public void applyImpulse(Vec2 vector){
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
