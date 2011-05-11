package physics;

import org.dom4j.Element;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Body;

public class PhysicsBall extends PhysicsDef {

	private float _radius;
	
	public PhysicsBall(float x, float y, float r){
		super(x,y);
		_radius = r;
		this.createBody();
	}
	
	/**
	 * Creates the ShapeDef and passes it up to the superclass.
	 */
	protected void createBody(){
		CircleDef circDef = new CircleDef();
		circDef.radius = _radius;
		// if it already exists, maintain position & rotation.
		if (_body != null) this.createBody(circDef, this.getBodyWorldCenter(), _body.getAngle());
		else this.createBody(circDef);
	}
	
	/**
	 * Sets the radius and recreates the object in the world.
	 */
	public void setRadius(float radius){
		_radius = radius;
		if (_body != null) this.createBody(); // takes care of removing old body and creates a new one
	}
	
	/**
	 * @return property as stored in this instance
	 */
	public float getRadius(){
		return _radius;
	}
	
	/**
	 * XML functionality.
	 */
	public Element writeXML() {
		// return element as defined in super with proper name and width/height
		return super.writeXML("ball", _radius * 2, _radius * 2);
	}

}
