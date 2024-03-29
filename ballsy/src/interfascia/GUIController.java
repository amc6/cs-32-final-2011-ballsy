// Interfascia ALPHA 002 -- http://superstable.net/interfascia/
// GUI Library for Processing -- http://www.processing.org/
//
// Copyright (C) 2006 Brendan Berg
// interfascia (at) thbbpt (dot) net
//
// This library is free software; you can redistribute it and/or 
// modify it under the terms of the GNU Lesser General Public 
// License as published by the Free Software Foundation; either 
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful, 
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public 
// License along with this library; if not, write to the Free Software 
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA



package interfascia;
import processing.core.*;

import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.util.ArrayList;

public class GUIController extends GUIComponent implements ClipboardOwner {
	private GUIComponent[] contents;
	private int numItems = 0;
	private int focusIndex = -1;
	private boolean visible;
	private IFLookAndFeel lookAndFeel;
	public IFPGraphicsState userState;
	private Clipboard clipboard;

	public PApplet parent;

	public boolean showBounds = false;
	
	public GUIController (PApplet newParent) {
		this(newParent, true);
	}
	
	public GUIController (PApplet newParent, int x, int y, int width, int height) {
		this(newParent, true);
		setPosition(x, y);
		setSize(width, height);
	}

	public GUIController (PApplet newParent, boolean newVisible) {
		setParent(newParent);
		setVisible(newVisible);
		contents = new GUIComponent[5];
		
		lookAndFeel = new IFLookAndFeel(parent, IFLookAndFeel.DEFAULT);
		userState = new IFPGraphicsState();
		
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			try {
				security.checkSystemClipboardAccess();
				clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			} catch (SecurityException e) {
				clipboard = new Clipboard("Interfascia Clipboard");
			}
		} else {
			try {
				clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			} catch (Exception e) {
				// THIS IS DUMB
			}
		}
		
		parent.registerKeyEvent(this);
		parent.registerDraw(this);
	}
	
	public void setLookAndFeel(IFLookAndFeel lf) {
		lookAndFeel = lf;
	}
	
	public IFLookAndFeel getLookAndFeel() {
		return lookAndFeel;
	}

	public void add (GUIComponent component) {
		if (numItems == contents.length) {
			GUIComponent[] temp = contents;
			contents = new GUIComponent[contents.length * 2];
			System.arraycopy(temp, 0, contents, 0, numItems);
		}
		component.setController(this);
		component.setLookAndFeel(lookAndFeel);
		//component.setIndex(numItems);
		contents[numItems++] = component;
		component.initWithParent();
	}

	public void remove (GUIComponent component) {
		int componentIndex = -1;
		
		for (int i = 0; i < numItems; i++) {
			if (component == contents[i]){
				componentIndex = i;
				break;
			}
		}
		
		if (componentIndex != -1) {
			contents[componentIndex] = null;
			if (componentIndex < numItems - 1) {
				System.arraycopy(contents, componentIndex + 1, contents, componentIndex, numItems - (componentIndex + 1));
			}
			numItems--;
		}
	}
	
	public void setParent (PApplet argParent) {
		parent = argParent;
	}
	
	public PApplet getParent () {
		return parent;
	}
	
	public void setVisible (boolean newVisible) {
		visible = newVisible;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void requestFocus(GUIComponent c) {
		for (int i = 0; i < numItems; i++) {
			if (c == contents[i])
				focusIndex = i;
		}
	}
	
	// ****** LOOK AT THIS, I DON'T THINK IT'S RIGHT ******
	public void yieldFocus(GUIComponent c) {
		if (focusIndex > -1 && focusIndex < numItems && contents[focusIndex] == c) {
			focusIndex = -1;
		}
	}
	
	public GUIComponent getComponentWithFocus() {
		if (focusIndex < 0) return null;
		return contents[focusIndex];
	}
	
	public boolean getFocusStatusForComponent(GUIComponent c) {
		if (focusIndex >= 0 && focusIndex < numItems)
			return c == contents[focusIndex];
		else
			return false;
	}



	public void lostOwnership (Clipboard parClipboard, Transferable parTransferable) {
		//System.out.println ("Lost ownership");
	}
	
	public void copy(String v)
	{
		StringSelection fieldContent = new StringSelection (v);
		clipboard.setContents (fieldContent, this);
	}
	
	public String paste()
	{
		Transferable clipboardContent = clipboard.getContents (this);
		
		if ((clipboardContent != null) &&
			(clipboardContent.isDataFlavorSupported (DataFlavor.stringFlavor))) {
			try {
				String tempString;
				tempString = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
				return tempString;
			}
			catch (Exception e) {
				e.printStackTrace ();
			}
		}
		return "";
	}
	


	public void keyEvent(KeyEvent e) {
		
		/* HOLY FUCKING SHIT. BIGGEST WASTE OF TIME EVER.
		 * We need to make it so that tabs don't cycle through multiple Controllers at once.
		 * All components should be in the same one. And visibility should be controlled on 
		 * per component basis. Tabs disabled.
		 */
		/*
		if (visible && this.getComponentWithFocus() != null) { // visible and something focused
			if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB) {
				if (focusIndex != -1 && contents[focusIndex] != null) {
					contents[focusIndex].actionPerformed(
						new GUIEvent(contents[focusIndex], "Lost Focus")
					);
				}
				
				if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK)
					giveFocusToPreviousComponent();
				else
					giveFocusToNextComponent();
				
				if (focusIndex != -1 && contents[focusIndex] != null) {
					contents[focusIndex].actionPerformed(
						new GUIEvent(contents[focusIndex], "Received Focus")
					);
				}

			} else if (e.getKeyCode() != KeyEvent.VK_TAB) {
				if (focusIndex >= 0 && focusIndex < contents.length)
					contents[focusIndex].keyEvent(e);
			}
		}
		*/
		
		// Don't let that tab fucker get in the way
		if (e.getKeyCode() != KeyEvent.VK_TAB) {
			if (focusIndex >= 0 && focusIndex < contents.length)
				contents[focusIndex].keyEvent(e);
		}
	}
	
	private void giveFocusToPreviousComponent() {
		int oldFocus = focusIndex;
		focusIndex = (focusIndex - 1) % numItems;
		while (!contents[focusIndex].canReceiveFocus() && focusIndex != oldFocus) {
			focusIndex = (focusIndex - 1) % numItems;
		}
	}
	
	private void giveFocusToNextComponent() {
		int oldFocus = focusIndex;
		focusIndex = (focusIndex + 1) % numItems;
		while (!contents[focusIndex].canReceiveFocus() && focusIndex != oldFocus) {
			focusIndex = (focusIndex + 1) % numItems;
		}
	}

	public void draw() {
		if (visible) {
			userState.saveSettingsForApplet(parent);
			lookAndFeel.defaultGraphicsState.restoreSettingsToApplet(parent);
			//parent.background(parent.g.backgroundColor);
			parent.fill(parent.color(0));
			parent.rect(getX(), getY(), getWidth(), getHeight());
			for(int i = 0; i < contents.length; i++){
				if(contents[i] != null){
					//parent.smooth();
					contents[i].draw();
				}
			}
			userState.restoreSettingsToApplet(parent);   
		}
	}  
	
	public ArrayList<GUIComponent> getComponents(){
		ArrayList<GUIComponent> toReturn = new ArrayList<GUIComponent>();
		for (int i = 0; i < contents.length; i++){
			if (contents[i] != null) toReturn.add(contents[i]);
		}
		return toReturn;
	}
}
