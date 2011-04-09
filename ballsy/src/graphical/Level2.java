package graphical;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import processing.core.PConstants;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example


public class Level2 extends AbstractLevel {

	//A reference to our box2d world
	private PhysicsWorld box2d;

	// A list we'll use to track fixed objects
	ArrayList<Boundary> boundaries;
	// A list for all of our rectangles
	ArrayList<Box> boxes;
	
	private Box _mainBox;


	public void setup() {
		size(400,300);
		smooth();

		// Initialize box2d physics and create the world
		box2d = new PhysicsWorld(this);
		box2d.createWorld();
		// We are setting a custom gravity
		box2d.setGravity(0, -20);

		// Create ArrayLists	
		boxes = new ArrayList<Box>();
		boundaries = new ArrayList<Boundary>();

		// Add a bunch of fixed boundaries
		boundaries.add(new Boundary(width/2, 0, width, 10, box2d, this));
		boundaries.add(new Boundary(width/2, height - 5, width, 10, box2d, this));
		boundaries.add(new Boundary(width-5,height/2,10,height,box2d,this));
		boundaries.add(new Boundary(5,height/2,10,height,box2d,this));
		
		_mainBox = new Box(width/3,height-50,box2d,this);
		boxes.add(_mainBox);
	}

	public void draw() {
		background(255);

		// We must always step through time!
		box2d.step();

		// When the mouse is clicked, add a new Box object
		if (mousePressed) {
			Box p = new Box(mouseX,mouseY,box2d,this);
			boxes.add(p);
		}

		// Display all the boundaries
		for (Boundary wall: boundaries) {
			wall.display();
		}

		// Display all the boxes
		for (Box b: boxes) {
			b.display();
		}

		// Boxes that leave the screen, we delete them
		// (note they have to be deleted from both the box2d world and our list
		for (int i = boxes.size()-1; i >= 0; i--) {
			Box b = boxes.get(i);
			if (b.done()) {
				boxes.remove(i);
			}
		}
	}
	
	public void keyPressed() {
	
		switch (this.keyCode){
		case PConstants.LEFT:
			_mainBox.applyForce(new Vec2(-35,0));
			break;
		case PConstants.RIGHT:
			_mainBox.applyForce(new Vec2(35,0));
			break;
		case PConstants.DOWN:
			_mainBox.applyForce(new Vec2(0,-35));
			break;
		case PConstants.UP:
			_mainBox.applyForce(new Vec2(0,35));
			break;				
		}
	}

}






