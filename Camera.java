import org.joml.Matrix4d;
import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * 
 *
 */
public class Camera {
	
	private Film film;
	private Vector3d eye;
	private Matrix4d rotation;
	
	
	public Camera(Vector3d pos, Vector3d at, Vector3d up, Film f) {
		eye = pos;
		
		Vector3d n = eye.sub(at);
		Vector3d u = up.cross(n);
		Vector3d v = n.cross(u);
 		n.normalize();
		u.normalize();
		v.normalize();
		
		
		this.film = f;
	}

	private void setRotation(Vector3d u, Vector3d v, Vector3d n) {
		rotation._m00(u.x); rotation._m01(u.y); rotation._m02(u.z);
		rotation._m10(v.x); rotation._m11(v.y); rotation._m12(v.z);
		rotation._m20(n.x); rotation._m21(n.y); rotation._m22(n.z);
	}
	
	private Matrix4d viewMatrix() {
		Matrix4d m = new Matrix4d();
		m._m30(this.eye.x);
		m._m30(this.eye.y);
		m._m30(this.eye.z);
		
		this.rotation.mul(m,m);
		
		return m;
	}
	
	public void generateRay( Sample samp) {
		// method stub for future sprint
	}
	

}
