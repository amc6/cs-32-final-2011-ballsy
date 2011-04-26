package graphics;

import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import java.util.Vector;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsGrappleRope;

import ballsy.Window;
import bodies.AbstractBody;
import bodies.Link;

public class GraphicsGrappleRope extends GraphicsDef{
	
	private bodies.UserBall _ball;
	int _weight = 2, _size = 12, _pointerSize = 50, _fillColor = 255, _fillOpacity = 150, 
	_activeDrawColor = Window.getInstance().color(0, 255, 0), _inactiveDrawColor = 200, _drawColor = _inactiveDrawColor;
	private Window _window;

	public GraphicsGrappleRope(int color, bodies.UserBall ball) {
		this.setColor(color);
		_ball = ball;
		_window = Window.getInstance();
		
	}

	@Override
	public void display() {
		if (_ball.isGrappled()) {
			
			_window.strokeWeight(2);
			_window.stroke(_window.color(255,20,20));
			_window.fill(_window.color(255,20,20));
			
			PhysicsGrappleRope physicsDef = (PhysicsGrappleRope) _physicsDef;
			
			Vector<Link> links = physicsDef.getLinks();
			
			AbstractBody lastLink = links.get(0);
			
			Vec2 pos1 = physicsDef.getFirstJoint().getAnchor1();
			Vec2 pos2 = physicsDef.getLastJoint().getAnchor2();
	
			//first link
			_window.line(_world.coordWorldToPixels(pos1).x, _world.coordWorldToPixels(pos1).y,
					lastLink.getPixelPosition().x, lastLink.getPixelPosition().y);
			
			//chain
			for (int i = 1; i<links.size(); i++) {
				AbstractBody link = links.get(i);
				
				//////***UNCOMMENT TO SEE LINKS***///////
				// link.display();
				////////////////////////////////////////

				
				_window.line(lastLink.getPixelPosition().x, lastLink.getPixelPosition().y,
						link.getPixelPosition().x, link.getPixelPosition().y);
				lastLink = link;
			}
			
			//last link
			_window.line(_world.coordWorldToPixels(pos2).x, _world.coordWorldToPixels(pos2).y,
					lastLink.getPixelPosition().x, lastLink.getPixelPosition().y);
			
			
			_window.strokeWeight(DEFAULT_LINE_WIDTH);
		
		}
	}
	
//	public Point2D.Float getGrapplePoint() {
//		return _ball.getGrapplePoint();
//	}
	
	public Element writeXML() {
		return null; // never write XML for grapple!
	}

}
