package zoldshit;


import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import ballsy.AbstractLevel;
import bodies.AbstractBody;

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
		
		// Initialize box2d physics and create the world
		box2d = new PhysicsWorld(_window);
		box2d.createWorld();
		// We are setting a custom gravity
		box2d.setGravity(0, -20);

		// Create ArrayLists	
		boxes = new ArrayList<Box>();
		boundaries = new ArrayList<Boundary>();

		// Add a bunch of fixed boundaries
		boundaries.add(new Boundary(_window.width/2, 0, _window.width, 10, box2d, _window));
		boundaries.add(new Boundary(_window.width/2, _window.height - 5, _window.width, 10, box2d, _window));
		boundaries.add(new Boundary(_window.width-5,_window.height/2,10,_window.height,box2d,_window));
		boundaries.add(new Boundary(5,_window.height/2,10,_window.height,box2d,_window));
		
		_mainBox = new Box(_window.width/3,_window.height-50,box2d,_window);
		boxes.add(_mainBox);
	}

	public void draw() {
		_window.background(255);

		// We must always step through time!
		box2d.step();

		// When the mouse is clicked, add a new Box object
		if (_window.mousePressed) {
			Box p = new Box(_window.mouseX,_window.mouseY,box2d,_window);
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
	
		switch (_window.keyCode){
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

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(AbstractBody object) {
		// TODO Auto-generated method stub
		
	}

}






