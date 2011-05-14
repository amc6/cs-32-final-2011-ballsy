package physics;

import java.util.ArrayList;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.shapes.EdgeChainDef;
import org.jbox2d.common.Vec2;

/**
 * Represents a vertex surface within the world. A vertex surface
 * is a sequence of points connected in a jagged line. This feature
 * was ultimately not included in the first release of ballsy.
 */
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
	
	/**
	 * Creates the ShapeDef and passes it up to the superclass.
	 */
	protected void createBody(){
		EdgeChainDef edgeChain = new EdgeChainDef();
		
		for (Vec2 vec : _points){
			edgeChain.addVertex(vec);
		}
		
		edgeChain.setIsLoop(false);
		this.createBody(edgeChain);
	}

	/**
	 * @return the list of points in this vertex surface
	 */
	public ArrayList<Vec2> getWorldPoints(){
		return _points;
	}
	
	
	/**
	 * To allow for saving of the object.
	 */
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
	
	// The following are mathematical functions to generate the proper
	// sequence of points.
	
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
