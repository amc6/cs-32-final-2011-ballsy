package editor;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import processing.core.PConstants;
import processing.core.PImage;

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
					FileFilter type = new ExtensionFilter("Ballsy Level (*.ball)", ".ball");
					  
					fc.setAcceptAllFileFilterUsed(false);
					fc.addChoosableFileFilter(type);
			        fc.setFileFilter(type);
			        
					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();

						String filename = file.getName();
						if(!filename.endsWith(".ball")){
							filename = filename.concat(".ball");
						}
						
						_level.setSave(filename);

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
		System.out.println("tooltip here");
		return "Click to save your level.";
	}
	
}
