package graphics;

import ballsy.Window;

public class Background {
	
	private Window _window = Window.getInstance();
	
	public Background() {
		
	}
	
	public void draw() {
		_window.fill(0);
		_window.stroke(0);
		_window.strokeWeight(2);
		
		float a = 0.0f;
		float inc = 1;

		float xLast = 0;
		float yLast = 500;
		float x = 0;
		float y = 500;
		
		for(float i=0; i<_window.width; i++) {
			x++;
			y = y + _window.sin(a/105)*.5f + _window.cos(a/205)*.3f;
//			_window.line(xLast, yLast, x, y);
			_window.line(x, 0, x, y);
			a = a + inc;
//			xLast = x;
//			yLast = y;
		}
	}

}
