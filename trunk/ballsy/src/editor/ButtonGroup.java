package editor;

import java.util.ArrayList;

import ballsy.Window;

public class ButtonGroup {
	
	private ArrayList<AbstractButton> _buttons;
	
	public ButtonGroup(){
		_buttons = new ArrayList<AbstractButton>();
	}
	
	public void add(AbstractButton button){
		_buttons.add(button);
	}
	
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
