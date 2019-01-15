package com.shffl.control;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * GUI that gives the user a display of the image 
 * as it is rendering, gives options to return to 
 * file select and to export the image.
 *
 */
public class RenderView extends javax.swing.JFrame {
	
	private RenderController rendCon;
	private ImageIcon displayImage;
	
	private JPanel panelMid;
	private JPanel panelSouth;
	
	private JButton exportButton;
	private JButton returnButton;
	
	private JLabel imageLabel;
	
	/**
	 * Constructor for RenderView, set default values 
	 * for the JFrame and connects this view to the 
	 * controller.
	 * @param height 
	 * @param width 
	 * @param renderController
	 */
	public RenderView(RenderController rCon, int width, int height) {
		this.rendCon = rCon;
		
		buildView();
		initActionListeners();
		
		setTitle("Ray Tracing");
		setSize(width,(height+60));
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);

	}// RenderView

	/**
	 * Builds the JFrame components of the GUI
	 */
	private void buildView() {
		
		panelMid = new JPanel();
		panelSouth = new JPanel();
		
		getContentPane().add(panelMid);
		getContentPane().add(panelSouth, "South");
		
		displayImage = new ImageIcon();
		imageLabel = new JLabel(displayImage);
		panelMid.add(imageLabel);
		
		returnButton = new JButton("Render Another Image");
		exportButton = new JButton("Export");
		
		panelSouth.add(returnButton);
		panelSouth.add(exportButton);
		
	}// buildView
	
	/**
	 * Sets up the action listeners for the buttons
	 */
	private void initActionListeners() {
		ActionListener buttonListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Pressed a button: " + e.getActionCommand());
				
				if(e.getActionCommand().equals("Export")) {
					System.out.println("exporting image");
					rendCon.exportImage();
				}
				
				
			}
		};
		exportButton.addActionListener(buttonListener);
	}// initActionListeners
	
	/** 
	 * Called every by the RenderController to update 
	 * the image display to the User via the Render GUI
	 * 
	 * @param newImage the updated BufferedIMage to be displayed
	 */
	public void updateView(BufferedImage newImage) {
		
		displayImage = new ImageIcon(newImage);
		imageLabel.setIcon(displayImage);
		panelMid.add(imageLabel);
		
		
	}// updateView

}
