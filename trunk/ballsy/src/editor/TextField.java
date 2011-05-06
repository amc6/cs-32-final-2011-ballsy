package editor;

import interfascia.IFTextField;

public class TextField extends IFTextField {

	/**
	* creates an empty IFTextField with the specified label and with specified position and width.
	* @param argLabel the text field's label
	* @param argX the text field's X location on the screen, relative to the PApplet.
	* @param argY the text filed's Y location on the screen, relative to the PApplet.
	* @param argWidth the text field's width
	*/
	
	public TextField(String argLabel, int argX, int argY, int argWidth) {
		super(argLabel, argX, argY, argWidth);
	}
	
	
	
}
