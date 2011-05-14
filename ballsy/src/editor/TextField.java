package editor;

/**
 * TextField for level editor GUI
 */

import interfascia.IFTextField;

public class TextField extends IFTextField {

	private LevelEditor _editor;
	
	/**
	* creates an empty IFTextField with the specified label and with specified position and width.
	* @param argLabel the text field's label
	* @param argX the text field's X location on the screen, relative to the PApplet.
	* @param argY the text filed's Y location on the screen, relative to the PApplet.
	* @param argWidth the text field's width
	*/
	public TextField(String argLabel, int argX, int argY, int argWidth, LevelEditor editor) {
		super(argLabel, argX, argY, argWidth);
		_editor = editor;
	}
	
	@Override
	public void loseFocus(){
		_editor.focusLost(this);
	}
	
}
