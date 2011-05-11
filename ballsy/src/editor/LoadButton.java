package editor;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import processing.core.PConstants;
import processing.core.PImage;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import ballsy.Window;
import ballsy.XMLUtil;

public class LoadButton extends AbstractButton {

	private PImage _image;
	private String _name;
	private boolean _load;
	
	public LoadButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(editor, factory, minX, minY, maxX, maxY);
	
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
			        
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String filename = file.getName();
						if(filename.endsWith("ball")) {
							_level.setLoad("levels/" + filename);
						}
						
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
		
		if (_load) {
			System.out.println("loading.. " + _name);
			System.out.println(XMLUtil.getInstance().readFile(_level, _name));
			_level.play();
			_level.stop(); //lol this gets rid of crosshair and smoke. we should probs do it more directly iunno.
			_level.centerCamera();
			_load = false;
		}
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "Click to load a saved level.";
	}
	
}
