package menu;

import org.jbox2d.common.Vec2;

import ballsy.ScreenLoader.Screens;
import ballsy.Window;
import ballsy.XMLLevel;
import processing.core.PImage;
import editor.AbstractButton;
import editor.BodyFactory;
import static menu.MenuConstants.*;

public class MenuButton {
	
	private Window _window;
	private PImage _thumbnail;
	private int _minX, _minY, _maxX, _maxY;
	private String _levelPath, _thumbnailPath, _nextLevelPath;
	private MenuButton _nextLevel;
	private boolean _locked = false, _custom = true;
	
	public MenuButton(String levelPath, String thumbPath) {
		_window = Window.getInstance();
		_levelPath = levelPath;
		_thumbnailPath = thumbPath;
		_thumbnail = _window.loadImage(thumbPath);
		_nextLevelPath = null;
	}
	
	public Vec2 getPosition() {
		return new Vec2(_minX, _minY);
	}
	
	public void setPosition(int x, int y) {
		_minX = x;
		_minY = y;
		_maxX = x + THUMBNAIL_SIZE;
		_maxY = y + THUMBNAIL_SIZE;
	}
	
	public void setLocked(boolean b) {
		_locked = b;
	}
	
	public boolean isLocked() {
		return _locked;
	}
	
	public void setCustom(boolean b) {
		_custom = b;
	}
	
	public boolean isCustom() {
		return _custom;
	}
	
	public String getLevelPath() {
		return _levelPath;
	}
	
	public String getThumbnailPath() {
		return _thumbnailPath;
	}
	
	public void setNextLevelPath(String path) {
		_nextLevelPath = path;
	}

	public String getNextLevelPath() {
		return _nextLevelPath;
	}
	
	public void setNextLevel(MenuButton b) {
		_nextLevel = b;
	}
	
	public MenuButton getNextLevel() {
		return _nextLevel;
	}
	
	public void draw() {
		_window.fill(255, 255);
		_window.stroke(255, 0);
		_window.rect(_minX-3, _minY-3, THUMBNAIL_SIZE+6, THUMBNAIL_SIZE+6);

		_window.imageMode(_window.CORNER);
		if (_locked) _window.tint(100, 100, 100);
		else _window.tint(255, 255, 255);
		_window.image(_thumbnail, _minX, _minY);
		_window.tint(255, 255, 255);
		if (mouseInBounds() && !_locked) { //inactive
			_window.rectMode(_window.CORNER);
			_window.fill(255, 100);
			_window.rect(_minX, _minY, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
		}
	}
	
	public void click() {
		if (mouseInBounds() && !_locked) {
			//_window.setScreenAndSetup(new XMLLevel(_levelPath, this));
			_window.loadScreen(Screens.XML_LEVEL,_levelPath,this);
		}
	}
	
	public boolean mouseInBounds() {
		int mouseX = _window.mouseX;
		int mouseY = _window.mouseY;
		return mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY;
	}

}
