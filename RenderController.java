
public class RenderController {
	
	private Film film;
	private Environment enviro;
	
	private MasterController mastCon;
	private RenderView rendView;
	private boolean running = false;

	public RenderController(MasterController mCon, Environment env) {
		this.mastCon = mCon;
		this.enviro = env;
		film = new Film(enviro.width, enviro.height);
	}

	public void display() {
		rendView = new RenderView(this, enviro.width, enviro.height);
		this.startRendering();
		
	}
	
	public void startRendering() {
		
		System.out.println("in renderCon.startRender");
		
		Thread renderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				running = true;
				
				System.out.println("in renderThread.run");
				
				// Updates the image displaying to the GUI after a certain increment of time
				while(running) {
					System.out.println("getting buffered Image from the film...");
					rendView.updateView(film.getRenderedImage());
					try { Thread.sleep(2000); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		});
		
		renderThread.start();
		
	}// startRendering
	
	public void stopRendering() {
		running = false;
	}

}
