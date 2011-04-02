package graphical;

import org.jbox2d.dynamics.Body;

import pbox2d.PBox2D;

public abstract class BallsyObject {

	protected Body _body;
	protected PBox2D _box2d;
	protected Level _level;
	private float _width;
	private float _height;
	
	public BallsyObject(float posX, float posY, PBox2D box2d, Level level){
		_box2d = box2d;
		_level = level;
	}
	
	public abstract boolean done();
	
	public abstract void killBody();
	
	public abstract void display();
	
	public abstract void makeBody();	
	
}
