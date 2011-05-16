package ballsy;

/**
 * Class to contain all XML I/O functionality. Both read and write take in a level and modify
 * it according to a specified file.
 */

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import menu.MenuButton;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PhysicsPath;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.EndPoint;
import bodies.IrregularPolygon;
import bodies.Rectangle;
import bodies.UserBall;

public class XMLUtil {
	private static XMLUtil XMLUTIL; // the one and only instance!
	
	// accessor/mutator for singleton
	public static void setInstance(XMLUtil u) { XMLUTIL = u; }
	public static XMLUtil getInstance() { return XMLUTIL; }
	
	/**
	 * loads menu buttons from xml
	 * If loadCustom, then it will only load the custom, and if not 
	 * loadCustom, it will only load the non-custom levels
	 * @return
	 */
	public Vector<MenuButton> loadMenuButtons(boolean loadCustom) {
    	Vector<MenuButton> _buttons = new Vector<MenuButton>();
		String path = "builtin_levels/list.xml";
    	SAXReader reader = new SAXReader();
    	Document doc;
		try { doc = reader.read(new FileReader(path));} 
		catch (FileNotFoundException e) { 
			// File not found
			return _buttons; } 
		catch (DocumentException e) { 
			// Document exception
			return _buttons; }
    	Element root = doc.getRootElement();
    	MenuButton lastButton = null;
    	for (Iterator i = root.elementIterator("LEVEL"); i.hasNext();) {
    		// get references to the various elements, and make new buttons out of them
    		Element currLevel = (Element) i.next();
    		String levelPath = currLevel.attributeValue("PATH");
    		String thumbPath = currLevel.attributeValue("THUMBNAIL");
    		Boolean locked = Boolean.parseBoolean(currLevel.attributeValue("LOCKED"));
    		Boolean custom = Boolean.parseBoolean(currLevel.attributeValue("CUSTOM"));
    		MenuButton currButton = new MenuButton(levelPath, thumbPath);
    		currButton.setCustom(custom);
    		currButton.setLocked(locked);
    		if (loadCustom == custom) {
	    		if (lastButton != null) {
	    			lastButton.setNextLevel(currButton);
	    		}
	    		lastButton = currButton;
	    		_buttons.add(lastButton);
    		}
    	}
    	// return the compiled list of buttons
		return _buttons;
	}
	
	/**
	 * Returns all menubuttons, both the custom and non-custom
	 * @return
	 */
	public Vector<MenuButton> loadMenuButtons() {
		Vector<MenuButton> custom = loadMenuButtons(true);
		Vector<MenuButton> builtIn = loadMenuButtons(false);
		custom.addAll(builtIn); // combine the two vectors
		return custom;
	}
	
	/**
	 * Saves menu buttons to xml
	 * @param buttons
	 * @return
	 */
	public boolean saveMenuButtons(Vector<MenuButton> buttons) {
		Document doc = DocumentHelper.createDocument();
		
		Element root = DocumentHelper.createElement("LEVELS_LIST");
		for (Iterator<MenuButton> i = buttons.iterator(); i.hasNext();) {
			MenuButton button = i.next();
			Element buttonEl = DocumentHelper.createElement("LEVEL");
			buttonEl.addAttribute("PATH", button.getLevelPath());
			buttonEl.addAttribute("THUMBNAIL", button.getThumbnailPath());
			buttonEl.addAttribute("LOCKED", Boolean.toString(button.isLocked()));
			buttonEl.addAttribute("CUSTOM", Boolean.toString(button.isCustom()));
			root.add(buttonEl);
		}
    	doc.setRootElement(root);
		// actually save the file, return false if IO failure
		OutputFormat pretty = OutputFormat.createPrettyPrint();
		try {
			// write that shit out, pretty style
			XMLWriter fileWriter = new XMLWriter(new FileWriter("builtin_levels/list.xml"), pretty);
			fileWriter.write(doc);
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			// something went horribly wrong
			return false;
		}
	}
	
	/**
	 * Add a menubutton to the list.
	 * @param buttonToAdd
	 */
	public void addMenuButton(MenuButton buttonToAdd) {
		String levelPath = buttonToAdd.getLevelPath();
		Vector<MenuButton> buttons = loadMenuButtons();
		boolean newButton = true;
		//check for existing. if so, overwrite
		for (Iterator<MenuButton> i = buttons.iterator(); i.hasNext();) {
			MenuButton button = i.next();
			if (levelPath.equals(button.getLevelPath())) {
				int index = buttons.indexOf(button);
				buttons.remove(button);
				buttons.add(index, buttonToAdd);
				newButton = false;
				break;
			}
		}
		if (newButton) {
			buttons.add(buttonToAdd);
		}
		saveMenuButtons(buttons);
	}
	
	/**
	 * Read in an XML file at path (formatted as in writeFile) into the provided level.
	 * Returns success of method.
	 * @param level
	 * @param path
	 */
	public boolean readFile(AbstractLevel level, String path) {
		// set up XML reader stuffs
    	SAXReader reader = new SAXReader();
    	Document doc;
		try { doc = reader.read(new FileReader(path));} 
		catch (FileNotFoundException e) { 
			// File not found
			return false; } 
		catch (DocumentException e) { 
			// Document exception
			return false; }
    	Element root = doc.getRootElement();
    	this.restoreXML(level, root);
    	return true;
	}
	
	/**
	 * Write out the provided level into an XML file at the provided path.
	 * Returns success of method.
	 * @param level
	 * @param path
	 * @return
	 */
	public boolean writeFile(AbstractLevel level, String path) {
		// set up the XML document, using dom4j
		Document doc = DocumentHelper.createDocument();
    	doc.setRootElement(genXML(level));
		// actually save the file, return false if IO failure
		OutputFormat pretty = OutputFormat.createPrettyPrint();
		try {
			// write that shit out, pretty style
			XMLWriter fileWriter = new XMLWriter(new FileWriter(path), pretty);
			fileWriter.write(doc);
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			// something went horribly wrong
			return false;
		}
	}
	
	public Element genXML(AbstractLevel level) {
		// get the bodies, to iterate through
		ArrayList<AbstractBody> bodies = level.getBodies();
		// move player to the end (for cursor shit)
		int index = bodies.indexOf(level.getPlayer());
		for (int i = index; i < bodies.size() - 1; i++) {
			Collections.swap(bodies, i, i + 1);
		}
		// make the element
		Element root = DocumentHelper.createElement("BALLSY_LEVEL");
		// add attributes pertinent to the level
		// physics world bounds
    	Vec2 min = level.getWorldBounds()[0];
		Vec2 max = level.getWorldBounds()[1];
		root.addAttribute("MIN_X", Float.toString(min.x));
		root.addAttribute("MIN_Y", Float.toString(min.y));
		root.addAttribute("MAX_X", Float.toString(max.x));
		root.addAttribute("MAX_Y", Float.toString(max.y));
		// bg color
		root.addAttribute("BG_COLOR", Integer.toString(level.getBGColor()));
		// gravity
		root.addAttribute("GRAV_X", Float.toString(level.getGravity().x));
		root.addAttribute("GRAV_Y", Float.toString(level.getGravity().y));
		// iterate through bodies, writing each's output xml to the document
		for (AbstractBody b : bodies) {
			Element newEl = b.writeXML();
			root.add(newEl);
		}
		
		return root;
	}
	
	public void restoreXML(AbstractLevel level, Element root) {
		// set up the eventual new stuff to be written into the level (after populated by XML parsing)
		ArrayList<AbstractBody> newBodies = new ArrayList<AbstractBody>();
		UserBall newPlayer = null;
		
		// clear current level bodies; needed to copy the current bodies into another vector to avoid concurrent modification
		if (level.getBodies() != null) {
	    	Vector<AbstractBody> vec = new Vector<AbstractBody>();
			for (AbstractBody b : level.getBodies()) { vec.add(b); }
			for (AbstractBody b : vec) { b.killBody(); }
		}
		// interpret level settings
		float minX = Float.parseFloat(root.attributeValue("MIN_X"));
		float minY = Float.parseFloat(root.attributeValue("MIN_Y"));
		float maxX = Float.parseFloat(root.attributeValue("MAX_X"));
		float maxY = Float.parseFloat(root.attributeValue("MAX_Y"));
		level.setupWorld(minX, minY, maxX, maxY);
		level.setBGColor(Integer.parseInt(root.attributeValue("BG_COLOR")));
		level.setGravity(new Vec2( Float.parseFloat(root.attributeValue("GRAV_X")), Float.parseFloat(root.attributeValue("GRAV_Y"))));
    	// iterate through elements of XML file
    	for (Iterator i = root.elementIterator("BODY"); i.hasNext();) {
    		// get references to the various elements
    		Element currBody = (Element) i.next();
    		Element currPhysDef = currBody.element("PHYSICS_DEF");
    		Element currGraphDef = currBody.element("GRAPHICAL_DEF");
    		Element currPathDef = currBody.element("PATH_DEF");
    		// get the values stored in these elements
    		String bodyType = currBody.attributeValue("TYPE");
    		boolean grappleable = Boolean.parseBoolean(currBody.attributeValue("GRAPPLEABLE"));
    		boolean endpoint = Boolean.parseBoolean(currBody.attributeValue("ENDPOINT"));
    		boolean deadly = Boolean.parseBoolean(currBody.attributeValue("DEADLY"));
    		int color = Integer.parseInt(currGraphDef.attributeValue("COLOR"));
    		float xPos = Float.parseFloat(currPhysDef.attributeValue("X"));
    		float yPos = Float.parseFloat(currPhysDef.attributeValue("Y"));
    		float rotation = Float.parseFloat(currPhysDef.attributeValue("ROTATION"));
    		float xVel = Float.parseFloat(currPhysDef.attributeValue("X_VELOCITY"));
    		float yVel = Float.parseFloat(currPhysDef.attributeValue("Y_VELOCITY"));
    		float aVel = Float.parseFloat(currPhysDef.attributeValue("ANGULAR_VELOCITY"));
    		float density = Float.parseFloat(currPhysDef.attributeValue("DENSITY"));
    		float friction = Float.parseFloat(currPhysDef.attributeValue("FRICTION"));
    		float restitution = Float.parseFloat(currPhysDef.attributeValue("RESTITUTION"));
    		float width = 0;
    		if (currPhysDef.attributeValue("WIDTH") != null)
    			width = Float.parseFloat(currPhysDef.attributeValue("WIDTH"));
    		float height = 0;
    		if (currPhysDef.attributeValue("WIDTH") != null)
    			height = Float.parseFloat(currPhysDef.attributeValue("HEIGHT"));
    		boolean mobile = Boolean.parseBoolean(currPhysDef.attributeValue("MOBILE"));
    		boolean graphicalOnly = Boolean.parseBoolean(currPhysDef.attributeValue("GRAPHICALONLY"));
    		// now reconstruct the present object into ballsy
    		Body body = null;
    		if (bodyType.compareTo("user_ball") == 0) {
    			// o hai userball, let's make you
    			newPlayer = new UserBall(xPos, yPos, width/2);
    			newPlayer.getGraphicsDef().setColor(color);
    			newPlayer.getPhysicsDef().setRotation(rotation);
    			body = newPlayer.getPhysicsDef().getBody();
    			newBodies.add(newPlayer); // add it to the bodies too!
    		} else if (bodyType.compareTo("end_point") == 0 || endpoint) {
    			EndPoint endPoint = new EndPoint(xPos,yPos,width,height);
    			endPoint.getGraphicsDef().setColor(color);
    			endPoint.getPhysicsDef().setRotation(rotation);
    			endPoint.setGrappleable(grappleable);
    			endPoint.setEndpoint(true);
    			endPoint.setDeadly(deadly);
    			this.setGeneralPhysicsProperties(endPoint, mobile, graphicalOnly, rotation, xVel, yVel, aVel, friction, restitution, density);
    			body = endPoint.getPhysicsDef().getBody();
    			newBodies.add(endPoint);
    		}
    		else if (bodyType.compareTo("ball") == 0) {
    			// it's a ball
    			Ball newBall = new Ball(xPos, yPos, width/2);
    			newBall.getGraphicsDef().setColor(color);
    			newBall.setGrappleable(grappleable);
    			newBall.setEndpoint(endpoint);
    			newBall.setDeadly(deadly);
    			// display the line if necessary
    			boolean showLine = Boolean.parseBoolean(currGraphDef.attributeValue("DISPLAY_LINE"));
    			((graphics.GraphicsBall) newBall.getGraphicsDef()).setLine(showLine);
    			this.setGeneralPhysicsProperties(newBall, mobile, graphicalOnly, rotation, xVel, yVel, aVel, friction, restitution, density);
    			body = newBall.getPhysicsDef().getBody();
    			newBodies.add(newBall);
    		} else if (bodyType.compareTo("rectangle") == 0) {
    			// it's a rectangle
    			Rectangle newRect = new Rectangle(xPos, yPos, width, height);
    			newRect.getGraphicsDef().setColor(color);
    			newRect.setGrappleable(grappleable);
    			newRect.setEndpoint(endpoint);
    			newRect.setDeadly(deadly);
    			this.setGeneralPhysicsProperties(newRect, mobile, graphicalOnly, rotation, xVel, yVel, aVel, friction, restitution, density);
    			body = newRect.getPhysicsDef().getBody();
    			newBodies.add(newRect);
    		} else if (bodyType.compareTo("regular_polygon") == 0 || bodyType.compareTo("irregular_polygon") == 0) {
    			// it's a regular polygon or irregular polygon (same here)
    			// get the list of points
    			ArrayList<Vec2> pointList = new ArrayList<Vec2>();
    			for (Iterator path_i = currPhysDef.elementIterator("POLYGON_POINT"); path_i.hasNext();) {
    				// iterate through polygon points, adding them to the vector
    				Element currPoint = (Element) path_i.next();
    				float x = Float.parseFloat(currPoint.attributeValue("X"));
    				float y = Float.parseFloat(currPoint.attributeValue("Y"));
    				pointList.add(new Vec2(x, y));
    			}
    			// now make the polygon
    			IrregularPolygon newPoly = new IrregularPolygon(xPos, yPos, pointList);
    			newPoly.getGraphicsDef().setColor(color);
    			newPoly.setGrappleable(grappleable);
    			newPoly.setEndpoint(endpoint);
    			newPoly.setDeadly(deadly);
    			this.setGeneralPhysicsProperties(newPoly, mobile, graphicalOnly, rotation, xVel, yVel, aVel, friction, restitution, density);
    			
    			body = newPoly.getPhysicsDef().getBody();
    			newBodies.add(newPoly);
    		} else if (bodyType.compareTo("vertex_surface") == 0) {
    			// it's a surface
    			
    			/////////////////////////////////////////////////////////////
    			// to be filled out if/when surfaces are complete, if ever //
    			/////////////////////////////////////////////////////////////
    			
    		}
    		// handle pathing
    		if (currPathDef != null) {
    			Vector<Point2D.Float> pathPoints = new Vector<Point2D.Float>();
    			// there is a path! We must resurrect it (paths are Jesus)
    			// start by getting all associated values
    			int currTarget = Integer.parseInt(currPathDef.attributeValue("CURRENT_TARGET"));
    			float velCoeff = Float.parseFloat(currPathDef.attributeValue("VEL_COEFF"));
    			float initX = Float.parseFloat(currPathDef.attributeValue("INITIAL_X")); 
    			float initY = Float.parseFloat(currPathDef.attributeValue("INITIAL_Y")); 
    			boolean isStatic = Boolean.parseBoolean(currPathDef.attributeValue("STATIC"));
    			float pathRotation = Float.parseFloat(currPathDef.attributeValue("ROTATION"));
    			for (Iterator path_i = currPathDef.elementIterator("PATH_POINT"); path_i.hasNext();) {
    				// iterate through path points, adding them to the vector
    				Element currPathPoint = (Element) path_i.next();
    				float x = Float.parseFloat(currPathPoint.attributeValue("X"));
    				float y = Float.parseFloat(currPathPoint.attributeValue("Y"));
    				pathPoints.add(new Point2D.Float(x, y));
    			}
    			AbstractBody lastBody = newBodies.get(newBodies.size()-1); // get the last body added to the list
    			// create a "new" path (new in form, not in feature) and set it's variables properly
    			PhysicsPath newPath = new PhysicsPath(lastBody.getPhysicsDef(), pathPoints);
    			newPath.setCurrTarget(currTarget);
    			newPath.setInitialPoint(new Point2D.Float(initX, initY));
    			newPath.setVelCoeff(velCoeff);
    			newPath.setRotation(pathRotation);
    			newPath.setStatic(isStatic);
    			lastBody.setPath(newPath); // apply the new path to this body
    		}
    	}
    	// set the bodies and player to be the new objects as determined above.
    	level.setBodies(newBodies);
    	level.setPlayer(newPlayer);
	}
	
	/**
	 * Helper method - sets up the provided body with a bunch of properties:
	 * @param body
	 * @param mobile
	 * @param graphicalOnly
	 * @param rotation
	 * @param xVel
	 * @param yVel
	 * @param aVel
	 * @param friction
	 * @param restitution
	 * @param density
	 */
	private void setGeneralPhysicsProperties(AbstractBody body, boolean mobile, boolean graphicalOnly, float rotation, float xVel, float yVel, float aVel, float friction, float restitution, float density){
		body.getPhysicsDef().setMobile(mobile);
		body.getPhysicsDef().setRotation(rotation);
		body.getPhysicsDef().setLinearVelocity(new Vec2(xVel, yVel));
		body.getPhysicsDef().setAngularVelocity(aVel);
		body.getPhysicsDef().setFriction(friction);
		body.getPhysicsDef().setBounciness(restitution);
		body.getPhysicsDef().setDensity(density);
		body.getPhysicsDef().setGraphicalOnly(graphicalOnly);
	}
}
