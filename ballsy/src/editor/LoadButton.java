package editor;

import java.io.File;

import processing.core.PConstants;
import processing.core.PImage;
import javax.swing.*;

import ballsy.Window;

public class LoadButton extends AbstractButton {

	private PImage _image;
	
	public LoadButton(BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(factory, minX, minY, maxX, maxY);
	
		_image = _window.loadImage("res/loadicon.png");
		
		try { 
			  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) { 
			  e.printStackTrace();  
		} 

		
	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.imageMode(PConstants.CORNER);
		_window.image(_image, _minX+8, _minY+8, _maxX-_minX-16f, _maxY-_minY-16);	
		_window.popMatrix();
	}



	public void select() {
		System.out.println("LOAD!");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFileChooser fc = new JFileChooser();
				    // Set the current directory
				    File f = new File(new File("./levels").getCanonicalPath());
				    fc.setCurrentDirectory(f);
				    
					int returnVal = fc.showOpenDialog(null);
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
		return "Load";
	}
	
	
	
	
}
