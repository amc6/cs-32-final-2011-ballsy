package bodies;

import static bodies.BodyConstants.DEFAULT_BODY_BOUNCINESS;
import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import static bodies.BodyConstants.DEFAULT_BODY_DENSITY;
import static bodies.BodyConstants.DEFAULT_BODY_FRICTION;

import java.util.ArrayList;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import ballsy.AbstractLevel;

public class IrregularPolygon extends AbstractBody {
		
	public IrregularPolygon(AbstractLevel level, PhysicsWorld world, float x, float y, ArrayList<Vec2> worldOffsets){
		super(level, world, new physics.PhysicsPolygonDef(world, x, y, worldOffsets, DEFAULT_BODY_DENSITY, DEFAULT_BODY_FRICTION, DEFAULT_BODY_BOUNCINESS, true), new graphical.PolygonDef(DEFAULT_BODY_COLOR));
	}
	
	public Element writeXML() {
		return super.writeXML("irregular_polygon");
	}
}
