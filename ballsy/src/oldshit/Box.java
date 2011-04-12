package oldshit;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import physics.PhysicsWorld;
import processing.core.PApplet;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example

//A rectangular box
public class Box  {

	// We need to keep track of a Body and a width and height
	Body body;
	float w;
	float h;
	private PhysicsWorld _box2d;
	private PApplet _level;

	// Constructor
	Box(float x, float y, PhysicsWorld box2d, PApplet level) {
		_box2d = box2d;
		_level = level;
		
		w = _level.random(4,16);
		h = _level.random(4,16);
		// Add the box to the box2d world
		makeBody(new Vec2(x,y),w,h);
	}

	// This function removes the particle from the box2d world
	void killBody() {
		_box2d.destroyBody(body);
	}

	// Is the particle ready for deletion?
	boolean done() {
		// Let's find the screen position of the particle
		Vec2 pos = _box2d.getBodyPixelCoord(body);
		// Is it off the bottom of the screen?
		if (pos.y > _level.height+w*h) {
			killBody();
			return true;
		}
		return false;
	}

	// Drawing the box
	void display() {
		// We look at each body and get its screen position
		Vec2 pos = _box2d.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();

		_level.rectMode(_level.CENTER);
		_level.pushMatrix();
		_level.translate(pos.x,pos.y);
		_level.rotate(-a);
		_level.fill(175);
		_level.stroke(0);
		_level.rect(0,0,w,h);
		_level.popMatrix();
	}

	// This function adds the rectangle to the box2d world
	void makeBody(Vec2 center, float w_, float h_) {

		// Define a polygon (this is what we use for a rectangle)
		PolygonDef sd = new PolygonDef();
		float box2dW = _box2d.scalarPixelsToWorld(w_/2);
		float box2dH = _box2d.scalarPixelsToWorld(h_/2);
		sd.setAsBox(box2dW, box2dH);

		// Parameters that affect physics
		sd.density = 1.0f;
		sd.friction = 0.3f;
		sd.restitution = 0.5f;

		// Define the body and make it from the shape
		BodyDef bd = new BodyDef();
		bd.position.set(_box2d.coordPixelsToWorld(center));

		body = _box2d.createBody(bd);
		body.createShape(sd);
		body.setMassFromShapes();

		// Give it some initial random velocity
		body.setLinearVelocity(new Vec2(_level.random(-5,5),_level.random(2,5)));
		body.setAngularVelocity(_level.random(-5,5));
	
	}
	
	public void applyForce(Vec2 vector){
		body.applyImpulse(vector, body.getWorldCenter());
	}

}



