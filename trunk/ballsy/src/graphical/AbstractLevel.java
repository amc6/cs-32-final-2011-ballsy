package graphical;

import ballsy.Screen;
import bodies.BallsyObject;

public abstract class AbstractLevel extends Screen {
	
	public abstract void setup();
	public abstract void draw();
	public abstract void remove(BallsyObject object);
	
}
