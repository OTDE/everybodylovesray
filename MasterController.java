import javafx.application.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.gson.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.gson.*;

/**
 * @author Seth Chapman and Ethan Wiederspan 
 * 
 * Main Controller of Ray Tracing process. Transitions control 
 * from file selection to file parsing to rendering and displaying.
 * Holds references to the File View and other controllers but not 
 * to the Film or other parts of the rendering process.
 */
public class MasterController {
	
	
		private FileView fView;
		private RenderController rendCon;
		public Environment enviro;
		
		/**
		 * Constructor for MasterController Class. Connects 
		 * this to the FileView.
		 * 
		 * @param fv the FileView to connect to this MasterController
		 */
		public MasterController(FileView fv) {
			fView = fv;
		}
		
		/**
		 * Builds the controller for the rendering process 
		 * and tells it to build the Render GUI.
		 */
		public void beginRender() {
			
			// Make RenderController
			rendCon = new RenderController(this, enviro);	
			
			// start display loop
			rendCon.display();
			
		}// beginRender

		/**
		 * Parses the input JSON chosen in FileView. Uses GSON 
		 * to convert input file to wrapper class EnviroShell. 
		 * Then build Environment Class from wrapper shell.
		 * 
		 * @param path String of path to to JSON file
		 */
		public void parseFile(String path) {
			
			// Build file from path
			File input = new File(path);
			
			try {
				
				// Read data from file using GSON
				InputStream fileStream = new FileInputStream(input);
				Reader fileReader = new InputStreamReader(fileStream, "UTF-8");
			    Gson gson = new GsonBuilder().create();
			    JsonStreamParser fileParser = new JsonStreamParser(fileReader);
			    
			    // Load data into Environment Wrapper Class
			    JsonElement e = fileParser.next();
			    EnviroShell shell = gson.fromJson(e, EnviroShell.class);
			    
			    // Make Environment from wrapper
			    enviro = shell.environment;
			    
			    // Start rendering and displaying the Environment
			    this.beginRender();
			    
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}// parseFile
		
	}