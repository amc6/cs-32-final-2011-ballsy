package graphics;

/**
 * Graphical image, utilized for HoverImage and GraphicsiImage.
 */

import org.jbox2d.common.Vec2;

import processing.core.PConstants;
import processing.core.PImage;
import ballsy.Window;

public class Image {

	protected Window _window;
	protected PImage _image;
	protected int _width, _height, _x, _y;
	private int _minX, _maxX, _minY, _maxY;
	protected int _imageMode = Window.CENTER;
	
	
	public Image(String image, int x, int y) {
		_window = Window.getInstance();
		_image = _window.loadImage(image);
		_width = _image.width;
		_height = _image.height;
		_x = x;
		_y = y;
		this.updateBounds();
	}
	
	/**
	 * Draws image
	 */
	public void draw() {
		_window.imageMode(_imageMode);
		_window.image(_image, _x, _y);
	}
	
	/**
	 * get location
	 * @return location
	 */
	public Vec2 getLocation() {
		return new Vec2(_x,_y);
	}
	
	/**
	 * Sets location of image
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		_x = x;
		_y = y;
		this.updateBounds();
	}
	
	public void setImageMode(int imageMode) {
		_imageMode = imageMode;
	}
	
	/**
	 * Reset the bounds of the image.
	 */
	public void updateBounds() {
		if(_imageMode == PConstants.CORNER){
			_minX = _x;
			_maxX = _x+_width;
			_minY = _y;
			_maxY = _y+_height;
		}
		else if (_imageMode == PConstants.CENTER) {
			_minX = _x-_width/2;
			_maxX = _x+_width/2;
			_minY = _y-_height/2;
			_maxY = _y+_height/2;
		}
	}
	
	/** 
	 * @return true if mouse is over image, false otherwise
	 */
	public boolean mouseOver() {
		int mouseX = _window.mouseX;
		int mouseY = _window.mouseY;
		return mouseX > _minX && mouseX < _maxX && mouseY > _minY && mouseY < _maxY;
	}
	
	
}
