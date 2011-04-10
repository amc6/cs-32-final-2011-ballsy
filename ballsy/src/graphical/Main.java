package graphical;

import processing.core.PApplet;

public class Main extends PApplet {
	
	private Screen _screen;

	public void setup() {
		_screen = new WelcomeScreen(this);
		_screen.setup();
	}
	
	public void draw() {
		_screen.draw();
	}
	
	public void setScreen(Screen screen) {
		this.background(255);
		_screen = screen;
		_screen.setup();
	}
	
	public void mousePressed() {
		_screen.mousePressed();
	}
	
	public void mouseReleased() {
		_screen.mouseReleased();
	}

}
