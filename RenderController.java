import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		cam = new Camera(enviro.getAt(), enviro.getEye(), enviro.getUp(), film);
		
		
		// Build new Film based on Environment's specs
		film = new Film(enviro.width, enviro.height);
		
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

	private void startRendering() {
		
		Thread renderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("began render");
				// Starts the Thread, calling its run method
				
				Sampler sampler = new Sampler(10);
				
				// Split pixels into squares
				int n = enviro.width / 5;
				int m = enviro.height / 5;
				
				int j,k,xi,xn,yi,yn;
				for(int i = 0; i < 25; i++) {
					j = Tags.TILE_ORDER[i][1];
					k = Tags.TILE_ORDER[i][0];
					
					if(j != 4 ) {
						xi = j*n;
						xn = ((j+1)*n)-1;
					}else {
						xi = j*n;
						xn = ((j+1)*n) + enviro.width % 5;
						xn -=1;
					}
					if(k != 4 ) {
						yi = k*m;
						yn = ((k+1)*m)-1;
					}else {
						yi = k*m;
						yn = ((k+1)*m) + enviro.height % 5;
						yn -= 1;
					}
					
					for(int a = xi; a <= xn; a++) {
						for(int b = yi; b <= yn; b++) {
							film.testDevelop(sampler.getPixelSamples(a, b));
						}
					}
					
					//System.out.println("done with a cube");
				}
				
			}
			
		});
		
		renderThread.start();
	}

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
			}
		});
		displayThread.start();
	}// startDisplaying

	

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

}
