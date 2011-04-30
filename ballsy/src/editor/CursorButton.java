package editor;

import processing.core.PConstants;
import processing.core.PImage;

public class CursorButton extends AbstractButton {

	private PImage _image;
	
	public CursorButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		_image = _window.loadImage("res/cursor.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8);	
		_window.popMatrix();
	}



	public void onClick() {
		System.out.println("CURSOR!");
	}
	
	public String tooltip(){
		return "Click to select objects in the level.";
	}
	
}
