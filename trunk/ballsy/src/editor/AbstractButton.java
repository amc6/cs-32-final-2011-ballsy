package editor;

import processing.core.PConstants;
import ballsy.Window;

public abstract class AbstractButton {

	protected int _minX, _minY, _maxX, _maxY;
	protected Window _window;
	protected boolean _clicked;
	private int _color, _borderColor, _inactiveColor, _activeColor, _inactiveBorderColor, _activeBorderColor;
	
	public AbstractButton(int minX, int minY, int maxX, int maxY) {
		_window = Window.getInstance();
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
	

	public void display() {
		_window.stroke(EditorConstants.BUTTON_BORDER_COLOR);
		_window.strokeWeight(2);
		
		if (_clicked){
			_window.fill(EditorConstants.BUTTON_ACTIVE_COLOR);
		}else{
			if (this.mouseInBounds()){
				_window.fill(EditorConstants.BUTTON_HOVER_COLOR);
			}else{
				_window.fill(EditorConstants.BUTTON_INACTIVE_COLOR);
			}
		}
		
		_window.rect(_minX, _minY, _maxX - _minX, _maxY - _minY);
		_window.strokeWeight(1);
			

	}
		
	public void click() {
		if (mouseInBounds()) {
			_clicked = true;
		}
		this.onClick();
	}
	
	public boolean mouseInBounds() {
		return _window.mouseX > _minX && _window.mouseX < _maxX && _window.mouseY > _minY && _window.mouseY < _maxY;

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
