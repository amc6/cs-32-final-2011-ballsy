package graphics;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import ballsy.Window;
import bodies.AbstractBody;

public class TrackingCamera {
	
	public final float SCREEN_PERCENT = .2f;
	
	private PhysicsWorld _world;
	private Window _window;
	private AbstractBody _body;
	private float _oldX, _oldY;
	
	
	public TrackingCamera(AbstractBody body) {
		_world = PhysicsWorld.getInstance();
		_window = Window.getInstance();
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
//		_world.moveCamera(dx,dy,false);
		if (_world.worldXtoPixelX(pos.x) < _window.width*SCREEN_PERCENT || _world.worldXtoPixelX(pos.x) > _window.width*(1-SCREEN_PERCENT)) {
			_world.moveCamera(dx, 0, false);
		}
		if (_world.worldYtoPixelY(pos.y) < _window.height*SCREEN_PERCENT || _world.worldYtoPixelY(pos.y) > _window.height*(1-SCREEN_PERCENT)) {
			_world.moveCamera(0, dy, false);
		}	
		
		_oldX = pos.x;
		_oldY = pos.y;
		
	}

}
