package com.shffl.control;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * The file selector GUI. Allows user to select
 * a JSON file for input into the renderer, and choose between
 * two options for output filetype.
 */

@SuppressWarnings("serial")
public class FileView extends JFrame {

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

	/**
	 * Builds layout.
	 */
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

	/**
	 * Event handlers for button clicks.
	 */
	private void initEventHandlers() {

		buttonRender.addActionListener(new ActionListener() {

			/**
			 * Render button
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				if(isInfoValid()) {
					mastCon.parseFile(selectedFile, fieldOutput.getText());
				}
			}			
		}); 
		
		chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		buttonBrowse.addActionListener(new ActionListener() {

			/**
			 * File chooser
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = chooser.getSelectedFile();
					filename = selectedFile.getName();
					fieldFile.setText(filename);
					fileSelected = true;
				}
			}
		});
	}//initEventHandlers

	/**
	 * Checks if the input is valid.
	 * @return true if the file is a JSON file, false otherwise.
	 */
	public boolean isInfoValid() {
		
		if(!fileSelected) {
			labelMessage.setText("Please select an input file");
			return false;
		}
		if(fieldOutput.getText().length() == 0) {
			labelMessage.setText("Please select an output file name");
			return false;
		}
		
		int extStart = filename.lastIndexOf(".");
		String ext = filename.substring(extStart, filename.length());
	
		if(!ext.equals(".json") && !ext.equals(".JSON")) {
			labelMessage.setText("Please select a .JSON file as input");
			return false;
		} else if(fieldOutput.getText().contains(" ")) {
			labelMessage.setText("Output cannot include spaces");
			return false;
		}
		return true;
	}//isInfoValid

}//class

