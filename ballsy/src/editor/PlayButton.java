package editor;

import processing.core.PConstants;
import processing.core.PImage;

public class PlayButton extends AbstractButton {

	private PImage _image;
	private PImage _stopImage;
	
	public PlayButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		_image = _window.loadImage("res/playicon.png");
		_stopImage = _window.loadImage("res/stopicon.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		if (!_clicked){
			_window.image(_image, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);
		}else{
			_window.image(_stopImage, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);
		}			
		_window.popMatrix();
	}



	public void onClick() {
		System.out.println("PLAY!");
	}
	
	public String tooltip(){
		return "Play";
	}
	
}
