package com.shffl.util;
import org.joml.Matrix3dc;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Quaterniondc;
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

		eye = new Vector3d(pos);
		
		System.out.println("INPUT:\npos:"+pos + "\nat:" + at + "\nup:" + up);
		
		Vector3d n = (new Vector3d(pos)).sub(at);
		Vector3d u = (new Vector3d(up)).cross(n);
		Vector3d v = (new Vector3d(n)).cross(u);
  		n.normalize();
		//u.normalize();
		v.normalize();
		this.setRotation(u, v, n);
		
		System.out.println("Made camera,");
		System.out.println("pos:"+eye);
		System.out.println("u: "+u+", v: "+v+", n:"+n);
		System.out.println("rotation:\n"+rotation);
		System.out.println("ViewMatrix: \n"+this.viewMatrix());
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
		rotation._m00(u.x); rotation._m01(u.y); rotation._m02(u.z);
		rotation._m10(v.x); rotation._m11(v.y); rotation._m12(v.z);
		rotation._m20(n.x); rotation._m21(n.y); rotation._m22(n.z);
	}
	
	/*
	 * Returns the view matrix of this camera generated via
	 * the eye and rotation
	 * 
	 * @return Matrix4d containing the view matrix
	 */
	private Matrix4d viewMatrix() {
				
		Matrix4d view = new Matrix4d(this.rotation);
		view.translate(this.eye);
		
		return view;
	}
	
	public Ray generateRay( Sample samp, int pixelX, int pixelY) {
		
		
		double aspectRatio = film.getHeight() / (double)film.getHeight();   // imageWidth / (float)imageHeight;
		double fov = 60 / 2 * Math.PI / 180; // convert fov to radians
		
		// Get camera coordinates of pixels
		double camX = (2 * (((double)pixelX + samp.getOffsetX()) / film.getWidth()) - 1) * Math.tan(fov) * aspectRatio;
		double camY = (1 - 2 * (((double)pixelY + samp.getOffsetY()) / film.getHeight()))* Math.tan(fov);
	
		// Get direction and origin in world coordinates
		Vector3d rayDirection = (new Vector3d(camX, camY, -1)).mulDirection(this.viewMatrix());
		Vector3d rayOrigin = new Vector3d(this.eye);
	
		rayDirection.normalize();
		
		//System.out.println("dir: "+rayDirection+"\norigin:"+rayOrigin);

		return new Ray(rayOrigin, rayDirection);
	}	

}
