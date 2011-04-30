package editor;

import static bodies.BodyConstants.USER_RADIUS;
import graphics.Smoke;

import java.util.ArrayList;

import physics.PhysicsWorld;
import ballsy.AbstractLevel;
import ballsy.GeneralConstants;
import bodies.AbstractBody;
import bodies.UserBall;

public class EditorLevel extends AbstractLevel {
	private boolean _running = false;
	private float _minX, _minY, _maxX, _maxY;
	private float _lastMouseX, _lastMouseY;
	
	public EditorLevel() {
		// load in the level
		this.setInstance(); // set this level as the singleton
		this.setup(); //run setup
	}
	
	public void setup() {
		setupWorld(-100, -100, 100, 100);
		// make a player
		_player = new UserBall(0, 0, USER_RADIUS);
		_bodies.add(_player);
		_player.setInPlay(_running);
		_player.getGraphicsDef().setSmoke(null);
	}
	
	public void play() {
		_running = true;
		_player.setInPlay(_running);
		_player.getGraphicsDef().setSmoke(new Smoke(_player));
	}
	
	public void stop() {
		_running = false;
		_player.setInPlay(_running);
		_player.getGraphicsDef().setSmoke(null);
		
	}
	
	public void resume() {
		_running = true;
		_player.setInPlay(_running);
		_player.getGraphicsDef().setSmoke(new Smoke(_player));
	}
	
	public void setupWorld(float minX, float minY, float maxX, float maxY) {
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		// set up the physics world && bodies
		_world = new PhysicsWorld(_window);
		_world.createWorld(minX, minY, maxX, maxY);
		_world.setGravity(_gravity.x, _gravity.y);
		_bodies = new ArrayList<AbstractBody>();
	}
	
	public void draw() {
		// draw window stuffs
		_window.background(_backgroundColor);
		// draw a line around the world
		_window.stroke(0);
		_window.strokeWeight(1);
		_window.line(_world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_minY), _world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_minY)); //bottom
		_window.line(_world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_minY), _world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_maxY)); //left
		_window.line(_world.worldXtoPixelX(_minX), _world.worldYtoPixelY(_maxY), _world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_maxY)); //top
		_window.line(_world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_minY), _world.worldXtoPixelX(_maxX), _world.worldYtoPixelY(_maxY)); //right		

		_window.stroke(ballsy.GeneralConstants.DEFAULT_LINE_WIDTH);
		// display all objects
		for (AbstractBody body : _bodies) { body.display(); }
		// do the running things if it's running
		if (_running) {
			_window.noCursor();
			// step physics world
			_world.step();
			// handle keypresses
			this.applyInput();
		} else {
			_window.cursor();
		}
	}
	
	public void mousePressed() {
		if (_running) super.mousePressed();
		else {
			_lastMouseX = _window.mouseX;
			_lastMouseY = _window.mouseY;
		}
	}
	
	public void mouseDragged() {
		if (_running) super.mouseDragged();
		else {
			float distX = - _window.mouseX + _lastMouseX;
			float distY = _window.mouseY - _lastMouseY;
			_world.moveCamera(distX, distY, true);
			// reset mouse shits
			_lastMouseX = _window.mouseX;
			_lastMouseY = _window.mouseY;
		}
	}
}
