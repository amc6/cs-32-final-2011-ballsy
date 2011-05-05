package editor;

import processing.core.PConstants;
import processing.core.PImage;
import javax.swing.*;

import ballsy.Window;

public class LoadButton extends AbstractButton {

	private PImage _image;
	private JFileChooser _fileChooser;
	
	public LoadButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		_image = _window.loadImage("res/loadicon.png");
		
		try { 
			  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) { 
			  e.printStackTrace();  
		} 

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
		System.out.println("LOAD!");
		int result = _fileChooser.showOpenDialog(Window.getInstance());

		// Determine which button was clicked to close the dialog
		switch (result) {
		  case JFileChooser.APPROVE_OPTION:
		    // Approve (Open or Save) was clicked
		    break;
		  case JFileChooser.CANCEL_OPTION:
		    // Cancel or the close-dialog icon was clicked
		    break;
		  case JFileChooser.ERROR_OPTION:
		    // The selection process did not complete successfully
		    break;
		}
		
		_clicked = false; // make button inactive
	}
	
	public String tooltip(){
		return "Load";
	}
	
	
	
	
}
