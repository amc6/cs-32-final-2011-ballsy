package graphical;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ballsy.Window;

import processing.core.PConstants;

public class RectangleDef extends GraphicalDef {

	public RectangleDef(int color) {
		super(color);
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
		window.fill(_color);
		window.stroke(0);
		window.rect(0,0,_world.scalarWorldToPixels(physicsDef.getWidth()),_world.scalarWorldToPixels(physicsDef.getHeight()));
		window.popMatrix();		
	}

	
}