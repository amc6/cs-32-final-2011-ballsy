package editor;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import processing.core.PConstants;
import processing.core.PImage;
import ballsy.AbstractLevel;
import ballsy.XMLUtil;

public class SaveButton extends AbstractButton {

	private PImage _image;
	
	public SaveButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);
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
		_clicked = false; 
	
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				try {
					JFileChooser fc = new JFileChooser();
					// Set the current directory
					File f = new File(new File("./levels").getCanonicalPath());
					fc.setCurrentDirectory(f);

					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();

						String filename = file.getName();
						if(filename.endsWith("xml")) {

							_level.setSave(filename.substring(0, filename.indexOf('.')));

							
						}

					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
		 
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "Save";
	}
	
}
