package physics;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;

public class PhysicsPolygonDef extends PhysicsDef {
	
	private PolygonDef _polygonDef;
	private ArrayList<Vec2> _pointOffsets;
	
	public PhysicsPolygonDef(PhysicsWorld world, float x, float y, ArrayList<Vec2> offsets, float d, float f, float b, boolean mobile) {
		super(world, mobile);
		//_radius = r;
		
		_polygonDef = new PolygonDef();
		_pointOffsets = new ArrayList<Vec2>();
				
		_polygonDef.clearVertices();
		
		for (Vec2 vec : offsets){;
			_polygonDef.addVertex(vec);
			_pointOffsets.add(vec);
		}			
				
		this.createBody(_polygonDef, d, f, b, x, y);
		
	}
		
	public List<Vec2> getPointOffsets(){
		return _pointOffsets;
	}
	
}
