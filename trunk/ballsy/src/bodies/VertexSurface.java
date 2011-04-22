package bodies;

import java.util.ArrayList;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

public class VertexSurface extends AbstractBody {
		
	public VertexSurface(ArrayList<Vec2> worldPoints){
		this.setPhysicsAndGraphics(new physics.PhysicsVertexSurface(0,0,worldPoints), new graphics.GraphicsVertexSurface());
	}

	public Element writeXML() {
		return super.writeXML("vertex_surface");
	}
}
