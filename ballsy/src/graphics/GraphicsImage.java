package graphics;

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
		float pixelHeight = 0;
		float pixelWidth = 0;
		if (_physicsDef instanceof PhysicsRectangle) {
			PhysicsRectangle recdef = (PhysicsRectangle) _physicsDef;
			pixelWidth = _world.scalarWorldToPixels(recdef.getWidth());
			pixelHeight = _world.scalarWorldToPixels(recdef.getHeight());
		}
		else if (_physicsDef instanceof PhysicsBall) {
			PhysicsBall balldef = (PhysicsBall) _physicsDef;
			pixelHeight = pixelWidth = _world.scalarWorldToPixels(balldef.getRadius());
		}
		else {
			throw new RuntimeException("A Graphics Image can only use a Rectangle or Ball PhysicsDef");
		}
		if (_image == null) _image = new Image(Window.getInstance(), _filename , (int) pixelWidth,(int) pixelHeight);

		_image.draw();
		
	
		window.popMatrix();

	}

	@Override
	public Element writeXML() {
		Element newEl = super.writeXML("graphics_image");
		newEl.addAttribute("FILENAME", _filename);
		return newEl;
	}

}
