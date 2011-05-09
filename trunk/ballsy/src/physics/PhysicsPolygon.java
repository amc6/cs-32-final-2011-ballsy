package physics;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;

public class PhysicsPolygon extends PhysicsDef {
	
	private ArrayList<Vec2> _currOffsets;
	private final ArrayList<Vec2> _originalOffsets;
	private float _scale;
	private final float MINIMUM_SIZE = 1; // radius or width-or-height/2
	
	public PhysicsPolygon(float x, float y, ArrayList<Vec2> offsets) {
		super(x,y);
		_scale = 1;
		_originalOffsets = offsets;
		this.setPoints(offsets);
	}
	
	protected void createBody(){
		PolygonDef polygonDef = new PolygonDef();		
		polygonDef.clearVertices();
		for (Vec2 vec : _currOffsets){;
			polygonDef.addVertex(vec);
		}
		// if it already exists, maintain position & rotation.
		if (_body != null) this.createBody(polygonDef, this.getBody().getXForm().position, _body.getAngle());
		else this.createBody(polygonDef);
	}
	
	protected void setPoints(ArrayList<Vec2> offsets) {
		_currOffsets = offsets;		
		this.createBody();
	}
		
	public ArrayList<Vec2> getPointOffsets(){
		return _currOffsets;
	}
	
	public void scalePoints(float scale) {
		
		ArrayList<Vec2> newOffsets = new ArrayList<Vec2>();
		int countTooSmall = 0; // we'll count the number of points < minSize. if == # of points, return
		for (Vec2 v : _currOffsets) {
			//System.out.println(Math.abs(v.x*r) + " " + Math.abs(v.y*r));
			if (Math.sqrt(v.x*v.x*scale*scale + v.y*v.y*scale*scale) < MINIMUM_SIZE) countTooSmall++; 
			if (countTooSmall == _currOffsets.size()) return; // bail if it's getting too small
			newOffsets.add(new Vec2(v.x*scale, v.y*scale));
		}
		_scale *= scale;
		this.setPoints(newOffsets);
	}
	
	public void setScale(float scale){
		
		ArrayList<Vec2> newOffsets = new ArrayList<Vec2>();
		int countTooSmall = 0; // we'll count the number of points < minSize. if == # of points, return
		for (Vec2 v : _originalOffsets) {
			//System.out.println(Math.abs(v.x*r) + " " + Math.abs(v.y*r));
			if (Math.sqrt(v.x*v.x*scale*scale + v.y*v.y*scale*scale) < MINIMUM_SIZE) countTooSmall++; 
			if (countTooSmall == _originalOffsets.size()) return; // bail if it's getting too small
			newOffsets.add(new Vec2(v.x*scale, v.y*scale));
		}
		_scale = scale;
		this.setPoints(newOffsets);
	}
	
	public float getScale(){
		return _scale;
	}
	
	public static ArrayList<Vec2> getOffsets(ArrayList<Vec2> points, Vec2 center) {
		ArrayList<Vec2> newList = new ArrayList<Vec2>();
		for (Vec2 v : points) {
			newList.add(new Vec2(v.x - center.x, v.y - center.y));
		}
		return newList;
	}
	
	@Override
	public Element writeXML() {
		// get the general element for this guy...
		Element newEl = super.writeXML("polygon"); // center / offsets are all we need
		// now iterate through the points, making them sub elements
		for (Vec2 v : _currOffsets) {
			Element pointEl = DocumentHelper.createElement("POLYGON_POINT");
			pointEl.addAttribute("X", Float.toString(v.x));
			pointEl.addAttribute("Y", Float.toString(v.y));
			newEl.add(pointEl);
		}
		return newEl;
	}
	
}
