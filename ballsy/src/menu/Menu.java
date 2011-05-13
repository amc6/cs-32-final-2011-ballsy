package menu;

import java.util.Iterator;
import java.util.Vector;

import org.jbox2d.common.Vec2;

import processing.core.PImage;

import editor.LevelEditor;
import graphics.HoverImage;
import graphics.Image;
import graphics.ScreenBackground;
import graphics.Text;
import ballsy.Screen;
import ballsy.XMLLevel;
import ballsy.XMLUtil;
import ballsy.ScreenLoader.Screens;
import static menu.MenuConstants.*;

public class Menu extends Screen {

	private int _sidePadding;
	private PImage _title, _titleBack;
	private Vector<MenuButton> _buttons;
	private ScreenBackground _background;
	private HoverImage _right, _left;
	private int _page, _maxPages;
	private boolean _animating = false;
	private int _dx, _step, _maxSteps;

	@Override
	public void setup() {
		_background = new ScreenBackground();
		_title = _window.loadImage("res/pick_level_title.png");
		_titleBack = _window.loadImage("res/pick_level_back.png");
		_buttons = XMLUtil.getInstance().loadMenuButtons();
		_right = new HoverImage("res/right_arrow.png", "res/right_arrow_hover.png",
				_window.width-50, _window.height/2);
		_left = new HoverImage("res/left_arrow.png", "res/left_arrow_hover.png",
				50, _window.height/2);	

		int numCols = (int) Math.floor(_window.width/(2*THUMBNAIL_SIZE));
		int numRows = (int) Math.floor(_window.height/(2*THUMBNAIL_SIZE)) - 1;
		int numPerPage = numCols * numRows;
		
		_page = 1;
		_maxPages = (int) Math.ceil((float)_buttons.size()/(float)numPerPage);
		System.out.println("pages: " + _maxPages);
		
		int menuWidth = (numCols)*THUMBNAIL_SIZE + (numCols-1)*THUMBNAIL_PADDING;
		_sidePadding = (_window.width - menuWidth)/2;
		int x = _sidePadding;
		int y = 170;
		
		//place all buttons
		int rowCounter = 0;
		int numLevelsPerPage = 0;
		int xStart = _sidePadding;
		for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext(); rowCounter++, numLevelsPerPage++) {
			System.out.println("xStart: " + xStart);
			MenuButton b = i.next();
			System.out.println(b);
			b.setPosition(x, y);
			x = x + THUMBNAIL_SIZE + THUMBNAIL_PADDING;
			if (rowCounter == numCols-1) {
				x = xStart;
				y += THUMBNAIL_SIZE + THUMBNAIL_PADDING;
				rowCounter = -1;
			}
			if (numLevelsPerPage == numPerPage-1) {
				xStart = xStart + _window.width;
				x = xStart;
				y = 170;
				numLevelsPerPage = -1;
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
		
		if (_animating) {
			if (_step == _maxSteps) {
				_animating = false;
				_step = 0;
			}
			else {
				for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
					MenuButton b = i.next();
					Vec2 currPos = b.getPosition();
					b.setPosition((int)(currPos.x + _dx), (int)currPos.y);
				}
				_step++;
			}
		}
		
		

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
		
		//left right buttons
		if (_page < _maxPages) {
			_right.draw();
		}
		if (_page > 1 ) {
			_left.draw();
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
		
		if (_right.mouseOver() && _page < _maxPages) {
			int dx = -(_window.width); 
			_animating = true;
			_maxSteps = (int) ((float)_window.width/60.f);
			_dx = -60;
//			for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
//				MenuButton b = i.next();
//				Vec2 currPos = b.getPosition();
//				b.setPosition((int)(currPos.x + dx), (int)currPos.y);
//			}
			_page++;
		}
		if (_left.mouseOver() && _page > 1) {
			int dx = (_window.width); 
			_animating = true;
			_maxSteps = (int) ((float)_window.width/60.f);
			_dx = 60;
//			for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
//				MenuButton b = i.next();
//				Vec2 currPos = b.getPosition();
//				b.setPosition((int)(currPos.x + dx), (int)currPos.y);
//			}
			_page--;
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
