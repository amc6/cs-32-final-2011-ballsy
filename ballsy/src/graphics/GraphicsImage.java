package graphics;

/**
 * Graphics image, used for things such as the ball and pail which have
 * graphics defined in images. 
 */

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsBall;
import physics.PhysicsRectangle;
import ballsy.Window;

public class GraphicsImage extends GraphicsDef {
	
	private Image _image;
	private String _filename;
	
	public GraphicsImage(String filename) {
		_filename = filename;
	}

	/**
	 * Display the picture as a graphical object
	 */
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
		
//		float pixelHeight = 0;
//		float pixelWidth = 0;
//		if (_physicsDef instanceof PhysicsRectangle) {
//			PhysicsRectangle recdef = (PhysicsRectangle) _physicsDef;
//			pixelWidth = _world.scalarWorldToPixels(recdef.getWidth());
//			pixelHeight = _world.scalarWorldToPixels(recdef.getHeight());
//		}
//		else if (_physicsDef instanceof PhysicsBall) {
//			PhysicsBall balldef = (PhysicsBall) _physicsDef;
//			pixelHeight = pixelWidth = _world.scalarWorldToPixels(balldef.getRadius());
//		}
//		else {
//			throw new RuntimeException("A GraphicsImage can be paired with only a PhysicsRectangle or a PhysicsBall.");
//		}

		if (_image == null) _image = new Image(_filename, 0,0);
		_image.draw();		
	
		window.popMatrix();

	}

	/**
	 * Write it to XML
	 */
	public Element writeXML() {
		Element newEl = super.writeXML("graphics_image");
		newEl.addAttribute("FILENAME", _filename);
		return newEl;
	}

}
