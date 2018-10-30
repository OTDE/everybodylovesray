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

public class MasterController {
		
		private FileView fView;
		private RenderController rendCon;
		
		public Environment enviro;
		
		public void promptForFile() {
			
		}
		
		public void beginRender() {
			
			// Make RenderController
			rendCon = new RenderController(this);	
			// TO-DO: Send File to JSON Parser
			
			// start display loop
			rendCon.display();
		}

		public void parseFile(File input) {
			try {
				InputStream fileStream = new FileInputStream(input);
				Reader fileReader = new InputStreamReader(fileStream, "UTF-8");
			    Gson gson = new GsonBuilder().create();
			    JsonStreamParser fileParser = new JsonStreamParser(fileReader);
			    
			    JsonElement e = fileParser.next();
			    enviro = gson.fromJson(e, Environment.class);
			    this.beginRender();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}