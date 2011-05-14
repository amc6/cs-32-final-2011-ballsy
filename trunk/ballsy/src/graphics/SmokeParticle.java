package graphics;

/**
 * Represents the particles within a smoke cloud, and manages teir position and motion. 
 */

import java.util.Random;
import org.jbox2d.common.Vec2;
import ballsy.GeneralConstants;
import ballsy.Window;
import physics.PhysicsWorld;
import processing.core.PConstants;
import processing.core.PImage;

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
		_vel.addLocal((float) _generator.nextDouble()*6 - 3, (float) _generator.nextDouble()*6 -3);
		_loc.add(_vel);
		_timer -= 2.5;
		_acc = new Vec2(0,0);
	}

	// Method to display
	private void render() {
		Window window = Window.getInstance();
		window.imageMode(PConstants.CORNER);
		window.tint(GeneralConstants.DEFAULT_SMOKE_COLOR,_timer); // set main color of trail here
		Vec2 pixelLoc = PhysicsWorld.getInstance().coordWorldToPixels(_loc);
		window.image(_img,pixelLoc.x-_img.width/2,pixelLoc.y-_img.height/2);
		window.noTint(); // remove current fill value
	}

	// Is the particle still useful?
	boolean dead() {
		return _timer <= 0.0;
	}
}


