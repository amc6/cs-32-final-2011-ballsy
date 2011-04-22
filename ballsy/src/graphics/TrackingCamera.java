package graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsWorld;
import bodies.AbstractBody;

public class TrackingCamera {
	
	private PhysicsWorld _world;
	private AbstractBody _body;
	private float _oldX, _oldY;
	
	public TrackingCamera(AbstractBody body) {
		_world = PhysicsWorld.getInstance();
		_body = body;
		Vec2 pos = _body.getWorldPosition();
		_world.centerCameraOn(pos.x, pos.y);
		_oldX = pos.x;
		_oldY = pos.y;
	}
	
	public void update() {
		Vec2 pos = _body.getWorldPosition();
//		_world.centerCameraOn(pos.x, pos.y);
		float dx = _world.scalarWorldToPixels(pos.x-_oldX);
		float dy = _world.scalarWorldToPixels(pos.y-_oldY);
		_world.moveCamera(dx,dy,false);
		_oldX = pos.x;
		_oldY = pos.y;
	}

}
