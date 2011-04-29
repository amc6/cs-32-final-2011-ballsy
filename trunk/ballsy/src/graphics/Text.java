package graphics;

import ballsy.Window;
import processing.core.PFont;

public class Text {

	private Window _window;
	private PFont _font;
	private int _textAlign = _window.CORNER;
	private int _x, _y;
	private int _color = 100;
	private String _text;

	public Text(String text, int x, int y) {
		_window = Window.getInstance();
		_text = text;
		_x = x;
		_y = y;
		// The font must be located in the sketch's 
		// "data" directory to load successfully
		_font = _window.loadFont("StrandedBRK-48.vlw"); 

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
