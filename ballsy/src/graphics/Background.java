package graphics;

/**
 * Class which creates awesome procedurally generated backgrounds.
 * Has a little trouble on machines with low memory or low memory allocation,
 * so if a config.txt file is specified with draw_background: false as the
 * first line, it won't draw.
 */

import java.io.BufferedReader;
import java.io.FileReader;

import org.jbox2d.common.Vec2;

import physics.PhysicsWorld;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import ballsy.Window;

public class Background {
	
	private Window _window = Window.getInstance();
	private PhysicsWorld _world = PhysicsWorld.getInstance();
	private PImage _img;
	
	public static final int HORIZON_DISTANCE = 600;
	
	/**
	 * Constructor: generate the background!
	 */
	public Background() {
		
		int worldPixelWidth = (int) _world.scalarWorldToPixels(_world.getWidth());
		int worldPixelHeight = (int) _world.scalarWorldToPixels(_world.getHeight());
		
		
		float[] yarray = new float[worldPixelWidth];
		int horizon = worldPixelHeight-HORIZON_DISTANCE;
		float ylast = horizon;
		float a = 0.0f;
		float inc = 1;
		
		int m1 = (int) (Math.random() * 200) + 100;
		int m2 = (int) (Math.random() * 200) + 100;
		int m3 = (int) (Math.random() * 200) + 100;
		
		int rand = (int) Math.floor(Math.random()*2);

		for (int x = 0; x<worldPixelWidth; x++) {
			if (rand == 0) {
				yarray[x] = (ylast + PApplet.sin(a/m1)*.1f + PApplet.cos(a/m2)*.2f + PApplet.sin(a/m3)*.15f);
			}
			else {
				yarray[x] = (ylast + PApplet.cos(a/m1)*.1f + PApplet.sin(a/m2)*.2f + PApplet.cos(a/m3)*.15f);
			}
			a+=inc;
			ylast=yarray[x];
			
		}		
		
		_img = _window.createImage(worldPixelWidth, worldPixelHeight, PConstants.RGB);
		_img.loadPixels();
		
		ylast = 500;
		a=0.0f;
		inc = 1;
		
		for (int i = 0; i < _img.pixels.length; i++) {
			int x = i % _img.width;
			int y = i / _img.width;
			if (y > horizon) { //water
				float yFloat = (float) y;
				float ratio = (yFloat-horizon)/100;
				int r = (int) (ratio*250);
				int g = (int) (ratio*(241-200) + 200);
				int b = (int) (ratio*(218-200) + 200);
				_img.pixels[i] = Color.color(r,g,b);
			}
			else { //sky
				float yFloat = (float) y;
				float ratio = yFloat/horizon;
				int r = (int) (ratio*(79-9) + 9);
				int g = (int) (ratio*(228-96) + 96);
				_img.pixels[i] = Color.color(r,g,255);
			} 
			if (y > yarray[x]) {
				  _img.pixels[i] = Color.color(250,241,218);
			}
		}
		_img.updatePixels();
	}
	
	/**
	 * Draws the background, unless a config file exists and says not to
	 */
	public void draw() {
		_world = PhysicsWorld.getInstance();
		_window.background(150);
		Vec2[] bounds = _world.getBounds();
		
		_window.imageMode(PConstants.CORNER);

		// read config file to make sure user wants us to draw backgrounds
		try {
			FileReader input = new FileReader("config.txt");
			BufferedReader br = new BufferedReader(input);
			String line = br.readLine();
			if (line.equals("draw_background: false")) {
				return; // exit if they don't want the background drawn
			}
		} catch (Exception e) {
			// if there's a problem, just continue.
		}
		
		float pixelX = _world.worldXtoPixelX(bounds[0].x);
		float pixelY = _world.worldYtoPixelY(bounds[1].y);
		_window.image(_img, pixelX, pixelY);
		
	}
	
}
