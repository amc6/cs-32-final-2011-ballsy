package bodies;

import static ballsy.GeneralConstants.DEFAULT_LINE_WIDTH;

import java.util.Vector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import ballsy.Window;

import physics.PhysicsWorld;

public class Rope {
	
	private Vector<AbstractBody> _links;
	private PhysicsWorld _world;
	private Window _window;
	private DistanceJoint _joint1, _joint2;
	private int _numLinks = 40;

	

	/**
	 * 
	 * @param body1 first body rope is attached to
	 * @param body2 first body rope is attached to
	 * @param pos1 world location of first connection
	 * @param pos2 world location of second connection
	 */
	public Rope(AbstractBody body1, AbstractBody body2, Vec2 pos1, Vec2 pos2) {
		_world = PhysicsWorld.getInstance();
		_window = Window.getInstance();
		
		_links = new Vector<AbstractBody>();
		
		Vec2 dist = pos2.sub(pos1);
		float dx = dist.x/_numLinks;
		float dy = dist.y/_numLinks;
		
		
		float x = pos1.x + dx;
		float y = pos1.y + dy;
			
		//connect body1 to first link
		Ball ball = new Ball(x, y, .5F);
		_links.add(ball);
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.initialize(body1.getPhysicsDef().getBody(), ball.getPhysicsDef().getBody(), body1.getWorldPosition(), ball.getWorldPosition());
		jointDef.collideConnected = false;
		_joint1 = (DistanceJoint) _world.createJoint(jointDef);
		Ball lastLink = ball;

		//build chain
		for (int i=0; i<_numLinks-2; i++) {
			x = x+dx;
			y = y+dy;
			ball = new Ball(x, y, .5F);
			_links.add(ball);
			jointDef = new DistanceJointDef();
			jointDef.initialize(lastLink.getPhysicsDef().getBody(), ball.getPhysicsDef().getBody(), lastLink.getWorldPosition(), ball.getWorldPosition());
			jointDef.collideConnected = false;
			_world.createJoint(jointDef);
			lastLink = ball;
		}
		
		//last link to body2
		jointDef = new DistanceJointDef();
		jointDef.initialize(lastLink.getPhysicsDef().getBody(), body2.getPhysicsDef().getBody(), lastLink.getWorldPosition(), pos2);
		jointDef.collideConnected = false;
		_joint2 = (DistanceJoint) _world.createJoint(jointDef);
		
	}
	
	public void display() {
		
		_window.strokeWeight(2);
		_window.stroke(_window.color(255,20,20));
		_window.fill(_window.color(255,20,20));
		
		AbstractBody lastLink = _links.get(0);
		
		Vec2 pos1 = _joint1.getAnchor1();
		Vec2 pos2 = _joint2.getAnchor2();

		//first link
		_window.line(_world.coordWorldToPixels(pos1).x, _world.coordWorldToPixels(pos1).y,
				lastLink.getPixelPosition().x, lastLink.getPixelPosition().y);
		
		//chain
		for (int i = 1; i<_links.size(); i++) {
			AbstractBody link = _links.get(i);
//			link.display();
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
