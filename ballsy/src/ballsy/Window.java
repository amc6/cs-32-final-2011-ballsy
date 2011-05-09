package ballsy;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import processing.core.PApplet;
import processing.core.PConstants;
import ballsy.ScreenLoader.Screens;
import graphics.Text;

public class Window extends PApplet {
	
	private static Window WINDOW;
	private Screen _screen;

	public void setup() {
		WINDOW = this;

		// Get the current screen size and set to max width
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		this.size(scrnsize.width, scrnsize.height, PConstants.OPENGL);
		
//		this.size(800,800,PConstants.OPENGL);
		// Set appearance and performance
		this.hint(PConstants.ENABLE_OPENGL_2X_SMOOTH);	
		this.frameRate(60);
		
		// default camera settings are as follows. could be used to properly implement map? would need to offset all mouse positions though...
		//this.camera(width/2.0f, height/2.0f, (height/2.0f) / (float) Math.tan(Math.PI*60.0 / 360.0), width/2.0f, height/2.0f, 0f, 0f, 1f, 0f);
		
//		this.setScreen(new LevelEditor());
//		this.loadScreen(Screens.WELCOME_SCREEN);
//		this.setScreen(new LevelOne());
//		this.setScreen(new LoadingScreen());
		this.loadScreen(Screens.WELCOME_SCREEN);
		
		// make a new XMLUtil, using singleton Pattern
		XMLUtil.setInstance(new XMLUtil());
		
		this.smooth();
	}
	
	public void draw() {
		if (_screen == null){
			this.loadScreen(Screens.WELCOME_SCREEN);
		}
		_screen.draw();	
	}
	
	public void setScreenAndSetup(Screen screen) {
		this.setScreen(screen);
		_screen.setup();
	}
	
	public void setScreen(Screen screen) {
		if (_screen != null) {
			_screen.onClose();
		}
		_screen = screen;
	}
	
	public void loadScreen(Screens s) {
		//this.setScreenAndSetup(new LoadingScreen());
		Text message = new Text("Loading...", this.width/2, this.height/2);
		message.setColor(WelcomeScreen.DEFAULT_TEXT_COLOR);
		this.noCursor();
		this.background(50,200,200);
		message.draw();
		//new Thread(new ScreenLoader(s)).start();
		new ScreenLoader(s).run();
	}
		
	/**
	 * Alternative to setScreen(), takes in a string of the path of a saved level,
	 * and constructs it inside a new instance of XMLLevel.
	 * @param path
	 */
	public void loadLevel(String path) {
		XMLLevel newLevel = new XMLLevel(path);
		_screen = newLevel;
	}
	
	public void mousePressed() {
		_screen.mousePressed();
	}
	
	public void mouseReleased() {
		_screen.mouseReleased();
	}
	
	public void mouseDragged() {
		_screen.mouseDragged();
	}
	
	public void keyPressed() {
		_screen.keyPressed();
	}
	
	public void keyReleased() {
		_screen.keyReleased();
	}
	
	public static Window getInstance(){
		return WINDOW;
	}
	
	/**
	 *  callback methods for handling contact (for some reason this is delegated to the
	 *  PApplet, which is Window, from the PhysicsWorld. Works for me.
	 */
	public void addContact(ContactPoint cp) {
		// a contact has happened!
		_screen.handleCollision(cp.shape1.getBody(), cp.shape2.getBody(), cp.velocity.length());
	}
	
	public void persistContact(ContactPoint cp) { }
	public void removeContact(ContactPoint cp) { }
	public void resultContact(ContactResult cr) { }
	
}
