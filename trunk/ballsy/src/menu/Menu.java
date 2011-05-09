package menu;

import java.util.Iterator;
import java.util.Vector;

import editor.LevelEditor;
import graphics.Image;
import graphics.Text;
import ballsy.Screen;
import ballsy.XMLLevel;
import ballsy.XMLUtil;
import ballsy.ScreenLoader.Screens;
import static menu.MenuConstants.*;

public class Menu extends Screen {

	private Image _title;
	private Vector<MenuButton> _buttons;
//	private Text _title, _titleBack;
	
	@Override
	public void setup() {
		_title = new Image(_window, "res/pick_level_title.png", 582,125, 10,30);
		_title.setImageMode(_window.CORNER);
		_buttons = XMLUtil.getInstance().loadMenuButtons();
		int x = 100;
		int y = 200;
		for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
			MenuButton b = i.next();
			System.out.println(b);
			b.setPosition(x, y);
			x = x + THUMBNAIL_SIZE + THUMBNAIL_PADDING;
		}

	}

	@Override
	public void draw() {
		_window.cursor();
		_window.background(50,200,200);
//		_window.background(255);
		_window.stroke(0);

		//title
		_title.draw();
		
		for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
			MenuButton b = i.next();
			b.draw();
		}
		
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
			MenuButton b = i.next();
			b.click();
		}
	}
	
	public void addButtons() {
		
	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		
		if (_window.key == 27 ) { //ESC
			_window.key = 0;
			_window.loadScreen(Screens.WELCOME_SCREEN);
		}
		
	}

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged() {
		// TODO Auto-generated method stub
		
	}

}
