/**
 * PBox2d
 * This is a simple little wrapper to help integrate JBox2d with Processing
 * It doesn't do much right now and, in some ways, limits the user
 * It's an open question as to whether this should really be a library
 * or a set of examples. Right now, it's a little bit of both
 * Daniel Shiffman <http://www.shiffman.net>
 */

package physics;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;

import ballsy.Window;

import processing.core.PApplet;
import processing.core.PVector;

public class PhysicsWorld {

	PApplet parent;

	// The Box2D world
	public World world;

	// Variables to keep track of translating between world and screen coordinates
	public float transX;// = 320.0f;
	public float transY;// = 240.0f;
	public float scaleFactor;// = 10.0f;
	public float yFlip;// = -1.0f; //flip y coordinate

	PContactListener contactlistener;

	// Construct with a default scaleFactor of 10
	public PhysicsWorld(PApplet p) {
		this(p,10);
	}

	public PhysicsWorld(PApplet p, float sf) {
		parent = p;
		transX = parent.width/2;
		transY = parent.height/2;
		scaleFactor = sf;
		yFlip = -1;
	}
	
	public PhysicsWorld(PApplet p, float sf, float lx, float ly) {
		parent = p;
		yFlip = -1;
		scaleFactor = sf;
		transX = lx;
		transY = ly;
		
	}

	public void listenForCollisions() {
		contactlistener = new PContactListener(parent);
		world.setContactListener(contactlistener);
	}

	// Change the scaleFactor
	public void setScaleFactor(float scale) {
		scaleFactor = scale;
	}

	// This is the all important physics "step" function
	// Says to move ahead one unit in time
	// Default
	public void step() {
		float timeStep = 1.0f / 60f;
		this.step(timeStep,10);
	}

	// Custom
	public void step(float timeStep, int iterationCount) {
		this.step(true,true,true,timeStep, iterationCount);
	}

	// More custom
	public void step(boolean starting, boolean correction, boolean continuous, float timeStep, int iterationCount) {

		world.setWarmStarting(starting);
		world.setPositionCorrection(correction);
		world.setContinuousPhysics(continuous);
		world.step(timeStep, iterationCount);
	}

	public float getWidth(){
		AABB worldAABB = world.getWorldAABB();
		return worldAABB.upperBound.x - worldAABB.lowerBound.x;
	}
	
	public float getHeight(){
		AABB worldAABB = world.getWorldAABB();
		return worldAABB.upperBound.y - worldAABB.lowerBound.y;
	}
	
	public float getCenterX(){
		return world.getWorldAABB().upperBound.x - this.getWidth()/2;
	}
	
	public float getCenterY(){
		return world.getWorldAABB().upperBound.y - this.getHeight()/2;
	}
	
	
	// Create a default world
	@Deprecated
	public void createWorld() {
		this.createWorld(-100,-100,100,100);
	}
	
	public void createWorld(float xSize, float ySize) {
		this.createWorld(0, 0, xSize, ySize);
	}

	// Slightly more custom world
	// These values define how much we are looking at?
	public void createWorld(float lx,float ly, float ux, float uy) {
		AABB worldAABB = new AABB();
		worldAABB.lowerBound.set(lx, ly);
		worldAABB.upperBound.set(ux, uy);
		Vec2 gravity = new Vec2(0.0f, -10.0f);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);
	}

	// Set the gravity (this can change in real-time)
	public void setGravity(float x, float y) {
		world.setGravity(new Vec2(x,y));
	}
	
	/**
	 * Moves the camera in the specified direction. Units are pixels.
	 * @param dx
	 * @param dy
	 */
	public void moveCamera(float dx, float dy, boolean allowOutOfBounds) {
		transX-=dx;
		transY-=dy;
	}
	
	/**
	 * Moves the camera to the specified position. Units are
	 * world coords.
	 * @param worldX
	 * @param worldY
	 */
	public void centerCameraOn(float worldX, float worldY) {
		transX = -PApplet.map(worldX, 0f, 1f, 0f, scaleFactor);
		transY = -PApplet.map(worldY, 0f, 1f, 0f, scaleFactor);
		transX += Window.getInstance().width/2;
		transY += Window.getInstance().height/2;
	}
	
	public void scale(float factor) {
		scaleFactor*=factor;
	}

	// These functions are very important
	// Box2d has its own coordinate system and we have to move back and forth between them
	// convert from Box2d world to pixel space
	public Vec2 coordWorldToPixels(Vec2 world) {
		return coordWorldToPixels(world.x,world.y);
	}

	public PVector coordWorldToPixelsPVector(Vec2 world) {
		Vec2 v = coordWorldToPixels(world.x,world.y);
		return new PVector(v.x,v.y);
	}

	public Vec2 coordWorldToPixels(float worldX, float worldY) {
		//float pixelX = PApplet.map(worldX, 0f, 1f, transX, transX+scaleFactor);
		float pixelX = worldXtoPixelX(worldX);
		//float pixelY = PApplet.map(worldY, 0f, 1f, transY, transY+scaleFactor);
		//if (yFlip == -1.0f) pixelY = PApplet.map(pixelY,0f,parent.height, parent.height,0f);
		float pixelY = worldYtoPixelY(worldY);
		return new Vec2(pixelX, pixelY);
	}
	
	public float worldXtoPixelX(float worldX){
		return PApplet.map(worldX, 0f, 1f, transX, transX+scaleFactor);
	}
	
	public float worldYtoPixelY(float worldY){
		float pixelY = PApplet.map(worldY, 0f, 1f, transY, transY+scaleFactor);
		if (yFlip == -1.0f) pixelY = PApplet.map(pixelY,0f,parent.height, parent.height,0f);
		return pixelY;
	}

	// convert Coordinate from pixel space to box2d world
	public Vec2 coordPixelsToWorld(Vec2 screen) {
		return coordPixelsToWorld(screen.x,screen.y);
	}

	public Vec2 coordPixelsToWorld(PVector screen) {
		return coordPixelsToWorld(screen.x,screen.y);
	}

	public Vec2 coordPixelsToWorld(float pixelX, float pixelY) {
//		float worldX = PApplet.map(pixelX, transX, transX+scaleFactor, 0f, 1f);
//		float worldY = pixelY;
//		if (yFlip == -1.0f) worldY = PApplet.map(pixelY,parent.height,0f,0f,parent.height);
//		worldY = PApplet.map(worldY, transY, transY+scaleFactor, 0f, 1f);
		float worldX = pixelXtoWorldX(pixelX);
		float worldY = pixelYtoWorldY(pixelY);
		return new Vec2(worldX,worldY);
	}
	
	public float pixelXtoWorldX(float pixelX){
		return PApplet.map(pixelX, transX, transX+scaleFactor, 0f, 1f);
	}
	
	public float pixelYtoWorldY(float pixelY){
		float worldY = pixelY;
		if (yFlip == -1.0f) worldY = PApplet.map(pixelY,parent.height,0f,0f,parent.height);
		worldY = PApplet.map(worldY, transY, transY+scaleFactor, 0f, 1f);
		return worldY;
	}

	// Scale scalar quantity between worlds
	public float scalarPixelsToWorld(float val) {
		return val / scaleFactor;
	}

	public float scalarWorldToPixels(float val) {
		return val * scaleFactor;
	}

	// Scale vector between worlds
	public Vec2 vectorPixelsToWorld(Vec2 v) {
		Vec2 u = new Vec2(v.x/scaleFactor,v.y/scaleFactor);
		u.y *=  yFlip;
		return u;
	}

	public Vec2 vectorPixelsToWorld(PVector v) {
		Vec2 u = new Vec2(v.x/scaleFactor,v.y/scaleFactor);
		u.y *=  yFlip;
		return u;
	}

	public Vec2 vectorWorldToPixels(Vec2 v) {
		Vec2 u = new Vec2(v.x*scaleFactor,v.y*scaleFactor);
		u.y *=  yFlip;
		return u;
	}

	public PVector vectorWorldToPixelsPVector(Vec2 v) {
		PVector u = new PVector(v.x*scaleFactor,v.y*scaleFactor);
		u.y *=  yFlip;
		return u;
	}

	// A common task we have to do a lot
	public Body createBody(BodyDef bd) {
		return world.createBody(bd);
	}

	// A common task we have to do a lot
	public Joint createJoint(JointDef jd) {
		return world.createJoint(jd);
	}

	// Another common task, find the position of a body
	// so that we can draw it
	public Vec2 getBodyPixelCoord(Body b) {
		XForm xf = b.getXForm();
		return coordWorldToPixels(xf.position); 
	}
	
	public Vec2 getBodyWorldCoord(Body b){
		XForm xf = b.getXForm();
		return xf.position;
	}

	public PVector getBodyPixelCoordPVector(Body b) {
		XForm xf = b.getXForm();
		return coordWorldToPixelsPVector(xf.position); 
	}

	public void destroyBody(Body b) {
		world.destroyBody(b);
	}
	
	/**
	 * removes all bodies from the world.
	 */
	public void clear() {
		Body currBody = world.getBodyList();
		while (currBody != null) {
			Body tempBody = currBody.getNext();
			world.destroyBody(currBody);
			currBody = tempBody;
		}
	}
}