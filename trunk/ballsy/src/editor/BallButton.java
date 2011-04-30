package editor;

public class BallButton extends AbstractButton {

	public BallButton(int minX, int minY, int maxX, int maxY) {

		super(minX, minY, maxX, maxY);
	}


	public void display() {
		super.display();
		_window.pushMatrix();
		_window.fill(EditorConstants.BALL_COLOR);
		_window.translate((_maxX+_minX)/2, (_maxY+_minY)/2);
		_window.ellipse(0, 0, (float) ((_maxX-_minX)*0.65), (float) ((_maxY-_minY)*0.65));
		_window.popMatrix();
		this.displayTooltip();
	}



	public void onClick() {
		System.out.println("BALL!");
	}
	
	public String tooltip(){
		return "Click to create balls in the level.";
	}
}
