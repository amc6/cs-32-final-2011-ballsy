package graphical;

import ballsy.Window;
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
	public HoverImage(Window window, String image, String hover, int width, int height) {
		super(window, image, width, height);
		_hover = window.loadImage(hover);
	}

	/**
	 * Constructor also takes in x,y loc
	 * @param window
	 * @param image
	 * @param hover
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 */
	public HoverImage(Window window, String image, String hover, int width, int height, int x, int y) {
		super(window, image, width, height, x, y);
		_hover = window.loadImage(hover);
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
