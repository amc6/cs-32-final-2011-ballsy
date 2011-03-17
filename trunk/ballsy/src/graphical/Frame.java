package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;

public class Frame extends PApplet {

	//	An array of stripes
	Stripe[] stripes = new Stripe[50];

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
	}

}
