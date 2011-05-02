package graphics;
import processing.core.PImage;
import ballsy.Window;

public class Background {
	
	private Window _window = Window.getInstance();
	private float[] _y;
	private PImage _img;
	
	public Background() {
		_y = new float[_window.width];
		float ylast = 550;
		float a = 0.0f;
		float inc = 1;
		
		for (int x = 0; x<_window.width; x++) {
			_y[x] = (ylast + _window.sin(a/105)*.1f + _window.cos(a/205)*.2f);
			a+=inc;
			ylast=_y[x];
		}
		
		
		_img = _window.createImage(_window.width, _window.height, _window.RGB);
		_img.loadPixels();
		ylast = 500;
		a = 0.0f;
		inc = 1;
		
		//R: 9 to 79
		//G: 96 to 228
		//B: 255
		for (int i = 0; i < _img.pixels.length; i++) {
		  int x = i % _img.width;
		  int y = i/_img.width;
		  if (y < _y[x]) {
			  float yFloat = (float) y;
			  float ratio = yFloat/_img.height;
			  int r = (int) (ratio*(79-9) + 9);
			  int g = (int) (ratio*(228-96) + 96);
			  _img.pixels[i] = _window.color(r,g,255);
		  }
		  else {
			  _img.pixels[i] = _window.color(250,241,218);
		  }
//		  _img.pixels[i] = _window.color(y,0,0);
//		  ylast = yCurr;
		}
		_img.updatePixels();
	}
	
	public void draw() {
//		_window.fill(50,200,200);
//		_window.stroke(50,200,200);
//		_window.strokeWeight(3);
		
		_window.imageMode(_window.CORNER);
		_window.image(_img, 0, 0);

		
		//float a = 0.0f;
		//float inc = 1;

		//float xLast = 0;
		//float yLast = 500;
		//float x = 0;
		//float y = 500;
		
//		for(int x=0; x<_y.length; x+=3) {
//			//x++;
//			//y = y + _window.sin(a/105)*.5f + _window.cos(a/205)*.3f;
////			_window.line(xLast, yLast, x, y);
//			_window.line(x, 0, x, (int)_y[x]);
//			//a = a + inc;
////			xLast = x;
////			yLast = y;
//		}
	}

}
