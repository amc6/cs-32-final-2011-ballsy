package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;

import processing.core.PApplet;
import processing.core.PConstants;

public abstract class Screen {
	
	protected Main _window;
	
	public Screen(Main window) {
		_window = window;
		// Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// Get the current screen size
		Dimension scrnsize = toolkit.getScreenSize();
		
		_window.size(scrnsize.width, scrnsize.height, PConstants.OPENGL);
		_window.hint(PConstants.ENABLE_OPENGL_4X_SMOOTH);
		
		_window.smooth();
		
		_window.frameRate(60);
		
	}
	
	public abstract void setup();
	
	public abstract void draw();
	
	public abstract void mousePressed();
	
	public abstract void mouseReleased();

}
