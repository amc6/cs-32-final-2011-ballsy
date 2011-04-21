package bodies;

import graphical.GraphicalDef;
import physics.PhysicsDef;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import org.jbox2d.p5.*;


public class Grapple extends AbstractBody {

	public Grapple(AbstractLevel level, PhysicsWorld world,
			UserBall ball) {
		super(level, world, new physics.GrappleDef(world, true), new graphical.GrappleDef(0, ball));
		
		
	}
	

}
