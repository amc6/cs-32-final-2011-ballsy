package editor;

import processing.core.PConstants;
import processing.core.PImage;

public class SaveButton extends AbstractButton {

	private PImage _image;
	
	public SaveButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		_image = _window.loadImage("res/saveicon.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);	
		_window.popMatrix();
	}



	public void onClick() {
		System.out.println("SAVE!");
	}
	
	public String tooltip(){
		return "Save";
	}
	
}
