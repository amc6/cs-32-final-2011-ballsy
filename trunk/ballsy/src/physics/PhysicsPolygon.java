package physics;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;

public class PhysicsPolygon extends PhysicsDef {
	
	private ArrayList<Vec2> _pointOffsets;
	
	public PhysicsPolygon(float x, float y, ArrayList<Vec2> offsets) {
		super(x,y);
		_pointOffsets = offsets;		
		this.createBody();
	}
	
	protected void createBody(){
		PolygonDef polygonDef = new PolygonDef();		
		polygonDef.clearVertices();
		for (Vec2 vec : _pointOffsets){;
			polygonDef.addVertex(vec);
		}						
		this.createBody(polygonDef);
	}
		
	public ArrayList<Vec2> getPointOffsets(){
		return _pointOffsets;
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
