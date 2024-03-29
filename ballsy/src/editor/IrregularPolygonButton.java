package editor;

/**
 * Subclass for specific "create irregular polygon" button.
 */

import processing.core.PConstants;

public class IrregularPolygonButton extends AbstractButton {

	public IrregularPolygonButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);
	}


	public void display() {
		super.display();
		_window.fill(EditorConstants.IRREGULAR_POLYGON_COLOR);	
		// the shape for the button
		_window.beginShape();
		_window.vertex(_minX+12, _maxY - 12);
		_window.vertex(_minX+12, (_maxY+_minY)/2);
		_window.vertex((_maxX+_minX)/2, _minY+12);
		_window.vertex(_maxX-12, (_maxY+_minY)/2);
		_window.vertex(_maxX-12, _maxY - 12);		
		_window.endShape(PConstants.CLOSE);

	}

	public void select() {
		_level.setPlacemode(true);
		_level.startPoints();
		
	}	

	@Override
	public void unselect() {
		_level.clearPoints();
	}
	
	public String tooltip(){
		return "Click to create irregular, convex polygons in the level.";
	}
}
