package editor;

import java.util.ArrayList;

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

}
