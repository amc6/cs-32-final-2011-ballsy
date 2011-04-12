package ballsy;

import bodies.AbstractBody;
import processing.core.PApplet;

public abstract class AbstractLevel extends Screen {
	
	public abstract void setup();
	public abstract void draw();
	public abstract void remove(AbstractBody object);
	
}
