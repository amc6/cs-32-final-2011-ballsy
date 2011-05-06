package graphics;
import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import processing.core.PImage;
import ballsy.Window;

public class Background {
	
	private Window _window = Window.getInstance();
	private PhysicsWorld _world = PhysicsWorld.getInstance();
	private float[] _y;
	private PImage _img;
	private int _minX, _initMinY, _minY, _initTransY, _transY;
	
	public Background() {
		
		int worldWidth = (int)_world.scalarWorldToPixels(_world.getWidth());
		int worldHeight = (int)_world.scalarWorldToPixels(_world.getHeight());
		int horizon = worldHeight-200;
		
		Vec2[] bounds = _world.getBounds();
		Vec2 min = bounds[0];
		_initTransY = (int) _world.transY;
		System.out.println("init transY" + _initTransY);
		_transY = _initTransY;
		_minX = (int) _world.scalarWorldToPixels(min.x);
		_initMinY = (int) (_world.scalarWorldToPixels(min.y) + _initTransY);
		_minY = _initMinY;
		System.out.println("minX, minY: " + _minX + "," + _minY);
		
		
		_y = new float[worldWidth];
		float ylast = horizon;
		float a = 0.0f;
		float inc = 1;
		
		int m1 = (int) (Math.random() * 200) + 100;
		int m2 = (int) (Math.random() * 200) + 100;
		int m3 = (int) (Math.random() * 200) + 100;
		
		int rand = (int) Math.floor(Math.random()*2);

		for (int x = 0; x<_world.scalarWorldToPixels(_world.getWidth()); x++) {
			if (rand == 0) {
				_y[x] = (ylast + _window.sin(a/m1)*.1f + _window.cos(a/m2)*.2f + _window.sin(a/m3)*.15f);
			}
			else {
				_y[x] = (ylast + _window.cos(a/m1)*.1f + _window.sin(a/m2)*.2f + _window.cos(a/m3)*.15f);
			}
			a+=inc;
			ylast=_y[x];
		}

		System.out.println(worldWidth + ", " + worldHeight);
		
		_img = _window.createImage(worldWidth, worldHeight, _window.RGB);
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
		  if (y > horizon) { //water
			  float yFloat = (float) y;
			  float ratio = (yFloat-horizon)/100;
			  int r = (int) (ratio*250);
			  int g = (int) (ratio*(241-200) + 200);
			  int b = (int) (ratio*(218-200) + 200);
			  _img.pixels[i] = _window.color(r,g,b);
		  }
		  else { //sky
			  float yFloat = (float) y;
			  float ratio = yFloat/horizon;
			  int r = (int) (ratio*(79-9) + 9);
			  int g = (int) (ratio*(228-96) + 96);
			  _img.pixels[i] = _window.color(r,g,255);
		  } //beach
		  if (y > _y[x]) {
			  _img.pixels[i] = _window.color(250,241,218);
		  }

//		  _img.pixels[i] = _window.color(y,0,0);
//		  ylast = yCurr;
		}
		_img.updatePixels();
	}
	
	public void draw() {
		
		_window.background(150);
		
		int transX = (int) _world.scalarWorldToPixels(_world.transX);
		int transY = (int) _world.scalarWorldToPixels(_world.transY);
//		System.out.println("bg trans:" + _world.transX + ", " + _world.transY);
//		System.out.println("trans pixels" + transX + ", " + transY);

		_window.imageMode(_window.CORNER);
		_window.image(_img, _minX+_world.transX, _minY-_world.transY+_transY);
//		_window.image(_img, _minX+_world.transX, _minY);

		

	}

	/**
	 * Restores translations to old shit
	 * @param savedTransX
	 * @param savedTransY
	 */
	public void restoreTrans(int savedTransX, int savedTransY) {
		_world.transX = savedTransX;
		_world.transY = savedTransY;
	}
	
}
