package com.shffl.assets;

import org.joml.Vector3d;

public class Intersection {
	
	private Vector3d position;
	private Vector3d normal; 
	public boolean hasNormal = false;
	public Material material;
	
	public Intersection() {
		material = new Material();
	}
	
	/**
	 * Initializes the material of the point of Ray-Triangle intersection
	 * 
	 * @param diff Vector3d the RGB data diffuse reflection of the material
	 * @param spec Vector3d the RGB data specular reflection of the material
	 * @param shin double the shininess coefficient of the material
	 */
	public void setMaterialAttributes(Vector3d diff, Vector3d spec, double shin) {
		this.material.diffuse = new Vector3d(diff);
		this.material.specular = new Vector3d(spec);
		this.material.shiny = shin;
	}
	
	public void setPosition(Vector3d p) {
		this.position = new Vector3d(p);
	}
	public Vector3d getPosition() { return this.position; }
	
	public void setNormal(Vector3d n) {
		this.normal = new Vector3d(n);
		this.hasNormal = true;
	}
	public Vector3d getNormal() { return this.normal; }
	
	public class Material{
		public Vector3d diffuse;
		public Vector3d specular;
		public double shiny;
	}
}

