package bodies;

import ballsy.Window;

public final class BodyConstants {

	/* General body constants. */
	public static final float DEFAULT_BODY_DENSITY = 0.2f;
	public static final float DEFAULT_BODY_FRICTION = 0.3f;
	public static final float DEFAULT_BODY_BOUNCINESS = 0.5f;
	public static final int DEFAULT_BODY_COLOR = Window.getInstance().color(0, 0, 0);
	
	/* Ball constants. */
	public static final float DEFAULT_BALL_RADIUS = .2f;
	
	/* Rectangle constants. */
	public static final float DEFAULT_RECTANGLE_WIDTH = 4;
	public static final float DEFAULT_RECTANGLE_HEIGHT = 4;
	
	/* UserBall constants. */
	public static final float USER_MOVE_COEFFICIENT = 20;
	public static final float USER_MAX_VELOCITY = 100;
	public static final float USER_RADIUS = 3;
	public static final int USER_COLOR = Window.getInstance().color(0, 0, 255);
	
	/*Grapple constants*/
	public static final float LINK_RADIUS = .5f;
	public static final float LINK_DIST = .1f;

	
	/* Crosshair constants. */
	public static final int CROSSHAIR_SIZE = 10;
	public static final float CROSSHAIR_LINE_DIST_COEFF = 1.2f; // How far the line starts from shape. 1f starts at edge of shape
	public static final float CROSSHAIR_RANGE = 70f; // How far the crosshair will stretch to a target 
	public static final int CROSSHAIR_LINE_WIDTH = 2; // Stroke weight for the crosshair and the line
	public static final int CROSSHAIR_LINE_LENGTH = 50; // Length of the helper line
	public static final int CROSSHAIR_FILL_OPACITY = 150; // Opacity of the fill within the crosshair
	public static final int CROSSHAIR_FILL_COLOR = 255;
	public static final int CROSSHAIR_ACTIVE_LINE_COLOR = Window.getInstance().color(0, 255, 0);
	public static final int CROSSHAIR_INACTIVE_LINE_COLOR = 200;
	public static final int CROSSHAIR_HELPER_DIAMETER = 10;
	
	public static final int DEFAULT_SURFACE_STROKE = 2;
	
	/* endpoint constant(s) */
	public static final int ENDPOINT_SIZE = 4;
	public static final int ENDPOINT_COLOR = Window.getInstance().color(255, 0, 0);
}
