package graphical;

import java.util.ArrayList;

import physics.PhysicsWorld;
import processing.core.PApplet;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example


public class BallTestLevel extends PApplet {
	
	//A reference to our box2d world
	private PhysicsWorld _box2d;
	private ArrayList<Boundary> _boundary;
	
	

	public void setup() {
		this.size(700,600);
		this.smooth();
		
		_box2d = new PhysicsWorld(this);
		_box2d.createWorld();
		// We are setting a custom gravity
		_box2d.setGravity(0, -20);
		
		_boundary = new ArrayList<Boundary>();
	}
	


	public void draw() {
		
		
	}

}






