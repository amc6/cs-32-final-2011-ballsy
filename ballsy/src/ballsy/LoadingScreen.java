package ballsy;

import graphics.Text;

public class LoadingScreen extends Screen {
	
	private Window _window;
	private Text _message;
	
	public LoadingScreen() {
		_window = Window.getInstance();
		_message = new Text("Loading...", _window.width/2, _window.height/2);
		_message.setColor(WelcomeScreen.DEFAULT_TEXT_COLOR);
	}

	@Override
	public void setup() {
	}

	@Override
	public void draw() {
		_window.noCursor();
		_window.background(50,200,200);
		_message.draw();
	}

	@Override
	public void mousePressed() {
	}

	@Override
	public void mouseReleased() {
	}

	@Override
	public void keyPressed() {
		_message.setText("Pressing Keys Won't Make This Go Any Quicker");
	}

	@Override
	public void keyReleased() {
	}

	@Override
	public void mouseDragged() {
	}

}
