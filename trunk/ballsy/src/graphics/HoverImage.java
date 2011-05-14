package graphics;

/**
 * Hover image, used for buttons to be hovered over, and whatnot.
 */

import processing.core.PImage;

public class HoverImage extends Image {
	
	private PImage _hover;

	/**
	 * Constructor
	 * @param window
	 * @param image
	 * @param hover
	 * @param width
	 * @param height
	 */
	public HoverImage(String image, String hover, int x, int y) {
		super(image, x, y);
		_hover = _window.loadImage(hover);
	}
	
	/**
	 * Draws the image, checking for hover
	 */
	public void draw() {
		_window.imageMode(_imageMode);
		if (this.mouseOver()) {
			_window.image(_hover, _x, _y);
		}
		else {
			_window.image(_image, _x, _y);
		}
	}
	

}
