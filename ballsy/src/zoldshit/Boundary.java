package zoldshit;

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

//A fixed boundary class

public class Boundary {

	// A boundary is a simple rectangle with x,y,width,and height
	float x;
	float y;
	float w;
	float h;
	// But we also have to make a body for box2d to know about it
	Body b;
	private PhysicsWorld _box2d;
	private PApplet _level;

	public Boundary(float x_,float y_, float w_, float h_, PhysicsWorld box2d, PApplet level) {
		_box2d = box2d;
		_level = level;
		x = x_;
		y = y_;
		w = w_;
		h = h_;

		// Figure out the box2d coordinates
		float box2dW = box2d.scalarPixelsToWorld(w/2);
		float box2dH = box2d.scalarPixelsToWorld(h/2);
		Vec2 center = new Vec2(x,y);

		// Define the polygon
		PolygonDef sd = new PolygonDef();
		sd.setAsBox(box2dW, box2dH);
		sd.density = 1.0f;    // No density means it won't move!
		sd.friction = 0.3f;

		// Create the body
		BodyDef bd = new BodyDef();
		bd.position.set(box2d.coordPixelsToWorld(center));
		b = box2d.createBody(bd);
		b.createShape(sd);
		//b.setMassFromShapes();
	}

	// Draw the boundary, if it were at an angle we'd have to do something fancier
	public void display() {
		_level.fill(0);
		_level.stroke(0);
		_level.rectMode(_level.CENTER);
		_level.rect(x,y,w,h);
	}

}

