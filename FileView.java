import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.*;

public class FileView extends Application {
	
	private MasterController mastCon;
	private File file;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Platform.setImplicitExit(false);
		
		//Setting up the grid
		GridPane fileCanvas = new GridPane();
		fileCanvas.setMinSize(600, 300);
		fileCanvas.setPadding(new Insets(10, 10, 10, 10));
		fileCanvas.setVgap(5);
		fileCanvas.setHgap(10);
		
		TextField fileName = new TextField();
		fileName.setPromptText("Select a file:");
		fileName.setEditable(false);
		
		//The file chooser for the .JSON input file
		FileChooser importChooser = new FileChooser();
		Button importButton = new Button("Load");
		
		//Event on click for file chooser
		importButton.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                	configureImportChooser(importChooser);
	                    file = importChooser.showOpenDialog(primaryStage);
	                    if(file != null)
	                    	fileName.setText(file.getName());
	                }//handle
	            });
		
		//Label for "export as"
		Text exportLabel = new Text();
		exportLabel.setText("Export as");
		
		
		//Drop-down menu for selecting file type for export
		ChoiceBox<String> fileTypesView = new ChoiceBox<String>(); 
	    fileTypesView.getItems().addAll(Tags.PNG, Tags.HDR);
		
		//Export button
		Button exportButton = new Button("Export");
		
		//Event on click for export button
				exportButton.setOnAction(
			            new EventHandler<ActionEvent>() {
			                @Override
			                public void handle(final ActionEvent e) {
			                	//Add listener for if file type isn't selected later
			                	if(file != null) {
			                		mastCon = new MasterController();
			                		openFile(file, primaryStage);
			                	} else
			                		fileName.setText("Please select a file.");
			                }//handle
			            });
		
		//Popping the nodes into the grid, reading order
		fileCanvas.add(fileName, 0, 0);
		fileCanvas.add(importButton, 1, 0);
		fileCanvas.add(exportLabel, 0, 1);
		fileCanvas.add(fileTypesView, 1, 1);
		fileCanvas.add(exportButton, 2, 1);
				
		//Adding the grid into the scene
		Scene window = new Scene(fileCanvas);
		
		//Stage setup
		primaryStage.setTitle("SHFFL");
		primaryStage.setScene(window);
		primaryStage.show();
	}//start
	
	//Private method to test if the file works
	private void openFile(File file, Stage primaryStage) {
        try {
        	//primaryStage.hide();
        	String path = file.getPath();
        	mastCon.parseFile(path);
        } catch (Exception ex) {
            Logger.getLogger(
                FileView.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }//catch
    }//openFile
	
	//Private method to configure the file chooser's input
	private static void configureImportChooser(FileChooser chooser) {      
		chooser.setTitle("Load JSON File");
		chooser.setInitialDirectory(new File(System.getProperty("user.home")));                 
	    chooser.getExtensionFilters().addAll(
	    		new FileChooser.ExtensionFilter("JSON file", "*.json*"));
	}//configureInputChooser
}
