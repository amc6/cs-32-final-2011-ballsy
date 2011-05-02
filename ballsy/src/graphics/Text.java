package graphics;

import ballsy.Window;
import processing.core.PFont;
import static ballsy.GeneralConstants.*;

public class Text {

	private Window _window;
	private PFont _font = DEFAULT_FONT;
	protected int _textAlign = _window.CENTER;
	private int _x, _y;
	private int _color = DEFAULT_FONT_INACTIVE;
	private String _text;

	public Text(String text, int x, int y) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		// The font must be located in the sketch's 
		// "data" directory to load successfully
	}
	
	public void setSize(int size) {
		switch (size) {
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
	
	public Text(String text, int x, int y, int color) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		_color = color;
	}
	
	public Text(String text, int x, int y, int color, int align) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		_color = color;
		_textAlign = align;
	}
	
	
	public void setAlign(int align) {
		_textAlign = align;
	}
	
	public void setColor(int color) {
		_color = color;
	}
	
	public void draw() {
		_window.fill(_color);
		_window.textAlign(_textAlign);
		_window.textFont(_font); 
		_window.text(_text, _x, _y);
	}
}
