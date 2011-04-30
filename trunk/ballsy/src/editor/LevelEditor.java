package editor;

import java.util.ArrayList;

import org.jbox2d.dynamics.Body;

import processing.core.PConstants;
import ballsy.AbstractLevel;
import ballsy.GeneralConstants;
import ballsy.LevelOne;
import ballsy.Screen;

public class LevelEditor extends Screen {

	private AbstractLevel _level;
	private ArrayList<ButtonGroup> _buttonGroups;

	private float _newLevelWidth, _scaleFactor, _newLevelHeight;
	
	@Override
	public void setup() {
		_level = new LevelOne();
		_level.setup();

		_newLevelWidth = _window.width - EditorConstants.LEFT_PANEL_WIDTH;
		_scaleFactor = _newLevelWidth/_window.width;
		_newLevelHeight = _window.height*_scaleFactor;
		
		int topPart = (int) (_window.height - _newLevelHeight);
		
		_buttonGroups = new ArrayList<ButtonGroup>();
		
		ButtonGroup shapeGroup = new ButtonGroup();
		_buttonGroups.add(shapeGroup);	
		
		CursorButton cursorButton = new CursorButton(15,topPart,75,topPart+60);
		shapeGroup.add(cursorButton);
		
		RectangleButton rectButton = new RectangleButton(90,topPart,150,topPart+60);
		shapeGroup.add(rectButton);
		
		TriangleButton triangleButton = new TriangleButton(15,topPart+75,75,topPart+135);
		shapeGroup.add(triangleButton);
		
		IrregularPolygonButton irregPolyButton = new IrregularPolygonButton(90,topPart+75,150,topPart+135);
		shapeGroup.add(irregPolyButton);
		
		BallButton ballButton = new BallButton(15,topPart+150,75,topPart+210);
		shapeGroup.add(ballButton);
		
		PathButton pathButton = new PathButton(90,topPart+150,150,topPart+210);
		shapeGroup.add(pathButton);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub



		_window.pushMatrix();

		_window.translate(_window.width - _newLevelWidth, _window.height - _newLevelHeight);

		_window.scale(_scaleFactor, _scaleFactor);

		_level.draw();		
		_window.cursor();

		_window.popMatrix();

		_window.pushMatrix();

		_window.fill(255); // white
		_window.rectMode(PConstants.CORNER);
		_window.rect(0,0,EditorConstants.LEFT_PANEL_WIDTH,_window.height);
		_window.rect(EditorConstants.LEFT_PANEL_WIDTH, 0, _newLevelWidth, _window.height - _newLevelHeight);

		_window.translate(_window.width - _newLevelWidth, _window.height - _newLevelHeight);
		_window.rectMode(PConstants.CORNER);
		_window.strokeWeight(2);
		_window.stroke(0);
		_window.noFill();
		_window.rect(0,0,_newLevelWidth,_newLevelHeight);
		_window.strokeWeight(GeneralConstants.DEFAULT_LINE_WIDTH);
		_window.popMatrix();

		_window.fill(0);
		_window.textSize(30);
		_window.text("Level Editor",35,50);
		
		for (ButtonGroup group : _buttonGroups){
			group.display();
		}
		
		for (ButtonGroup group : _buttonGroups){
			group.displayTooltips();
		}

	}

	@Override
	public void keyPressed() {
		_level.keyPressed();
	}

	@Override
	public void mousePressed() {
		_level.mousePressed();
		for (ButtonGroup group : _buttonGroups){
			group.click(_window.mouseX, _window.mouseY);
		}
	}

	@Override
	public void mouseReleased() {
		_level.mouseReleased();
	}

	/**
	 * Pass shit to current level.
	 */
	public void handleCollision(Body b1, Body b2, float velocity) { 
		_level.handleCollision(b1, b2, velocity);
	}

	@Override
	public void keyReleased() {
		_level.keyReleased();
		
	}

	@Override
	public void mouseDragged() {
		_level.mouseDragged();
	}


}
