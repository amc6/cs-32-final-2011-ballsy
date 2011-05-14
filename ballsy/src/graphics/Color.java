package graphics;

/**
 * Class to create the colors from RGB components as processing colors.
 * This is faster than querying processing to do it.
 */

public class Color {
	
	/**
	 * Create a color from provided RGB components
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int color(int r, int g, int b){
		if (r < 0) r = 0;
		else if (r > 255) r = 255;
		if (g < 0) g = 0;
		else if (g > 255) g = 255;
		if (b < 0) b = 0;
		else if (b > 255) b = 255;
		return 65536*r + 256*g + b;
	}
}
