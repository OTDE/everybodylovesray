package com.shffl.assets;
import java.io.IOException;

import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector4d;

import com.owens.oobjloader.builder.*;
import com.owens.oobjloader.parser.*;

/**
 * Object Model class.
 * @author Ethan Wiederspan and Seth Chapman
 *
 */
public class ObjModel {
	
	public String filepath;
	public float[] translation;
	public float[] rotation;
	
	/*
	 * Fields for accessing data:
	 * Geometric verticies: objData.verticiesG
	 * Vertex normals: objData.verticiesN
	 * Faces (these are our triangles): objData.faces
	 * Face normal vector: objData.faces.get(index).faceNormal
	 * 
	 * Notes:
	 * Each face in objData.faces has access to calculateTriangleNormal(),
	 * which sets a normal vector for the face that is zero by default until
	 * the method is called.
	 */
	
	private transient Build objData;
	private transient Vector4d objTranslate;
	private transient Matrix4d objRotate;
	
	public void build() {
		objData = new Build();
		try {
			objTranslate = new Vector4d(translation[0],
						translation[1], translation[2],1);
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Translation Input array is incorrectly sized");
		}
		try {
			objRotate = new Matrix4d(
					rotation[0], rotation[1], rotation[2], rotation[3],
					rotation[4], rotation[5], rotation[6], rotation[7],
					rotation[8], rotation[9], rotation[10], rotation[11], 
					objTranslate.x, objTranslate.y, objTranslate.z, rotation[15] );
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Rotation Input matrix is incorrectly sized");
		}
		
		System.out.println("obj rotate: \n"+objRotate);
		
		
		
	}
	
	
	
	public void parse() {
		try {
			Parse objParser = new Parse(objData, filepath);
		} catch(IOException e) {
			System.out.println("File not found");
		}
	}
	
	public Intersection intersect(Ray r, Intersection inter) {
		
		int i = 0;
		for(Face f: objData.faces) {
			i++;
			
			// Determine if there is an intersection between the ray and face.
			Vector3d s, edge1, edge2, v0, v1, v2, rayDirection, rayOrigin;
			double coefficient, barycentric1, barycentric2;

			
			// Get edge vertices
			v0 = translatePoint(f.vertices.get(0).v);
			v1 = translatePoint(f.vertices.get(1).v);
			v2 = translatePoint(f.vertices.get(2).v);
			
			s = (new Vector3d(r.origin)).sub(v1); 
			
			edge1 = (new Vector3d(v1)).sub(v0);   
			edge2 = (new Vector3d(v2)).sub(v0);  
			
			rayDirection = new Vector3d(r.direction);
			coefficient = 1 / (rayDirection.cross(edge2).dot(edge1));
			
			// First check b1
			rayDirection = new Vector3d(r.direction);
			barycentric1 = coefficient * rayDirection.cross(edge2).dot(s);
			//System.out.println("b1: "+barycentric1);
			if (barycentric1 > 0 && barycentric1 < 1) {
				
				// Next check b2 with the same parameters
				rayDirection = new Vector3d(r.direction);
				barycentric2 = coefficient * s.cross(edge1).dot(rayDirection);
				
				//System.out.println("b2: "+barycentric2);
				
				if(barycentric2 > 0 && barycentric2 < 1 & barycentric1 + barycentric2 <= 1) {
					
					// The ray intersected the face, update the intersection's data
					f.calculateTriangleNormal();
					inter.hasNormal = true;
					inter.setNormal(new Vector3d(f.faceNormal.x, f.faceNormal.y, f.faceNormal.z));
					return inter;
				}else {
					// Didn't hit
				}
			}else {
				// Didn't hit
			}
			
			
		}
		return inter;

	}
		
	public Vector3d translatePoint(Vector3d v) {
		
		Vector3d point = new Vector3d(v.x, v.y, v.z);
		point.mulPosition(this.objRotate);
		
		return point;
	}
}
