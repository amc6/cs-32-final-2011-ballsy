package graphics;

import static ballsy.GeneralConstants.*;
import ballsy.Window;
import static ballsy.Window.*;

public class TextButton extends Text {
	

	private Window _window;
	private float _width, _height;
	private int _x, _y;
	private String _text;

	public TextButton(String text, int x, int y) {
		super(text, x, y);
		_window = Window.getInstance();

		_window.textFont(DEFAULT_FONT);
		_width = _window.textWidth(text);
		_height = DEFAULT_FONT_HEIGHT;
		_text = text;
		_x = x;
		_y = y;
	}
	

	public TextButton(String text, int x, int y, int color, int align) {
		super(text, x, y, color, align);
		_window = Window.getInstance();

		_window.textFont(DEFAULT_FONT);
		_width = _window.textWidth(text);
		_height = DEFAULT_FONT_HEIGHT;
		_text = text;
		_x = x;
		_y = y;
	}
	
	public void setSize(int size) {
		super.setSize(size);
		_height = _font.getFont().getSize();
	}
	
	public boolean mouseOver() {
		int x = _window.mouseX;
		int y = _window.mouseY;
		int xMin, xMax, yMin, yMax;
		switch(_textAlign) {
		case CORNER:
			xMin = _x;
			xMax = (int) (_x + _width);
			yMin = _y;
			yMax = (int) (_y + _height);
			break;
		default: //center
			xMin = (int) (_x - _width/2);
			xMax = (int) (_x + _width/2);
			yMin = (int) (_y - _height/2);
			yMax = (int) (_y + _height/2);
		}
		return xMin < x && x < xMax && yMin < y && y < yMax;
	}
	
	public void draw() {
		if (mouseOver()) {
			_window.fill(DEFAULT_FONT_ACTIVE);
			_window.textAlign(_textAlign);
			_window.textFont(DEFAULT_FONT_BIGGER);
			_window.text(_text, _x, _y);
		}
		else {
			super.draw();
		}
	}

}
