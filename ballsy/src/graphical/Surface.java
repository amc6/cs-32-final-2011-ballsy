package graphical;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.EdgeChainDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import physics.PhysicsWorld;
import processing.core.PApplet;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example

//An uneven surface boundary

class Surface {
	// We'll keep track of all of the surface points
	ArrayList<Vec2> surface;
	private Level _level;
	private PhysicsWorld _box2d;


	Surface(Level level, PhysicsWorld box2d) {
		_level = level;
		_box2d = box2d;
		surface = new ArrayList<Vec2>();

		// This is what box2d uses to put the surface in its world
		EdgeChainDef edges = new EdgeChainDef();

		// Perlin noise argument
		float xoff = 0.0f;

		// This has to go backwards so that the objects  bounce off the top of the surface
		// This "edgechain" will only work in one direction!
		for (float x = _level.width+10; x > -10; x -= 5) {

			// Doing some stuff with perlin noise to calculate a surface that points down on one side
			// and up on the other
			float y;
			if (x > _level.width/2) {
				y = 100 + (_level.width - x)*1.1f + PApplet.map(_level.noise(xoff),0,1,-80,80);
			} 
			else {
				y = 100 + x*1.1f + PApplet.map(_level.noise(xoff),0,1,-80,80);
			}

			// The edge point in our window
			Vec2 screenEdge = new Vec2(x,y);
			// We store it for rendering
			surface.add(screenEdge);

			// Convert it to the box2d world and add it to our EdgeChainDef
			Vec2 edge = _box2d.coordPixelsToWorld(screenEdge);
			edges.addVertex(edge);

			// Move through perlin noise
			xoff += 0.1;

		}
		edges.setIsLoop(false);   // We could make the edge a full loop
		edges.friction = 2.0f;    // How much friction
		edges.restitution = 0.3f; // How bouncy

		// The edge chain is now a body!
		BodyDef bd = new BodyDef();
		bd.position.set(0.0f,0.0f);
		Body body = _box2d.world.createBody(bd);
		body.createShape(edges);

	}

	// A simple function to just draw the edge chain as a series of vertex points
	void display() {
		_level.strokeWeight(2);
		_level.stroke(0);
		_level.noFill();
		_level.beginShape();
		for (Vec2 v: surface) {
			_level.vertex(v.x,v.y);
		}
		_level.endShape();
	}

}

