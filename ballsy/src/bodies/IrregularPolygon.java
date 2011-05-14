package bodies;

/**
 * Body wrapper for physics and graphics defs of the irregular polygon.
 */

import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import graphics.GraphicsPolygon;
import java.util.ArrayList;
import org.dom4j.Element;
import org.jbox2d.common.Vec2;
import physics.PhysicsPolygon;

public class IrregularPolygon extends AbstractBody {
		
	public IrregularPolygon(float x, float y, ArrayList<Vec2> worldOffsets){
		PhysicsPolygon physics = new PhysicsPolygon(x, y, worldOffsets);
		GraphicsPolygon graphics = new GraphicsPolygon(DEFAULT_BODY_COLOR);
		this.setPhysicsAndGraphics(physics, graphics);
	
	}
	
	public Element writeXML() {
		return super.writeXML("irregular_polygon");
	}
}
