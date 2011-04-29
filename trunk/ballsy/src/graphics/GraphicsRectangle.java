package graphics;

import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import processing.core.PConstants;
import ballsy.Window;

public class GraphicsRectangle extends GraphicsDef {

	public GraphicsRectangle(int color) {
		this.setColor(color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void display() {
		
		Window window = Window.getInstance();
		
		physics.PhysicsRectangle physicsDef = (physics.PhysicsRectangle) _physicsDef;
		Body body = _physicsDef.getBody();
		Vec2 pos = _world.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();

		window.rectMode(PConstants.CENTER);
		window.pushMatrix();
		window.translate(pos.x,pos.y);
		window.rotate(-a);
		window.fill(this.getColor());
		window.stroke(0);
		window.strokeWeight(DEFAULT_LINE_WIDTH);
		window.rect(0,0,_world.scalarWorldToPixels(physicsDef.getWidth()),_world.scalarWorldToPixels(physicsDef.getHeight()));
		window.popMatrix();		
	}

	/**
	 * Override of super no-parameter writeXML(), makes a call to super's writeXML(String type) 
	 * with the proper type of this subclass
	 */
	public Element writeXML() {
		// return the XML element as designated in the super, with the appropriate type.
		return super.writeXML("rectangle");
	}
}
