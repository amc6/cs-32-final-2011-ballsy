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
import java.util.Iterator;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import physics.PathDef;
import bodies.AbstractBody;
import bodies.Ball;
import bodies.IrregularPolygon;
import bodies.Rectangle;
import bodies.UserBall;

public class XMLUtil {
	private static XMLUtil XMLUTIL; // the one and only instance!
	
	// accessor/mutator for singleton
	public static void setInstance(XMLUtil u) { XMLUTIL = u; }
	public static XMLUtil getInstance() { return XMLUTIL; }
	
	/**
	 * Read in an XML file at path (formatted as in writeFile) into the provided level.
	 * Returns success of method.
	 * @param level
	 * @param path
	 */
	public boolean readFile(AbstractLevel level, String path) {
		// set up the eventual new stuff to be written into the level (after populated by XML parsing)
		ArrayList<AbstractBody> newBodies = new ArrayList<AbstractBody>();
		UserBall newPlayer = null;
		// set up XML reader stuffs
    	SAXReader reader = new SAXReader();
    	Document doc;
		try { doc = reader.read(new FileReader(path));} 
		catch (FileNotFoundException e) { return false; } 
		catch (DocumentException e) { return false; }
    	Element root = doc.getRootElement();
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
		level.setGravity(new Point2D.Float( Float.parseFloat(root.attributeValue("GRAV_X")), Float.parseFloat(root.attributeValue("GRAV_Y"))));
    	// iterate through elements of XML file
    	for (Iterator i = root.elementIterator("BODY"); i.hasNext();) {
    		// get references to the various elements
    		Element currBody = (Element) i.next();
    		Element currPhysDef = currBody.element("PHYSICS_DEF");
    		Element currGraphDef = currBody.element("GRAPHICAL_DEF");
    		Element currPathDef = currBody.element("PATH_DEF");
    		// get the values stored in these elements
    		String bodyType = currBody.attributeValue("TYPE");
    		String graphicalType = currGraphDef.attributeValue("TYPE"); // graphical and physics type may not ever be needed
    		int color = Integer.parseInt(currGraphDef.attributeValue("COLOR"));
    		String physicsType = currPhysDef.attributeValue("TYPE");
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
    		// now reconstruct the present object into ballsy
    		Body body = null;
    		if (bodyType.compareTo("user_ball") == 0) {
    			// o hai userball, let's make you
    			newPlayer = new UserBall(level, level.getWorld(), xPos, yPos);
    			newPlayer.setColor(color);
    			body = newPlayer.getPhysicsDef().getBody();
    			newBodies.add(newPlayer); // add it to the bodies too!
    		} else if (bodyType.compareTo("ball") == 0) {
    			// it's a ball
    			Ball newBall = new Ball(level, level.getWorld(), xPos, yPos, width/2, mobile);
    			newBall.setColor(color);
    			// display the line if necessary
    			boolean showLine = Boolean.parseBoolean(currGraphDef.attributeValue("DISPLAY_LINE"));
    			((graphical.BallDef) newBall.getGraphicalDef()).setLine(showLine);
    			body = newBall.getPhysicsDef().getBody();
    			newBodies.add(newBall);
    		} else if (bodyType.compareTo("rectangle") == 0) {
    			// it's a rectangle
    			Rectangle newRect = new Rectangle(level, level.getWorld(), xPos, yPos, width, height, mobile);
    			newRect.setColor(color);
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
    			IrregularPolygon newPoly = new IrregularPolygon(level, level.getWorld(), xPos, yPos, pointList);
    			newPoly.setColor(color);
    			body = newPoly.getPhysicsDef().getBody();
    			newBodies.add(newPoly);
    		} else if (bodyType.compareTo("vertex_surface") == 0) {
    			// it's a surface
    			
    			/////////////////////////////////////////////////
    			// to be filled out when surfaces are complete //
    			/////////////////////////////////////////////////
    			
    		}
    		if (body != null) {
        		// set the body properties
				body.setLinearVelocity(new Vec2(xVel, yVel));
				body.setAngularVelocity(aVel);
				body.m_sweep.a = rotation; // no setter?
				body.getShapeList().setFriction(friction);
				body.getShapeList().setRestitution(restitution);
				body.getShapeList().m_density = density; // idk why there's not a setter for this...
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
    			for (Iterator path_i = currPathDef.elementIterator("PATH_POINT"); path_i.hasNext();) {
    				// iterate through path points, adding them to the vector
    				Element currPathPoint = (Element) path_i.next();
    				float x = Float.parseFloat(currPathPoint.attributeValue("X"));
    				float y = Float.parseFloat(currPathPoint.attributeValue("Y"));
    				pathPoints.add(new Point2D.Float(x, y));
    			}
    			AbstractBody lastBody = newBodies.get(newBodies.size()-1); // get the last body added to the list
    			// create a "new" path (new in form, not in feature) and set it's variables properly
    			PathDef newPath = new PathDef(lastBody.getPhysicsDef(), pathPoints);
    			newPath.setCurrTarget(currTarget);
    			newPath.setInitialPoint(new Point2D.Float(initX, initY));
    			newPath.setVelCoeff(velCoeff);
    			lastBody.setPath(newPath); // apply the new path to this body
    		}
    	}
    	// set the bodies and player to be the new objects as determined above.
    	level.setBodies(newBodies);
    	level.setPlayer(newPlayer);
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
		// get the bodies, to iterate through
		ArrayList<AbstractBody> bodies = level.getBodies();
		// set up the XML document, using dom4j
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("BALLSY_LEVEL");
    	doc.setRootElement(root);
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
}
