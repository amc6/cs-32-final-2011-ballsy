package editor;

/**
 * The level editor screen. Beast of a class, mostly manages GUI and interaction
 * between the user and the editorLevel.
 */

import static editor.EditorConstants.ERROR;
import static editor.EditorConstants.INFO;
import static editor.EditorConstants.WARNING;
import graphics.Text;
import interfascia.GUIComponent;
import interfascia.GUIController;
import interfascia.GUIEvent;
import interfascia.IFButton;
import interfascia.IFCheckBox;
import interfascia.IFLabel;
import interfascia.IFLookAndFeel;
import interfascia.IFPGraphicsState;
import interfascia.IFRadioButton;
import interfascia.IFRadioController;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import physics.PhysicsBall;
import physics.PhysicsPolygon;
import physics.PhysicsRectangle;
import processing.core.PConstants;
import processing.core.PImage;
import ballsy.GeneralConstants;
import ballsy.Screen;
import ballsy.XMLUtil;
import ballsy.ScreenLoader.Screens;

public class LevelEditor extends Screen {

	private EditorLevel _level;
	private ArrayList<ButtonGroup> _buttonGroups;
	private AbstractButton _cursorButton, _rectButton, _triangleButton, 
			_irregPolyButton, _ballButton, _checkpointButton;
	private float _newLevelWidth, _scaleFactor, _newLevelHeight;
	private Text _errorMessage;
	private PImage _levelEditorTitle, _levelEditorBack;

	private GUIController _worldC, _togglesC, _mainC, _objectC, _rectC, _polyC, _sidesC, _ballC, _pathC, _pathbuttonC;
	private IFButton _pathButton;
	private IFCheckBox _grappleableCheckBox, _deadlyCheckBox;
	private IFRadioButton _dynamicRadio, _staticRadio, _graphicalRadio;
	private IFLabel _gravityXLabel, _gravityYLabel, _frictionLabel, _bouncinessLabel, _densityLabel, 
				_centerXLabel, _centerYLabel, _rotationLabel,
				_widthLabel, _heightLabel, _sizeLabel, _radiusLabel, _sidesLabel,
				_pathSpeedLabel, _pathRotationLabel, _worldWidthLabel, _worldHeightLabel;
	private TextField _gravityX, _gravityY, _friction, _bounciness, _density,
				_centerX, _centerY, _rotation, _width, _height, _size, _radius,
				_pathSpeed, _pathRotation, _worldWidth, _worldHeight, _sides;
	private BodyFactory _factory;
	private GUIComponent _componentWithFocus; // managed in draw step
	private ArrayList<GUIComponent> _components;
	private String _blankLevelPath = "res/GenericLevel.ball";
	
	/**
	 * Setup the screen, with all GUI elements and whatnot.
	 */
	public void setup() {
		_factory = new BodyFactory();
		_level = new EditorLevel(this, _factory);
		_level.setup();
		IFPGraphicsState temp = new IFPGraphicsState(_window);
		
		_components = new ArrayList<GUIComponent>();
			
		_levelEditorTitle = _window.loadImage("res/level_editor_title.png");
		_levelEditorBack = _window.loadImage("res/level_editor_back.png");

		_newLevelWidth = _window.width - EditorConstants.LEFT_PANEL_WIDTH;
		_scaleFactor = _newLevelWidth/_window.width;
		_newLevelHeight = _window.height*_scaleFactor;

		int topPart = (int) (_window.height - _newLevelHeight);
		
		_buttonGroups = new ArrayList<ButtonGroup>();

		///////// GUI STUFF: ////////////
		
		_errorMessage = new Text("", (int) (EditorConstants.LEFT_PANEL_WIDTH+5), _window.height-10);
		_errorMessage.setAlign(PConstants.LEFT);
		_errorMessage.setColor(_window.color(0));
		_errorMessage.setSize(0); //sets to STUPID_FONT. really need to redo this shit lol.
		
		_mainC = new GUIController(_window, false);

		//look and feel
		IFLookAndFeel ballsyLook = new IFLookAndFeel(_window, IFLookAndFeel.DEFAULT);
		ballsyLook.baseColor = _window.color(255, 0);
		ballsyLook.highlightColor = _window.color(55, 100);	  
		ballsyLook.borderColor = _window.color(150);
		ballsyLook.textBG = _window.color(255,100);
		_mainC.setLookAndFeel(ballsyLook);
		_mainC.setVisible(false);

		_togglesC = new GUIController(_window, false);
		_togglesC.setLookAndFeel(ballsyLook);
		_togglesC.setVisible(false);
		
		_worldC = new GUIController(_window, false);
		_worldC.setLookAndFeel(ballsyLook);
		_worldC.setVisible(false);
		
		//title: Shape Properties
		IFLabel shapeLabel = new IFLabel("Shape Properties:", 15, (int) topPart+30);
		_togglesC.add(shapeLabel);
		
		//check boxes
		int checkBoxStart = topPart + 60;
		_grappleableCheckBox = new IFCheckBox("Grappleable", 15, checkBoxStart);
		_grappleableCheckBox.addActionListener(this);
		_grappleableCheckBox.setSelected(true);
		_deadlyCheckBox = new IFCheckBox("Deadly", 15, checkBoxStart+20);
		_deadlyCheckBox.addActionListener(this);	
		_togglesC.add(_grappleableCheckBox);
		_togglesC.add(_deadlyCheckBox);
		
		//radio buttons
		IFRadioController rc = new IFRadioController("Mode Selector");
		rc.addActionListener(this);
		int radioStart = topPart+110;
		_dynamicRadio = new IFRadioButton("Dynamic Object", 15, radioStart, rc);
		_staticRadio = new IFRadioButton("Static Object", 15, radioStart+20, rc);
		_graphicalRadio = new IFRadioButton("Graphical Only", 15, radioStart+40, rc);
		_dynamicRadio.setSelected();

		_mainC.add(_dynamicRadio);
		_mainC.add(_staticRadio);
		//_mainC.add(_graphicalRadio); Future version maybe

		//more default shape properties
		int defaultStart = radioStart + 60;
		_frictionLabel = new IFLabel("Friction", 15, (int) defaultStart);
		_friction = new TextField("Friction", 90, (int) defaultStart - 4, 60, this);
		_friction.setValue(_factory.friction);
		_friction.addActionListener(this);
		_mainC.add(_frictionLabel);
		_mainC.add(_friction);
		
		_bouncinessLabel = new IFLabel("Bounciness", 15, (int) defaultStart + 30);
		_bounciness = new TextField("Bounciness", 90, (int) defaultStart - 4 + 30, 60, this);
		_bounciness.setValue(_factory.bounciness);
		_bounciness.addActionListener(this);
		_mainC.add(_bouncinessLabel);
		_mainC.add(_bounciness);
		
		_densityLabel = new IFLabel("Density", 15, (int) defaultStart + 60);
		_density = new TextField("Density", 90, (int) defaultStart - 4 + 60, 60, this);
		_density.setValue(_factory.density);
		_density.addActionListener(this);
		_mainC.add(_densityLabel);
		_mainC.add(_density);
		
		_rotationLabel = new IFLabel("Rotation", 15, (int) defaultStart + 90);
		_rotation = new TextField("Rotation", 90, (int) defaultStart - 4 + 90, 60, this);
		_rotation.setValue(_factory.rotation);
		_rotation.addActionListener(this);
		_mainC.add(_rotationLabel);
		_mainC.add(_rotation);
	
		
		//rectangle controls- display when rectangle is selected
		_rectC = new GUIController(_window);
		_rectC.setLookAndFeel(ballsyLook);
		int customControlStart = defaultStart + 120;
		
		_widthLabel = new IFLabel("Width", 15, (int) customControlStart);
		_width = new TextField("Width", 90, (int) customControlStart - 4, 60, this);
		_width.setValue(_factory.width);
		_width.addActionListener(this);
		_rectC.add(_widthLabel);
		_rectC.add(_width);
		
		_heightLabel = new IFLabel("Height", 15, (int) customControlStart+30);
		_height = new TextField("Height", 90, (int) customControlStart - 4 + 30, 60, this);
		_height.setValue(_factory.height);
		_height.addActionListener(this);
		_rectC.add(_heightLabel);
		_rectC.add(_height);
		_rectC.setVisible(false);
		
		//polygon controls- display when irreg polygon is selected
		_polyC = new GUIController(_window);
		_polyC.setLookAndFeel(ballsyLook);
		
		_sizeLabel = new IFLabel("Scale", 15, (int) customControlStart);
		_size = new TextField("Scale", 90, (int) customControlStart - 4, 60, this);
		_size.setValue(1);
		_size.addActionListener(this);
		_polyC.add(_sizeLabel);
		_polyC.add(_size);
		_polyC.setVisible(false);
		
		//ball controls- display when ball is selected
		_ballC = new GUIController(_window);
		_ballC.setLookAndFeel(ballsyLook);
		
		_radiusLabel = new IFLabel("Radius", 15, (int) customControlStart);
		_radius = new TextField("Radius", 90, (int) customControlStart - 4, 60, this);
		_radius.setValue(_factory.radius);
		_radius.addActionListener(this);
		_ballC.add(_radiusLabel);
		_ballC.add(_radius);
		_ballC.setVisible(false);
		
		//number of sides controller
		_sidesC = new GUIController(_window);
		_sidesC.setLookAndFeel(ballsyLook);
		
		_sidesLabel = new IFLabel("Sides", 15, (int) customControlStart + 30);
		_sides = new TextField("Radius", 90, (int) customControlStart - 4 + 30, 60, this);
		_sides.setValue(_factory.polyPointCount);
		_sides.addActionListener(this);
		_sidesC.add(_sidesLabel);
		_sidesC.add(_sides);
		_sidesC.setVisible(false);
		
		//object controls- display when object is selected
		_objectC = new GUIController(_window);
		_objectC.setLookAndFeel(ballsyLook);
		_objectC.setVisible(false);
		int objectControlStart = customControlStart + 60;
		_centerXLabel = new IFLabel("Center X", 15, (int) objectControlStart);
		_centerX = new TextField("Center X", 90, (int) objectControlStart - 4, 60, this);
		_centerX.setValue(0); // will be changed
		_centerX.addActionListener(this);
		_objectC.add(_centerXLabel);
		_objectC.add(_centerX);
	
		_centerYLabel = new IFLabel("Center Y", 15, (int) objectControlStart+30);
		_centerY = new TextField("Center Y", 90, (int) objectControlStart - 4 + 30, 60, this);
		_centerY.setValue(0); // will be changed
		_centerY.addActionListener(this);
		_objectC.add(_centerYLabel);
		_objectC.add(_centerY);
				
	
		_pathbuttonC = new GUIController(_window);
		_pathbuttonC.setLookAndFeel(ballsyLook);
		
		//path controls: display if there is a path
		_pathC = new GUIController(_window);
		_pathC.setLookAndFeel(ballsyLook);
		int pathStart = objectControlStart + 60;
		
		_pathButton = new IFButton("Add Path", 15, pathStart, (int) (EditorConstants.LEFT_PANEL_WIDTH-30));
		_pathButton.addActionListener(this);
		_pathbuttonC.add(_pathButton);
		
		_pathSpeedLabel = new IFLabel("Path Speed", 15, (int) pathStart + 30);
		_pathSpeed = new TextField("Path Speed", 90, (int) pathStart - 4 + 30, 60, this);
		_pathSpeed.setValue(0);
		_pathSpeed.addActionListener(this);
		_pathC.add(_pathSpeedLabel);
		_pathC.add(_pathSpeed);
		
		_pathRotationLabel = new IFLabel("Rotation Sp.", 15, (int) pathStart + 60);
		_pathRotation = new TextField("Rotation Speed", 90, (int) pathStart - 4+ 60, 60, this);
		_pathRotation.setValue(0);
		_pathRotation.addActionListener(this);
		_pathC.add(_pathRotationLabel);
		_pathC.add(_pathRotation);
		_pathC.setVisible(false);
		
		//world properties
		float propertiesStart = _window.height - 120;
		IFLabel worldLabel = new IFLabel("World Properties:", 15, (int) propertiesStart-30);
		_worldC.add(worldLabel);
		
		_worldWidthLabel = new IFLabel("Width", 15, (int) propertiesStart);
		_worldWidth = new TextField("World Width", 90, (int) propertiesStart - 4, 60, this);
		_worldWidth.setValue(_level.getWorldWidth());
		_worldWidth.addActionListener(this);
		_worldC.add(_worldWidthLabel);
		_worldC.add(_worldWidth);
		
		_worldHeightLabel = new IFLabel("Height", 15, (int) propertiesStart + 30);
		_worldHeight = new TextField("World Height", 90, (int) propertiesStart - 4 + 30, 60, this);
		_worldHeight.setValue(_level.getWorldHeight());
		_worldHeight.addActionListener(this);
		_worldC.add(_worldHeightLabel);
		_worldC.add(_worldHeight);
		
		_gravityXLabel = new IFLabel("Gravity X", 15, (int) propertiesStart + 60);
		_gravityX = new TextField("Gravity X", 90, (int) propertiesStart - 4 + 60, 60, this);
		_gravityX.setValue(_level.getGravity().x);
		_gravityX.addActionListener(this);
		_worldC.add(_gravityXLabel);
		_worldC.add(_gravityX);
		
		_gravityYLabel = new IFLabel("Gravity Y", 15, (int) propertiesStart + 90);
		_gravityY = new TextField("Gravity Y", 90, (int) propertiesStart - 4 + 90, 60, this);
		_gravityY.setValue(_level.getGravity().y);
		_gravityY.addActionListener(this);
		_worldC.add(_gravityYLabel);
		_worldC.add(_gravityY);
		
		this.addTopControls();

		_components.addAll(_sidesC.getComponents());
		_components.addAll(_mainC.getComponents());
		_components.addAll(_togglesC.getComponents());
		_components.addAll(_objectC.getComponents());
		_components.addAll(_rectC.getComponents());
		_components.addAll(_polyC.getComponents());
		_components.addAll(_ballC.getComponents());
		_components.addAll(_pathC.getComponents());
		_components.addAll(_pathbuttonC.getComponents());
		
		temp.restoreSettingsToApplet(_window);
	
		this.clear(); // show default level
	}
	
	/**
	 * Add the controls on the top bar of the screen (shape creation, load, save, etc.)
	 */
	private void addTopControls(){
		ButtonGroup topControls = new ButtonGroup();
		_buttonGroups.add(topControls);
		
		float topPart = (_window.height - _newLevelHeight);
		
		float padding = (topPart - EditorConstants.TOP_BUTTONS_SIZE)/2;
		
		PlayButton playButton = new PlayButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE - padding/2), (int) padding, (int) (_window.width - padding/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(playButton);
		
		SaveButton saveButton = new SaveButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*2 - padding*2/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE - padding*2/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(saveButton);
		
		LoadButton loadButton = new LoadButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*3 - padding*3/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*2 - padding*3/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(loadButton);
		
		NewButton newButton = new NewButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*4 - padding*4/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*3 - padding*4/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		topControls.add(newButton);
		
		ButtonGroup shapeGroup = new ButtonGroup();
		_buttonGroups.add(shapeGroup);	
		
		_cursorButton = new CursorButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*12 - padding*6/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*11 - padding*6/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		_cursorButton.setActive(true);
		shapeGroup.add(_cursorButton);
		
		_rectButton = new RectangleButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*11 - padding*5/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*10 - padding*5/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		shapeGroup.add(_rectButton);
		
		_triangleButton = new TriangleButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*10 - padding*4/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*9 - padding*4/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		shapeGroup.add(_triangleButton);
		
		_irregPolyButton = new IrregularPolygonButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*9 - padding*3/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*8 - padding*3/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		shapeGroup.add(_irregPolyButton);
		
		_ballButton = new BallButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*8 - padding*2/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*7 - padding*2/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		shapeGroup.add(_ballButton);
		
		_checkpointButton = new CheckpointButton(this, _factory, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*7 - padding*1/2), (int) padding, (int) (_window.width - EditorConstants.TOP_BUTTONS_SIZE*6 - padding*1/2), (int) (padding + EditorConstants.TOP_BUTTONS_SIZE));
		shapeGroup.add(_checkpointButton);
	}
	
	/**
	 * Sets the notification in the bottom of the screen.
	 * @param message
	 * @param errorness
	 */
	public void setErrorMessage(String message, int errorness) {
		switch(errorness) {
		case ERROR:
			_errorMessage.setColor(_window.color(255,0,0));
			break;
		case WARNING:
			_errorMessage.setColor(_window.color(255,255,0));
			break;
		case INFO:
			_errorMessage.setColor(_window.color(80,80,80));
		}
		_errorMessage.setText(message);
	}
	
	/**
	 * Listener for actions performed on GUI elements.
	 * take appropriate actions here!
	 * @param e
	 */
	public void actionPerformed(GUIEvent e){
		
		if (e.getSource() == _grappleableCheckBox) {
			//grappleable
			
			if (_graphicalRadio.isSelected()) {
				_grappleableCheckBox.setSelected(false);
				return;
			}
			
			if (_level.getSelected() == null) {
				//nothing is selected
				_factory.grappleable = _grappleableCheckBox.isSelected();
			} else {
				// Only set attribute if body is not the user's ball or the end
				if (!(_level.getSelected() instanceof bodies.UserBall) && !(_level.getSelected() instanceof bodies.EndPoint)){
					_level.getSelected().setGrappleable(_grappleableCheckBox.isSelected());
				}else{
					_grappleableCheckBox.setSelected(false);
				}
			}
			
		}
		else if (e.getSource() == _deadlyCheckBox) {
			//deadly
			
			if (_graphicalRadio.isSelected()) {
				_deadlyCheckBox.setSelected(false);
				return;
			}
			if (_level.getSelected() == null) {
				//nothing is selected
				_factory.deadly = _deadlyCheckBox.isSelected();
			} else {
				// Only set attribute if body is not the user's ball or the end
				if (!(_level.getSelected() instanceof bodies.UserBall) && !(_level.getSelected() instanceof bodies.EndPoint)){
					_level.getSelected().setDeadly(_deadlyCheckBox.isSelected());
				}else {
					_deadlyCheckBox.setSelected(false);
				}
			}
		} 
		else if (e.getSource() == _dynamicRadio) {
			  //dynamic object
			if (_level.getSelected() == null) {
				//nothing is selected
				_factory.dynamic = _dynamicRadio.isSelected();
				_factory.graphicalOnly = false;
			} else {
				
				_level.getSelected().getPhysicsDef().setMobile(true);
				if (_level.getSelected().getPath() != null)
					_level.getSelected().getPath().setStatic(false);
				
				_level.getSelected().getPhysicsDef().setGraphicalOnly(false);
			}
		} 
		else if (e.getSource() == _staticRadio) {
			//static object
			if (_level.getSelected() == null) {
				//nothing is selected
				_factory.dynamic = _dynamicRadio.isSelected();
				_factory.graphicalOnly = false;
			} else {
				// Only set attribute if body is not the user's ball or the end
				if (!(_level.getSelected() instanceof bodies.UserBall)){
					_level.getSelected().getPhysicsDef().setMobile(false);
					if (_level.getSelected().getPath() != null)
						_level.getSelected().getPath().setStatic(true);
					
					_level.getSelected().getPhysicsDef().setGraphicalOnly(false);
				}else{
					_dynamicRadio.setSelected(); // don't change the userball off of dynamic
				}
			}
		}
		else if (e.getSource() == _graphicalRadio) {
			//graphical object
			if (_level.getSelected() == null) {
				//nothing is selected
				_factory.graphicalOnly = _graphicalRadio.isSelected();
				_factory.grappleable = false;
				_factory.deadly = false;
				_grappleableCheckBox.setSelected(false);
				_deadlyCheckBox.setSelected(false);
			} else {
				// Only set attribute if body is not the user's ball or the end
				if (!(_level.getSelected() instanceof bodies.UserBall) && !(_level.getSelected() instanceof bodies.EndPoint)){
					_level.getSelected().getPhysicsDef().setGraphicalOnly(_graphicalRadio.isSelected());
					_level.getSelected().setDeadly(false);
					_level.getSelected().setGrappleable(false);
					_grappleableCheckBox.setSelected(false);
					_deadlyCheckBox.setSelected(false);
				}else{
					if (_level.getSelected().getPhysicsDef().getMobile())
						_dynamicRadio.setSelected();
					else if (!_level.getSelected().getPhysicsDef().getMobile())
						_staticRadio.setSelected();
				}
			}
		}
		else if (e.getSource() == _pathButton) {
			if (_pathButton.getLabel().equals("Add Path")) {
				_pathButton.setLabel("End Path");
				_level.startPoints();
			}
			else if (_pathButton.getLabel().equals("End Path")) {
				_pathButton.setLabel("Remove Path");
				_level.handleRightClick();
			}
			else if (_pathButton.getLabel().equals("Remove Path")) {
				_pathButton.setLabel("Add Path");
				_level.getSelected().clearPath();
			}	
		}	
	}
	
	/**
	 * Called when a text field loses its focus, we'll want to set whatever value is
	 * indicated therein
	 * @param e
	 */
	public void focusLost(GUIComponent e){
		if (e == _gravityX) {
			//set gravity
			if (LevelEditor.isValidNumber(_gravityX.getValue())){
				_level.setGravity(new Vec2(Float.parseFloat(_gravityX.getValue()),_level.getGravity().y));
			}
		}
		else if (e == _gravityY) {
			//set gravity
			if (LevelEditor.isValidNumber(_gravityY.getValue())){
				_level.setGravity(new Vec2(_level.getGravity().x,Float.parseFloat(_gravityY.getValue())));
			}
		} 
		else if (e == _bounciness) {
			// set bounciness
			if (LevelEditor.isPositive(_bounciness.getValue())){
				if (_level.getSelected() == null) {
					_factory.bounciness = Float.parseFloat(_bounciness.getValue());
				} else {
					_level.getSelected().getPhysicsDef().setBounciness(Float.parseFloat(_bounciness.getValue()));
				}
			}
		} 
		else if (e == _density) {
			// set density
			if (LevelEditor.isPositive(_density.getValue())){
				if (_level.getSelected() == null) {
					_factory.density = Float.parseFloat(_density.getValue());
				} else {
					_level.getSelected().getPhysicsDef().setDensity(Float.parseFloat(_density.getValue()));
				}
			}
		}
		else if (e == _friction) {
			// set friction
			if (LevelEditor.isPositive(_friction.getValue())){
				if (_level.getSelected() == null) {
					_factory.friction = Float.parseFloat(_friction.getValue());
				} else {
					_level.getSelected().getPhysicsDef().setFriction(Float.parseFloat(_friction.getValue()));
				}
			}
		}
		else if (e == _centerX) {
			// set x pos
			if (LevelEditor.isValidNumber(_centerX.getValue())){
				if (_level.getSelected() != null) {
					Vec2 newPos = new Vec2(Float.parseFloat(_centerX.getValue()), _level.getSelected().getPhysicsDef().getBodyWorldCenter().y);
					_level.getSelected().getPhysicsDef().setBodyWorldCenter(newPos);
				} else if (_level.getSelectedCP() != null) {
					_level.getSelectedCP().setPos(Float.parseFloat(_centerX.getValue()), _level.getSelectedCP().center().y);
				}
			}
		}
		else if (e == _centerY) {
			// set y pos
			if (LevelEditor.isValidNumber(_centerY.getValue())){
				if (_level.getSelected() != null) {
					Vec2 newPos = new Vec2(_level.getSelected().getPhysicsDef().getBodyWorldCenter().x, Float.parseFloat(_centerY.getValue()));
					_level.getSelected().getPhysicsDef().setBodyWorldCenter(newPos);
				} else if (_level.getSelectedCP() != null) {
					_level.getSelectedCP().setPos(_level.getSelectedCP().center().x, Float.parseFloat(_centerY.getValue()));
				}
			}
		}
		else if (e == _rotation) {
			// set rotation from degrees to radians
			if (LevelEditor.isValidNumber(_rotation.getValue())){
				if (_level.getSelected() == null) {
					_factory.rotation = (float) Math.toRadians(Float.parseFloat(_rotation.getValue()));
				} else {
					_level.getSelected().getPhysicsDef().setRotation((float) Math.toRadians(Float.parseFloat(_rotation.getValue())));
				}
			}
		}
		else if (e == _width) {
			// set width of rect
			if (LevelEditor.isPositive(_width.getValue())){
				if (_level.getSelected() == null) {
					_factory.width = Float.parseFloat(_width.getValue());
				} else {
					// assume this has a width
					PhysicsRectangle physicsDef = (PhysicsRectangle) _level.getSelected().getPhysicsDef();
					physicsDef.setWidth(Float.parseFloat(_width.getValue()));
				}
			}
		}
		else if (e == _height) {
			// set height of rect
			if (LevelEditor.isPositive(_height.getValue())){
				if (_level.getSelected() == null) {
					_factory.height = Float.parseFloat(_height.getValue());
				} else {
					// assume this has a width
					PhysicsRectangle physicsDef = (PhysicsRectangle) _level.getSelected().getPhysicsDef();
					physicsDef.setHeight(Float.parseFloat(_height.getValue()));
				}
			}
		}
		else if (e == _size) {
			// set size of polygon
			if (LevelEditor.isPositive(_size.getValue())){
				// assume a polygon
				PhysicsPolygon physicsDef = (PhysicsPolygon) _level.getSelected().getPhysicsDef();
				physicsDef.setScale(Float.parseFloat(_size.getValue()));
			}
		}
		else if (e == _sides) {
			// set number of sides
			if (LevelEditor.isGreaterOrEqual(_sides.getValue(), 3)){ // minimum of three sides
				// assume we're not selecting anything
				_factory.polyPointCount = Integer.parseInt(_sides.getValue());
			}
		}
		else if (e == _radius) {
			// set radius of circle
			if (LevelEditor.isPositive(_radius.getValue())){
				if (_level.getSelected() == null) {
					_factory.radius = Float.parseFloat(_radius.getValue());
				} else {
					// assume this has a radius
					PhysicsBall physicsDef = (PhysicsBall) _level.getSelected().getPhysicsDef();
					physicsDef.setRadius(Float.parseFloat(_radius.getValue()));
				}
			}
		}
		else if (e == _pathSpeed) {
			// set speed of path
			if (LevelEditor.isPositive(_pathSpeed.getValue())){
				if (_level.getSelected() != null) {
					_level.getSelected().getPath().setVelCoeff(Float.parseFloat(_pathSpeed.getValue()));
				}
			}
		}
		else if (e == _pathRotation) {
			// path
			if (LevelEditor.isValidNumber(_pathRotation.getValue())){
				if (_level.getSelected() != null) {
					_level.getSelected().getPath().setRotation((float) Math.toRadians(Float.parseFloat(_pathRotation.getValue())));
				}
			}
		}
		else if (e == _worldWidth) {
			//set world width 
			if (LevelEditor.isGreaterLessEqual(_worldWidth.getValue(), 150, 400)){
				_level.updateWorldDimensions(Float.parseFloat(_worldWidth.getValue()), _level.getWorldHeight());
			}
		}
		else if (e == _worldHeight) {
			//set world gravity
			if (LevelEditor.isGreaterLessEqual(_worldHeight.getValue(), 100, 400)){ 
				_level.updateWorldDimensions(_level.getWorldWidth(), Float.parseFloat(_worldHeight.getValue()));
			}
		}
		this.updateFieldValues(); // update field values, thus if user entered something wrong, it will get overwritten
	}
	
	/**
	 * Remove the GUI stuffs.
	 */
	public void clear() {
		XMLUtil.getInstance().readFile(_level, _blankLevelPath);
		_level.createBorders();
		_level.play();
		_level.stop();
		_level.centerCamera();
	}

	/**
	 * Draw everything, if level isn't running.
	 */
	public void draw() {
		
		_level.draw();
		
		this.onClose();
		
		if (!_level.isRunning()){
			_window.cursor();
	
			_window.pushMatrix();
	
			_window.fill(255, 150); // white
			_window.stroke(255, 0);
			_window.rectMode(PConstants.CORNER);
			_window.rect(0,0,EditorConstants.LEFT_PANEL_WIDTH,_window.height);
			_window.rect(EditorConstants.LEFT_PANEL_WIDTH, 0, _newLevelWidth, _window.height - _newLevelHeight);
			
			_window.translate(_window.width - _newLevelWidth, _window.height - _newLevelHeight);
			_window.strokeWeight(2);
			_window.stroke(255);
			_window.rectMode(PConstants.CORNER);
			_window.rect(0, _newLevelHeight - 30, _newLevelWidth, 30);
			_window.noFill();
			_window.rect(0,0,_newLevelWidth,_newLevelHeight);
			_window.strokeWeight(GeneralConstants.DEFAULT_LINE_WIDTH);
			_window.popMatrix();
	
			_window.imageMode(PConstants.CORNER);
			_window.image(_levelEditorTitle, 45, 17);
			
			//if hover
			int mouseX = _window.mouseX;
			int mouseY = _window.mouseY;
			if (mouseX < 40 && mouseY > 17 && mouseY < 80) {
				_window.image(_levelEditorBack, -12, 18);
			}
			else {
				_window.image(_levelEditorBack, -15, 18);
			}


			for (ButtonGroup group : _buttonGroups){
				group.setActiveTooltip();
			}
			
			for (ButtonGroup group : _buttonGroups){
				group.display();
				group.displayTooltips();
			}
			
			//DISPLAY CUSTOM CONTROLS
			_objectC.setVisible(false);
			_rectC.setVisible(false);
			_polyC.setVisible(false);
			_ballC.setVisible(false);
			_pathC.setVisible(false);
			_pathbuttonC.setVisible(false);
			_sidesC.setVisible(false);
			
			_togglesC.setVisible(true);
			_worldC.setVisible(true);
			
			if (_level.getSelectedCP() != null) {
				_objectC.setVisible(true);
				_togglesC.setVisible(false);
			} else if (_checkpointButton.isClicked()) {
				_togglesC.setVisible(false);
			} else if (!(_level.getSelected() != null && _level.isBorder(_level.getSelected()))){
				_mainC.setVisible(true);
	
		
				if (_level.getSelected() != null){
					_objectC.setVisible(true);
				}
				if (_level.getSelected() != null && !(_level.getSelected() instanceof bodies.UserBall)){
					_pathbuttonC.setVisible(true);
				}
				if (_rectButton.isClicked() || _level.getSelected() instanceof bodies.Rectangle) {
					_rectC.setVisible(true);
				}
				if (_level.getSelected() instanceof bodies.IrregularPolygon || _level.getSelected() instanceof bodies.RegularPolygon) {
					_polyC.setVisible(true);
				}
				if (_triangleButton.isClicked() || _ballButton.isClicked() || _level.getSelected() instanceof bodies.Ball) {
					_ballC.setVisible(true);
				}
				if (_triangleButton.isClicked()){
					_sidesC.setVisible(true);
				}			
				if (_level.getSelected() != null && _level.getSelected().getPath() != null) {
					_pathC.setVisible(true);
				}
			}
			_errorMessage.draw();
		}
	}

	/**
	 * Destructor, if you will.
	 */
	public void onClose() {
		_mainC.setVisible(false);
		_togglesC.setVisible(false);
		_worldC.setVisible(false);
		_objectC.setVisible(false);
		_rectC.setVisible(false);
		_polyC.setVisible(false);
		_sidesC.setVisible(false);
		_ballC.setVisible(false);
		_pathC.setVisible(false);
		_pathbuttonC.setVisible(false);
	}
	
	/**
	 * Key pressed, handle. Manage esc differently.
	 */
	public void keyPressed() {
		if (_level.isRunning()) {
			// act as normal, unless they press r or escape
			if (_window.key == 27) { 
				_window.key = 0;
				_level.stop();
			} else _level.keyPressed();
		} else if (_level.selectingPoints()) {
			this.updateFieldValues();
		}
		_level.keyPressed();
	}
	
	
	@Override
	public void keyReleased() {
		_level.keyReleased();
		
	}

	/**
	 * handle the mouse pressed (pass to correct elements)
	 */
	public void mousePressed() {
		
		if (_componentWithFocus != null && !_componentWithFocus.isMouseOver(_window.mouseX, _window.mouseY)){
			this.focusLost(_componentWithFocus);
			_componentWithFocus = null;
		}
		
		for (GUIComponent comp : _components){
			if (comp.isMouseOver(_window.mouseX, _window.mouseY) && comp.getController().getVisible()){
				_componentWithFocus = comp;
				break;
			}
		}
		
		// Checks to ensure that a mouse click is not within the control area before passing down unless running
		if (_window.mouseX > _window.width - _newLevelWidth && _window.mouseY > _window.height - _newLevelHeight || _level.isRunning())
			_level.mousePressed();
		
		if (!_level.isRunning()){
			for (ButtonGroup group : _buttonGroups){
				group.click(_window.mouseX, _window.mouseY);
			}
			//stupid back button hack lol.
			int mouseX = _window.mouseX;
			int mouseY = _window.mouseY;
			if (mouseX < 40 && mouseY > 17 && mouseY < 80) {
				_window.loadScreen(Screens.WELCOME_SCREEN);
			}
		}
	}

	@Override
	public void mouseReleased() {
		// Checks to ensure that a mouse click is not within the control area before passing down unless running
		if (_window.mouseX > _window.width - _newLevelWidth && _window.mouseY > _window.height - _newLevelHeight || _level.isRunning())
		// uncomment the above to do selective mouseRelease passing... as it is, this is less buggy to do it all the time
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
	
	/* functions for handling input to text fields, making sure they're right and whatnot.... */
	
	private static boolean isValidNumber(String string){
		string = string.trim();
		return (string.matches("((\\-)?[0-9]*(\\.[0-9]*)?)") && !string.equals("") && !string.equals(".") && !string.equals("-"));
	}
	
	private static boolean isPositive(String string){
		return LevelEditor.isValidNumber(string) && Float.parseFloat(string) > 0;
	}
	
	private static boolean isGreaterOrEqual(String string, float min){
		return LevelEditor.isValidNumber(string) && Float.parseFloat(string) >= min;
	}
	
	private static boolean isGreaterLessEqual(String string, float min, float max){
		return LevelEditor.isValidNumber(string) && Float.parseFloat(string) >= min && Float.parseFloat(string) <= max;
	}
	
	/**
	 * Gather what the field values should be from editor level, and set them.
	 */
	public void updateFieldValues(){
		// If no shape is selected we want to display factory values
		if (_level.getSelected() == null && _level.getSelectedCP() == null) {
			
			//check boxes
			_grappleableCheckBox.setSelected(_factory.grappleable);
			_deadlyCheckBox.setSelected(_factory.deadly);
			
			//radio buttons
			if (_factory.graphicalOnly) _graphicalRadio.setSelected();
			else if(_factory.dynamic) _dynamicRadio.setSelected();
			else _staticRadio.setSelected();
			
			//text fields
			_density.setValue(_factory.density);
			_friction.setValue(_factory.friction);
			_bounciness.setValue(_factory.bounciness);
			
			_rotation.setValue((float) Math.toDegrees(_factory.rotation));
			
			// For rectangles
			_width.setValue(_factory.width);
			_height.setValue(_factory.height);
			
			// For circle
			_radius.setValue(_factory.radius);

			// For polygon sides
			_sides.setValue((_factory.polyPointCount));
					
		} else if (_level.getSelectedCP() != null) { // if a checkpoint is selected..
			//text fields
			_centerX.setValue(_level.getSelectedCP().center().x);
			_centerY.setValue(_level.getSelectedCP().center().y);
		} else if (_level.getSelected() != null) { //Alternatively if a shape is selected, display its values

			//check boxes
			_grappleableCheckBox.setSelected(_level.getSelected().isGrappleable());
			_deadlyCheckBox.setSelected(_level.getSelected().isDeadly());
			
			//radio buttons
			if(_level.getSelected().getPhysicsDef().getGraphicalOnly()) _graphicalRadio.setSelected();
			else if(_level.getSelected().getPhysicsDef().getMobile()) _dynamicRadio.setSelected();
			else _staticRadio.setSelected();
			
			
			//text fields
			_density.setValue(_level.getSelected().getPhysicsDef().getDensity());
			_friction.setValue(_level.getSelected().getPhysicsDef().getFriction());
			_bounciness.setValue(_level.getSelected().getPhysicsDef().getBounciness());
			
			_centerX.setValue(_level.getSelected().getPhysicsDef().getBodyWorldCenter().x);
			_centerY.setValue(_level.getSelected().getPhysicsDef().getBodyWorldCenter().y);
			_rotation.setValue((float) Math.toDegrees(_level.getSelected().getPhysicsDef().getRotation()));
			
			// For rectangles
			if (_level.getSelected().getPhysicsDef() instanceof PhysicsRectangle){
				_width.setValue(((PhysicsRectangle) _level.getSelected().getPhysicsDef()).getWidth());
				_height.setValue(((PhysicsRectangle) _level.getSelected().getPhysicsDef()).getHeight());
			}
			
			// For circle
			if (_level.getSelected().getPhysicsDef() instanceof PhysicsBall){
				_radius.setValue(((PhysicsBall) _level.getSelected().getPhysicsDef()).getRadius());
			}

			// For polygons
			if (_level.getSelected().getPhysicsDef() instanceof PhysicsPolygon){
				_size.setValue(((PhysicsPolygon) _level.getSelected().getPhysicsDef()).getScale());
			}

			
			if (_level.getSelected().getPath() != null){
				_pathSpeed.setValue(_level.getSelected().getPath().getVelCoeff());
				_pathRotation.setValue((float) Math.toDegrees(_level.getSelected().getPath().getRotation()));
				_pathButton.setLabel("Remove Path");
			} else {
				if (_pathButton.getLabel().equals("Remove Path") || (_pathButton.getLabel().equals("End Path") && !_level.selectingPoints())) 
					_pathButton.setLabel("Add Path");
			}
		}		
		
		// Properties that never change regardless of selection
		_worldWidth.setValue(_level.getWorldWidth());
		_worldHeight.setValue(_level.getWorldHeight());
		_gravityX.setValue(_level.getGravity().x);
		_gravityY.setValue(_level.getGravity().y);

	}
	
	/**
	 * Alters the clickedness of cursor button, so we can represent selection of 
	 * newly created objects in the level editor.
	 * @param b
	 */
	public void setCursorButton(boolean b) {
		_cursorButton.setActive(b);
		if (b) {
			_rectButton.setActive(false);
			_triangleButton.setActive(false);
			_irregPolyButton.setActive(false);
			_ballButton.setActive(false);
			_checkpointButton.setActive(false);
		}
	}
}
