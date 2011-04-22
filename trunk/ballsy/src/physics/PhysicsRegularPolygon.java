package physics;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

public class PhysicsRegularPolygon extends PhysicsPolygon {

	public PhysicsRegularPolygon(float x, float y, int numSides, float r) {
		super(x, y, PhysicsRegularPolygon.calculateOffsets(numSides, r));
	}
	
	public static ArrayList<Vec2> calculateOffsets(int numSides, float radius){
	
		ArrayList<Vec2> offsets = new ArrayList<Vec2>();	
		for (int i = 1; i <= numSides; i++){

			Double angle = ((double) (i - 1)/numSides) * 2*Math.PI;

			Double worldX = radius * Math.cos(angle);
			Double worldY = radius * Math.sin(angle);
			
			// Points should be as (dx,dy) from center of object
			Vec2 pos = new Vec2(worldX.floatValue(), worldY.floatValue());

			offsets.add(pos);
		}
		
		return offsets;
		
	}
}
