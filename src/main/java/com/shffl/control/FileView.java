package com.shffl.control;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shffl.util.Tags;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * The file selector GUI. Allows user to select
 * a JSON file for input into the renderer, and choose between
 * two options for output filetype.
 */
public class FileView extends Application {
	
	private MasterController mastCon;
	private File file;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	    
	    mastCon = new MasterController(this);
		
		Platform.setImplicitExit(false);
		
		//Setting up the grid
		GridPane fileCanvas = new GridPane();
		fileCanvas.setMinSize(600, 300);
		fileCanvas.setPadding(new Insets(10, 10, 10, 10));
		fileCanvas.setVgap(5);
		fileCanvas.setHgap(10);
		
		//File field for the file chooser
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
		
		//Export button
		Button exportButton = new Button("Export");
		
		//Event on click for export button
				exportButton.setOnAction(
			            new EventHandler<ActionEvent>() {
			                @Override
			                public void handle(final ActionEvent e) {
			                	//Add listener for if file type isn't selected later
			                	if(file != null && isFileValid(file)) {
			                		openFile(file, primaryStage);
			                	} else if(!isFileValid(file)) {
			                		fileName.setText("Select a JSON file.");
			                	}else 
			                		fileName.setText("Please select a file.");
			                }//handle

							
			            });
		
		//Popping the nodes into the grid, reading order
		fileCanvas.add(fileName, 0, 0);
		fileCanvas.add(importButton, 1, 0);
		fileCanvas.add(exportButton, 2, 0);
				
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
	}//configureInputChooser
	
	private boolean isFileValid(File f) {
		
		System.out.println("testing the filename");
		
		String name = f.getName();
		int extStart = name.lastIndexOf(".");
		String ext = name.substring(extStart, name.length());
		
		System.out.println("ext:" + ext);
		
		if(ext.equals(".json") || ext.equals(".JSON")) {
			System.out.println("returning true");
			return true;
		}
		
		return false;
	}
}
