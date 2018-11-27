import org.joml.Vector3d;

public class Ray {
	
	// origin, directing, maxt, time(?)
	public Vector3d origin;
	public Vector3d direction;
	
	public double tMax = 0.0;
	public double time = 0.0;
	public double radiance = 1.0;
	
	public Ray(Vector3d o, Vector3d d) {
		this.origin = o;
		this.direction = d;
	}
	
	
	public Vector3d positionAtTime(double t) {
		return origin.add(direction.mul(t));
	}
}
