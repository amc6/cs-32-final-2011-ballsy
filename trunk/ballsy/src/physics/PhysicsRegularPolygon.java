package physics;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

/**
 * Represents a regular polygon within the physics world.
 */

public class PhysicsRegularPolygon extends PhysicsPolygon {
	private int _numSides;
	private float _radius;
	public PhysicsRegularPolygon(float x, float y, int numSides, float r) {
		super(x, y, PhysicsRegularPolygon.calculateOffsets(numSides, r));
		_numSides = numSides;
		_radius = r;
	}
	
	/**
	 * Augments radius of regular polygon by an amount, a
	 * @param a
	 */
	public void resizeBy(float a) {
		ArrayList<Vec2> newPoints = PhysicsRegularPolygon.calculateOffsets(_numSides, _radius+a);
		//System.out.println("MSWEEP: " + angle);
		_radius = _radius+a;
		super.setPoints(newPoints);
	}
	
	/**
	 * @return the radius of the regular polygon
	 */
	public float getRadius() {
		return _radius;
	}
	
	/**
	 * Generates the points of a regular polygon, specifying the number of sides
	 * and the radius of the deired polygon.
	 */
	public static ArrayList<Vec2> calculateOffsets(int numSides, float radius){
	
		ArrayList<Vec2> offsets = new ArrayList<Vec2>();	
		for (int i = 1; i <= numSides; i++){

			Double angle = ((double) (i - 1)/numSides) * 2*Math.PI;;

			Double worldX = radius * Math.cos(angle);
			Double worldY = radius * Math.sin(angle);
			
			// Points should be as (dx,dy) from center of object
			Vec2 pos = new Vec2(worldX.floatValue(), worldY.floatValue());

			offsets.add(pos);
		}
		
		return offsets;
		
	}
}
