package physics;

import static bodies.BodyConstants.DEFAULT_BODY_BOUNCINESS;
import static bodies.BodyConstants.DEFAULT_BODY_DENSITY;
import static bodies.BodyConstants.DEFAULT_BODY_FRICTION;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;



public abstract class PhysicsDef {
	
	protected PhysicsWorld _world = PhysicsWorld.getInstance();
	protected Vec2 _initialPos;
	private float _density = DEFAULT_BODY_DENSITY,
				  _friction = DEFAULT_BODY_FRICTION,
				  _bounciness = DEFAULT_BODY_BOUNCINESS;
	private boolean _mobile = true;
	protected Body _body;
		
	public PhysicsDef(float x, float y){
		_initialPos = new Vec2(x, y);
	}
	
	/**
	 * Require subclasses to implement.
	 * Should call PhysicsDef.createBody(...) with the specific ShapeDef
	 * of the subclass.
	 */
	protected abstract void createBody();
	
	/**
	 * Must be called in the subclasses constructor to properly create the body
	 */
	protected void createBody(ShapeDef shape) {
		
		// If we're re-creating a body, we need to remove the old one
		if (_body != null){
			_world.destroyBody(_body); 
		}
		
		shape.density = _density;
		shape.friction = _friction;
		shape.restitution = _bounciness;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(_initialPos);
		
		_body = _world.createBody(bodyDef);
		_body.createShape(shape);
		
		// Shape does not move if immobile
		this.setMobile(_mobile);
	}
	
	/**
	 * If the object exists in the PhysicsWorld, we edit the property
	 * directly and change the corresponding instance variable. Otherwise
	 * we just change the instance variable in preparation of createObject(...)
	 * @param density
	 */
	public void setDensity(float density){
		_density = density;
		if (_body != null) _body.getShapeList().m_density = density;
	}
	
	/**
	 * @return property as stored in this instance
	 */
	public float getDensity(){
		return _density;
	}
	
	/**
	 * If the object exists in the PhysicsWorld, we edit the property
	 * directly and change the corresponding instance variable. Otherwise
	 * we just change the instance variable in preparation of createObject(...)
	 * @param density
	 */
	public void setFriction(float friction){
		_friction = friction;
		if (_body != null) _body.getShapeList().m_friction = friction;
	}
	
	/**
	 * @return property as stored in this instance
	 */
	public float getFriction(){
		return _friction;
	}
	
	/**
	 * If the object exists in the PhysicsWorld, we edit the property
	 * directly and change the corresponding instance variable. Otherwise
	 * we just change the instance variable in preparation of createObject(...)
	 * @param density
	 */
	public void setBounciness(float bounciness){
		_bounciness = bounciness;
		if (_body != null) _body.getShapeList().m_restitution = bounciness;
	}
	
	/**
	 * @return property as stored in this instance
	 */
	public float getBounciness(){
		return _bounciness;
	}
	
	public Body getBody(){
		return _body;
	}
	
	public boolean getMobile(){
		return _mobile;
	}
		
	public void setMobile(boolean mobile){
		_mobile = mobile;
		if (_body != null){
			if (_mobile) {
				_body.setMassFromShapes();
			}else {
				MassData md = new MassData();
				md.mass = 0f;
				_body.setMass(md);
			}
		}
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
	
	public Vec2 getBodyWorldCenter(){
		return _world.getBodyWorldCoord(_body);
	}
	
	public Vec2 getBodyGravityCenter(){
		return _body.getWorldCenter();
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
}
