package physics;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;

public class PhysicsPolygonDef extends PhysicsDef {
	
	protected float _radius;
	protected PolygonDef _polygonDef;
	protected ArrayList<Vec2> _pointOffsets;
	
	public PhysicsPolygonDef(PhysicsWorld world, float x, float y, int numSides, float r, float d, float f, float b, boolean mobile) {
		super(world, mobile);
		_radius = r;
		
		_polygonDef = new PolygonDef();
		_pointOffsets = new ArrayList<Vec2>();
		
		
		_polygonDef.clearVertices();
		for (int i = 1; i <= numSides; i++){

			Double angle = ((double) (i - 1)/numSides) * 2*Math.PI;

			Double worldX = _radius * Math.cos(angle);
			Double worldY = _radius * Math.sin(angle);
			
			System.out.println(worldX + " " + worldY);
			
			// Points should be as (dx,dy) from center of object
			Vec2 pos = new Vec2(worldX.floatValue(), worldY.floatValue());

			System.out.println(pos);
			_polygonDef.addVertex(pos);
			_pointOffsets.add(pos);
		}
				
				
		this.createBody(_polygonDef, d, f, b, x, y);
		

	}
	

	public float getWidth(){
		return _radius * 2;
	}
	
	public float getHeight(){
		return _radius * 2;
	}
	
	public float getRadius() {
		return 0; // doesn't really have a radius... never used.
	}
	
	public List<Vec2> getPointOffsets(){
		return _pointOffsets;
	}
	
}
