package editor;

import org.jbox2d.common.Vec2;

import processing.core.PConstants;

public class TriangleButton extends AbstractButton {

	public TriangleButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		this.setActiveColors(255, 200);
		this.setInactiveColors(240, 190);

	}


	public void display() {
		super.display();
		_window.fill(EditorConstants.TRIANGLE_COLOR);	
		_window.beginShape();
		_window.vertex(_minX+12, _maxY - 12);
		_window.vertex((_maxX+_minX)/2, _minY+12);
		_window.vertex(_maxX-12, _maxY - 12);		
		_window.endShape(PConstants.CLOSE);
		
	}



	public void onClick() {
		System.out.println("RECTANGLE!");
	}
	
}
