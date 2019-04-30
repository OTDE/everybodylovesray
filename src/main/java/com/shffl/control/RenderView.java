package com.shffl.control;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * GUI that gives the user a display of the image 
 * as it is rendering, gives options to return to 
 * file select and to export the image.
 *
 */
@SuppressWarnings("serial")
public class RenderView extends JFrame {
	
	private RenderController rendCon;
	private ImageIcon displayImage;
	
	private JPanel panelMid;
	private JPanel panelSouth;
	
	private JButton exportButton;
	private JButton returnButton;
	
	private JLabel imageLabel;
	JScrollPane imagePane;
	
	int width;
	int height;
	
	/**
	 * Constructor for RenderView, set default values 
	 * for the JFrame and connects this view to the 
	 * controller.
	 * @param height 
	 * @param width 
	 * @param renderController
	 */
	public RenderView(RenderController rCon, int w, int h) {

		this.rendCon = rCon;
		this.width = w;
		this.height = h;
		
		buildView();
		initActionListeners();
		
		setTitle("Ray Tracing");
		
		int frameWidth = width;
		int frameHeight = height;
		if(frameWidth < 300) {
			frameWidth = 300;
		} else if(frameWidth > 1200) {
			frameWidth = 1200;
		}
		if (frameHeight < 300) {
			frameHeight = 300;
		} else if(frameHeight > 700) {
			frameHeight = 700;
		}
		
		setSize(frameWidth+15,frameHeight+65);
		
		
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setVisible(true);
	}//RenderView

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
		imagePane = new JScrollPane(imageLabel);
		
		int frameWidth = width;
		int frameHeight = height;
		if(frameWidth < 300) {
			frameWidth = 300;
		}else if(frameWidth > 1200) {
			frameWidth = 1200;
		}
		if (frameHeight < 300) {
			frameHeight = 300;
		}else if(frameHeight > 700) {
			frameHeight = 700;
		}
		imagePane.setPreferredSize(new Dimension(frameWidth+10, frameHeight+10));
		panelMid.add(imagePane);
		
		returnButton = new JButton("Return");
		exportButton = new JButton("Export");
		
		panelSouth.add(returnButton);
		panelSouth.add(exportButton);
	}//buildView
	
	/**
	 * Sets up the action listeners for the buttons
	 */
	private void initActionListeners() {
		
		exportButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				rendCon.exportImage();
			}
		});
		
		returnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exitProcedure();
			}
		});
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				exitProcedure();
			}
		});

	}//initActionListeners
	
	/** 
	 * Called every by the RenderController to update 
	 * the image display to the User via the Render GUI.
	 * 
	 * @param newImage the updated BufferedImage to be displayed
	 */
	public void updateView(BufferedImage newImage) {
		
		//Ensure the Swing GUI Updates in its own thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				displayImage = new ImageIcon(newImage);
				imageLabel.setIcon(displayImage);
				panelMid.add(imagePane);
			}
		});
		
	}//updateView
	
	private void exitProcedure() {
		rendCon.stopRendering();
		this.dispose();
	}//exitProcedure

}//class
