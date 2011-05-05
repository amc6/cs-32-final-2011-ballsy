package editor;

import processing.core.PConstants;
import processing.core.PImage;

public class NewButton extends AbstractButton {

	private PImage _image;
	
	public NewButton(BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(factory, minX, minY, maxX, maxY);
	
		_image = _window.loadImage("res/newicon.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);	
		_window.popMatrix();
	}



	public void select() {
		System.out.println("NEW!");
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "New Level";
	}
	
}
