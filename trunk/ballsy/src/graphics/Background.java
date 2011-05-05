package graphics;
import processing.core.PImage;
import ballsy.Window;

public class Background {
	
	private Window _window = Window.getInstance();
	private float[] _y;
	private PImage _img;
	
	public Background() {
		_y = new float[_window.width];
		float ylast = 500;
		float a = 0.0f;
		float inc = 1;
		
		int m1 = (int) (Math.random() * 200) + 100;
		int m2 = (int) (Math.random() * 200) + 100;
		int m3 = (int) (Math.random() * 200) + 100;
		
		int rand = (int) Math.floor(Math.random()*2);

		for (int x = 0; x<_window.width; x++) {
			if (rand == 0) {
				_y[x] = (ylast + _window.sin(a/m1)*.1f + _window.cos(a/m2)*.2f + _window.sin(a/m3)*.15f);
			}
			else {
				_y[x] = (ylast + _window.cos(a/m1)*.1f + _window.sin(a/m2)*.2f + _window.cos(a/m3)*.15f);
			}
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
		  if (y > 500) {
			  float yFloat = (float) y;
			  float ratio = (yFloat-500)/100;
			  int r = (int) (ratio*250);
			  int g = (int) (ratio*(241-200) + 200);
			  int b = (int) (ratio*(218-200) + 200);
			  _img.pixels[i] = _window.color(r,g,b);
		  }
		  else {
			  float yFloat = (float) y;
			  float ratio = yFloat/500;
			  int r = (int) (ratio*(79-9) + 9);
			  int g = (int) (ratio*(228-96) + 96);
			  _img.pixels[i] = _window.color(r,g,255);
		  }
		  if (y > _y[x]) {
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
