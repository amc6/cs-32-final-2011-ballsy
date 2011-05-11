package graphics;

import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.common.Vec2;

import processing.core.PImage;
import processing.core.PVector;
import ballsy.GeneralConstants;
import ballsy.Window;
import bodies.AbstractBody;

public class Smoke {

	private Random _generator;
	private AbstractBody _body;
	private ArrayList<SmokeParticle> _particles; 
	private PImage _image;

	public Smoke(AbstractBody body){
		
		_body = body;
		_generator = new Random();
		_particles = new ArrayList<SmokeParticle>();

		Window window = Window.getInstance();
		
		// Create an alpha masked image to be applied as the particle's texture
		PImage msk = window.loadImage(GeneralConstants.SMOKE_GRAPHIC);
		_image = new PImage(msk.width,msk.height);
		for (int i = 0; i < _image.pixels.length; i++){
			_image.pixels[i] = window.color(255);
		}
		_image.mask(msk);	

	}

	public void display() {

		// Utilize the object's velocity as its smoke trail as if it were wind
		Vec2 vel = _body.getPhysicsDef().getBody().getLinearVelocity();
		Vec2 wind = new Vec2(-vel.x, -vel.y);

		for (int i = _particles.size() - 1; i >= 0; i--) {
			SmokeParticle p = _particles.get(i);
			p.addForce(wind);
			p.run();
			if (p.dead()) {
				_particles.remove(i);
			}
		}
		
		// Add a particle
		Vec2 vec = _body.getWorldPosition();
		vec.addLocal((float) _generator.nextDouble()*6f - 3f, (float) _generator.nextDouble()*6f - 3f);
		_particles.add(new SmokeParticle(vec, _image));
		
	}

}
