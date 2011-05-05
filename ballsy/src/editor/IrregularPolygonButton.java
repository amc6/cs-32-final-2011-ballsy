package editor;

import org.jbox2d.common.Vec2;

import processing.core.PConstants;

public class IrregularPolygonButton extends AbstractButton {

	public IrregularPolygonButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);

	}


	public void display() {
		super.display();
		_window.fill(EditorConstants.IRREGULAR_POLYGON_COLOR);	
		_window.beginShape();
		_window.vertex(_minX+12, _maxY - 12);
		_window.vertex(_minX+12, (_maxY+_minY)/2);
		_window.vertex((_maxX+_minX)/2, _minY+12);
		_window.vertex(_maxX-12, (_maxY+_minY)/2);
		_window.vertex(_maxX-12, _maxY - 12);		
		_window.endShape(PConstants.CLOSE);

	}



	public void onClick() {
		System.out.println("IRREGULAR POLYGON!");
	}
	
	public String tooltip(){
		return "Click to create irregular, convex polygons in the level.";
	}
	
}