package ballsy;

import editor.LevelEditor;
import graphics.Image;

public class LevelMenu1 extends Screen {

	private Image _title;
	
	@Override
	public void setup() {
		_title = new Image(_window, "res/pick_level_title.png", 582,125, 10,30);
		_title.setImageMode(_window.CORNER);

	}

	@Override
	public void draw() {
		
		_window.background(255,255,255);

		//title
		_title.draw();
		
		//line
		_window.strokeWeight(2);
		_window.line(0, 160, _window.width, 160);
		
		
		
		//test click
		_window.rect(100,200,100,100);
		
		_window.rect(300,200,100,100);

		
		_window.strokeWeight(1); // reset
		
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
					
			_window.setScreen(new LevelEditor());
		}
		
		//new game click detect
		x_left = 300;
		x_right = 400;
		y_top = 200;
		y_bottom = 300;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
					
			_window.setScreen(new LevelTwo());
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

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged() {
		// TODO Auto-generated method stub
		
	}

}
