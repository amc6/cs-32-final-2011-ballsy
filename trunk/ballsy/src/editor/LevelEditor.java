package editor;

import interfascia.GUIController;
import interfascia.GUIEvent;
import interfascia.IFLabel;
import interfascia.IFRadioButton;
import interfascia.IFRadioController;
import interfascia.IFTextField;
import graphics.Text;

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
	private Text _levelEditorTitle;
	
	@Override
	public void setup() {
		_level = new LevelOne();
		_level.setup();
		
		_levelEditorTitle = new Text("Level Editor", 20, 90);
		_levelEditorTitle.setAlign(_window.CORNER);
		_levelEditorTitle.setSize(40);
		_levelEditorTitle.setColor(0);

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
		

		GUIController c = new GUIController(_window);
		IFRadioController rc = new IFRadioController("Mode Selector");
		rc.addActionListener(this);

		IFRadioButton b1 = new IFRadioButton("Dynamic Object", 15, topPart+285, rc);

		IFRadioButton b2 = new IFRadioButton("Static Object", 15, topPart+315, rc);

		IFRadioButton b3 = new IFRadioButton("Graphical Only", 15, topPart+345, rc);



		c.add(b1);
		c.add(b2);
		c.add(b3);

		float propertiesStart = _window.height - 200;
		
		IFLabel frictionLabel = new IFLabel("Friction", 15, (int) propertiesStart);
		IFTextField friction = new IFTextField("Friction", 90, (int) propertiesStart - 4, 60);
		friction.setValue("1.3");
		friction.addActionListener(this);
		c.add(frictionLabel);
		c.add(friction);
		
		IFLabel bouncinessLabel = new IFLabel("Bounciness", 15, (int) propertiesStart + 30);
		IFTextField bounciness = new IFTextField("Bounciness", 90, (int) propertiesStart - 4 + 30, 60);
		bounciness.setValue("0.3");
		bounciness.addActionListener(this);
		c.add(bouncinessLabel);
		c.add(bounciness);
		
		IFLabel densityLabel = new IFLabel("Density", 15, (int) propertiesStart + 60);
		IFTextField density = new IFTextField("Density", 90, (int) propertiesStart - 4 + 60, 60);
		density.setValue("4.3");
		density.addActionListener(this);
		c.add(densityLabel);
		c.add(density);
	

	}
	
	public void actionPerformed(GUIEvent e){
		System.out.println("action performed in GUI");
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub



		_window.pushMatrix();

//		_window.translate((_window.width - _newLevelWidth)/2, _window.height - _newLevelHeight);
//
//		_window.scale(_scaleFactor, _scaleFactor);

		_level.draw();		
		_window.cursor();

		_window.popMatrix();

		_window.pushMatrix();

		_window.fill(255, 150); // white
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

		_levelEditorTitle.draw();
		
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
