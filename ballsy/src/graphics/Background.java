package graphics;
import ballsy.Window;

public class Background {
	
	private Window _window = Window.getInstance();
	private float[] _y;
	
	public Background() {
		_y = new float[_window.width];
		float ylast = 500;
		float a = 0.0f;
		float inc = 1;
		
		for (int x = 0; x<_window.width; x++) {
			_y[x] = (ylast + _window.sin(a/105)*.5f + _window.cos(a/205)*.3f);
			a+=inc;
			ylast=_y[x];
		}
	}
	
	public void draw() {
		_window.fill(50,200,200);
		_window.stroke(50,200,200);
		_window.strokeWeight(3);
		
		//float a = 0.0f;
		//float inc = 1;

		//float xLast = 0;
		//float yLast = 500;
		//float x = 0;
		//float y = 500;
		
		for(int x=0; x<_y.length; x+=3) {
			//x++;
			//y = y + _window.sin(a/105)*.5f + _window.cos(a/205)*.3f;
//			_window.line(xLast, yLast, x, y);
			_window.line(x, 0, x, (int)_y[x]);
			//a = a + inc;
//			xLast = x;
//			yLast = y;
		}
	}

}
