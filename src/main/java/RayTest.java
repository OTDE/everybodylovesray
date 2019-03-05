import com.shffl.control.MasterController;
import com.shffl.control.RenderController;

public class RayTest {
	
	static MasterController mastCon;
	static RenderController rendCon;

	public static void main(String[] args) {
		
		// Load File and build controllers
		mastCon = new MasterController(null);
		mastCon.parseFile("/Users/EthanWiederspan/Desktop/FilmTest.obj");
		rendCon = new RenderController(mastCon, mastCon.enviro);
		
		
		

	}

}
