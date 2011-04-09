package graphical;

import processing.core.PApplet;

public abstract class AbstractLevel extends Screen {
	

	public AbstractLevel(Main window) {
		super(window);
		// TODO Auto-generated constructor stub
	}
	public abstract void setup();
	public abstract void draw();
	
}
