package graphics;

/**
 * Graphical representation of text, incorporating font!
 */

import ballsy.Window;
import processing.core.PConstants;
import processing.core.PFont;
import static ballsy.GeneralConstants.*;

public class Text {

	private Window _window;
	protected PFont _font = DEFAULT_FONT;
	protected int _textAlign = PConstants.CENTER;
	private int _x, _y;
	protected int _activeColor = DEFAULT_FONT_ACTIVE;
	protected int _inactiveColor = DEFAULT_FONT_INACTIVE;
	private String _text;

	/**
	 * Create the text with whatever string is desired
	 * @param text
	 * @param x
	 * @param y
	 */
	public Text(String text, int x, int y) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		// The font must be located in the sketch's 
		// "data" directory to load successfully
	}
	
	/**
	 * Set the size of the text.
	 * @param size
	 */
	public void setSize(int size) {
		switch (size) {
		case 0:
			_font = STUPID_FONT;
			break;
		case 30:
			_font = DEFAULT_FONT_SMALLER;
			break;
		case 38:
			_font = DEFAULT_FONT;
			break;
		case 40:
			_font = DEFAULT_FONT_BIGGER;
			break;
		case 72:
			_font = DEFAULT_FONT_TITLE;
			break;
		default:
			_font = DEFAULT_FONT;
		}
	}
	
	/**
	 * Set the font.
	 * @param font
	 */
	public void setFont(PFont font) {
		_font = font;
	}
	
	/**
	 * Another constructor, now with color!
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 */
	public Text(String text, int x, int y, int color) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		_inactiveColor = color;
	}
	
	/**
	 * And now with alignment!
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 * @param align
	 */
	public Text(String text, int x, int y, int color, int align) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		_inactiveColor = color;
		_textAlign = align;
	}
	
	
	public void setAlign(int align) {
		_textAlign = align;
	}
	
	public void setColor(int color) {
		_inactiveColor = color;
	}
	
	public void setColor(int gray, int alpha) {
		_inactiveColor = _window.color(gray, alpha);
	}
	
	public void setActiveColor(int color) {
		_activeColor = color;
	}
	
	public void draw() {
		_window.fill(_inactiveColor);
		_window.textAlign(_textAlign);
		_window.textFont(_font); 
		_window.text(_text, _x, _y);
		_window.textFont(STUPID_FONT);
	}
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		_text = text;
	}
}
