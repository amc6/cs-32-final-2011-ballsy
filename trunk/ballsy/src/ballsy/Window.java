package ballsy;

import java.awt.Dimension;
import java.awt.Toolkit;

import oldshit.Level;
import processing.core.PApplet;
import processing.core.PConstants;

public class Window extends PApplet {
	
	private static Window WINDOW;
	private Screen _screen;

	public void setup() {
		WINDOW = this;

		// Get the current screen size and set to max width
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		this.size(scrnsize.width, scrnsize.height, PConstants.OPENGL);
		
		//this.size(800,800,PConstants.OPENGL);
		// Set appearance and performance
		this.hint(PConstants.ENABLE_OPENGL_2X_SMOOTH);	
		this.frameRate(60);
		
		// default camera settings are as follows. could be used to properly implement map? would need to offset all mouse positions though...
		//this.camera(width/2.0f, height/2.0f, (height/2.0f) / (float) Math.tan(Math.PI*60.0 / 360.0), width/2.0f, height/2.0f, 0f, 0f, 1f, 0f);
		
		this.setScreen(new LevelTwo());
	}
	
	public void draw() {
		_screen.draw();
	}
	
	public void setScreen(Screen screen) {
		this.background(255); // default screen color
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
