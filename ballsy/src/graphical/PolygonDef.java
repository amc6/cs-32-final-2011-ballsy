package graphical;

import java.util.List;

import org.jbox2d.common.Vec2;

import physics.PhysicsPolygonDef;
import processing.core.PConstants;
import ballsy.Window;

public class PolygonDef extends GraphicalDef {

	public PolygonDef(int color) {
		super(color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void display() {
		
		Window window = Window.getInstance();
		
		PhysicsPolygonDef polygonDef = (PhysicsPolygonDef) _physicsDef;
		List<Vec2> vertices = polygonDef.getPointOffsets();
		
		Vec2 pos = _world.getBodyPixelCoord(_physicsDef.getBody());

		float a = _physicsDef.getBody().getAngle();
			
		window.pushMatrix();
		window.beginShape();
		window.translate(pos.x,pos.y);
		window.rotate(-a);
		window.fill(_color);
		for (Vec2 vert : vertices){
			
			window.vertex(_world.scalarWorldToPixels(vert.x), -_world.scalarWorldToPixels(vert.y)); // -y for some reason
		}
		
		window.endShape(PConstants.CLOSE);

		window.popMatrix();
		
		//System.out.println("never");
		
	}

	
}
