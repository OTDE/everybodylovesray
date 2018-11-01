
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
		
	}// RenderController

	/**
	 * Builds the GUI and starts the display loop
	 */
	public void display() {
		
		// Build the Rendering GUI
		rendView = new RenderView(this, enviro.width, enviro.height);
		
		// Call the render loop
		this.startRendering();
		
	}// display

	/**
	 * The loop that retrieves the BufferedImage periodically 
	 * as it is being developed.  Makes a separate Thread so
	 * film can continue to develop on another.
	 */
	public void startRendering() {

		System.out.println("in renderCon.startRender");

		// Makes the Thread fo rendering and defines its run function
		Thread renderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				running = true;

				System.out.println("in renderThread.run");

				// Updates the image displaying to the GUI after a certain increment of time
				while(running) {
					System.out.println("getting buffered Image from the film...");
					rendView.updateView(film.getRenderedImage());
					
					// Wait 2 seconds
					try { Thread.sleep(2000); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Starts the Thread, calling its run method
		renderThread.start();

	}// startRendering

	/**
	 * Sets running to false to end the loop in the Render Thread.
	 */
	public void stopRendering() {
		running = false;
	}// stopRendering

}
