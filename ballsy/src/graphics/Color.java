package graphics;

public class Color {
	
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
