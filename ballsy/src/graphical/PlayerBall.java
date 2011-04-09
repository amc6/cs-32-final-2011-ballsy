package graphical;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import physics.PhysicsWorld;
import processing.core.PApplet;

public class PlayerBall {
	
	Body _body;
	float _radius;
	private PhysicsWorld _world;
	private PApplet _level;
	
	public PlayerBall(PhysicsWorld physics, PApplet level) {
		_world = physics;
		_level = level;
		_radius = 10;
		
		BodyDef bd = new BodyDef();
		bd.position = _world.coordPixelsToWorld(_level.width/2, _level.height/2);
		_body = _world.createBody(bd);
		
		CircleDef cd = new CircleDef();
		cd.radius = _world.scalarPixelsToWorld(_radius);
		System.out.println(cd.radius);
		cd.density = 1;
		cd.friction = .1f;
		cd.restitution = 1f;
		_body.createShape(cd);
		_body.setMassFromShapes();
	}
	
	void display() {
		Vec2 pos = _world.getBodyPixelCoord(_body);
		float angle = _body.getAngle();
		_level.pushMatrix();
		_level.translate(pos.x, pos.y);
		_level.rotate(-angle);
		_level.fill(175);
		_level.stroke(0);
		_level.strokeWeight(1);
		_level.ellipse(0, 0, _radius*2, _radius*2);
		_level.line(0, 0, _radius, 0);
		_level.popMatrix();
	}

}
