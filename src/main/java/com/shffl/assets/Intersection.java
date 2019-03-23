package com.shffl.assets;

import org.joml.Vector3d;

public class Intersection {
	
	private Vector3d position;
	private Vector3d normal; // will only be used by the normal integrator
	// private Face face;
	// private ObjModel obj;
	public boolean hasNormal = false;
	
	public Intersection() {
		
	}
	
	public void setNormal(Vector3d n) {
		this.normal = n;
	}
	public Vector3d getNormal() {
		return this.normal;
	}

}