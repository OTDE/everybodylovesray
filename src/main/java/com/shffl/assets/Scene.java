package com.shffl.assets;
import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
<<<<<<< HEAD
 * Scene class. GSON parses into this,
 * which represents a 3D scene and everything
 * it contains within it.
 */
public class Scene {

	public int height;
	public int width;
	public double[] eyeInput;
	public double[] atInput;
	public double[] upInput;
	
	public ObjModel[] objects;
	
	public transient Vector3d eye;
	public transient Vector3d at;
	public transient Vector3d up;
	
	/**
	 * Converts the eyeInput array into a vector if it hasn't already. Returns eye otherwise.
	 * Returns the zero vector if both the eye array and the eye vector aren't initialized.
	 * @return a 3D vector indicating the position of the camera's center in 3D space
	 */
	public Vector3d getEye() {
		if(eye == null) {
			if(eyeInput != null) {
				eye = new Vector3d(eyeInput[0], eyeInput[1], eyeInput[2]);
				return eye;
			}
			return new Vector3d(0, 0, 0);
		}
		return eye;
	}
	
	/**
	 * Converts the atInput array into a vector if it hasn't already. Returns at otherwise.
	 * Returns the zero vector if both the at array and the at vector aren't initialized.
	 * @return a 3D vector indicating the point where the camera is facing
	 */
	public Vector3d getAt() {
		if(at == null ) {
			if(atInput != null) {
				at = new Vector3d(atInput[0], atInput[1], atInput[2]);
				return at;
			}
			return new Vector3d(0, 0, 0);
		}
		return at;
	}

	/**
	 * Converts the upInput array into a vector if it hasn't already. Returns up otherwise.
	 * Returns zero vector if both the up array and the up vector aren't initialized.
	 * @return a 3D vector indicating upwards in the scene
	 */
	public Vector3d getUp() {
		if(up == null) {
			if(upInput != null) {
				up = new Vector3d(upInput[0], upInput[1], upInput[2]);
				return up;
			}
			return new Vector3d(0, 0, 0);
		}
		return up;
	}
	
	public Intersection intersect(Ray r, Intersection inter) {
		
		// Call intersect on every object in the scene
		for(ObjModel obj: objects) {
			inter = obj.intersect(r, inter);
			if(inter.hasNormal) {
				return inter;
			}
		}
		return null;
	}	
}
