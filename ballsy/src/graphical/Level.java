package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import physics.PhysicsWorld;
import processing.core.PApplet;
import processing.core.PConstants;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example


public class Level extends AbstractLevel {
	
	//A reference to our box2d world
	private PhysicsWorld _box2d;
	
	//An ArrayList of particles that will fall on the surface
	ArrayList<Particle> _particles;

	//An object to store information about the uneven surface
	Surface _surface;
	

	public void setup() {
		// Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// Get the current screen size
		Dimension scrnsize = toolkit.getScreenSize();

		this.size(scrnsize.width, scrnsize.height, PConstants.OPENGL);
		this.hint(PConstants.ENABLE_OPENGL_4X_SMOOTH);
		
		
		smooth();
		
		this.frameRate(60);
		
		// Initialize box2d physics and create the world
		_box2d = new PhysicsWorld(this);
		_box2d.createWorld();
		// We are setting a custom gravity
		_box2d.setGravity(0, -20);

		// Create the empty list
		_particles = new ArrayList<Particle>();
		// Create the surface
		_surface = new Surface(this, _box2d);
		//this.frameRate(120);
		

	}
	


	public void draw() {
		
		
		// If the mouse is pressed, we make new particles
		if (mousePressed) {
			float sz = random(2,6);
			_particles.add(new Particle(mouseX,mouseY,sz, _box2d, this));
		}

		// We must always step through time!
		_box2d.step();

		background(255,255,255);

		// Draw the surface
		_surface.display();

		// Draw all particles
		for (Particle p: _particles) {
			p.display();
		}

		// Particles that leave the screen, we delete them
		// (note they have to be deleted from both the box2d world and our list
		for (int i = _particles.size()-1; i >= 0; i--) {
			Particle p = _particles.get(i);
			if (p.done()) {
				_particles.remove(i);
			}
		}

		// Just drawing the framerate to see how many particles it can handle
		fill(0);
		textSize(20);
		text("framerate: " + (int)frameRate,12,46);
		
	}

}






