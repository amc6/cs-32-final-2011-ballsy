package graphics;

import ballsy.Window;

public class CloudPiece {

	private Window _window;
	private int _x, _y;
	private float _radius;
	
	public CloudPiece(int centerX, int centerY, float radius) {
		_window = Window.getInstance();
		_x = centerX;
		_y = centerY;
		_radius = radius;
	}
	
	public void draw(int red) {
		_window.fill(red,255,255,255);
		_window.stroke(255, 0);
		_window.ellipse(_x, _y, 2*_radius, 2*_radius);
	}

	public void drift(int dx) {
		_x+=dx;
	}

	public void setBackX(int dx) {
		_x += dx;
	}
}
