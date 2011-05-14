package editor;

/**
 * Class which handles points in the creation of a new irregular polygon. They
 * must be in clockwise order and convex, so this class makes sure that happens.
 */

import java.util.ArrayList;
import java.util.Random;
import org.jbox2d.common.Vec2;

public class PointMath {
	/**
	 * Comparision method.s
	 * @param anchor
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static int compare(Vec2 anchor, Vec2 pointA, Vec2 pointB){
		float ax = pointA.x - anchor.x;
		float ay = pointA.y - anchor.y;
		float bx = pointB.x - anchor.x;
		float by = pointB.y - anchor.y;
		
		float areaTrapezoid = ax*by - ay*bx;
		
		if (areaTrapezoid > 0.0)
			return 1;
		else if (areaTrapezoid == 0.0)
			return 0;
		else
			return -1;
	}
	
	/**
	 * Gets the center of a set of points.
	 * @param points
	 * @return
	 */
	public static Vec2 getCenter(ArrayList<Vec2> points) {
		float xSum = 0;
		float ySum = 0;
		for (Vec2 point : points) {
			xSum += point.x;
			ySum += point.y;
		}
		return new Vec2(xSum/points.size(), ySum/points.size());
	}
	
	/**
	 * Sort a set of points around an anchor in counter-clockwise order.
	 * @param array
	 * @param anchor
	 * @return
	 */
	public static ArrayList<Vec2> sortCCW(ArrayList<Vec2> array, Vec2 anchor){
		
		// If array is empty, return as base case
		if (array.isEmpty())
			return array;
		
		Random random = new Random();
		int pivotIndex = random.nextInt(array.size());
		Vec2 pivot = array.get(pivotIndex);
		array.remove(pivotIndex);
		
		ArrayList<Vec2> less = new ArrayList<Vec2>();
		ArrayList<Vec2> greater = new ArrayList<Vec2>();
		
		for (Vec2 point : array){
			if (PointMath.compare(anchor, point, pivot) == 1)
				less.add(point);
			else
				greater.add(point);
		}
		
		ArrayList<Vec2> recursiveLess = PointMath.sortCCW(less, anchor);
		ArrayList<Vec2> recursiveGreater = PointMath.sortCCW(greater, anchor);
		
		ArrayList<Vec2> toReturn = new ArrayList<Vec2>();
		toReturn.addAll(recursiveLess);
		toReturn.add(pivot);
		toReturn.addAll(recursiveGreater);
		
		return toReturn;
		
	}
	
	/**
	 * test if a set of points are convex
	 * @param points
	 * @return
	 */
	public static boolean isConvex(ArrayList<Vec2> points){
		Boolean allPositive = null;
		
		for (int i = 0; i < points.size(); i++){
			
			int index = (i-1) % points.size();
			if (index < 0) index = points.size() + index; // because java does % wrong for negatives
			Vec2 first = points.get(index);

			Vec2 second = points.get(i);
			
			Vec2 third = points.get((i+1)%points.size());
			
			float dot = ((second.x-first.x)*(third.y-first.y)) - ((second.y-first.y)*(third.x-first.x));
			
			if (allPositive == null && dot > 0.0){
				allPositive = true;
				continue;
			}else if (allPositive == null && dot < 0.0){
				allPositive = false;
				continue;
			}else if (allPositive == null){
				continue;
			}
			
			
			if (allPositive && dot < 0.0)
				return false;
			else if (!allPositive && dot > 0.0)
				return false;
		}
		
		return true;
	}
}
