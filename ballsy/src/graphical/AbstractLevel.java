package graphical;

import bodies.BallsyObject;
import processing.core.PApplet;

public abstract class AbstractLevel extends Screen {
	
	public abstract void setup();
	public abstract void draw();
	public abstract void remove(BallsyObject object);
	
}
