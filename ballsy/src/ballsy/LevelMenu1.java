package ballsy;

import editor.LevelEditor;
import graphics.Image;
import graphics.Text;
import ballsy.ScreenLoader.Screens;

public class LevelMenu1 extends Screen {

	private Image _title;
//	private Text _title, _titleBack;
	
	@Override
	public void setup() {
		_title = new Image(_window, "res/pick_level_title.png", 582,125, 10,30);
		_title.setImageMode(_window.CORNER);
//		_title = new Text("Pick Level", 10, 100);
//		_title.setAlign(_window.LEFT);
//		_title.setSize(72);
//		_title.setColor(_window.color(246,183,0));
	}

	@Override
	public void draw() {
		
		_window.background(50,200,200);
		_window.stroke(0);

		//title
		_title.draw();
		
		//line
		_window.strokeWeight(2);
//		_window.line(0, 160, _window.width, 160);
		
		
		
		//test click
		_window.fill(150, 255, 255, 255);
		_window.stroke(0, 0);
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
					
//			_window.setScreenAndSetup(new LevelEditor());
			_window.loadScreen(Screens.LEVEL_EDITOR);
		}
		
		//new game click detect
		x_left = 300;
		x_right = 400;
		y_top = 200;
		y_bottom = 300;
		if (_window.mouseX > x_left && _window.mouseX < x_right && _window.mouseY > y_top && _window.mouseY < y_bottom) {
//			_window.setScreenAndSetup(new EditorLevel());
			//_window.loadScreen(Screens.LEVEL_TWO, null);
		}

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
