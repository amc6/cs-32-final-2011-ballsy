package physics;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Element writeXML() {
		// get the general element for this guy...
		Element newEl = super.writeXML("polygon"); // center / offsets are all we need
		// now iterate through the points, making them sub elements
		for (Vec2 v : _pointOffsets) {
			Element pointEl = DocumentHelper.createElement("POLYGON_POINT");
			pointEl.addAttribute("X", Float.toString(v.x));
			pointEl.addAttribute("Y", Float.toString(v.y));
			newEl.add(pointEl);
		}
		return newEl;
	}
	
}
