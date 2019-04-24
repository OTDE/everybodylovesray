package com.shffl.control;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

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

public class FileView extends javax.swing.JFrame {

	MasterController mastCon;

	JButton buttonBrowse, buttonRender;
	JFileChooser chooser;
	JLabel labelMessage,labelFile, labelOutput;
	JTextField fieldFile, fieldOutput;

	boolean fileSelected = false;
	boolean nameSelected = false;
	
	File selectedFile;
	String filename;
	
	public FileView(MasterController mC) {

		this.mastCon = mC;

		buildDisplay();
		initEventHandlers();

		setTitle("Ray Tracer");
		setSize(500,200);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
	}

	private void buildDisplay() {

		JPanel panelNorth = new JPanel();
		JPanel panelMain = new JPanel(new GridBagLayout());

		getContentPane().add(panelNorth, "North");
		getContentPane().add(panelMain);

		// Construct the upper panel with label for messages
		labelMessage = new JLabel("Welcome to the Ray Tracer.");
		panelNorth.add(labelMessage);

		// Construct the main panel with the user interface
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		fieldFile = new JTextField(10);
		fieldOutput = new JTextField(10);

		buttonBrowse = new JButton("Browse...");
		buttonRender = new JButton("Render");

		labelFile = new JLabel("Input File:");
		labelOutput = new JLabel("Output File");

		c.anchor = GridBagConstraints.LINE_START;
		panelMain.add(labelFile, c);
		c.gridx++;
		panelMain.add(fieldFile, c);
		fieldFile.setText("Select a JSON");
		c.gridx++;
		panelMain.add(buttonBrowse, c);

		c.gridy++;
		c.gridx = 0;

		c.anchor = GridBagConstraints.LINE_START;	
		panelMain.add(labelOutput,c);
		c.gridx++;
		panelMain.add(fieldOutput,c);
		fieldOutput.setText("untitled.png");
		c.gridx++;
		panelMain.add(buttonRender, c);	

	}

	private void initEventHandlers() {

		buttonRender.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {

				if(isInfoValid()) {
					mastCon.parseFile(selectedFile, fieldOutput.getText());
				}
			}			
		}); 
		
		chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		buttonBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				
				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = chooser.getSelectedFile();
					filename = selectedFile.getName();
					fieldFile.setText(filename);
					fileSelected = true;
				}
			}
		});



	}

	public boolean isInfoValid() {
		
		if(!fileSelected) {
			labelMessage.setText("Please Select an input file");
			return false;
		}
		
		int extStart = filename.lastIndexOf(".");
		String ext = filename.substring(extStart, filename.length());


		if(!ext.equals(".json") && !ext.equals(".JSON")) {
			labelMessage.setText("Please Select a .JSON file as input");
			return false;
		}else if(fieldOutput.getText().contains(" ")) {
			labelMessage.setText("Output cannot include spaces");
			return false;
		}
		
		return true;
	}

}



/*
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
 */
