package com.shffl.assets;

import org.joml.Vector3d;

public class Plane {
	
	public Vector3d normal;
	public double distance;
	
	public Plane(Vector3d normal, double distance) {
		this.normal = normal;
		this.distance = distance;
	}
	
	public double pointDistance(Vector3d point) {
		return point.dot(this.normal) - this.distance;
	}
	
	// gives time at which plane intersects with ray
	public double intersectsWith(Vector3d origin, Vector3d direction) {
		double nd = direction.dot(this.normal);
		double pn = origin.dot(this.normal);
		if(nd >= 0.0)
			return -1;
		double t = (this.distance - pn) / nd;
		if(t >= 0.0)
			return t;
		return -1;
	}
	
	// gives point at which ray intersects with plane
	public double distanceAtTime(Vector3d origin, Vector3d direction, double t) {
		Vector3d tv = new Vector3d(direction);
		tv.mul(t);
		Vector3d result = new Vector3d(origin);
		result.add(tv);
		return result.length();
	}
}
