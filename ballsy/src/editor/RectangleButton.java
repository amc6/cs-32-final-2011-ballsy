package editor;

public class RectangleButton extends AbstractButton {

	public RectangleButton(BodyFactory factory, int minX, int minY, int maxX, int maxY) {

		super(factory, minX, minY, maxX, maxY);
	}


	public void display() {
		super.display();
		_window.fill(EditorConstants.RECTANGLE_COLOR);
		_window.rect(_minX+12, _minY+12, (_maxX-_minX)-24, (_maxY-_minY)-24);
	}



	public void select() {
		_level.setPlacemode(true);
		_factory.setBody(BodyFactory.RECT);
	}
	

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}
	
	public String tooltip(){
		return "Click to create rectangles in the level.";
	}
	
}
