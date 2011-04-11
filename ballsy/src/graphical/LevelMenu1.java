package graphical;

import processing.core.PImage;

public class LevelMenu1 extends Screen {

	@Override
	public void setup() {

	}

	@Override
	public void draw() {
		
		Window window = Window.getInstance();
		
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
					
			_window.setScreen(new Level());
		}

	}

	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		
	}

}
