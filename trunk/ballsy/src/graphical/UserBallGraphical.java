package graphical;

import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ballsy.Window;

public class UserBallGraphical extends BallDef {
	
	private Image _image;

	public UserBallGraphical() {
		super(0);

		physics.PhysicsBall physicsDef = (physics.PhysicsBall) _physicsDef; 
	}
	
	@Override
	public void display() {
		Window window = Window.getInstance();
		physics.PhysicsBall physicsDef = (physics.PhysicsBall) _physicsDef; 
		
		Body body = _physicsDef.getBody();
		Vec2 pos = _world.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		
		window.pushMatrix();
		window.translate(pos.x, pos.y);
		window.rotate(-a);
		window.fill(_color);
		window.stroke(0);
		if (_image == null) _image = new Image(Window.getInstance(), "res/beachball.png",(int) _world.scalarWorldToPixels(physicsDef.getWidth()),(int)_world.scalarWorldToPixels(physicsDef.getHeight()));

		//window.ellipse(0,0,_world.scalarWorldToPixels(physicsDef.getRadius()) * 2 , _world.scalarWorldToPixels(physicsDef.getRadius()) * 2);
		_image.draw();
		
	
		window.popMatrix();
	}

}
