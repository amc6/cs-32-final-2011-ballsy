package physics;

import org.dom4j.Element;
import org.jbox2d.collision.shapes.PolygonDef;

/**
 * Represents a rectangle within the physics world.
 */

public class PhysicsRectangle extends PhysicsDef {
	
	private float _width;
	private float _height;
	
	public PhysicsRectangle(float x, float y, float w, float h) {
		super(x,y);
		_width = w;
		_height = h;
		this.createBody();
	}
	
	/**
	 * Creates the ShapeDef and passes it up to the superclass.
	 */
	protected void createBody(){
		PolygonDef polygonDef = new PolygonDef();
		float halfWidth = _width/2; // because defined from center
		float halfHeight = _height/2; // same
		polygonDef.setAsBox(halfWidth, halfHeight);
		if (this.getBody() == null)
			this.createBody(polygonDef);
		else
			this.createBody(polygonDef, this.getBodyWorldCenter(), this.getBody().getAngle());
	}

	/**
	 * Sets the width and recreates the object in the world.
	 */
	public void setWidth(float width){
		_width = width;
		if (_body != null) this.createBody(); // takes care of removing old body and creates a new one
	}
	
	/**
	 * @return the width of the rectangle
	 */
	public float getWidth(){
		return _width;
	}
	
	/**
	 * Sets the width and recreates the object in the world.
	 */
	public void setHeight(float height){
		_height = height;
		if (_body != null) this.createBody(); // takes care of removing old body and creates a new one
	}
	
	/**
	 * @return the height of the rectangle
	 */
	public float getHeight(){
		return _height;
	}
	
	public Element writeXML() {
		// return element as defined in super with proper name and width/height
		return super.writeXML("rectangle", _width, _height);
	}
		
}
