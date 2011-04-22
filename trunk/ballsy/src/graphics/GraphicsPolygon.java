package graphics;

import java.util.List;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsPolygon;
import processing.core.PConstants;
import ballsy.Window;

public class GraphicsPolygon extends GraphicsDef {

	public GraphicsPolygon(int color) {
		this.setColor(color);
	}

	@Override
	public void display() {
		
		Window window = Window.getInstance();
		
		PhysicsPolygon polygonDef = (PhysicsPolygon) _physicsDef;
		List<Vec2> vertices = polygonDef.getPointOffsets();
		
		Vec2 pos = _world.getBodyPixelCoord(_physicsDef.getBody());

		float a = _physicsDef.getBody().getAngle();

		window.pushMatrix();
		window.beginShape();
		window.translate(pos.x,pos.y);
		window.rotate(-a);
		window.fill(this.getColor());
		for (Vec2 vert : vertices){
			
			window.vertex(_world.scalarWorldToPixels(vert.x), _world.scalarWorldToPixels(vert.y)); // -y for some reason
		}
		
		window.endShape(PConstants.CLOSE);

		window.popMatrix();
	}

	/**
	 * Override of super no-parameter writeXML(), makes a call to super's writeXML(String type) 
	 * with the proper type of this subclass
	 */
	public Element writeXML() {
		// return the XML element as designated in the super, with the appropriate type.
		return super.writeXML("polygon");
	}
	
}
