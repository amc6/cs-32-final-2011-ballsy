package physics;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Body;

public class PhysicsBall extends PhysicsDef {

	protected float _radius;
	
	public PhysicsBall(PhysicsWorld world, float x, float y, float r, float d, float f, float b, boolean mobile) {
		super(world, mobile);
		
		_radius = r;
		
		CircleDef circDef = new CircleDef();
		circDef.radius = _radius;
		
		this.createBody(circDef, d, f, b, x, y);
		
//		circDef.density = d;
//		circDef.friction = f;
//		circDef.restitution = b;
//		
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.position.set(new Vec2(x, y));
//		
//		_body = _world.createBody(bodyDef);
//		_body.createShape(circDef);
//		
//		// Shape does not move if immobile
//		if (_mobile) {
//			_body.setMassFromShapes();
//		}
	}
	
	public float getHeight() {
		// TODO Auto-generated method stub
		return _radius * 2;
	}

	public float getRadius() {
		// TODO Auto-generated method stub
		return _radius;
	}

	public float getWidth() {
		// TODO Auto-generated method stub
		return _radius * 2;
	}

}
