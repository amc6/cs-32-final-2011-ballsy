package graphics;

/**
 * Procedurally generated cloud image, for use in teh menus.
 */

import java.util.Iterator;
import java.util.Vector;

import ballsy.Window;

public class Cloud {
	
	private Vector<CloudPiece> _cloud;
	private int _radiusBig,_radiusMed, _radiusSmall, _red;
	private int _counter, _maxCount,_x, _y;
	private Window _window;
	
	/**
	 * Constructor: generate the image pieces
	 * @param centerX
	 * @param centerY
	 * @param bigRadius
	 */
	public Cloud(int centerX, int centerY, int bigRadius) {
		_window = Window.getInstance();
		_cloud = new Vector<CloudPiece>();
		_x = centerX;
		_y = centerY;
		
		_radiusBig = bigRadius;
		_radiusMed = (int) (bigRadius*.625);
		_radiusSmall = bigRadius/4;
		
		_red = 255-(40-bigRadius)*2;
		
		_counter = 0;
		_maxCount = (int) Math.ceil(40/bigRadius);
		
		// add some cloud pieces. 
		_cloud.add(new CloudPiece(centerX, centerY, _radiusBig));
		_cloud.add(new CloudPiece(centerX-_radiusBig, centerY, _radiusBig));
		_cloud.add(new CloudPiece(centerX+_radiusBig, centerY, _radiusBig));
		_cloud.add(new CloudPiece(centerX, centerY-_radiusBig, _radiusBig));
		_cloud.add(new CloudPiece(centerX-_radiusBig-_radiusBig, centerY+_radiusMed/2, _radiusMed));
		_cloud.add(new CloudPiece(centerX+_radiusBig+_radiusBig, centerY+_radiusMed/2, _radiusMed));
	}
	
	/**
	 * Draw the cloud.
	 */
	public void draw() {
		for (Iterator<CloudPiece> i = _cloud.iterator(); i.hasNext();) {
			i.next().draw(_red);
		}
	}

	/**
	 * Move the cloud a little in the x direction
	 * @param dx
	 */
	public void drift(int dx) {
		if (_x > _window.width*2) { //wrap
			for (Iterator<CloudPiece> i = _cloud.iterator(); i.hasNext();) {
				i.next().setBackX(-_window.width*2-500);
			}
			_x -= (_window.width*2+500);
		}
		//move every other whatever
		if (_counter == _maxCount) {
			_counter = 0;
			for (Iterator<CloudPiece> i = _cloud.iterator(); i.hasNext();) {
				i.next().drift(dx);
			}
			_x += dx;
		}
		else {
			_counter++;
		}
	}
}
