package bodies;

import org.dom4j.Element;

import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.Window;
import static bodies.BodyConstants.*;

public class RegularPolygon extends AbstractBody {
		
	public RegularPolygon(AbstractLevel level, PhysicsWorld world, float x, float y, int numSides, float sideLen){
		super(level, world, new physics.PhysicsRegularPolygon(world, x, y, numSides, sideLen, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS, true), new graphical.PolygonDef(DEFAULT_BODY_COLOR));
	}
	
	public Element writeXML() {
		return super.writeXML("regular_polygon");
	}
}
