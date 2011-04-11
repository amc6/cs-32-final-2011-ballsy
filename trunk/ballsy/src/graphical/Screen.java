package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;
import processing.core.PConstants;

public abstract class Screen {
	
	protected Window _window;
	
	public Screen() {
		_window = Window.getInstance();
	}
	
	public abstract void setup();
	
	public abstract void draw();
	
	public abstract void mousePressed();
	
	public abstract void mouseReleased();
	
	public abstract void keyPressed();

}
