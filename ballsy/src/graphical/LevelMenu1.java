package graphical;

import processing.core.PImage;

public class LevelMenu1 extends Screen {

	public LevelMenu1(Main window) {
		super(window);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		_window.smooth();
	}

	@Override
	public void draw() {
		_window.background(255,255,255);

		_window.imageMode(_window.CORNER);
		//title
		PImage title = _window.loadImage("res/pick_level_title.png");
		_window.image(title,10,30);
		
		_window.strokeWeight(7);
		_window.line(0, 160, _window.width, 160);
		
		
		
		//test click
		_window.rect(100,200,100,100);
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
		//new game click detect
		int x_left = 100;
		int x_right = 200;
		int y_top = 200;
		int y_bottom = 300;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
					
			_window.setScreen(new Level(_window));
		}

	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub

	}

}
