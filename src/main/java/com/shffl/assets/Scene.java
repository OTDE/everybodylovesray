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
			    Vector3d s, edge1, edge2, v0, v1, v2, normal, rayDirection;
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
			

			    // For calculating Point Normal later
			    normal = (new Vector3d(edge1)).cross(edge2);
			
			    // Get intersection point
			    double d = (new Vector3d(normal)).dot(v0);
			    double t = ((new Vector3d(normal)).dot(r.origin) + d);
			    double nDotDirection = -1 * (new Vector3d(normal).dot(r.direction));
			    t = t / nDotDirection;
			    Vector3d P = (new Vector3d(r.direction).mul(t));
			    P = P.add(r.origin);
			
			    // First check b1
			    rayDirection = new Vector3d(r.direction);
			    barycentric1 = coefficient * rayDirection.cross(edge2).dot(s);
			
			    if (barycentric1 > 0 && barycentric1 < 1) {
			    
				    // Next check b2 with the same parameters
				    rayDirection = new Vector3d(r.direction);
				    barycentric2 = coefficient * s.cross(edge1).dot(rayDirection);
				
				    if(barycentric2 > 0 && barycentric2 < 1 && barycentric1 + barycentric2 <= 1) {
							
					    //Vector3d norm = new Vector3d(barycentric1, barycentric2, 1 - barycentric1 - barycentric2);
					    //norm.normalize();
					    //inter.setNormal(norm);
					
					    // TEMP, just for use with obj. models that don't include point normals
					    v0.normalize();
					    v1.normalize();
					    v2.normalize();
					
					    v0.mul(1-barycentric1-barycentric2);
					    v1.mul(barycentric1);
					    v2.mul(barycentric2);
					
					    Vector3d norm = new Vector3d(v0).add(v1).add(v2);
					    norm.normalize();
					    inter.setNormal(norm);
					
					
				    }else {
					    // Didn't hit
				    }
			    }else {
			    	// Didn't hit
			    }
			}
		}
		return inter;
	}// intersect
	
	private Vector3d getCartesianPoint(Vector3d barycentric, Face f) {
		Vector3d x = new Vector3d();
		Vector3d y = new Vector3d();
		Vector3d z = new Vector3d();
		f.vertices.get(0).v.mul(barycentric.x, x);
		f.vertices.get(1).v.mul(barycentric.y, y);
		f.vertices.get(2).v.mul(barycentric.z, z);
		return x.add(y.add(z));
	}
	
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
