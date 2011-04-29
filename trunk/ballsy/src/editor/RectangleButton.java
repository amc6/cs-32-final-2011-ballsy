package editor;

public class RectangleButton extends AbstractButton {

	public RectangleButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
		this.setActiveColors(255, 200);
		this.setInactiveColors(240, 190);

	}


	public void display() {
		super.display();
		_window.fill(EditorConstants.RECTANGLE_COLOR);
		_window.rect(_minX+12, _minY+12, (_maxX-_minX)-24, (_maxY-_minY)-24);
	}



	public void onClick() {
		System.out.println("RECTANGLE!");
	}
	
}
