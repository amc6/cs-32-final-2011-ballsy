package editor;

/**
 * A button within our Editor GUI. This class handles display (of clicked/unclicked
 * buttons) as well as their tooltips and actions.
 */

import static editor.EditorConstants.INFO;
import interfascia.GUIController;
import ballsy.AbstractLevel;
import ballsy.Window;

public abstract class AbstractButton {

	protected int _minX, _minY, _maxX, _maxY;
	protected Window _window;
	protected boolean _clicked;
	protected GUIController _customController;
	protected EditorLevel _level = (EditorLevel) AbstractLevel.getInstance();
	protected BodyFactory _factory;
	protected LevelEditor _editor;
	
	public AbstractButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {
		_editor = editor;
		_factory = factory;
		_window = Window.getInstance();
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		_clicked = false;
		_customController = new GUIController(_window,false);
	}

	/**
	 * Display with whatever properties currently appropriate.
	 */
	public void display() {
		_window.stroke(EditorConstants.BUTTON_BORDER_COLOR);
		_window.strokeWeight(EditorConstants.BUTTON_BORDER_WIDTH);
		
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
	}
	
	/** accessor **/
	public boolean isClicked() {
		return _clicked;
	}
	
	/**
	 * Display the tooltip of the button.
	 */
	public void displayTooltip(){
		_editor.setErrorMessage(this.tooltip(), INFO);
	}
	
	/**
	 * Set active... if clicked or needed to be otherwise active (see selection arrow)
	 * @param active
	 */
	public void setActive(boolean active) {
		if (active){
			_clicked = true;
			this.select();
		}else{
			_clicked = false;
			this.unselect();
		}
	}
	
	/**
	 * check if the mouse is within the button. Helper method.
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean mouseInBounds(int mouseX, int mouseY) {
		return mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY;
	}
	
	/**
	 * To be filled by subclass.
	 */
	public abstract void select();
	
	public abstract void unselect();
	
	public abstract String tooltip();
	
}
