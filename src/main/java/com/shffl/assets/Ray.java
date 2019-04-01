package com.shffl.assets;
import org.joml.Vector3d;
import org.joml.Vector4d;

public class Ray {
	
	public Vector3d origin;
	public Vector3d direction;
	public Vector3d inverse;
	
	public double tMax = 0.0;
	public double time = 0.0;
	public double radiance = 1.0;
	
	public Ray(Vector3d o, Vector3d d) {
		this.origin = o;
		this.direction = d;
		this.inverse = new Vector3d(1/d.x, 1/d.y, 1/d.z);
	}
	
	/*
	public Vector4d positionAtTime(double t) {
		return origin.add(direction.mul(t));
	}
	*/
}
