package bodies;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import ballsy.AbstractLevel;

public class UserBall extends Ball {
	private float _moveCoeff = 20;
	private float _maxVel = 100;
	
	public UserBall(AbstractLevel level, PhysicsWorld world, float centerX, float centerY) {
		super(level, world, centerX, centerY, 3, true);
		this.setColor(0, 0, 255);
	}

	public void moveLeft() {
		if (_physicsDef.getBody().getLinearVelocity().x > -_maxVel)
			_physicsDef.applyImpulse(new Vec2(-_moveCoeff, 0));
	}
	
	public void moveRight() {
		if (_physicsDef.getBody().getLinearVelocity().x < _maxVel)
			_physicsDef.applyImpulse(new Vec2(_moveCoeff, 0));
	}
	
	public void moveDown() {
		if (_physicsDef.getBody().getLinearVelocity().y > -_maxVel)
			_physicsDef.applyImpulse(new Vec2(0, -_moveCoeff));
	}
	
	public void moveUp() {
		if (_physicsDef.getBody().getLinearVelocity().y < _maxVel)
			_physicsDef.applyImpulse(new Vec2(0, _moveCoeff));
	}
}
