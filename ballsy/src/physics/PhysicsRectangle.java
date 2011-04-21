package physics;

import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

public class PhysicsRectangle extends PhysicsDef {
	
	protected float _width;
	protected float _height;
	
	public PhysicsRectangle(PhysicsWorld world, float x, float y, float w, float h, float d, float f, float b, boolean mobile) {
		super(world, mobile);
		_width = w;
		_height = h;
		
		PolygonDef polygonDef = new PolygonDef();
		
		float halfWidth = _width/2; // because defined from center
		float halfHeight = _height/2; // same
		polygonDef.setAsBox(halfWidth, halfHeight);

		this.createBody(polygonDef, d, f, b, x, y);

	}

	public float getWidth(){
		return _width;
	}
	
	public float getHeight(){
		return _height;
	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
		
}
