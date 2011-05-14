package physics;

import static bodies.BodyConstants.DEFAULT_BODY_BOUNCINESS;
import static bodies.BodyConstants.DEFAULT_BODY_DENSITY;
import static bodies.BodyConstants.DEFAULT_BODY_FRICTION;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

/**
 * Superclass definition of objects within the physics world.
 * Contains setters and getters for frequent operations on objects
 * in addition to adding the object to the world.
 */
public abstract class PhysicsDef {
	
	protected PhysicsWorld _world = PhysicsWorld.getInstance();
	protected Vec2 _initialPos;
	private float _density = DEFAULT_BODY_DENSITY,
				  _friction = DEFAULT_BODY_FRICTION,
				  _bounciness = DEFAULT_BODY_BOUNCINESS;
	private boolean _mobile = true;
	private boolean _graphicalOnly = false;
	protected Body _body;
	
			
	/**
	 * Stores the initial position of an object.
	 */
	public PhysicsDef(float x, float y){
		_initialPos = new Vec2(x, y);
	}
	
	/**
	 * Require that subclasses can create themselves to some degree.
	 * Subclasses should call PhysicsDef.createBody(...) with their specific
	 * ShapeDef.
	 */
	protected abstract void createBody();
	
	/**
	 * Must be called in the subclasses constructor to properly create the body
	 */
	protected void createBody(ShapeDef shape) {
		this.createBody(shape, _initialPos, 0); // create an object with no rotation
	}
	
	/**
	 * Create an object in the physics world with a pre-created ShapeDef
	 * at some initial position and a starting angle.
	 */
	protected void createBody(ShapeDef shape, Vec2 pos, float angle) {
		
		// If we're re-creating a body, we need to remove the old one
		if (_body != null){
			_world.destroyBody(_body); 
		}
		
		// Set shape properties
		shape.density = _density;
		shape.friction = _friction;
		shape.restitution = _bounciness;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos);
		bodyDef.angle = angle;
		
		// Create physics body
		_body = _world.createBody(bodyDef);
		_body.createShape(shape);
		_body.m_sweep.a = angle;
		
		// Shape does not move if immobile
		this.setMobile(_mobile);
	}
	
	/**
	 * Set an existing body's rotation in radians.
	 */
	public void setRotation(float r) {
		this.getBody().setXForm(this.getBody().getXForm().position, r);
	}
	
	/**
	 * Get the existing body's rotation in radians.
	 */
	public float getRotation(){
		return this.getBody().getAngle();
	}
	
	/**
	 * If the object exists in the PhysicsWorld, we edit the property
	 * directly and change the corresponding instance variable. Otherwise
	 * we just change the instance variable in preparation of createObject(...)'s
	 * invocation.
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
	 * we just change the instance variable in preparation of createObject(...)'s
	 * invocation.
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
	 * we just change the instance variable in preparation of createObject(...)'s
	 * invocation.
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
	
	/**
	 * @return the reference to the Body object in the physics world.
	 */
	public Body getBody(){
		return _body;
	}
	
	/**
	 * @return true if the shape is dynamic, false if the shape is static
	 * within the physics world.
	 */
	public boolean getMobile(){
		return _mobile;
	}
		
	/**
	 * Sets the instance variable for mobility accordingly and, if
	 * the shape has already been instantiated in the physcis world,
	 * sets its mobility directly.
	 */
	public void setMobile(boolean mobile){
		_mobile = mobile;
		if (_body != null){
			if (_mobile) {
				_body.setMassFromShapes(); // dynamic
			}else {
				MassData md = new MassData();
				md.mass = 0f; // static by lack of mass
				_body.setMass(md);
			}
		}
	}
	
	/**
	 * @return true if the object is graphical only, otherwise false.
	 */
	public boolean getGraphicalOnly(){
		return _graphicalOnly;
	}

	/**
	 * Sets the instance variable for graphical only accordingly and, if
	 * the shape has already been instantiated in the physcis world,
	 * sets its graphical-only-ness directly.
	 * 
	 * This feature has been removed from the first release of Ballsy.
	 * The functionality is there, but it has been removed from the GUI
	 * of the Level Editor.
	 */
	public void setGraphicalOnly(boolean graphicalOnly){
		_graphicalOnly = graphicalOnly;
		if (_body != null) {
			if (graphicalOnly) {
				this.setMobile(false);
				FilterData fd = new FilterData();
				fd.maskBits = 0x0000;
				_body.getShapeList().setFilterData(fd);
				_body.getShapeList().m_isSensor = true;
			} else {
				FilterData fd = new FilterData();
				fd.maskBits = 0xFFFF;
				fd.categoryBits = 0x0001;
				fd.groupIndex = 0;
				_body.getShapeList().setFilterData(fd);
				_body.getShapeList().m_isSensor = false;
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
	
	
	/**
	 * Sets the linear velocity of the object.
	 */
	public void setLinearVelocity(Vec2 vec){
		_body.setLinearVelocity(vec);
	}
	
	/**
	 * Sets the the angular velocity of the object.
	 */
	public void setAngularVelocity(float vel){
		_body.setAngularVelocity(vel);
	}		
	
	/**
	 * @return the world coordinates of the body's center.
	 */
	public Vec2 getBodyWorldCenter(){
		return _world.getBodyWorldCoord(_body);
	}
	
	/**
	 * Sets the location of the body.
	 */
	public void setBodyWorldCenter(Vec2 vec){
		_initialPos = vec;
		if (_body != null) _body.setXForm(vec, _body.getAngle());
	}
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file.
	 * @return DOM4J element
	 */
	public abstract Element writeXML();
	
	/**
	 * General function for physics definitions, when passed a type, width, and height.
	 * Called by subclasses when implementing above method.
	 * @param type
	 * @param width
	 * @param height
	 * @return DOM4J element
	 */
	public Element writeXML(String type, float width, float height) {
		// create the element for the XML representation of the physics ball def
		Element newEl = DocumentHelper.createElement("PHYSICS_DEF");
		// Add the properties for all objects
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
		newEl.addAttribute("GRAPHICALONLY", Boolean.toString(_graphicalOnly));
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
		// Add the properties for objects with no width or height
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
		newEl.addAttribute("GRAPHICALONLY", Boolean.toString(_graphicalOnly));
		// return
		return newEl;
	}
}
