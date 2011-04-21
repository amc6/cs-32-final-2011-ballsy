package physics;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

public abstract class PhysicsDef {
	
	protected PhysicsWorld _world;
	protected Body _body;
	protected boolean _mobile;
	
	public PhysicsDef(PhysicsWorld world, boolean mobile){
		_world = world;
		_mobile = mobile;
	}
	
	/**
	 * Must be called in the subclasses constructor to properly create the body
	 */
	public void createBody(ShapeDef shape, float d, float f, float b, float x, float y) {
		shape.density = d;
		shape.friction = f;
		shape.restitution = b;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vec2(x, y));
		
		_body = _world.createBody(bodyDef);
		_body.createShape(shape);
		
		// Shape does not move if immobile
		if (_mobile) {
			_body.setMassFromShapes();
		}else {
			MassData md = new MassData();
			md.mass = 0f;
			_body.setMass(md);
		}
	}
	
	public Body getBody(){
		return _body;
	}
		
	public void setMobile(boolean mobile){
		_mobile = mobile;
	}
	
	/**
	 * Applies a force given by the parameter to the object's center of mass.
	 * @param vector
	 */
	public void applyForce(Vec2 vector){
		_body.applyForce(vector, _body.getWorldCenter());
	}
	
	/**
	 * Applies an impulse given by the parameter to the object's center of mass.
	 * @param vector
	 */
	public void applyImpulse(Vec2 vector){
		_body.applyImpulse(vector, _body.getWorldCenter());
	}
	
	
	public void setLinearVelocity(Vec2 vec){
		_body.setLinearVelocity(vec);
	}
	
	public void setAngularVelocity(float vel){
		_body.setAngularVelocity(vel);
	}
	
	public PhysicsWorld getWorld() {
		return _world;
	}
		
	public abstract float getWidth();
	public abstract float getHeight();
	public abstract float getRadius();
	
	public float getPixelRadius() {
		return _world.scalarWorldToPixels(this.getRadius());
	}
	
	public Vec2 getBodyWorldCenter(){
		return _world.getBodyWorldCoord(_body);
	}
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file.
	 * @return
	 */
	public abstract Element writeXML();
	
	/**
	 * General function for physics definitions, when passed a type, width, and height.
	 * Called by subclasses when implementing above method.
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 */
	public Element writeXML(String type, float width, float height) {
		// create the element for the XML representation of the physics ball def
		Element newEl = DocumentHelper.createElement("PHYSICS_DEF");
		// add appropriate attributes for ball
		newEl.addAttribute("TYPE", type);
		newEl.addAttribute("X", Float.toString(_body.getPosition().x));
		newEl.addAttribute("Y", Float.toString(_body.getPosition().y));
		newEl.addAttribute("ROTATION", Float.toString(_body.getAngle()));
		newEl.addAttribute("X_VELOCITY", Float.toString(_body.getLinearVelocity().x));
		newEl.addAttribute("Y_VELOCITY", Float.toString(_body.getLinearVelocity().y));
		newEl.addAttribute("ANGULAR_VELOCITY", Float.toString(_body.getAngularVelocity()));
		newEl.addAttribute("DENSITY", Float.toString(_body.getShapeList().getDensity()));
		newEl.addAttribute("FRICTION", Float.toString(_body.getShapeList().getFriction()));
		newEl.addAttribute("RESTITUTION", Float.toString(_body.getShapeList().getRestitution()));
		newEl.addAttribute("WIDTH", Float.toString(width));
		newEl.addAttribute("HEIGHT", Float.toString(height));
		newEl.addAttribute("MOBILE", Boolean.toString(_mobile));
		// return
		return newEl;
	}
	
	/**
	 * No width/height for objects without (see polygon, surface)
	 * @param type
	 * @return
	 */
	public Element writeXML(String type) {
		// create the element for the XML representation of the physics ball def
		Element newEl = DocumentHelper.createElement("PHYSICS_DEF");
		// add appropriate attributes for ball
		newEl.addAttribute("TYPE", type);
		newEl.addAttribute("X", Float.toString(_body.getPosition().x));
		newEl.addAttribute("Y", Float.toString(_body.getPosition().y));
		newEl.addAttribute("ROTATION", Float.toString(_body.getAngle()));
		newEl.addAttribute("X_VELOCITY", Float.toString(_body.getLinearVelocity().x));
		newEl.addAttribute("Y_VELOCITY", Float.toString(_body.getLinearVelocity().y));
		newEl.addAttribute("ANGULAR_VELOCITY", Float.toString(_body.getAngularVelocity()));
		newEl.addAttribute("DENSITY", Float.toString(_body.getShapeList().getDensity()));
		newEl.addAttribute("FRICTION", Float.toString(_body.getShapeList().getFriction()));
		newEl.addAttribute("RESTITUTION", Float.toString(_body.getShapeList().getRestitution()));
		newEl.addAttribute("MOBILE", Boolean.toString(_mobile));
		// return
		return newEl;
	}

	public Vec2 getBodyGravityCenter(){
		return _body.getWorldCenter();
	}
}
