package menu;

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
	private String _levelPath, _thumbnailPath;
	
	public MenuButton(String levelPath, String thumbPath) {
		_window = Window.getInstance();
		_levelPath = levelPath;
		_thumbnailPath = thumbPath;
		_thumbnail = _window.loadImage(thumbPath);
	}
	
	public void setPosition(int x, int y) {
		_minX = x;
		_minY = y;
		_maxX = x + THUMBNAIL_SIZE;
		_maxY = y + THUMBNAIL_SIZE;
	}
	
	public String getLevelPath() {
		return _levelPath;
	}
	
	public String getThumbnailPath() {
		return _thumbnailPath;
	}
	
	public void draw() {
		_window.fill(255, 255);
		_window.stroke(255, 0);
		_window.rect(_minX-3, _minY-3, THUMBNAIL_SIZE+6, THUMBNAIL_SIZE+6);

		_window.imageMode(_window.CORNER);
		_window.image(_thumbnail, _minX, _minY);
		if (mouseInBounds()) { //inactive
			_window.rectMode(_window.CORNER);
			_window.fill(255, 100);
			_window.rect(_minX, _minY, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
		}
	}
	
	public void click() {
		if (mouseInBounds()) {
			_window.setScreenAndSetup(new XMLLevel(_levelPath));
		}
	}
	
	public boolean mouseInBounds() {
		int mouseX = _window.mouseX;
		int mouseY = _window.mouseY;
		return mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY;
	}

}
