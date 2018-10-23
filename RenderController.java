
public class RenderController {
	
	MasterController mastCon;
	RenderView rendView;

	public RenderController(MasterController mCon) {
		this.mastCon = mCon;
	}

	public void render() {
		rendView = new RenderView(this);
		
	}

}
