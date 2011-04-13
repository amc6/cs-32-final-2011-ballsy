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
		_width = w;
		_height = h;
	}
	
	public void createBody(){
		
		PolygonDef polygonDef = new PolygonDef();
		
		float w = _width/2; // because defined from center
		float h = _height/2; // same
		polygonDef.setAsBox(w, h);
		
		polygonDef.density = _density;
		polygonDef.friction = _friction;
		polygonDef.restitution = _bounciness;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vec2(_x, _y));

		_body = _world.createBody(bodyDef);
		_body.createShape(polygonDef);
		
		// Shape does not move if immobile
		if (_mobile) {
			_body.setMassFromShapes();
		}		
		
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
