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
	
	public ObjModel[] objects;
	
	private transient Octree faceStorage;
	public transient ArrayList<Face> allFaces;
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

		ArrayList<Face> currentFaces = faceStorage.getFacesWithin(r);	
		if(currentFaces != null) {
			System.out.println(currentFaces.size());
			for(Face f: currentFaces) {
				// Determine if there is an intersection between the ray and face.
				Vector3d s, edge1, edge2, v0, v1, v2, rayDirection;
				double denom, coefficient, barycentric1, barycentric2;

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
				barycentric1 = coefficient * rayDirection.cross(edge2).dot(s);
			
				if (barycentric1 > 0 && barycentric1 < 1) {
			    
					// Next check b2 with the same parameters
					rayDirection = new Vector3d(r.direction);
					barycentric2 = coefficient * s.cross(edge1).dot(rayDirection);
				
					if(barycentric2 > 0 && barycentric1 + barycentric2 <= 1) {
					
						// The ray intersected the face, update the intersection's data
						f.calculateTriangleNormal();
						inter.hasNormal = true;
						f.faceNormal.normalize();
						inter.setNormal(new Vector3d(f.faceNormal.x, f.faceNormal.y, f.faceNormal.z));
						return inter;
					} else {
						// Didn't hit
					}
				} else {
					// Didn't hit
				}
			}
		}
		return inter;
	}// intersect
	
	public void initializeFaces() {
		allFaces = new ArrayList<Face>();
		for(ObjModel obj : objects) {
			allFaces.addAll(obj.objData.faces);
		}
		Vector3d min = new Vector3d(-0.5, -0.5, -0.5);
		Vector3d max = new Vector3d(0.5, 0.5, 0.5);
		faceStorage = new Octree(allFaces, new BoundingBox(min, max));
	}
}
