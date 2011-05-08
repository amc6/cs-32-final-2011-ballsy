package editor;

import processing.core.PConstants;
import processing.core.PImage;

public class PathButton extends AbstractButton {

	private PImage _image;
	
	public PathButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);
		_image = _window.loadImage("res/pathicon.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8);	
		_window.popMatrix();
	}



	public void select() {
		System.out.println("PATH!");
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "Click to create paths for objects to follow.";
	}
	
}
