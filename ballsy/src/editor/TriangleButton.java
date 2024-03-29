package editor;

/**
 * Subclass for specific "create ball" button.
 */

import processing.core.PConstants;

public class TriangleButton extends AbstractButton {

	public TriangleButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);

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

	public void select() {
		_level.setPlacemode(true);
		_factory.setBody(BodyFactory.RPOLY);
	}

	@Override
	public void unselect() { }
	
	public String tooltip(){
		return "Click to create regular polygons in the level.";
	}
	
}
