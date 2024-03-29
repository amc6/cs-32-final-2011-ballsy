package ballsy;

/**
 * Top level class for the application. Handles version checking, and launching of the PApplet
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import processing.core.PApplet;

public class App {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		
		// make the eventual textbox the current OS's theme
		try { 
			  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) { 
			  e.printStackTrace();  
		} 
		
		App.checkJavaVersion();
		App.checkBitting();
		
		/* check online (if online) if this version is up to date. if so, run it, else, notify. */
		int runCode = 0; // by default, just run Ballsy
		try {
			// get this copy's version from file
			FileReader input = new FileReader("res/version_info.txt");
			BufferedReader br = new BufferedReader(input);
			String line = br.readLine();
			// check this copy's version number against the online version, if online.
			URL versionFile = new URL("http://www.beballsy.com/version_info.txt");
			URLConnection conn = versionFile.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			boolean upToDate = false;
			if ((inputLine = in.readLine()) != null && inputLine.equals(line)) upToDate = true;
			if (!upToDate && inputLine != null && !inputLine.startsWith("<")) { // should not fire if in a captured portal, i.e. accessing a webpage other than ours
				String message = "A newer version of Ballsy is available. For best performance, " +
			    "please update to the most recent release.\n\n" +
			    "Revision " + inputLine + ":";
				while ((inputLine = in.readLine()) != null && inputLine != "") {
					message = message + "\n" + "-> " + inputLine;
				}
				message = message + "\n\nNavigate to www.beballsy.com to get your update!";
				Object[] options = {"Play Outdated Version", "Exit"};
				runCode = JOptionPane.showOptionDialog(null,
				    wrapKinda(message, 60),
				    "Ballsy Update",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.WARNING_MESSAGE,
				    null,
				    options,
				    options[1]); // now: 1 = exit, 0 = play outdated, -1 = message box closed
			}
			// close connection
			in.close();
		} catch (Exception e) {
			// maybe we're offline. Do nothing.
			System.out.println("No Ballsy version information available.");
		}
		// do the right thing
		if (runCode == 1) {
			// open the website - not supported in 1.5, so just notivy
			// java.awt.Desktop.getDesktop().browse(new URI("http://www.beballsy.com"));
			System.exit(0); 
		} else {
			// run ballsy
			PApplet.main(new String[] { "--present", "ballsy.Window" });
		}
	}
	
	/**
	 * Provided a string str and a size, this will insert new lines at the closest spaces
	 * after the character length provided. Wraps the content of the message box, so we can
	 * have bug-fix notifications of unlimited length.
	 * @param str
	 * @param size
	 * @return
	 */
	private static String wrapKinda(String str, int size) {
		String[] lines = str.split("\n");
		String output = "";
		for (String s : lines) { // break it up
			if (output.length() > 0) output = output + "\n"; // add a new line after first
			if (s.length() > size) {
				int position = size;
				while (!s.substring(position, position+1).equals(" ") && position+1 < s.length()) position ++;
				if (position != s.length() - 1) position ++;
				output = output + s.substring(0, position) + "\n" + wrapKinda(s.substring(position, s.length()), size);
			} else output = output + s; // else just add it
		}
		return output;
	}
	
	private static void checkJavaVersion(){
		String version = System.getProperty("java.version");
		if (!version.startsWith("1.6")){
			String message = "Ballsy requires Java 1.6 in order to run properly. Please visit www.java.com to update your version of Java.";
			Object[] options = {"Exit"};
			JOptionPane.showOptionDialog(null,
			    wrapKinda(message, 60),
			    "Java Update Required",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE,
			    null,
			    options,
			    options[0]);
			
			System.exit(0);
		}
	}
	
	private static void checkBitting(){
		
		if(System.getProperty("os.name").contains("Windows")){
			FileReader file;
			try {
				file = new FileReader("res/bitting.txt");
				BufferedReader reader = new BufferedReader(file);
				String bitting = reader.readLine();
				
				if (bitting.equals(System.getProperty("sun.arch.data.model"))){
					// The user's Java architecture matches the files they have
				}else{
		
					String message = "The version of Ballsy you have downloaded does not match the version of Java you have installed. Go to www.beballsy.com to download the proper version.";
					Object[] options = {"Exit"};
					JOptionPane.showOptionDialog(null,
					    wrapKinda(message, 60),
					    "Ballsy Error",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.WARNING_MESSAGE,
					    null,
					    options,
					    options[0]);
					
					System.exit(0);
					
				}
			} catch (Exception e) {
				// Could not read from file
			}
		}
	}
	
}

