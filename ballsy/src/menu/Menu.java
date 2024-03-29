package menu;
 
/**
 * A level chooser menu. Manages alignment of buttons for levels, positioning them
 * based on their customness and position in list (allowing viewing of more levels if there
 * are more than can be viewed).
 */

import java.util.Iterator;
import java.util.Vector;

import org.jbox2d.common.Vec2;

import processing.core.PConstants;
import graphics.HoverImage;
import graphics.Image;
import graphics.ScreenBackground;
import ballsy.Screen;
import ballsy.XMLUtil;
import ballsy.ScreenLoader.Screens;
import static menu.MenuConstants.*;

public class Menu extends Screen {

	private int _sidePadding;
	private Image _levelsTitle, _customLevelsTitle, _titleBack, _customLevelsTab, _levelsTab;
	private Vector<Image> _slidingTop;
	private Vector<MenuButton> _buttons, _customLevels, _defaultLevels;
	private ScreenBackground _background;
	private HoverImage _right, _left;
	private int _pageDefault, _maxPagesDefault, _pageCustom, _maxPagesCustom;
	private boolean _animating = false;
	private int _dx, _step, _maxSteps;
	private boolean _custom = false; 
	private boolean _animatingCustom = false;

	/**
	 * Set up GUI elements
	 */
	public void setup() {
		_background = new ScreenBackground();
		
		_levelsTitle = new Image("res/levels_title.png", 70, 20);
		_levelsTitle.setImageMode(PConstants.CORNER);
		_customLevelsTitle = new Image("res/custom_levels_title.png", 70+_window.width, 20);
		_customLevelsTitle.setImageMode(PConstants.CORNER);
		_titleBack = new Image("res/pick_level_back.png", -20, 20);
		_titleBack.setImageMode(PConstants.CORNER);
		_customLevelsTab = new Image("res/custom_levels_tab.png", _window.width-410, 20);
		_customLevelsTab.setImageMode(PConstants.CORNER);
		_levelsTab = new Image("res/levels_tab.png", _window.width-410+_window.width, 20);
		_levelsTab.setImageMode(PConstants.CORNER);
		
		_slidingTop = new Vector<Image>();
		_slidingTop.add(_levelsTitle);
		_slidingTop.add(_customLevelsTitle);
		_slidingTop.add(_customLevelsTab);
		_slidingTop.add(_levelsTab);
		
		_defaultLevels = XMLUtil.getInstance().loadMenuButtons(false);
		_customLevels = XMLUtil.getInstance().loadMenuButtons(true);

		_buttons = _defaultLevels;
		_right = new HoverImage("res/right_arrow.png", "res/right_arrow_hover.png",
				_window.width-50, _window.height/2);
		_left = new HoverImage("res/left_arrow.png", "res/left_arrow_hover.png",
				50, _window.height/2);	

		int numCols = (int) Math.floor(_window.width/(2*THUMBNAIL_SIZE));
		int numRows = (int) Math.floor(_window.height/(2*THUMBNAIL_SIZE)) - 1;
		int numPerPage = numCols * numRows;
		
		_pageDefault = 1;
		_maxPagesDefault = (int) Math.ceil((float)_defaultLevels.size()/(float)numPerPage);
		_pageCustom = 1;
		_maxPagesCustom = (int) Math.ceil((float)_customLevels.size()/(float)numPerPage);
		
		int menuWidth = (numCols)*THUMBNAIL_SIZE + (numCols-1)*THUMBNAIL_PADDING;
		_sidePadding = (_window.width - menuWidth)/2;

		
		//place all buttons
		int x = _sidePadding;
		int y = 170;
		int rowCounter = 0;
		int numLevelsPerPage = 0;
		int xStart = _sidePadding;
		for (Iterator<MenuButton> i = _defaultLevels.iterator(); i.hasNext(); rowCounter++, numLevelsPerPage++) {
			MenuButton b = i.next();
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
		//custom levels
		x = _sidePadding;
		y = 170;
		rowCounter = 0;
		numLevelsPerPage = 0;
		xStart = _sidePadding;
		for (Iterator<MenuButton> i = _customLevels.iterator(); i.hasNext(); rowCounter++, numLevelsPerPage++) {
			MenuButton b = i.next();
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

	/**
	 * draw menu's current state.
	 */
	public void draw() {
		_window.cursor();
		_window.background(50,200,200);
		_background.draw();
		_window.stroke(0);
		
		if (_animating) { //animate buttons transition
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
		
		
		if (_animatingCustom) { //animate top transition
			if (_step == _maxSteps) {
				_animatingCustom = false;
				_step = 0;
				if (_custom) { //custom
					_customLevelsTitle.setLocation(70, 20);
					_levelsTab.setLocation(_window.width-410, 20);
					_levelsTitle.setLocation(70+_window.width, 20);
					_customLevelsTab.setLocation(_window.width-410+_window.width, 20);
					_buttons = _customLevels;
				}
				else { //default
					_levelsTitle.setLocation(70, 20);
					_customLevelsTab.setLocation(_window.width-410, 20);
					_customLevelsTitle.setLocation(70+_window.width, 20);
					_levelsTab.setLocation(_window.width-410+_window.width, 20);
					_buttons = _defaultLevels;
				}
			}
			else {
				for (Iterator<Image> i = _slidingTop.iterator(); i.hasNext();) {
					Image img = i.next();
					Vec2 currPos = img.getLocation();
					img.setLocation((int)(currPos.x + _dx), (int)currPos.y);
				}
				_step++;
			}
		}
		
		

		//TITLE
		_levelsTitle.draw();
		_customLevelsTitle.draw();
		
		//CUSTOM LEVELS TAB
		if (!_animatingCustom) {
			if (!_custom) {
				if (_customLevelsTab.mouseOver()) {
					_customLevelsTab.setLocation(_window.width-420, 20);
				}
				else {
					_customLevelsTab.setLocation(_window.width-410, 20);
				}
			}
			else {
				if (_levelsTab.mouseOver()) {
					_levelsTab.setLocation(_window.width-420, 20);
				}
				else {
					_levelsTab.setLocation(_window.width-410, 20);
				}
			}
		}
		_customLevelsTab.draw();
		_levelsTab.draw();
		
		//MENU button
		int mouseX = _window.mouseX;
		int mouseY = _window.mouseY;
		if (mouseX < 50 && mouseY > 20 && mouseY < 110) {
			_titleBack.setLocation(-15, 20);
		}
		else {
			_titleBack.setLocation(-20, 20);
		}
		_titleBack.draw();
		
		for (Iterator<MenuButton> i = _buttons.iterator(); i.hasNext();) {
			MenuButton b = i.next();
			b.draw();
		}
		
		//left right buttons
		if (_custom) {
			if (_pageCustom < _maxPagesCustom) {
				_right.draw();
			}
			if (_pageCustom > 1 ) {
				_left.draw();
			}
		}
		else {
			if (_pageDefault < _maxPagesDefault) {
				_right.draw();
			}
			if (_pageDefault > 1 ) {
				_left.draw();
			}
		}
	}

	/**
	 * Handle click. Back button, custom levels, specific buttons, etc.
	 */
	public void mousePressed() {
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
		if (_custom) {
			if (_right.mouseOver() && _pageCustom < _maxPagesCustom) {
				int dx = -(_window.width); 
				_animating = true;
				_maxSteps = (int) ((float)_window.width/60.f);
				_dx = -60;
				_pageCustom++;
			}
			if (_left.mouseOver() && _pageCustom > 1) {
				int dx = (_window.width); 
				_animating = true;
				_maxSteps = (int) ((float)_window.width/60.f);
				_dx = 60;
				_pageCustom--;
			}
		}
		else {
			if (_right.mouseOver() && _pageDefault < _maxPagesDefault) {
				int dx = -(_window.width); 
				_animating = true;
				_maxSteps = (int) ((float)_window.width/60.f);
				_dx = -60;
				_pageDefault++;
			}
			if (_left.mouseOver() && _pageDefault > 1) {
				int dx = (_window.width); 
				_animating = true;
				_maxSteps = (int) ((float)_window.width/60.f);
				_dx = 60;
				_pageDefault--;
			}
		}
		if (_customLevelsTab.mouseOver()) {
			_animatingCustom = true;
			_maxSteps = (int) ((float)_window.width/40.f);
			_dx = -40;
			_custom = true;
		}
		if (_levelsTab.mouseOver()) {
			_animatingCustom = true;
			_maxSteps = (int) ((float)_window.width/40.f);
			_dx = -40;
			_custom = false;
		}
	}
	
	public void addButtons() { }

	@Override
	public void mouseReleased() { }

	@Override
	public void keyPressed() {
		if (_window.key == 27 ) { //ESC
			_window.key = 0;
			_window.loadScreen(Screens.WELCOME_SCREEN);
		}
	}

	@Override
	public void keyReleased() { }

	@Override
	public void mouseDragged() { }

}
