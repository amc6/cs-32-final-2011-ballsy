package editor;

import static bodies.BodyConstants.DEFAULT_BALL_RADIUS;
import static bodies.BodyConstants.DEFAULT_BODY_BOUNCINESS;
import static bodies.BodyConstants.DEFAULT_BODY_COLOR;
import static bodies.BodyConstants.DEFAULT_BODY_DENSITY;
import static bodies.BodyConstants.DEFAULT_BODY_FRICTION;
import static bodies.BodyConstants.DEFAULT_RECTANGLE_HEIGHT;
import static bodies.BodyConstants.DEFAULT_RECTANGLE_WIDTH;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import bodies.AbstractBody;
import bodies.Ball;
import bodies.IrregularPolygon;
import bodies.Rectangle;
import bodies.RegularPolygon;

public class BodyFactory {
	public static final int RECT = 0, IPOLY = 1, RPOLY = 2, BALL = 3;
	private int _currBody = RECT;
	// the  instance variables for current object properties, with default values
	public float width = DEFAULT_RECTANGLE_WIDTH;
	public float height = DEFAULT_RECTANGLE_HEIGHT;
	public float radius = DEFAULT_BALL_RADIUS;
	public float rotation = 0f;
	public int color = DEFAULT_BODY_COLOR;
	public float angularVelocity = 0f;
	public Vec2 velocity = new Vec2(0, 0);
	public float density = DEFAULT_BODY_DENSITY;
	public float friction = DEFAULT_BODY_FRICTION;
	public float bounciness = DEFAULT_BODY_BOUNCINESS;
	public boolean deadly = false;
	public boolean grappleable = true;
	public boolean dynamic = true;
	public boolean graphicalOnly = false;
	public int polyPointCount = 3;
	public ArrayList<Vec2> polyPoints = null;
	
	public BodyFactory() { }
	
	/**
	 * Sets the currently selected body in the level editor.
	 * @param i
	 */
	public void setBody(int i) {
		_currBody = i;
	}
	
	/**
	 * gets a body of the currently 
	 * @param pos
	 * @return
	 */
	public AbstractBody getBody(Vec2 pos) {
		AbstractBody newBody = null;
		switch (_currBody) {
		case RECT:
			// return a rectangle
			newBody = this.getRect(pos);
			break;
		case IPOLY:
			// return an irregular polygon
			newBody = this.getIPoly(pos);
			break;
		case RPOLY:
			// return a regular polygon
			newBody = this.getRPoly(pos);
			break;
		case BALL:
			// return a ball
			newBody = this.getBall(pos);
			break;
		}
		// set the properties
		newBody.getPhysicsDef().setMobile(dynamic);
		newBody.getPhysicsDef().setGraphicalOnly(graphicalOnly);
		newBody.getGraphicsDef().setColor(color);
		newBody.setGrappleable(grappleable);
		newBody.setDeadly(deadly);
		newBody.getPhysicsDef().setLinearVelocity(velocity);
		newBody.getPhysicsDef().setAngularVelocity(angularVelocity);
		newBody.getPhysicsDef().setRotation(rotation);
		newBody.getPhysicsDef().setFriction(friction);
		newBody.getPhysicsDef().setBounciness(bounciness);
		newBody.getPhysicsDef().setDensity(density);
		return newBody;
	}
	
	/**
	 * Return a ball with currently set properties.
	 * @return
	 */
	private Ball getBall(Vec2 pos) {
		return new Ball(pos.x, pos.y, radius);
	}
	
	/**
	 * Return a rectangle with currently set properties.
	 * @return
	 */
	private Rectangle getRect(Vec2 pos) {
		return new Rectangle(pos.x, pos.y, width, height);
	}
	
	/**
	 * Return an irregular polygon with currently set properties.
	 * @return
	 */
	private IrregularPolygon getIPoly(Vec2 pos) {
		return new IrregularPolygon(pos.x, pos.y, polyPoints);
	}
	
	/**
	 * Return an regular polygon with currently set properties.
	 * @return
	 */
	private RegularPolygon getRPoly(Vec2 pos) {
		return new RegularPolygon(pos.x, pos.y, polyPointCount, radius);
	}
}
