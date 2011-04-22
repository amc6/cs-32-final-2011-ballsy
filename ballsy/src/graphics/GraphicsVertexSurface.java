package graphics;

import java.util.List;

import org.dom4j.Element;
import org.jbox2d.common.Vec2;

import physics.PhysicsVertexSurface;
import ballsy.Window;
import static bodies.BodyConstants.*;

public class GraphicsVertexSurface extends GraphicsDef {

	@Override
	public void display() {
		
		Window window = Window.getInstance();
		
		PhysicsVertexSurface surfaceDef = (PhysicsVertexSurface) _physicsDef;
		List<Vec2> vertices = surfaceDef.getWorldPoints();
		
		//Vec2 pos = _world.coordWorldToPixels(_physicsDef.getBodyWorldCenter());
		//float a = _physicsDef.getBody().getAngle();
		
		//System.out.println("center: " + pos);
		
		window.pushMatrix();
		window.beginShape();
		window.stroke(this.getColor());
		window.strokeWeight(DEFAULT_SURFACE_STROKE);
		window.noFill();
		for (Vec2 vert : vertices){
			Vec2 pixelVec = _world.coordWorldToPixels(vert);
			window.vertex(pixelVec.x, pixelVec.y);
		}
		
		window.endShape();

		window.popMatrix();
		
		//System.out.println("never");
		
	}
	
	public Element writeXML() {
		return super.writeXML("vertex_surface");
	}
	
}
