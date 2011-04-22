package graphics;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import physics.PhysicsDef;
import physics.PhysicsWorld;
import ballsy.Window;
import static bodies.BodyConstants.*;

public abstract class GraphicsDef {

	protected PhysicsDef _physicsDef;
	protected PhysicsWorld _world = PhysicsWorld.getInstance();
	private int _color = DEFAULT_BODY_COLOR;
			
	public void setPhysicsDef(PhysicsDef physicsDef){
		_physicsDef = physicsDef;
	}
	
	public abstract void display();
	
	public int getColor(){
		return _color;
	}

	public void setColor(int val){
		Window w = Window.getInstance();
		_color = w.color(val);
	}
	
	public void setColor(int r, int g, int b) {
		Window w = Window.getInstance();
		_color = w.color(r, g, b);
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
