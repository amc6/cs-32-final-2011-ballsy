package editor;

/**
 * Subclass for specific "create new level" button.
 */

import processing.core.PConstants;
import processing.core.PImage;

public class NewButton extends AbstractButton {

	private PImage _image;
	
	public NewButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);
	
		_image = _window.loadImage("res/newicon.png");

	}

	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);	
		_window.popMatrix();
	}

	/**
	 * clears the level to make a new one.
	 */
	public void select() {
		_editor.clear();
		_clicked = false;
	}
	

	@Override
	public void unselect() { }
	
	public String tooltip(){
		return "Click to start from a blank level. You will lose all unsaved changes.";
	}
	
}
