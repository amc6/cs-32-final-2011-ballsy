package physics;

import org.dom4j.Element;

public class GrappleDef extends PhysicsDef {

	public GrappleDef(PhysicsWorld world, boolean mobile) {
		super(world, mobile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Element writeXML() {
		return null; // never write XML for a grapple!
	}
}
