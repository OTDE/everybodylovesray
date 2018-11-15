import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * Environment class. GSON parses into this,
 * which represents a 3D scene and everything
 * it contains within it.
 */
public class Environment {

	public int height;
	public int width;
	public double[] eye;
	public double[] at;
	public int[] up;
	
	public transient Vector3d eyeV;
	public transient Vector3d atV;
	public transient Vector3d upV;
	
	/**
	 * Converts the eye array into a vector if it hasn't already. Returns eyeV otherwise.
	 * Returns the zero vector if both the eye array and the eye vector aren't initialized.
	 * @return a 3D vector indicating the position of the camera's center in 3D space
	 */
	public Vector3d getEye() {
		if(eyeV == null) {
			if(eye != null) {
				eyeV = new Vector3d(eye[0], eye[1], eye[2]);
				return eyeV;
			}
			return new Vector3d(0, 0, 0);
		}
		return eyeV;
	}
	
	/**
	 * Converts the at array into a vector if it hasn't already. Returns atV otherwise.
	 * Returns the zero vector if both the at array and the at vector aren't initialized.
	 * @return a 3D vector indicating the point where the camera is facing
	 */
	public Vector3d getAt() {
		if(atV == null ) {
			if(at != null) {
				atV = new Vector3d(at[0], at[1], at[2]);
				return atV;
			}
			return new Vector3d(0, 0, 0);
		}
		return atV;
	}

	/**
	 * Converts the up array into a vector if it hasn't already. Returns upV otherwise.
	 * Returns zero vector if both the up array and the up vector aren't initialized.
	 * @return a 3D vector indicating upwards in the scene
	 */
	public Vector3d getUp() {
		if(upV == null) {
			if(up != null) {
				upV = new Vector3d(up[0], up[1], up[2]);
				return upV;
			}
			return new Vector3d(0, 0, 0);
		}
		return upV;
	}
	
	//Future fields, not implemented yet!
	//private transient Map<String, LightSource> lightMap;
	//private transient Map<String, Model> modelMap;
	//private transient Map<String, Material> matMap;
	
}
