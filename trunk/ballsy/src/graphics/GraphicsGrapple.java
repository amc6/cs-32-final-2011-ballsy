package graphics;

import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import ballsy.Window;

public class GraphicsGrapple extends GraphicsDef{
	
	private bodies.UserBall _ball;
	int _weight = 2, _size = 12, _pointerSize = 50, _fillColor = 255, _fillOpacity = 150, 
	_activeDrawColor = Window.getInstance().color(0, 255, 0), _inactiveDrawColor = 200, _drawColor = _inactiveDrawColor;


	public GraphicsGrapple(int color, bodies.UserBall ball) {
		this.setColor(color);
		_ball = ball;
	}

	@Override
	public void display() {
		Window window = Window.getInstance();

		if (_ball.isGrappled()) {
			//Point2D.Float grapplePoint = _ball.getPixelGrapplePoint();
			physics.PhysicsGrapple physicsDef = (physics.PhysicsGrapple) _physicsDef; 
			Vec2 grapplePoint = _world.coordWorldToPixels(physicsDef.getGrapplePoint());
			int ballX = (int) _ball.getPixelPosition().x;
			int ballY = (int) _ball.getPixelPosition().y;
			window.strokeWeight(_weight);
			window.stroke(window.color(0,0,255));
			window.fill(window.color(0,0,255));
			window.line(ballX, ballY, grapplePoint.x, grapplePoint.y);
		}
		window.strokeWeight(DEFAULT_LINE_WIDTH);
	}
	
//	public Point2D.Float getGrapplePoint() {
//		return _ball.getGrapplePoint();
//	}
	
	public Element writeXML() {
		return null; // never write XML for grapple!
	}

}
