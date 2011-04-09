package graphical;


import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import physics.PhysicsWorld;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example

//A circular particle

public class Particle {

	// We need to keep track of a Body and a radius
	private Body body;
	private float r;
	private PhysicsWorld _box2d;
	private Level _level;

	public Particle(float x, float y, float r_, PhysicsWorld box2d, Level level) {
		r = r_;
		_box2d = box2d;
		_level = level;
		// This function puts the particle in the Box2d world
		makeBody(x,y,r);

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
		if (pos.y > _level.height+r*2) {
			killBody();
			return true;
		}
		return false;
	}

	// 
	void display() {
		// We look at each body and get its screen position
		Vec2 pos = _box2d.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		_level.pushMatrix();
		_level.translate(pos.x,pos.y);
		_level.rotate(-a);
		_level.fill(175);
		_level.stroke(0);
		_level.strokeWeight(1);
		_level.ellipse(0,0,r*2,r*2);
		// Let's add a line so we can see the rotation
		_level.line(0,0,r,0);
		_level.popMatrix();
	}

	// Here's our function that adds the particle to the Box2D world
	void makeBody(float x, float y, float r) {
		// Define a body
		BodyDef bd = new BodyDef();
		// Set its position

		bd.position = _box2d.coordPixelsToWorld(x,y);
		body = _box2d.world.createBody(bd);

		// Make the body's shape a circle
		CircleDef cd = new CircleDef();
		cd.radius = _box2d.scalarPixelsToWorld(r);
		cd.density = 1.0f;
		cd.friction = 0.01f;
		cd.restitution = 0.9f; // Restitution is bounciness
		body.createShape(cd);

		// Always do this at the end
		body.setMassFromShapes();

		// Give it a random initial velocity (and angular velocity)
		body.setLinearVelocity(new Vec2(_level.random(-10f,10f),_level.random(5f,10f)));
		body.setAngularVelocity(_level.random(-10,10));
	}






}

