package graphical;

import java.awt.geom.Point2D;

import org.dom4j.Element;

import ballsy.AbstractLevel;
import ballsy.Window;
import static ballsy.GeneralConstants.*;

public class GrappleDef extends GraphicalDef{
	
	private bodies.UserBall _ball;
	int _weight = 2, _size = 12, _pointerSize = 50, _fillColor = 255, _fillOpacity = 150, 
	_activeDrawColor = Window.getInstance().color(0, 255, 0), _inactiveDrawColor = 200, _drawColor = _inactiveDrawColor;


	public GrappleDef(int color, bodies.UserBall ball) {
		super(color);
		_ball = ball;
	}

	@Override
	public void display() {
		Window window = Window.getInstance();

		if (_ball.isGrappled()) {
			Point2D.Float grapplePoint = _ball.getPixelGrapplePoint();
			int ballX = _ball.getPixelX();
			int ballY = _ball.getPixelY();
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
