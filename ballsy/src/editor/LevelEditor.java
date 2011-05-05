package editor;

import interfascia.GUIController;
import interfascia.GUIEvent;
import interfascia.IFCheckBox;
import interfascia.IFLabel;
import interfascia.IFLookAndFeel;
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

	private EditorLevel _level;
	private ArrayList<ButtonGroup> _buttonGroups;
	private AbstractButton _cursorButton, _rectButton, _triangleButton, 
			_irregPolyButton, _ballButton, _pathButton;

	private float _newLevelWidth, _scaleFactor, _newLevelHeight;
	private Text _levelEditorTitle;
	
	private GUIController _mainC, _objectC, _rectC, _polyC, _ballC;
	private IFCheckBox _grappleableCheckBox, _deadlyCheckBox;
	private IFRadioButton _dynamicRadio, _staticRadio, _graphicalRadio;
	private IFLabel _gravityLabel, _frictionLabel, _bouncinessLabel, _densityLabel, 
				_centerXLabel, _centerYLabel, _rotationLabel,
				_widthLabel, _heightLabel, _sizeLabel, _radiusLabel;
	private IFTextField _gravity, _friction, _bounciness, _density,
				_centerX, _centerY, _rotation, _width, _height, _size, _radius;
	private BodyFactory _factory;
	
	@Override
	public void setup() {
		_factory = new BodyFactory();
		_level = new EditorLevel(_factory);
		_level.setup();
		
		_levelEditorTitle = new Text("Level Editor", 20, 60);
		_levelEditorTitle.setAlign(PConstants.LEFT);
		_levelEditorTitle.setSize(40);
		_levelEditorTitle.setColor(0);

		_newLevelWidth = _window.width - EditorConstants.LEFT_PANEL_WIDTH;
		_scaleFactor = _newLevelWidth/_window.width;
		_newLevelHeight = _window.height*_scaleFactor;
		_window.strokeJoin(PConstants.MITER); // according to docs this doesn't do anything in OpenGL mode
		
		int topPart = (int) (_window.height - _newLevelHeight);
		
		_buttonGroups = new ArrayList<ButtonGroup>();
		
		
		ButtonGroup shapeGroup = new ButtonGroup();
		_buttonGroups.add(shapeGroup);	
		
		_cursorButton = new CursorButton(_factory, 15,topPart,75,topPart+60);
		_cursorButton.setActive(true);
		shapeGroup.add(_cursorButton);
		
		_rectButton = new RectangleButton(_factory, 90,topPart,150,topPart+60);
		shapeGroup.add(_rectButton);
		
		_triangleButton = new TriangleButton(_factory, 15,topPart+75,75,topPart+135);
		shapeGroup.add(_triangleButton);
		
		_irregPolyButton = new IrregularPolygonButton(_factory, 90,topPart+75,150,topPart+135);
		shapeGroup.add(_irregPolyButton);
		
		_ballButton = new BallButton(_factory, 15,topPart+150,75,topPart+210);
		shapeGroup.add(_ballButton);
		
		_pathButton = new PathButton(_factory, 90,topPart+150,150,topPart+210);
		shapeGroup.add(_pathButton);
		
		///////// GUI STUFF: ////////////

		_mainC = new GUIController(_window,false);
		
		//look and feel
		IFLookAndFeel ballsyLook = new IFLookAndFeel(_window, IFLookAndFeel.DEFAULT);
		ballsyLook.baseColor = _window.color(255, 0);
		ballsyLook.highlightColor = _window.color(55, 100);	  
		ballsyLook.borderColor = _window.color(255);
		_mainC.setLookAndFeel(ballsyLook);
		_mainC.setVisible(false);

		
		//check boxes
		_grappleableCheckBox = new IFCheckBox("Grappleable", 15, topPart+240);
		_grappleableCheckBox.addActionListener(this);
		_deadlyCheckBox = new IFCheckBox("Deadly", 15, topPart+260);
		_deadlyCheckBox.addActionListener(this);	
		_mainC.add(_grappleableCheckBox);
		_mainC.add(_deadlyCheckBox);
		
		//radio buttons
		IFRadioController rc = new IFRadioController("Mode Selector");
		rc.addActionListener(this);
		int radioStart = topPart+300;
		_dynamicRadio = new IFRadioButton("Dynamic Object", 15, radioStart, rc);
		_staticRadio = new IFRadioButton("Static Object", 15, radioStart+20, rc);
		_graphicalRadio = new IFRadioButton("Graphical Only", 15, radioStart+40, rc);
		_graphicalRadio.setSelected();
		_mainC.add(_dynamicRadio);
		_mainC.add(_staticRadio);
		_mainC.add(_graphicalRadio);

		//universal properties
		float propertiesStart = _window.height - 120;
		
		_gravityLabel = new IFLabel("Level Gravity", 15, (int) propertiesStart);
		_gravity = new IFTextField("Gravity", 90, (int) propertiesStart - 4, 60);
		_gravity.setValue("-20");
		_gravity.addActionListener(this);
		_mainC.add(_gravityLabel);
		_mainC.add(_gravity);
		
		_frictionLabel = new IFLabel("Friction", 15, (int) propertiesStart + 30);
		_friction = new IFTextField("Friction", 90, (int) propertiesStart - 4 + 30, 60);
		_friction.setValue("1.3");
		_friction.addActionListener(this);
		_mainC.add(_frictionLabel);
		_mainC.add(_friction);
		
		_bouncinessLabel = new IFLabel("Bounciness", 15, (int) propertiesStart + 60);
		_bounciness = new IFTextField("Bounciness", 90, (int) propertiesStart - 4 + 60, 60);
		_bounciness.setValue("0.3");
		_bounciness.addActionListener(this);
		_mainC.add(_bouncinessLabel);
		_mainC.add(_bounciness);
		
		_densityLabel = new IFLabel("Density", 15, (int) propertiesStart + 90);
		_density = new IFTextField("Density", 90, (int) propertiesStart - 4 + 90, 60);
		_density.setValue("4.3");
		_density.addActionListener(this);
		_mainC.add(_densityLabel);
		_mainC.add(_density);
		
		
		//object controls- display when object is selected
		_objectC = new GUIController(_window);
		_objectC.setLookAndFeel(ballsyLook);
		int objectControlStart = topPart + 390;
		_centerXLabel = new IFLabel("Center X", 15, (int) objectControlStart);
		_centerX = new IFTextField("Center X", 90, (int) objectControlStart - 4, 60);
		_centerX.setValue("0.0");
		_centerX.addActionListener(this);
		_objectC.add(_centerXLabel);
		_objectC.add(_centerX);
	
		_centerYLabel = new IFLabel("Center Y", 15, (int) objectControlStart+30);
		_centerY = new IFTextField("Center Y", 90, (int) objectControlStart - 4 + 30, 60);
		_centerY.setValue("0.0");
		_centerY.addActionListener(this);
		_objectC.add(_centerYLabel);
		_objectC.add(_centerY);
		
		_rotationLabel = new IFLabel("Rotation", 15, (int) objectControlStart+60);
		_rotation = new IFTextField("Rotation", 90, (int) objectControlStart - 4 + 60, 60);
		_rotation.setValue("0.0");
		_rotation.addActionListener(this);
		_objectC.add(_rotationLabel);
		_objectC.add(_rotation);
		_objectC.setVisible(false);
		
		
		//rectangle controls- display when rectangle is selected
		_rectC = new GUIController(_window);
		_rectC.setLookAndFeel(ballsyLook);
		int customControlStart = topPart + 480;
		
		_widthLabel = new IFLabel("Width", 15, (int) customControlStart);
		_width = new IFTextField("Width", 90, (int) customControlStart - 4, 60);
		_width.setValue("0.0");
		_width.addActionListener(this);
		_rectC.add(_widthLabel);
		_rectC.add(_width);
		
		_heightLabel = new IFLabel("Height", 15, (int) customControlStart+30);
		_height = new IFTextField("Height", 90, (int) customControlStart - 4 + 30, 60);
		_height.setValue("0.0");
		_height.addActionListener(this);
		_rectC.add(_heightLabel);
		_rectC.add(_height);
		_rectC.setVisible(false);
		
		//polygon controls- display when irreg polygon is selected
		_polyC = new GUIController(_window);
		_polyC.setLookAndFeel(ballsyLook);
		
		_sizeLabel = new IFLabel("Size", 15, (int) customControlStart);
		_size = new IFTextField("Size", 90, (int) customControlStart - 4, 60);
		_size.setValue("0.0");
		_size.addActionListener(this);
		_polyC.add(_sizeLabel);
		_polyC.add(_size);
		_polyC.setVisible(false);
		
		//ball controls- display when ball is selected
		_ballC = new GUIController(_window);
		_ballC.setLookAndFeel(ballsyLook);
		
		_radiusLabel = new IFLabel("Radius", 15, (int) customControlStart);
		_radius = new IFTextField("Radius", 90, (int) customControlStart - 4, 60);
		_radius.setValue("0.0");
		_radius.addActionListener(this);
		_ballC.add(_radiusLabel);
		_ballC.add(_radius);
		_ballC.setVisible(false);
		
		this.addTopControls();
		
		


	}
	
	private void addTopControls(){
		ButtonGroup topControls = new ButtonGroup();
		_buttonGroups.add(topControls);
		
		float topPart = (_window.height - _newLevelHeight);
		
		float padding = (topPart - EditorConstants.TOP_BUTTONS_SIZE)/2;
		
		PlayButton playButton = new PlayButton(_factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE - padding), (int) padding, (int) (_window.width - padding), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(playButton);
		
		SaveButton saveButton = new SaveButton(_factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*2 - padding*2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE - padding*2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(saveButton);
		
		LoadButton loadButton = new LoadButton(_factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*3 - padding*3), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*2 - padding*3), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(loadButton);
		
		NewButton newButton = new NewButton(_factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*4 - padding*4), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*3 - padding*4), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(newButton);

	}
	
	public void actionPerformed(GUIEvent e){
		if (e.getSource() == _grappleableCheckBox) {
			//grappleable
		}
		else if (e.getSource() == _deadlyCheckBox) {
			  //deadly
		} 
		else if (e.getSource() == _dynamicRadio) {
			  //dynamic object
		} 
		else if (e.getSource() == _staticRadio) {
			//static object
		}
		else if (e.getSource() == _graphicalRadio) {
			//graphical object
		}
		else if (e.getSource() == _gravity) {
			//set gravity
		}
		else if (e.getSource() == _friction) {
			//set friction
		} 
		else if (e.getSource() == _bounciness) {
			//set bounciness
		} 
		else if (e.getSource() == _density) {
			//set density
		} 
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

		_level.draw();	
		this.onClose();
		
		if (!_level.isRunning()){

				
			_window.cursor();
	
			_window.pushMatrix();
	
			_window.fill(255, 100); // white
			_window.rectMode(PConstants.CORNER);
			_window.rect(0,0,EditorConstants.LEFT_PANEL_WIDTH,_window.height);
			_window.rect(EditorConstants.LEFT_PANEL_WIDTH, 0, _newLevelWidth, _window.height - _newLevelHeight);
	
			_window.translate(_window.width - _newLevelWidth, _window.height - _newLevelHeight);
			_window.rectMode(PConstants.CORNER);
			_window.strokeWeight(2);
			_window.stroke(255);
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
			
			
			//DISPLAY CUSTOM CONTROLS
			_objectC.setVisible(false);
			_rectC.setVisible(false);
			_polyC.setVisible(false);
			_ballC.setVisible(false);
			
			_mainC.setVisible(true);
	
			if (_rectButton.isClicked() || _triangleButton.isClicked() || _irregPolyButton.isClicked() || _ballButton.isClicked()) {
				_objectC.setVisible(true);
			}
			if (_rectButton.isClicked()) {
				_rectC.setVisible(true);
			}
			if (_triangleButton.isClicked() || _irregPolyButton.isClicked()) {
				_polyC.setVisible(true);
			}
			if (_ballButton.isClicked()) {
				_ballC.setVisible(true);
			}
		
		}
	}

	public void onClose() {
//		System.out.println("hiding shit");
		_mainC.setVisible(false);
		_objectC.setVisible(false);
		_rectC.setVisible(false);
		_polyC.setVisible(false);
		_ballC.setVisible(false);
	}
	
	@Override
	public void keyPressed() {
		
		if (_level.isRunning()) {
			// act as normal, unless they press r or escape
			if (_window.key == 27) { 
				_window.key = 0;
				_level.stop();
			} else _level.keyPressed();
		} 
		
		_level.keyPressed();
	}
	
	@Override
	public void keyReleased() {
		_level.keyReleased();
		
	}

	@Override
	public void mousePressed() {
		// Checks to ensure that a mouse click is not within the control area before passing down unless running
		if (_window.mouseX > _window.width - _newLevelWidth && _window.mouseY > _window.height - _newLevelHeight || _level.isRunning())
			_level.mousePressed();
		
		for (ButtonGroup group : _buttonGroups){
			group.click(_window.mouseX, _window.mouseY);
		}
	}

	@Override
	public void mouseReleased() {
		// Checks to ensure that a mouse click is not within the control area before passing down unless running
		if (_window.mouseX > _window.width - _newLevelWidth && _window.mouseY > _window.height - _newLevelHeight || _level.isRunning())
			_level.mouseReleased();
	}

	/**
	 * Pass shit to current level.
	 */
	public void handleCollision(Body b1, Body b2, float velocity) { 
		_level.handleCollision(b1, b2, velocity);
	}

	@Override
	public void mouseDragged() {
		// Checks to ensure that a mouse click is not within the control area before passing down unless running
		if (_window.mouseX > _window.width - _newLevelWidth && _window.mouseY > _window.height - _newLevelHeight || _level.isRunning())
			_level.mouseDragged();
	}


}
