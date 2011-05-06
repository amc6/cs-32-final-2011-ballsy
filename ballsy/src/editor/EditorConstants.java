package editor;

import processing.core.PFont;
import ballsy.Window;

public class EditorConstants {

	public static final int BUTTON_BORDER_WIDTH = 0;
	public static final int BUTTON_BORDER_COLOR = Window.getInstance().color(0);
	public static final int BUTTON_INACTIVE_COLOR = Window.getInstance().color(255,100);
	public static final int BUTTON_ACTIVE_COLOR = Window.getInstance().color(255,200);
	public static final int BUTTON_HOVER_COLOR = Window.getInstance().color(255,150);
	public static final int TRIANGLE_COLOR = Window.getInstance().color(255,0,0);
	public static final int RECTANGLE_COLOR = Window.getInstance().color(0,255,0);
	public static final int BALL_COLOR = Window.getInstance().color(0,0,255);
	public static final int IRREGULAR_POLYGON_COLOR = Window.getInstance().color(255,0,255);
	
	public static final PFont TOOLTIP_FONT = Window.getInstance().createFont("Verdana", 12, true);
	public static final int TOOLTIP_BG = Window.getInstance().color(20,200);
	public static final float LEFT_PANEL_WIDTH = 165;
	public static final float TOP_BUTTONS_SIZE = 60;
	
}
