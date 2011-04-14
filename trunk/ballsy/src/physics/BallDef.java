package physics;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Body;

public class BallDef extends PhysicsDef {

	public BallDef(float x, float y, float r, float d, float f, float b) {
		super(x, y, d, f, b);
		_width = 2 * r;
		_height = 2 * r;
	}
	
	@Override
	public void createBody() {
		CircleDef circDef = new CircleDef();
		circDef.radius = _width/2;
		
		circDef.density = _density;
		circDef.friction = _friction;
		circDef.restitution = _bounciness;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vec2(_x, _y));
		
		_body = _world.createBody(bodyDef);
		_body.createShape(circDef);
		
		// Shape does not move if immobile
		if (_mobile) {
			_body.setMassFromShapes();
		}
	}

	public Body getBody() {
		return _body;
	}
	
	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return _height;
	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return _width / 2;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return _width;
	}

}
