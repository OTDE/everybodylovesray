
/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * 
 *
 */
public class Camera {
	
	private Film film;
	private double[] position; 
	
	public Camera(int at, int eye, int up, Film f) {
		
		this.film = f;
		generatePosition(at, eye, up);
	}

	private void generatePosition(int at, int eye, int up) {
		// convert input values to a position in 3d space
		
	}
	
	

}
