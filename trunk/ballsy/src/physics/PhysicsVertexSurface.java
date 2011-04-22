package physics;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.shapes.EdgeChainDef;
import org.jbox2d.common.Vec2;

public class PhysicsVertexSurface extends PhysicsDef {
	
	protected ArrayList<Vec2> _points;
	
	public PhysicsVertexSurface(float x, float y, ArrayList<Vec2> worldPoints) {
		super(x,y);
		_mobile = false;
		_points = worldPoints;	
		this.createBody();
	}
	
	protected void createBody(){
		EdgeChainDef edgeChain = new EdgeChainDef();
		
		for (int i = 0; i < _points.size(); i++){
			Vec2 vec = _points.get(i);
			edgeChain.addVertex(vec);
		}
		
		edgeChain.setIsLoop(false);
		this.createBody(edgeChain);
	}

	public ArrayList<Vec2> getWorldPoints(){
		return _points;
	}
	
	public Element writeXML() {
		// get the general element for this guy...
		Element newEl = super.writeXML("polygon"); // center / offsets are all we need
		// now iterate through the points, making them sub elements
		for (Vec2 v : _points) {
			Element pointEl = DocumentHelper.createElement("SURFACE_POINT");
			pointEl.addAttribute("X", Float.toString(v.x));
			pointEl.addAttribute("Y", Float.toString(v.y));
			newEl.add(pointEl);
		}
		return newEl;
	}
}
