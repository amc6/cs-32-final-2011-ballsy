package graphical;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

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
		
		Vec2 pos = _world.coordWorldToPixels(_physicsDef.getBodyWorldCenter());
		float a = _physicsDef.getBody().getAngle();
		
		System.out.println("center: " + pos);
		
		window.pushMatrix();
		window.beginShape();
		window.translate(pos.x,pos.y);
		window.rotate(-a);
		window.fill(_color);
		for (Vec2 vert : vertices){
			
			window.vertex(_world.scalarWorldToPixels(vert.x), _world.scalarWorldToPixels(vert.y));
		}
		
		window.endShape(PConstants.CLOSE);

		window.popMatrix();
		
		//System.out.println("never");
		
	}

	
}
