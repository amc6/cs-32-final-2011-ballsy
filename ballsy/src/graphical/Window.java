package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;
import processing.core.PConstants;

public class Window extends PApplet {
	
	private static Window WINDOW;
	private Screen _screen;

	public void setup() {

		// Get the current screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		this.size(scrnsize.width, scrnsize.height, PConstants.OPENGL);
		this.hint(PConstants.ENABLE_OPENGL_4X_SMOOTH);
		this.smooth();		
		this.frameRate(60);
		
		_screen = new WelcomeScreen();
		_screen.setup();
		WINDOW = this;
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
	
	public void keyPressed() {
		_screen.keyPressed();
	}
	
	public static Window getInstance(){
		return WINDOW;
	}
	

}
