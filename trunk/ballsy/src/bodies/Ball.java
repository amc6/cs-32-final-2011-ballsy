package bodies;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import ballsy.AbstractLevel;
import ballsy.Window;
import physics.PhysicsWorld;
import static bodies.BodyConstants.*;

public class Ball extends AbstractBody {
		
	public Ball(AbstractLevel level, PhysicsWorld world, float centerX, float centerY){
		super(level, world, new physics.PhysicsBall(world, centerX, centerY, DEFAULT_BALL_RADIUS, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS, true), new graphical.BallDef(DEFAULT_BODY_COLOR));
	}
	
	public Ball(AbstractLevel level, PhysicsWorld world, float centerX, float centerY, float radius, boolean mobile){
		super(level, world, new physics.PhysicsBall(world, centerX, centerY, radius, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS, mobile), new graphical.BallDef(DEFAULT_BODY_COLOR));
	}
	@Override
	public Element writeXML() {
		return super.writeXML("ball");
	}

}
