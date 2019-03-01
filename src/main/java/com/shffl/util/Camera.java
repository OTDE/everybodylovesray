package com.shffl.util;
import org.joml.Matrix3dc;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector4d;

import com.shffl.assets.Ray;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * Camera class defines the position of view point of the scene, 
 * also defines the rotation matrix needed to transform from world 
 * coordinates to camera coordinates.
 */
public class Camera {
	
	private Film film;
	private Vector3d eye;
	private Matrix4d rotation;
	
	
	public Camera(Vector3d pos, Vector3d at, Vector3d up, Film f) {

		eye = pos;
		
		System.out.println(pos + "\n" + at + "\n" + up);
		
		Vector3d n = eye.sub(at);
		Vector3d u = up.cross(n);
		Vector3d v = n.cross(u);
  		n.normalize();
		u.normalize();
		v.normalize();
		this.setRotation(u, v, n);
		
		System.out.println("Made camera,");
		System.out.println("pos:"+eye);
		System.out.println("rotation:"+rotation);
		this.film = f;
	}

	/**
	 * Builds the camera's rotation matrix based on imput from JSON file
	 * 
	 * @param u vector defined by up cross n 
	 * @param v vector defined by n cross u
	 * @param n vector defined by eye - at
	 */
	private void setRotation(Vector3d u, Vector3d v, Vector3d n) {

		rotation = new Matrix4d();
		rotation._m00(u.x); rotation._m10(u.y); rotation._m20(u.z);
		rotation._m01(v.x); rotation._m11(v.y); rotation._m21(v.z);
		rotation._m02(n.x); rotation._m12(n.y); rotation._m22(n.z);
	}
	
	/*
	 * Returns the view matrix of this camera generated via
	 * the eye and rotation
	 * 
	 * @return Matrix4d containing the view matrix
	 */
	private Matrix4d viewMatrix() {

		Matrix4d m = new Matrix4d();
		m._m30(this.eye.x);
		m._m30(this.eye.y);
		m._m30(this.eye.z);
		
		this.rotation.mul(m,m);
		
		return m;
	}
	
	public Ray generateRay( Sample samp, int pixelX, int pixelY) {
		//System.out.println(pixelX);
		//System.out.println(pixelY);
		double rayX = pixelX + samp.getOffsetX();
		double rayY = pixelY + samp.getOffsetY();
		double rayZ = -10.0;
		
		// Offset ray based on the dimensions of the film	
		rayX -= (film.getWidth()/2);
		rayY -= (film.getHeight()/2);
			
		
		Vector4d rayVector = new Vector4d(rayX, rayY, rayZ, 1);
		
			
		// Convert ray back to world by multiplying by camera's rotation matrix 
		//System.out.println("BINGUS " + rayVector);
		rayVector = rayVector.mul((Matrix4dc)this.rotation.transpose());
		rayVector.normalize();
		
		return new Ray(new Vector3d(0,0,0), new Vector3d(rayVector.w, rayVector.x, rayVector.y));
	}	
}
