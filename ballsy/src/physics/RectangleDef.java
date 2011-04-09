package physics;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

public class RectangleDef extends PhysicsDef {
	
	protected float _width;
	protected float _height;
	
	public RectangleDef(float x, float y, float w, float h, float d, float f, float b) {
		super(x, y, d, f, b);
		PolygonDef polygonDef = new PolygonDef();
		_width = w;
		_height = h;
		w = _width/2; // because defined from center
		h = _height/2; // same
		polygonDef.setAsBox(w, h);
		
		polygonDef.density = _density;
		polygonDef.friction = _friction;
		polygonDef.restitution = _bounciness;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vec2(x,y));

		_body = _world.createBody(bodyDef);
		_body.createShape(polygonDef);
		_body.setMassFromShapes();
		

		// Give it some initial random velocity
		//_body.setLinearVelocity(new Vec2(_level.random(-5,5),_level.random(2,5)));
		//_body.setAngularVelocity(_level.random(-5,5));
	
	}

	@Override
	public Body getBody() {
		return _body;
	}
	
	public float getWidth(){
		return _width;
	}
	
	public float getHeight(){
		return _height;
	}
	
	public float getRadius() {
		return 0; // doesn't really have a radius... never used.
	}
	
}
