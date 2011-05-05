package editor;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import processing.core.PConstants;
import processing.core.PImage;

public class SaveButton extends AbstractButton {

	private PImage _image;
	
	public SaveButton(BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(factory, minX, minY, maxX, maxY);
		_image = _window.loadImage("res/saveicon.png");

	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);	
		_window.popMatrix();
	}



	public void select() {
		System.out.println("SAVE!");
		
//		_fileChooser.showSaveDialog(null);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
//						File file = fc.getSelectedFile();
//						boolean executeOperation = false;
//						if(file.getName().endsWith("jpg") || file.getName().endsWith("gif")){
//							//temp = loadImage(file.getPath());
//
//						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		_clicked = false; // make button inactive
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "Save";
	}
	
}
