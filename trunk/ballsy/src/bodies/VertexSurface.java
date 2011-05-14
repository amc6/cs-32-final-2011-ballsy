package bodies;

/**
 * The Vertex Surface... ideally a platform without shape which the bodies
 * can interact with. Doesn't work for now, may be incorporated into future
 * versions of Ballsy.
 */

import java.util.ArrayList;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

public class VertexSurface extends AbstractBody {
		
	public VertexSurface(float x, float y, ArrayList<Vec2> worldPoints){
		this.setPhysicsAndGraphics(new physics.PhysicsVertexSurface(x,y,worldPoints), new graphics.GraphicsVertexSurface());
	}

	public Element writeXML() {
		return super.writeXML("vertex_surface");
	}
}
