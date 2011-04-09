package graphical;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import processing.core.PConstants;

public class RectangleDef extends GraphicalDef {

	@Override
	public void display() {
		Body body = _physicsDef.getBody();
		Vec2 pos = _world.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();

		_level.rectMode(PConstants.CENTER);
		_level.pushMatrix();
		_level.translate(pos.x,pos.y);
		_level.rotate(-a);
		_level.fill(175);
		_level.stroke(0);
		_level.rect(0,0,_world.scalarWorldToPixels(_physicsDef.getWidth()),_world.scalarWorldToPixels(_physicsDef.getHeight()));
		_level.popMatrix();		
	}

	
}
