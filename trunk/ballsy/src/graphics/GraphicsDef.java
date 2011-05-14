package graphics;

/**
 * Superclass for all graphics definitions. Handles various variables of appearance, 
 * including color, border, smoke effects, etc.
 */

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import physics.PhysicsDef;
import physics.PhysicsWorld;
import ballsy.Window;

public abstract class GraphicsDef {

	protected PhysicsDef _physicsDef;
	protected PhysicsWorld _world = PhysicsWorld.getInstance();
	private int _color = DEFAULT_BODY_COLOR;
	protected Smoke _smoke;
	protected int _strokeWeight;
	protected int _strokeColor;
	private boolean _selected = false;
			
	public void setPhysicsDef(PhysicsDef physicsDef){
		_physicsDef = physicsDef;
	}
	
	public abstract void display();
	
	/**
	 * Display the effects associated with this object. Smoke here.
	 */
	public void displayEffects(){
		if (_smoke != null){
			_smoke.display();
		}
	}
	
	public void setSmoke(Smoke smoke){
		_smoke = smoke;
	}
	
	public void setSelected(boolean b) {
		_selected = b;
	}
	
	public boolean getSelected() {
		return _selected;
	}
	
	public Smoke getSmoke(){
		return _smoke;
	}
	
	public int getColor(){
		return _color;
	}

	public void setColor(int val){
		Window w = Window.getInstance();
		_color = w.color(val);
	}
	
	/**
	 * Sets the color, straight up from the RGB parameters
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(int r, int g, int b) {
		Window w = Window.getInstance();
		_color = w.color(r, g, b);
	}
	
	/**
	 * Sets the color, from the processing color value and the percentage
	 * (to darken it, if need be)
	 * @param val
	 * @param percent
	 */
	public void setColor(int val, double percent) {
		Window w= Window.getInstance();
		// apply the percent, first decomposing value into RGB
		int r = (val & 0x00ff0000) >> 16;
		int g = (val & 0x0000ff00) >> 8;
		int b = val & 0x000000ff;
		_color = w.color((int) (r * percent), (int) (g * percent), (int) (b * percent));
	}
	
	/**
	 * Set both stroke weight and color, in one fell swoop!
	 * @param weight
	 * @param color
	 */
	public void setStrokeWeightAndColor(int weight, int color) {
		_strokeWeight = weight;
		_strokeColor = color;
	}
	
	/**
	 * Set stroke weight and color, from provided variables, including a percent
	 * for darkening the colors
	 * @param weight
	 * @param val
	 * @param percent
	 */
	public void setStrokeWeightAndColor(int weight, int val, double percent) {
		_strokeWeight = weight;
		// apply the percent, first decomposing value into RGB
		int r = (val & 0x00ff0000) >> 16;
		int g = (val & 0x0000ff00) >> 8;
		int b = val & 0x000000ff;
		_strokeColor = Window.getInstance().color((int) (r * percent), (int) (g * percent), (int) (b * percent));
	}
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file.
	 * To be overridden by subclasses, which will call the other overloaded method with the proper type
	 * @return
	 */
	public abstract Element writeXML();
	
	/**
	 * Return the representation of the object as a dom4j element to write into a saved XML file, 
	 * provided the proper type.
	 * @return
	 */
	public Element writeXML(String type) {
		// create the graphical def element
		Element newEl = DocumentHelper.createElement("GRAPHICAL_DEF");
		// add proper attributes
		newEl.addAttribute("TYPE", type);
		newEl.addAttribute("COLOR", Integer.toString(_color));
		// all other stuff is ultimately handled with values from the physics def, so this is small..
		return newEl;
		
	}

}
