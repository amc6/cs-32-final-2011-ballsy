package graphics;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import ballsy.Window;
import bodies.AbstractBody;

public class TrackingCamera {
	
	public final float SCREEN_PERCENT = .3f;
	
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
		Vec2 lowerLeft = _world.getBounds()[0];
		Vec2 upperRight = _world.getBounds()[1];
		float dx = 0, dy = 0;
		if (!_world.contains(_world.coordPixelsToWorld(0,_window.height/2))) {
			dx = _world.scalarWorldToPixels(lowerLeft.x - _world.pixelXtoWorldX(0));
		}
		else if (!_world.contains(_world.coordPixelsToWorld(_window.width, _window.height/2))) {
			dx = _world.scalarWorldToPixels(upperRight.x - _world.pixelXtoWorldX(_window.width));
		}
		if (!_world.contains(_world.coordPixelsToWorld(_window.width/2,0))) {
			dy = _world.scalarWorldToPixels(upperRight.y - _world.pixelYtoWorldY(0));
		}
		else if (!_world.contains( _world.coordPixelsToWorld(_window.width/2, _window.height))) {
			dy = _world.scalarWorldToPixels(lowerLeft.y - _world.pixelYtoWorldY(_window.height));
		}		
		_world.moveCamera(dx, dy);
		
		_oldX = pos.x;
		_oldY = pos.y;
	}
	
	public void update() {
		
		Vec2 pos = _body.getWorldPosition();
		float dx = _world.scalarWorldToPixels(pos.x-_oldX);
		float dy = _world.scalarWorldToPixels(pos.y-_oldY);

		Vec2 top = _world.coordPixelsToWorld(_window.width/2,dy);
		Vec2 bottom = _world.coordPixelsToWorld(_window.width/2, _window.height+dy);
		Vec2 farLeft = _world.coordPixelsToWorld(0+dx,_window.height/2);
		Vec2 farRight = _world.coordPixelsToWorld(_window.width+dx, _window.height/2);
		
		if (_world.worldXtoPixelX(pos.x) < _window.width*SCREEN_PERCENT && dx < 0 && _world.contains(farLeft.add(new Vec2(dx,0)))) _world.moveCamera(dx, 0);
		if (_world.worldXtoPixelX(pos.x) > _window.width*(1-SCREEN_PERCENT) && dx > 0 && _world.contains(farRight.add(new Vec2(dx,0)))) _world.moveCamera(dx, 0);
		if (_world.worldYtoPixelY(pos.y) < _window.height*SCREEN_PERCENT && dy > 0 && _world.contains(top.add(new Vec2(0,dy)))) _world.moveCamera(0, dy);
		if (_world.worldYtoPixelY(pos.y) > _window.height*(1-SCREEN_PERCENT) && dy < 0 && _world.contains(bottom.add(new Vec2(0,dy)))) _world.moveCamera(0, dy);
		
		
		_oldX = pos.x;
		_oldY = pos.y;
		
	}

}
