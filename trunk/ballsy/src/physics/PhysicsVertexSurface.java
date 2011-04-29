package physics;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.shapes.EdgeChainDef;
import org.jbox2d.common.Vec2;

public class PhysicsVertexSurface extends PhysicsDef {
	
	private ArrayList<Vec2> _points;
	
	// Offsets are defined from a starting point of the bottom left corner
	public PhysicsVertexSurface(float x, float y, ArrayList<Vec2> offsets) {
		super(x,y);
		this.setMobile(false);
		
		for (Vec2 vec : offsets){
			vec.addLocal(_initialPos);
		}
		_points = offsets;	
		
		this.createBody();
	}
	
	protected void createBody(){
		EdgeChainDef edgeChain = new EdgeChainDef();
		
		for (Vec2 vec : _points){
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
	
	public static ArrayList<Vec2> generateHalfCup(float height, float width, float stepsize){
		ArrayList<Vec2> vecList = new ArrayList<Vec2>();
		for (float x = width; x >= 0; x--){
			Vec2 vec = new Vec2((float) x, (float) Math.pow((x-Math.sqrt(40)),2));
			vecList.add(vec);
		}
		return vecList;
	}
	
	public static ArrayList<Vec2> generateLine(float width, float stepsize){
		ArrayList<Vec2> vecList = new ArrayList<Vec2>();
		for (float x = width; x >= 0; x--){
			Vec2 vec = new Vec2(x, 0);
			vecList.add(vec);
		}
		return vecList;
	}
}
