package graphics;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsBall;
import static ballsy.GeneralConstants.*;

import ballsy.Window;

public class GraphicsBall extends GraphicsDef {
	
	private boolean _showLine = false;
	
	public GraphicsBall() {
	}
	
	public GraphicsBall(int color) {
		this.setColor(color);
	}
	
	@Override
	public void display() {
		Window window = Window.getInstance();

		Body body = _physicsDef.getBody();
		Vec2 pos = _world.coordWorldToPixels(_physicsDef.getBodyWorldCenter());

		float a = body.getAngle();		
		window.pushMatrix();
		window.translate(pos.x, pos.y);
		window.rotate(-a);
		window.fill(this.getColor());
		window.stroke(0);
		window.ellipse(0,0,this.getPixelRadius() * 2 , this.getPixelRadius() * 2);

		// if appropriate, draw the line representing rotation of the ball
		if (_showLine) {
			window.strokeWeight(1);
			window.line(0,0,this.getPixelRadius(),0);
			window.strokeWeight(DEFAULT_LINE_WIDTH);
		}
	
		window.popMatrix();
	}
	
	public int getPixelRadius(){
		PhysicsBall def = (PhysicsBall) _physicsDef;
		return (int) _world.scalarWorldToPixels(def.getRadius());
	}
	
	/**
	 * Set the graphical definition of this ball to display the little line,
	 * so we can see it rotating.
	 */
	public void setLine(boolean s) {
		_showLine = s;
	}

	/**
	 * Override of super no-parameter writeXML(), makes a call to super's writeXML(String type) 
	 * with the proper type of this subclass
	 */
	public Element writeXML() {
		// return the XML element as designated in the super, with the appropriate type.
		Element newEl = super.writeXML("ball");
		newEl.addAttribute("DISPLAY_LINE", Boolean.toString(_showLine));
		return newEl;
	}

}
