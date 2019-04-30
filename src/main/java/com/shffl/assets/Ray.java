package com.shffl.assets;

import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * Ray class. Defined as having an origin and a direction.
 */
public class Ray {
	
	public Vector3d origin;
	public Vector3d direction;
	
	public double tMax = Double.POSITIVE_INFINITY;
	public double time = 0.0;
	public double radiance = 1.0;
	
	public Ray(Vector3d o, Vector3d d) {
		this.origin = new Vector3d(o);
		this.direction = new Vector3d(d);
	}//constructor
		
	/**
	 * Calculates and returns the position of the ray at time tMax
	 * 
	 * @return Vector3d holding the position as an xyz coordinate
	 */
	public Vector3d positionAtTMax() {
		Vector3d distance = new Vector3d(direction).mul(this.tMax);
		return new Vector3d(origin).add(distance);
	}//positionAtTMax
	
	/**
	 * Nudges the origin of this ray towards the ray's direction. 
	 * Aids in preventing a ray colliding with the face it was cast from
	 */
	public void nudgeOrigin(double nudgeFactor) {
		Vector3d nudge = new Vector3d(this.direction);
		nudge.mul(nudgeFactor);
		this.origin = this.origin.add(nudge);
	}//nudgeOrigin

}//class
