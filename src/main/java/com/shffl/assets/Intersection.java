package com.shffl.assets;

import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * Class for storing information about an intersection between
 * a ray and some object inside a 3D scene.
 */
public class Intersection {
	
	private Vector3d position;
	private Vector3d normal; 
	public boolean hasNormal = false;
	public Material material;
	
	/**
	 * Default constructor.
	 * Sets default values for this intersection's materials
	 * (instantiating non-primitive data).
	 */
	public Intersection() {
		material = new Material();
		this.material.ambient = new Vector3d(0,0,0);
		this.material.diffuse = new Vector3d(0,0,0);
		this.material.specular = new Vector3d(0,0,0);
	}//default constructor
	
	/**
	 * Sets the material attributes for the material hit at this intersection.
	 * 
	 * @param amb Vector3d the RGB data for the ambient reflection of the material
	 * @param diff Vector3d the RGB data for the diffuse reflection of the material
	 * @param spec Vector3d the RGB data for the specular reflection of the material
	 * @param shin the shininess coefficient of the material
	 * @param mir the amount of light the material reflects. (0 <= mir <= 1)
	 * @param ind the index of refraction of the material.
	 * @param op the opacity of the material. (0 <= op <= 1)
	 */
	public void setMaterialAttributes(Vector3d amb, Vector3d diff, Vector3d spec, double shin, double mir, double ind, double op) {
		this.material.ambient = new Vector3d(amb);
		this.material.diffuse = new Vector3d(diff);
		this.material.specular = new Vector3d(spec);
		this.material.shiny = shin;
		this.material.mirror = mir;
		this.material.opacity = op;
		this.material.indexOfRefraction = ind;
	}//setMaterialAttributes
	
	/**
	 * @author Ethan Wiederspan
	 * Private subclass designed to store material values.
	 */
	public class Material {
		public Vector3d ambient;
		public Vector3d diffuse;
		public Vector3d specular;
		public double shiny = 0.0;
		public double mirror = 0.0;
		public double opacity = 1.0;
		public double indexOfRefraction = 1.0;
	}//class
	
	// Accessors and Mutators
	
	public void setPosition(Vector3d p) {
		this.position = new Vector3d(p);
	}
	public Vector3d getPosition() { return this.position; }
	
	public void setNormal(Vector3d n) {
		this.normal = new Vector3d(n);
		this.hasNormal = true;
	}
	public Vector3d getNormal() { return this.normal; }
	

}//class

