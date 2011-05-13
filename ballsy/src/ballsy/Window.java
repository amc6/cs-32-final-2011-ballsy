package ballsy;

/**
 * The implementation of the Processing PApplet, as a full screen window.
 * Handles screen management, top level drawing stuff, fading, some collision
 * stuff, and passes all input methods down to the current screen.
 */

import graphics.Text;

import java.awt.Dimension;
import java.awt.Toolkit;

import menu.MenuButton;

import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import processing.core.PApplet;
import processing.core.PConstants;
import ballsy.ScreenLoader.Screens;

public class Window extends PApplet {
	
	private static Window WINDOW;
	private Screen _screen;
	private ScreenLoader _screenToLoad;
	private Dimension _screenSize;
	// for fading
	private int _fadeAlphaChange;
	private int _fadeAlphaCurr; 

	public void setup() {
		WINDOW = this;

		// Get the current screen size and set to max width
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		_screenSize = toolkit.getScreenSize();
		this.size(_screenSize.width, _screenSize.height, PConstants.OPENGL);
		this.hint(PConstants.ENABLE_OPENGL_2X_SMOOTH);	
		this.frameRate(60);
		this.loadScreen(Screens.WELCOME_SCREEN);
		
		// make a new XMLUtil, using singleton Pattern
		XMLUtil.setInstance(new XMLUtil());

	}
	
	/**
	 * Draw the current screen, manage fading
	 */
	public void draw() {
		// load a screen if needed
		if (_screenToLoad != null){		
			_screen = null;
			_screenToLoad.run();
			_screenToLoad = null;
		}
		// draw the screen, handling fading
		if (_screen != null){
			if (_fadeAlphaChange <= 0) // if not fading out
				_screen.draw(); // simply draw it
			// else, handle fading
			if (_fadeAlphaChange != 0){
				_fadeAlphaCurr += _fadeAlphaChange;
				if (_fadeAlphaCurr < 0) _fadeAlphaCurr = 0;
				if (_fadeAlphaCurr > 255) _fadeAlphaCurr = 255;
				
				this.rectMode(PConstants.CORNER);
				this.fill(0, _fadeAlphaCurr);
				this.rect(0, 0, _screenSize.width, _screenSize.height);
				
				if (_fadeAlphaChange > 0 && _fadeAlphaCurr == 255){ // end of fading out
					_fadeAlphaChange = -GeneralConstants.FADE_SPEED; // stop fading
					_fadeAlphaCurr = 255;
					if (_screen instanceof XMLLevel){ // if we're playing a level
						((XMLLevel) _screen).reload(); // includes fade in
					}
				}else if (_fadeAlphaChange < 0 && _fadeAlphaCurr == 0){ // end of fading in
					_fadeAlphaChange = 0; // stop fading
					_fadeAlphaCurr = 0;
				}
			}
		}
		
	}
		
	/**
	 * Set the current screen to the parameter screen
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		if (_screen != null) {
			_screen.onClose(); // close current screen
		}
		_screen = screen;
	}
	
	/**
	 * Load the indicated screen, using null values (for non-XMLLevels)
	 * @param s
	 */
	public void loadScreen(Screens s) {
		this.loadScreen(s, null, null);
	}
	
	/**
	 * Load a new screen with specified filename and current menubutton.
	 * Will probably be an XMLLevel, but this is handled by ScreenLoader
	 * @param s
	 * @param filename
	 * @param current
	 */
	public void loadScreen(Screens s, String filename, MenuButton current) {
		
		if (_screen != null)
			_screen.onClose();
		
		Text message = new Text("Loading...", this.width/2, this.height/2);
		message.setColor(WelcomeScreen.DEFAULT_TEXT_COLOR);
		this.noCursor();
		this.background(50,200,200);
		message.draw();
		
		_screenToLoad = new ScreenLoader(s, filename, current);
	}
	
	public void fadeOut(){
		_fadeAlphaCurr = 0;
		_fadeAlphaChange = GeneralConstants.FADE_SPEED;
	}
	
	public void fadeIn(){
		_fadeAlphaCurr = 255;
		_fadeAlphaChange = -GeneralConstants.FADE_SPEED;
	}
	
	/* All following are processing input stuff, we want to pass it down to the current screen */
	
	public void mousePressed() {
		if (_screen != null)
			_screen.mousePressed();
	}
	
	public void mouseReleased() {
		if (_screen != null)
			_screen.mouseReleased();
	}
	
	public void mouseDragged() {
		if (_screen != null)
			_screen.mouseDragged();
	}
	
	public void keyPressed() {
		if (_screen != null)
			_screen.keyPressed();
	}
	
	public void keyReleased() {
		if (_screen != null)
			_screen.keyReleased();
	}
		
	/**
	 *  callback methods for handling contact (for some reason this is delegated to the
	 *  PApplet, which is Window, from the PhysicsWorld. Works for me.
	 */
	public void addContact(ContactPoint cp) {
		// a contact has happened!
		if (_screen != null)
			_screen.handleCollision(cp.shape1.getBody(), cp.shape2.getBody(), cp.velocity.length());
	}
	
	public void persistContact(ContactPoint cp) { }
	public void removeContact(ContactPoint cp) { }
	public void resultContact(ContactResult cr) { }
	
	/** return singleton instance **/
	public static Window getInstance(){
		return WINDOW;
	}
	
}
