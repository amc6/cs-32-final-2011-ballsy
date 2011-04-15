package physics;

import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

public class RectangleDef extends PhysicsDef {
	
	protected float _width;
	protected float _height;
	
	public RectangleDef(PhysicsWorld world, float x, float y, float w, float h, float d, float f, float b, boolean mobile) {
		super(world, mobile);
		_width = w;
		_height = h;
		
		PolygonDef polygonDef = new PolygonDef();
		
		float halfWidth = _width/2; // because defined from center
		float halfHeight = _height/2; // same
		polygonDef.setAsBox(halfWidth, halfHeight);

		this.createBody(polygonDef, d, f, b, x, y);
//		polygonDef.density = d;
//		polygonDef.friction = f;
//		polygonDef.restitution = b;
//		
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.position.set(new Vec2(x, y));
//
//		
//		_body = _world.createBody(bodyDef);
//		_body.createShape(polygonDef);
//			
//		// Shape does not move if immobile
//		if (_mobile) {
//			_body.setMassFromShapes();
//		}
//		else {
//			MassData md = new MassData();
//			md.mass = 0f;
//			_body.setMass(md);
//		}
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
