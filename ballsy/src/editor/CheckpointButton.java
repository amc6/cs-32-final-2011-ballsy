package editor;

import org.jbox2d.common.Vec2;

import processing.core.PConstants;

/**
 * Subclass for specific "create checkpoint" button.
 */

public class CheckpointButton extends AbstractButton {

	public CheckpointButton(LevelEditor editor, BodyFactory factory, int minX, int minY, int maxX, int maxY) {
		super(editor, factory, minX, minY, maxX, maxY);
	}

	public void display() {
		super.display();
		_window.fill(EditorConstants.RECTANGLE_COLOR);
		//_window.rect(_minX+12, _minY+12, (_maxX-_minX)-24, (_maxY-_minY)-24);
		
		// create the shape
		_window.pushMatrix();
		_window.beginShape();
		_window.translate(_minX + (_maxX - _minX)/2, _minY + (_maxY - _minY)/2 - 6);
		_window.rotate((float) (Math.PI/4));
		_window.fill(EditorConstants.CHECKPOINT_COLOR);
		
		_window.vertex(13, 20);
		_window.vertex(13, -20);
		_window.vertex(3, -20);
		_window.vertex(3, 10);
		_window.vertex(-13, 10);
		_window.vertex(-13, 20);
		
		_window.endShape(PConstants.CLOSE);
		_window.popMatrix();
	}

	public void select() {
		_level.setPlaceCheckpoint(true);
	}	

	@Override
	public void unselect() { }
	
	public String tooltip(){
		return "Click to create a checkpoint in the level.";
	}
	
}
