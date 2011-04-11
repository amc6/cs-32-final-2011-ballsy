package graphical;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import physics.PhysicsWorld;
import processing.core.PApplet;
import processing.core.PConstants;

//The Nature of Code
//<http://www.shiffman.net/teaching/nature>
//Spring 2010
//PBox2D example


public class BallTestLevel extends Screen {
	


	//A reference to our box2d world
	private PhysicsWorld _world;
	private ArrayList<Boundary> _boundary;
	private PlayerBall _pBall;
	private Particle _particle;
	
	public void setup() {
		
		
		_world = new PhysicsWorld(_window);
		_world.createWorld();
		// We are setting a custom gravity
		_world.setGravity(0, -20);
		
		_boundary = new ArrayList<Boundary>();
		
		/*_boundary.add(new Boundary(width/2, 0, width, 30, _world, this));
		_boundary.add(new Boundary(width/2, height - 5, width, 30, _world, this));
		_boundary.add(new Boundary(width-5,height/2,30,height,_world,this));
		_boundary.add(new Boundary(5,height/2,30,height,_world,this));*/
		_boundary.add(new Boundary(_window.width/2, 0, _window.width, 10, _world, _window));
		_boundary.add(new Boundary(_window.width/2, _window.height - 5, _window.width, 10, _world, _window));
		_boundary.add(new Boundary(_window.width-5,_window.height/2,10,_window.height,_world,_window));
		_boundary.add(new Boundary(5,_window.height/2,10,_window.height,_world,_window));
		
		_pBall = new PlayerBall(_world,_window);
		_particle = new Particle(200,200,10,_world,_window);
	}

	public void draw() {
		_world.step();
		_window.background(255,255,255);
		for (Boundary wall: _boundary) {
			wall.display();
		}
		_pBall.display();
		_particle.display();
		
		_window.fill(0);
		_window.textSize(20);
		_window.text("framerate: " + (int)_window.frameRate,12,46);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[] { "--present", "graphical.BallTestLevel" });
		
	}


	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		
	}
}






