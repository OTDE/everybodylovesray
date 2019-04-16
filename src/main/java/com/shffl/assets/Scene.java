package com.shffl.assets;
import java.util.ArrayList;

import org.joml.Vector3d;

import com.owens.oobjloader.builder.Face;

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
	public double[] globalLight;

	public ObjModel[] objects;

	private transient Octree faceStorage;
	public transient ArrayList<Face> allFaces;
	public transient Vector3d eye;
	public transient Vector3d at;
	public transient Vector3d up;
	public transient Vector3d ambient;
	public transient Light[] lightSources;

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
	 * @return Vectro3d indicating upwards in the scene
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

	/**
	 * Converts the globalLight array into a Vector3d if it hasn't already. Returns ambient otherwise.
	 * Returns zero vector if both the globalLight array and the vector aren't initialized.
	 * @return Vectro3d indicating RGB values of the ambient light of the scene
	 */
	public Vector3d getAmbient() {
		if(ambient == null) {
			if(globalLight != null) {
				ambient = new Vector3d(globalLight[0], globalLight[1], globalLight[2]);
				return ambient;
			}
			return new Vector3d(0, 0, 0);
		}
		return ambient;
	}

	/**
	 * Checks a given ray against all the objects in a scene for intersections. 
	 * Utilizes barycentric coordinates to make an intersection test against 
	 * each triangle in each model contained in the scene. 
	 * 
	 * @param r Ray cast on the scene
	 * @param inter Intsection to fill with information about the triangle hit 
	 *        by the ray
	 */
	public Intersection intersect(Ray r, Intersection inter) {

		ArrayList<Face> faceGroup = faceStorage.getFacesWithin(r);
		for(Face f: faceGroup) {
			// Determine if there is an intersection between the ray and face.
			Vector3d s, edge1, edge2, v0, v1, v2, rayDirection;
			double denom, coefficient, b1, b2, t;


			// Get edge vertices
			v0 = new Vector3d(f.vertices.get(0).v);
			v1 = new Vector3d(f.vertices.get(1).v);
			v2 = new Vector3d(f.vertices.get(2).v);

			s = (new Vector3d(r.origin)).sub(v0); 
			edge1 = (new Vector3d(v1)).sub(v0);   
			edge2 = (new Vector3d(v2)).sub(v0);  

			rayDirection = new Vector3d(r.direction);
			denom = (rayDirection.cross(edge2).dot(edge1));
			coefficient = 1 / denom;

			// First check b1
			rayDirection = new Vector3d(r.direction);
			b1 = coefficient * rayDirection.cross(edge2).dot(s);

			if (b1 > 0 && b1 < 1) {

				// Next check b2 with the same parameters
				rayDirection = new Vector3d(r.direction);
				b2 = coefficient * new Vector3d(s).cross(edge1).dot(rayDirection);

				if(b2 > 0 && b2 < 1 && b1 + b2 <= 1) {

					// Next check t against tMax
					t = coefficient * s.cross(edge1).dot(edge2);

					// Only calculate intersection data if it is the closest intersection point
					if(r.tMax == -1 || t < r.tMax) {

						r.tMax = t;

						// Fill in intersection with normal of intersection 
						v0 = new Vector3d(f.vertices.get(0).n);
						v1 = new Vector3d(f.vertices.get(1).n);
						v2 = new Vector3d(f.vertices.get(2).n);

						v0.mul(1-b1-b2);
						v1.mul(b1);
						v2.mul(b2);

						Vector3d norm = new Vector3d(v0).add(v1).add(v2);
						norm.normalize();

						inter.setNormal(norm);

						// Get position of intersection
						inter.setPosition(r.positionAtTMax());

						// Get materials of triangle

						Vector3d ambient = f.material.ka.getRGB();
						Vector3d diffuse = f.material.kd.getRGB();
						Vector3d specular = f.material.ks.getRGB();
						double shiny = f.material.nsExponent;

						inter.setMaterialAttributes(ambient,diffuse,specular,shiny);

					}
				}else {
					// Didn't hit
				}
			}else {
				// Didn't hit
			}
		}// for faces

		return inter;
	}// intersect


	public void initializeFaces() {
		allFaces = new ArrayList<Face>();
		for(ObjModel obj : objects) {
			allFaces.addAll(obj.objData.faces);
		}
		Vector3d min = new Vector3d(-2.0, -2.0, -2.0);
		Vector3d max = new Vector3d(2.0, 2.0, 2.0);
		faceStorage = new Octree(allFaces, new BoundingBox(min, max));
		//faceStorage.build(0);
		faceStorage.testNodes();
	}

	/**
	 * Determines if ray intersects any object in the direction of a light 
	 * sources, used to determine if a shadow is being cast onto a point.
	 * 
	 * @param r Ray with a direction towards a light source.
	 * @return boolean describing whether the point is within a shadow.
	 */
	public boolean shadowIntersect(Ray r) {

		for(ObjModel obj: objects) {
			for(Face f: obj.objData.faces) {

				// Determine if there is an intersection between the ray and face.
				Vector3d s, edge1, edge2, v0, v1, v2, rayDirection;
				double denom, coefficient, b1, b2;

				// Get edge vertices
				v0 = new Vector3d(f.vertices.get(0).v);
				v1 = new Vector3d(f.vertices.get(1).v);
				v2 = new Vector3d(f.vertices.get(2).v);

				s = (new Vector3d(r.origin)).sub(v0); 
				edge1 = (new Vector3d(v1)).sub(v0);   
				edge2 = (new Vector3d(v2)).sub(v0);  

				rayDirection = new Vector3d(r.direction);
				denom = (rayDirection.cross(edge2).dot(edge1));
				coefficient = 1 / denom;

				// First check b1
				rayDirection = new Vector3d(r.direction);
				b1 = coefficient * rayDirection.cross(edge2).dot(s);

				if (b1 > 0 && b1 < 1) {

					// Next check b2 with the same parameters
					rayDirection = new Vector3d(r.direction);
					b2 = coefficient * new Vector3d(s).cross(edge1).dot(rayDirection);

					if(b2 > 0 && b2 < 1 && b1 + b2 <= 1) {

						// Make sure collision is in front of the object
						double t = coefficient * s.cross(edge1).dot(edge2);
						if(t > 0) {
							r.tMax = t;							
							// It hit something, its in a shadow
							return true;
						}
					}else {
						// Didn't hit
					}
				}else {
					// Didn't hit
				}
			}// for faces
		}// for objects
		// Didn't hit anything
		return false;
	}
}
