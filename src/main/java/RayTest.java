import org.joml.Vector3d;

import com.shffl.assets.Ray;

public class RayTest {

	/**
	 * Main method.
	 * Creates a miniature version of the projects'
	 * general ray tracing algorithm. 
	 * @param args
	 */
	public static void main(String[] args) {		
		
		Ray r;
		Vector3d[] triangle = new Vector3d[3];
		
		triangle[0] = new Vector3d(0,0,0);
		triangle[1] = new Vector3d(5,0,0);
		triangle[2] = new Vector3d(0,5,0);
		
		boolean hit = false;
		
		for(int i = 10; i > 0; i--) {
			for(int j = -5; j < 5; j++) {
				r = new Ray(new Vector3d(j, i, 2), new Vector3d(0, 0, -1));
				hit = testRayIntersect(triangle, r);
				if(hit) {
					System.out.print("X ");
				} else {
					System.out.print("o ");
				}
			}
			System.out.println();
		}
		
	}
	
	public static boolean testRayIntersect(Vector3d[] triangle, Ray r) {

		r.direction.normalize();

		// Determine if there is an intersection between the ray and face.
		Vector3d s, edge1, edge2, v0, v1, v2, rayDirection;
		double denom, coefficient, barycentric1, barycentric2;

		// Get edge vertices
		v0 = new Vector3d(triangle[0]);
		v1 = new Vector3d(triangle[1]);
		v2 = new Vector3d(triangle[2]);

		s = (new Vector3d(r.origin)).sub(v0); 

		edge1 = (new Vector3d(v1)).sub(v0);   
		edge2 = (new Vector3d(v2)).sub(v0);  

		rayDirection = new Vector3d(r.direction);
		denom = (rayDirection.cross(edge2).dot(edge1));
		if (denom > -.0000001 && denom < .0000001) {
			return false;
		}
		coefficient = 1 / denom;
		// First check b1
		rayDirection = new Vector3d(r.direction);
		barycentric1 = coefficient * rayDirection.cross(edge2).dot(s);

		if (barycentric1 > 0 && barycentric1 < 1) {

			// Next check b2 with the same parameters
			rayDirection = new Vector3d(r.direction);
			barycentric2 = coefficient * s.cross(edge1).dot(rayDirection);

			if(barycentric2 > 0 && barycentric1 + barycentric2 <= 1) {

				return true;
			} else {
				// Didn't hit
			}
		} else {
			// Didn't hit
		}		
		return false;
	}
}
