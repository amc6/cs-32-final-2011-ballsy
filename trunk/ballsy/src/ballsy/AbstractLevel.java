package ballsy;

import java.util.ArrayList;

import bodies.AbstractBody;

public abstract class AbstractLevel extends Screen {
	protected ArrayList<AbstractBody> _bodies; // an array of the bodies in a given level
	
	public abstract void setup();
	public abstract void draw();
	public abstract void remove(AbstractBody object);
	
	public ArrayList<AbstractBody> getBodies() { return _bodies; }
}
