package ballsy;

/**
 * Generic superclass for the screen, which is what is contained within the window
 * (each level extends screen, as do menus, such as the pause/welcome screens). 
 */

import org.jbox2d.dynamics.Body;

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
	
	public abstract void keyReleased();
	
	public abstract void mouseDragged();
	
	public void onClose() {};
	
	/**
	 * handle collisions, strictly for overriding in AbstractLevel (don't need it in all screens,
	 * so not abstract)
	 * @param b1
	 * @param b2
	 */
	public void handleCollision(Body b1, Body b2, float velocity) { 
		// abstract level will take it from here...
	}
}
