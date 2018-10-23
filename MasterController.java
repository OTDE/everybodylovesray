
public class MasterController {
	
	private FileView fView;
	private RenderController rendCon;
	
	public void promptForFile() {
		
	}
	
	public void beginRender() {
		
		// Make RenderController
		rendCon = new RenderController(this);	
		// TO-DO: Send File to JSON Parser
		
		// start display loop
		rendCon.render();
	}

}