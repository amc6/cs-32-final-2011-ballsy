package graphical;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ballsy.AbstractLevel;
import ballsy.Window;

public class BallDef extends GraphicalDef {
	
	private boolean _showLine = false;
	
	public BallDef(int color) {
		super(color);
		// TODO Auto-generated constructor stub
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
		window.fill(_color);
		window.stroke(0);
		window.ellipse(0,0,_world.scalarWorldToPixels(_physicsDef.getRadius()) * 2 , _world.scalarWorldToPixels(_physicsDef.getRadius()) * 2);
		
		// if appropriate, draw the line
		if (_showLine) {
			window.strokeWeight(1);
			window.line(0,0,_world.scalarWorldToPixels(_physicsDef.getRadius()),0);
			window.strokeWeight(AbstractLevel.DEFAULT_WEIGHT);
		}
	
		window.popMatrix();
	}
	
	/**
	 * Set the graphical definition of this ball to display the little line,
	 * so we can see it rotating.
	 */
	public void setLine(boolean s) {
		_showLine = s;
	}

}
