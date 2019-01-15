package com.shffl.control;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.shffl.assets.Environment;
import com.shffl.assets.Ray;
import com.shffl.util.Camera;
import com.shffl.util.Film;
import com.shffl.util.Integrator;
import com.shffl.util.Sample;
import com.shffl.util.SampleArray;
import com.shffl.util.Sampler;
import com.shffl.util.Tags;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * Controls the processes of rendering and displaying 
 * the developing film to the user. Contains a Thread 
 * to prompt the film for and update image every few seconds
 */
public class RenderController {

	private Film film;
	private Environment enviro;
	private Camera cam;
	private Integrator integrator;
	
	Thread displayThread;
	Thread renderThread;
	
	private MasterController mastCon;
	private RenderView rendView;
	private boolean running = false;

	/**
	 * Constructor for the RenderController Class. Connects
	 * this controller to the master controller and the 
	 * Environment build off of the input JSON.
	 * 
	 * @param mCon MasterController to connect to 
	 * @param env Environment built from the input JSON
	 */
	public RenderController(MasterController mCon, Environment env) {
		
		// Connect to MasterController and Environment
		this.mastCon = mCon;
		this.enviro = env;
		
		// Build new Film based on Environment's specs
		film = new Film(enviro.width, enviro.height);
		
		integrator = new Integrator(this);
		cam = new Camera(enviro.getAt(), enviro.getEye(), enviro.getUp(), film);
	
	}// RenderController

	/**
	 * Builds the GUI and starts the display loop
	 */
	public void display() {
		
		// Build the Rendering GUI
		rendView = new RenderView(this, enviro.width, enviro.height);
		
		// Call the render loop
		this.startDisplaying();
		this.startRendering();
		
	}// display

	/**
	 * builds the thread that holds the loop to render the image. 
	 * Sends pixels to the sample in a pattern that renders the 
	 * middle of the frame first, working its way out.
	 */
	private void startRendering() {
		
		Thread renderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("began render");
				// Starts the Thread, calling its run method
				
				Sampler sampler = new Sampler(10);
				SampleArray sampArr = null;
				Ray ray = null;
				
				// Split pixels into squares
				int n = enviro.width / 5;
				int m = enviro.height / 5;
				
				int tileX, tileY, xStart, xEnd, yStart, yEnd;
				for(int i = 0; i < 25; i++) {
					tileX = Tags.TILE_ORDER[i][1];
					tileY = Tags.TILE_ORDER[i][0];
					
					if(tileX != 4 ) {
						xStart = tileX*n;
						xEnd = ((tileX+1)*n)-1;
					} else {
						xStart = tileX*n;
						xEnd = ((tileX+1)*n) + enviro.width % 5;
						xEnd -=1;
					}
					if(tileY != 4 ) {
						yStart = tileY*m;
						yEnd = ((tileY+1)*m)-1;
					} else {
						yStart = tileY*m;
						yEnd = ((tileY+1)*m) + enviro.height % 5;
	 					yEnd -= 1;
					}
					
					for(int a = xStart; a <= xEnd; a++) {
						for(int b = yStart; b <= yEnd; b++) {
							sampArr = sampler.getPixelSamples(a, b);
							for(Sample s: sampArr.samples) {
								
								// For each sample, generate and cast ray
								ray = cam.generateRay(s, sampArr.getPixelX(), sampArr.getPixelY());
								Color c = integrator.propagate(ray);
								film.develop(s, sampArr.getPixelX(), sampArr.getPixelY(), c);				
							}
						}
					}
				}		
			}//run		
		});//thread	
		renderThread.start();
	}//startRendering

	/**
	 * The loop that retrieves the BufferedImage periodically 
	 * as it is being developed.  Makes a separate Thread so
	 * film can continue to develop on another.
	 */
	public void startDisplaying() {

		// Makes the Thread for rendering and defines its run function
		displayThread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("started running");
				
				running = true;
				
				// Updates the image displaying to the GUI after a certain increment of time
				while(running) {
					
					rendView.updateView(film.getRenderedImage());
					
					// Wait 2 seconds
					try { displayThread.sleep(10); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}//run
		});//thread
		displayThread.start();
	}//startDisplaying

	

	/**
	 * Takes image in Film class and exports it to a png with
	 * the name of the input file
	 */
	public void exportImage() {
		
		stopRendering();		
		System.out.println("making file");
		try {
		    BufferedImage finalImage = film.getRenderedImage(); 
		    File outputfile = new File(mastCon.getFilename() + ".png");
		    ImageIO.write(finalImage, "png", outputfile);
		} catch (IOException e) {
		    System.out.println("ERROR WHILE WRITING: " + e.getMessage());
		}		
		System.out.println("done writing!");
		continueRendering();
	}// exportImage
	
	/**
	 * Sets running to false to end the loop in the Render Thread.
	 */
	public void stopRendering() {
		running = false;
	}// stopRendering
	public void continueRendering() {
		running = true;
	}// continueRendering
	
	public Environment getEnvironement() {
		return this.enviro;
	}

}
