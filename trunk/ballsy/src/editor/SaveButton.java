package editor;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import ballsy.XMLUtil;

import processing.core.PConstants;
import processing.core.PImage;

public class SaveButton extends AbstractButton {

	private PImage _image;
	private String _name;
	private boolean _save = false;
	
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

		Runnable saveTask = new Runnable() {
//			public String fileName;
//			public boolean done = false;
			
			public void run() {
				try {
					JFileChooser fc = new JFileChooser();
				    // Set the current directory
				    File f = new File(new File("./levels").getCanonicalPath());
				    fc.setCurrentDirectory(f);
				    
					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
//						boolean executeOperation = false;
//						if(file.getName().endsWith("jpg") || file.getName().endsWith("gif")){
//							//temp = loadImage(file.getPath());
//
//						}
						String filename = file.getName();
						if(filename.endsWith("xml")) {
//							System.out.println(file.getName());
							_name = filename.substring(0, filename.indexOf('.'));
							_save = true;
						}
						
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		try { SwingUtilities.invokeAndWait(saveTask);} 
		catch (InterruptedException e) { e.printStackTrace(); } 
		catch (InvocationTargetException e) { e.printStackTrace();}
		
		_clicked = false; // make button inactive
		if (_save) { // because we need this in the OG thread yo.
			save(_name);
			_save = false;
		}
	}
	
	/**
	 * 
	 * @param name Name of level
	 */
	public void save(String name) {
		String pathname = "levels/" + name + ".xml";
		String thumbPathname = "levels/thumbs/" + name + ".png";
		XMLUtil.getInstance().writeFile(_level, pathname);
		_level.makeThumbnail(thumbPathname);
		XMLUtil.getInstance().addMenuButton(pathname, thumbPathname);
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "Save";
	}
	
}
