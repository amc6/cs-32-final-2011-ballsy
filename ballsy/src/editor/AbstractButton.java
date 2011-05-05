package editor;

import interfascia.GUIController;
import processing.core.PFont;
import ballsy.Window;

public abstract class AbstractButton {

	protected int _minX, _minY, _maxX, _maxY;
	protected Window _window;
	protected boolean _clicked;
	protected GUIController _customController;
	
	public AbstractButton(int minX, int minY, int maxX, int maxY) {
		_window = Window.getInstance();
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		_clicked = false;
		_customController = new GUIController(_window);
	}

	public void display() {
		_window.stroke(EditorConstants.BUTTON_BORDER_COLOR);
		_window.strokeWeight(EditorConstants.BUTTON_BORDER_WIDTH);
		_window.strokeJoin(_window.MITER);
		
		if (_clicked){
			_window.fill(EditorConstants.BUTTON_ACTIVE_COLOR);
		}else{
			if (this.mouseInBounds(_window.mouseX, _window.mouseY)){
				_window.fill(EditorConstants.BUTTON_HOVER_COLOR);
			}else{
				_window.fill(EditorConstants.BUTTON_INACTIVE_COLOR);
			}
		}
		
	
		
		_window.rect(_minX, _minY, _maxX - _minX, _maxY - _minY);
//		_window.strokeWeight(1);
			
		

	}
	
	public boolean isClicked() {
		return _clicked;
	}
	
	public void displayTooltip(){
		if (this.mouseInBounds(_window.mouseX, _window.mouseY)){
			float length = _window.textWidth(this.tooltip());
			
			_window.fill(EditorConstants.TOOLTIP_BG);
			_window.rect(_window.mouseX+10, _window.mouseY+10, length/2.5f + 16f, 22);
			_window.fill(255); // white
			_window.textFont(EditorConstants.TOOLTIP_FONT);
			_window.text(this.tooltip(),_window.mouseX+16,_window.mouseY+25);
		}
	}
		
	public void setActive(boolean active) {
		if (active){
			_clicked = true;
			this.onClick();
		}else{
			_clicked = false;
		}
	}
	
	public boolean mouseInBounds(int mouseX, int mouseY) {
		return mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY;
	}
	
	/**
	 * To be filled by subclass.
	 */
	public abstract void onClick();
	
	public abstract String tooltip();
	
}
