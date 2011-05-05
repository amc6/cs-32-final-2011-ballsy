package editor;

import javax.swing.JFileChooser;

import ballsy.Window;

import processing.core.PConstants;
import processing.core.PImage;

public class SaveButton extends AbstractButton {

	private PImage _image;
	private JFileChooser _fileChooser;

	
	public SaveButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		_image = _window.loadImage("res/saveicon.png");
		_fileChooser = new JFileChooser(); 

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
		_fileChooser.showSaveDialog(Window.getInstance());
		_clicked = false; // make button inactive
	}
	
	public String tooltip(){
		return "Save";
	}
	
}
