package menu;

import java.util.Iterator;
import java.util.Vector;

import processing.core.PImage;

import editor.LevelEditor;
import graphics.Image;
import graphics.ScreenBackground;
import graphics.Text;
import ballsy.Screen;
import ballsy.XMLLevel;
import ballsy.XMLUtil;
import ballsy.ScreenLoader.Screens;
import static menu.MenuConstants.*;

public class Menu extends Screen {

	private PImage _title, _titleBack;
	private Vector<MenuButton> _buttons;
	private ScreenBackground _background;
	
	@Override
	public void setup() {
		_background = new ScreenBackground();
		_title = _window.loadImage("res/pick_level_title.png");
		_titleBack = _window.loadImage("res/pick_level_back.png");
		_buttons = XMLUtil.getInstance().loadMenuButtons();

		
		int menuWidth = 6*THUMBNAIL_SIZE + 5*THUMBNAIL_PADDING;
		int x = (_window.width - menuWidth)/2;
		int y = 170;
		
		
		int rowCounter = 0;
		for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext(); rowCounter++) {
			MenuButton b = i.next();
			System.out.println(b);
			b.setPosition(x, y);
			x = x + THUMBNAIL_SIZE + THUMBNAIL_PADDING;
			if (rowCounter == 5) {
				x = (_window.width - menuWidth)/2;
				y += THUMBNAIL_SIZE + THUMBNAIL_PADDING;
				rowCounter = 0;
			}
		}

	}

	@Override
	public void draw() {
		_window.cursor();
		_window.background(50,200,200);
//		_window.background(255);
		_background.draw();
		_window.stroke(0);

		//title
		_window.image(_title, 70, 20);
		
		//stupid back button hack lol.
		int mouseX = _window.mouseX;
		int mouseY = _window.mouseY;
		if (mouseX < 50 && mouseY > 20 && mouseY < 110) {
			_window.image(_titleBack,-15, 20);
		}
		else {
			_window.image(_titleBack,-20, 20);
		}
		
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
		
		//stupid back button hack lol.
		int mouseX = _window.mouseX;
		int mouseY = _window.mouseY;
		if (mouseX < 50 && mouseY > 20 && mouseY < 110) {
			_window.loadScreen(Screens.WELCOME_SCREEN);
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
