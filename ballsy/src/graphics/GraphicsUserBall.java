package graphics;

import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsBall;
import processing.core.PImage;

import ballsy.Window;

public class GraphicsUserBall extends GraphicsDef {
	
	private Image _image;

	
	@Override
	public void display() {
		Window window = Window.getInstance();
	
		Body body = _physicsDef.getBody();
		Vec2 pos = _world.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		
		window.pushMatrix();
		window.translate(pos.x, pos.y);
		window.rotate(-a);
		window.fill(this.getColor());
		window.stroke(0);
		if (_image == null) _image = new Image(Window.getInstance(), "res/beachball.png", this.getPixelRadius()*2, this.getPixelRadius()*2);

		_image.draw();
		
	
		window.popMatrix();
	}

	public int getPixelRadius(){
		PhysicsBall def = (PhysicsBall) _physicsDef;
		return (int) _world.scalarWorldToPixels(def.getRadius());
	}
	
	/**
	 * Override of super no-parameter writeXML(), makes a call to super's writeXML(String type) 
	 * with the proper type of this subclass
	 */
	public Element writeXML() {
		// return the XML element as designated in the super, with the appropriate type.
		Element newEl = super.writeXML("user_ball");
		return newEl;
	}

}
