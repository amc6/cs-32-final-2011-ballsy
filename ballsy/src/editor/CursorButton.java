package editor;

/**
 * Subclass for specific "select" button.
 */

import processing.core.PConstants;
import processing.core.PImage;

public class CursorButton extends AbstractButton {

	private PImage _image;
	
	public CursorButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);
		_image = _window.loadImage("res/cursor.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8);	
		_window.popMatrix();
	}

	public void select() {
		_level.setPlacemode(false);
	}
	
	public String tooltip(){
		if (_level.getSelected() != null){
			return "Drag to move the object, SHIFT and drag to rotate, Z and drag to resize, X to snap rotation, and D to delete.";
		}else{
			return "Click on an object to select or drag to pan the level.";
		}
	}


	@Override
	public void unselect() {
		_level.setPlacemode(true); // alter stuff in level editor
	}
	
}
