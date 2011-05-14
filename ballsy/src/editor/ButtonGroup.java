package editor;

/**
 * A button group, as in swing. Helpful for passing clicks down, for now we don't
 * have to check for containment of clicks in buttons from the highest level of the
 * level editor, that can be delegated to button groups.
 */

import java.util.ArrayList;

import ballsy.Window;

public class ButtonGroup {
	
	private ArrayList<AbstractButton> _buttons;
	
	public ButtonGroup(){
		_buttons = new ArrayList<AbstractButton>();
	}
	
	/**
	 * Add a button.
	 * @param button
	 */
	public void add(AbstractButton button){
		_buttons.add(button);
	}
	
	/**
	 * This group has been clicked on... act accordingly (pass the click
	 * on to the correct button).
	 * @param mouseX
	 * @param mouseY
	 */
	public void click(int mouseX, int mouseY){
		AbstractButton clicked = null;
		for (AbstractButton button : _buttons){
			if (button.mouseInBounds(mouseX, mouseY)){
				clicked = button;
				clicked.setActive(true);
				break;
			}
		}
		
		if (clicked != null){
			for (AbstractButton button : _buttons){
				if (button == clicked){
					continue;
				}

				 button.setActive(false);
			}		
		}
		
	}
	
	public void display(){
		for (AbstractButton button : _buttons){
			button.display();
		}
	}
	
	/** 
	 * Display whatever appropriate tooltip
	 */
	public void displayTooltips(){
		for (AbstractButton button : _buttons){
			if (button.mouseInBounds(Window.getInstance().mouseX, Window.getInstance().mouseY))
				button.displayTooltip();
		}
	}
	
	public void setActiveTooltip(){
		for (AbstractButton button : _buttons){
			if (button.isClicked()){
				button.displayTooltip();
			}				
		}
	}

}
