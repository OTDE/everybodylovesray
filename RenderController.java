
public class RenderController {
	
	Film film;
	
	MasterController mastCon;
	RenderView rendView;
	boolean running = false;

	public RenderController(MasterController mCon) {
		this.mastCon = mCon;
		film = new Film();
	}

	public void display() {
		rendView = new RenderView(this);
		
	}
	
	public void startRendering() {
		
		Thread renderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				running = true;
				
				// Updates the image displaying to the GUI after a certain increment of time
				while(running) {
					rendView.updateView(film.getRenderedImage());
					try { Thread.sleep(2000); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		});
	}// startRendering
	
	public void stopRendering() {
		running = false;
	}

}