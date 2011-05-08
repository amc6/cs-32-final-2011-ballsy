package ballsy;

import processing.core.*;

public final class GeneralConstants {

	public static final int DEFAULT_LINE_WIDTH = 0;
	public static final PFont DEFAULT_FONT_SMALLER = Window.getInstance().loadFont("StrandedBRK-30.vlw");
	public static final PFont DEFAULT_FONT = Window.getInstance().loadFont("StrandedBRK-38.vlw");
	public static final PFont DEFAULT_FONT_BIGGER = Window.getInstance().loadFont("StrandedBRK-40.vlw");
	public static final PFont DEFAULT_FONT_TITLE = Window.getInstance().loadFont("StrandedBRK-72.vlw");
	public static final PFont STUPID_FONT = Window.getInstance().createFont("Verdana", 14, true);

	public static final int DEFAULT_FONT_INACTIVE = 200;
	public static final int DEFAULT_FONT_ACTIVE = 255;
	
	
	public static final float DEFAULT_FONT_HEIGHT = 72f;
	
	/* RESOURCES FINAL NAME CONSTANTS */
	public static final String SMOKE_GRAPHIC = "res/smoke.png";
	
	/* EFFECTS CONSTANTS */
	public static final int DEFAULT_SMOKE_COLOR = Window.getInstance().color(255,0,0,75);
}
