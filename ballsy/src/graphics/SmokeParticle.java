package graphics;

import java.util.Random;

import org.jbox2d.common.Vec2;

import ballsy.GeneralConstants;
import ballsy.Window;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class SmokeParticle {
	private Vec2 _loc;
	private Vec2 _vel;
	private Vec2 _acc;
	private float _timer;
	private PImage _img;
	private Random _generator;

	// Another constructor (the one we are using here)
	public SmokeParticle(Vec2 loc, PImage img) {
		_generator = new Random();
		_acc = new Vec2(0,0);
		_vel = new Vec2(0,0);
		_loc = loc.clone();
		_timer = 100.0f;
		_img = img;		
	}

	public void run() {
		this.update();
		this.render();
	}

	// Method to apply a force vector to the Particle object
	// Note we are ignoring "mass" here
	public void addForce(Vec2 f) {
		_acc.add(f);
	}  

	// Method to update location
	private void update() {
		_vel.add(_acc);
		_loc.add(_vel);
		_timer -= 2.5;
		_acc = new Vec2(0,0);
	}

	// Method to display
	private void render() {
		Window window = Window.getInstance();
		window.imageMode(PConstants.CORNER);
		window.tint(GeneralConstants.DEFAULT_SMOKE_COLOR,_timer); // set main color of trail here
		window.image(_img,_loc.x-_img.width/2,_loc.y-_img.height/2);
		window.noTint(); // remove current fill value
	}

	// Is the particle still useful?
	boolean dead() {
		return _timer <= 0.0;
	}
}


