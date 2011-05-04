package editor;

import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.common.Vec2;

import processing.core.PApplet;

public class PointMath {

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
	
	public static Vec2 getCenter(ArrayList<Vec2> points) {
		float xSum = 0;
		float ySum = 0;
		for (Vec2 point : points) {
			xSum += point.x;
			ySum += point.y;
		}
		return new Vec2(xSum/points.size(), ySum/points.size());
	}
	
	public static ArrayList<Vec2> sortCCW(ArrayList<Vec2> array, Vec2 anchor){
		
		// If array is empty, return as base case
		if (array.isEmpty())
			return array;
		
		Random random = new Random();
		int pivotIndex = random.nextInt(array.size());
//		pivotIndex = array.size()/2; // temp
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

	
	public static void main(String[] args) {
		ArrayList<Vec2> points = new ArrayList<Vec2>();
//		points.add(new Vec2(5,0));
//		points.add(new Vec2(7.4f, 1));
//		points.add(new Vec2(2.5f,2.5f));
//		points.add(new Vec2(5,5));
//		points.add(new Vec2(7.5f,2.5f));
//		points.add(new Vec2(4,5.1f));
		
		points.add(new Vec2(-34.8f,4.1f));
		points.add(new Vec2(-51.5f,-1.2f));
		points.add(new Vec2(-32.9f,-15.6f));
		points.add(new Vec2(-1.4f,18.3f));
		
		Vec2 anchor = PointMath.getCenter(points);
		ArrayList<Vec2> sortedPoints = PointMath.sortCCW(points, anchor);
		
		for (int i = 0; i < sortedPoints.size(); i++){
			System.out.println(sortedPoints.get(i));
		}
		
		System.out.println("Is convex: " + PointMath.isConvex(sortedPoints));
		
	}
	
	
}
