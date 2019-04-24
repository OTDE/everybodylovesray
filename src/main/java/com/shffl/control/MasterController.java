package com.shffl.control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.*;
import com.shffl.assets.SceneShell;
import com.shffl.assets.Scene;
import com.shffl.assets.Light;
import com.shffl.assets.LightShell;
import com.shffl.assets.ObjModel;
import com.shffl.assets.ObjShell;


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
		private String filename;
		

		public Scene scene;
		public ObjModel[] objArray;
		/**
		 * Constructor for MasterController Class. Connects 
		 * this to the FileView.
		 * 
		 * @param fv the FileView to connect to this MasterController
		 */
		public MasterController() {
			fView = new FileView(this);
		}
		
		/**
		 * Builds the controller for the rendering process 
		 * and tells it to build the Render GUI.
		 */
		public void beginRender() {
			
			// Make RenderController
			rendCon = new RenderController(this, scene);	
			
			// start display loop
			rendCon.display();
			
		}// beginRender

		/**
		 * Parses the input JSON chosen in FileView. Uses GSON 
		 * to convert input file to wrapper class EnviroShell. 
		 * Then build Scene Class from wrapper shell.
		 * 
		 * @param path String of path to to JSON file
		 */
		public void parseFile(File input, String filename) {
			
			// Build file from path
			//File input = new File(path);
			
			// Store file name for exporting
			setFilename(input.getName());
			System.out.println("setting filename: " + filename);
			
			try {
				
				// Read data from file using GSON
				InputStream fileStream = new FileInputStream(input);
				Reader fileReader = new InputStreamReader(fileStream, "UTF-8");
			    Gson gson = new GsonBuilder().create();
			    JsonStreamParser fileParser = new JsonStreamParser(fileReader);
			    
			    // Load data into Scene Wrapper Class
			    JsonElement sceneElement = fileParser.next();
			    SceneShell shell = gson.fromJson(sceneElement, SceneShell.class);
			    

			    // Make Scene from wrapper
			    scene = shell.scene;
			    
			    // Load the models into the ObjModel Wrapper Class
			    sceneElement = fileParser.next();
			    ObjShell oShell = gson.fromJson(sceneElement, ObjShell.class);
			    
			    // Make ObjModel array from wrapper
			    objArray = oShell.objects;
			    
			    // Build each object
			    for(ObjModel model : objArray) {
			    	model.build();
			    	model.parse();
			    }
			    scene.objects = objArray;
			    
			    // Load the light sources into the Light wrapper class
			    sceneElement = fileParser.next();
			    LightShell lShell = gson.fromJson(sceneElement, LightShell.class);
			    
			    for(Light l: lShell.lights) {
			    	l.convert();
			    }
			    	
			    
			    // Make Light array from wrapper
			    scene.lightSources = lShell.lights;
			    
			   
			    // Start rendering and displaying the Scene
			    this.beginRender();
			    
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}// parseFile
		
		
		public String getFilename() {
			return filename;
		}// getFilename
		
		public void setFilename(String name) {
			
			// clip off the extension
			int extStart = name.lastIndexOf('.');
			filename = name.substring(0, extStart);
		}//setFilename
}