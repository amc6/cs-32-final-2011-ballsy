package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;

public class Frame extends PApplet {

	//	An array of stripes
	//Stripe[] stripes = new Stripe[50];


	Spring2D s1, s2;

	float gravity = 6.0f;
	float mass = 2.0f;

	public void setup(){

		// Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// Get the current screen size
		Dimension scrnsize = toolkit.getScreenSize();

		size(scrnsize.width, scrnsize.height);
		smooth();
		fill(0);
		// Inputs: x, y, mass, gravity
		s1 = new Spring2D(0.0f, width/2, mass, gravity, this);
		//s2 = new Spring2D(0.0f, width/2, mass, gravity, this);
	}

	public void draw(){
		background(248,39,206);
		s1.update(mouseX, mouseY);
		s1.display(mouseX, mouseY);
		//s2.update(s1.x, s1.y);
		//s2.display(s1.x, s1.y);
	}

	/*

	public void setup() {

		// Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// Get the current screen size
		Dimension scrnsize = toolkit.getScreenSize();

		size(scrnsize.width, scrnsize.height);
		// Initialize all "stripes"
		for (int i = 0; i < stripes.length; i++) {
			stripes[i] = new Stripe(this);
		}
	}

	public void draw() {
		background(100);
		// Move and display all "stripes"
		for (int i = 0; i < stripes.length; i++) {
			stripes[i].move();
			stripes[i].display();
		}
	} */



}
