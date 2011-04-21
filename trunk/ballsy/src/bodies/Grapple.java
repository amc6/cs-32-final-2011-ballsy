package bodies;

import graphical.GraphicalDef;
import physics.PhysicsDef;
import physics.PhysicsWorld;
import ballsy.AbstractLevel;

import org.dom4j.Element;
import org.jbox2d.p5.*;


public class Grapple extends AbstractBody {

	public Grapple(AbstractLevel level, PhysicsWorld world,
			UserBall ball) {
		super(level, world, new physics.GrappleDef(world, true, ball), new graphical.GrappleDef(0, ball));
	}

	@Override
	public Element writeXML() {
		return null;
	}
	
	public void grapple() {
		((physics.GrappleDef) _physicsDef).grapple();
	}
	
	public void releaseGrapple() {
		((physics.GrappleDef) _physicsDef).releaseGrapple();
	}

	
	public void extendGrapple() {
		((physics.GrappleDef) _physicsDef).extendGrapple();
	}
	
	public void retractGrapple() {
		((physics.GrappleDef) _physicsDef).retractGrapple();
	}
}
