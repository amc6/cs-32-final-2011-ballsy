package graphics;

import ballsy.Window;

public abstract class SampleButton {

	protected int _minX, _minY, _maxX, _maxY;
	protected Window _canvas;
	protected boolean _clicked;
	private int _color, _borderColor, _inactiveColor, _activeColor, _inactiveBorderColor, _activeBorderColor;
	
	public SampleButton(int minX, int minY, int maxX, int maxY) {
		_canvas = Window.getInstance();
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		_clicked = false;
		_inactiveColor = 150;
		_inactiveBorderColor = 100;
		_activeColor = 200;
		_activeBorderColor = 150;
		
		_color = _inactiveColor;
		_borderColor = _inactiveBorderColor;
	}
	
	public void setActiveColors(int color, int borderColor) {
		_activeColor = color;
		_activeBorderColor = borderColor;
	}
	
	public void setInactiveColors(int color, int borderColor) {
		_inactiveColor = color;
		_inactiveBorderColor = borderColor;
	}
	
	public void setActive() {
		_color = _activeColor;
		_borderColor = _activeBorderColor;
		
		_canvas.stroke(_borderColor);
		_canvas.fill(_color);
	}
	
	public void setInactive() {
		_color = _inactiveColor;
		_borderColor = _inactiveBorderColor;
		
		_canvas.stroke(_borderColor);
		_canvas.fill(_color);
	}
	
	public void display() {
		if (_clicked) { //if mouse clicked in box ...
			if (mouseInBounds(_canvas.mouseX, _canvas.mouseY)) { //...and is still in box
				//draw rectangle
//				_canvas.stroke(BORDER_COLOR);
//				_canvas.fill(BACKGROUND_COLOR);
//				_canvas.rect(_minX, _minY, PALETTE_BUTTON_WIDTH, PALETTE_BUTTON_WIDTH);
				setActive();
			}
		}
		else {
			setInactive();
		}
		_canvas.stroke(_borderColor);
		_canvas.fill(_color);
	}
	
	public void displayBox() {
//		_canvas.stroke(BORDER_COLOR);
//		_canvas.fill(BACKGROUND_COLOR);
//		_canvas.rect(_minX, _minY, PALETTE_BUTTON_WIDTH, PALETTE_BUTTON_WIDTH);
	}
	
	public void click() {
		if (mouseInBounds(_canvas.mouseX, _canvas.mouseY)) {
			_clicked = true;
		}
	}
	
	public boolean mouseInBounds(int mouseX, int mouseY) {
		return mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY;

	}
	
	public void release(int mouseX, int mouseY) {
		if (_clicked) {
			if (mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY) {
				onClick();
			}
			_clicked = false;
		}
	}
	
	/**
	 * To be filled by subclass.
	 */
	public abstract void onClick();
	
}
