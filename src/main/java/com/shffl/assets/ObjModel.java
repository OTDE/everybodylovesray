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
	private transient Vector3d objTranslate;
	private transient Matrix4d objRotate;
	
	public void build() {
		objData = new Build();
		try {
			objTranslate = new Vector3d(translation[0],
						translation[1], translation[2]);
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Input array is incorrectly sized");
		}
		try {
			objRotate = new Matrix4d(
					rotation[0], rotation[1], rotation[2], rotation[3],
					rotation[4], rotation[5], rotation[6], rotation[7],
					rotation[8], rotation[9], rotation[10], rotation[11], 
					rotation[12], rotation[13], rotation[14], rotation[15] );
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Input matrix is incorrectly sized");
		}
		
	}
	
	public void parse() {
		try {
			Parse objParser = new Parse(objData, filepath);
		} catch(IOException e) {
			System.out.println("File not found");
		}
	}
	
	public Intersection intersect(Ray r, Intersection inter) {
		
		for(Face f: objData.faces) {
			
			// Determine if there is an intersection between the ray and face.
			Vector3d s, edge1, edge2;
			double coefficient, barometric1, barometric2;
			
			//System.out.println("testing triangle at:\n("+ f.vertices.get(0)+", "+f.vertices.get(1)+", "+f.vertices.get(2)+")");
			
			// Get edge vertices
			s = subtractVertices(r.origin, f.vertices.get(0).v);
			edge1 = subtractVertices(f.vertices.get(1).v, f.vertices.get(0).v);
			edge2 = subtractVertices(f.vertices.get(2).v, f.vertices.get(0).v);
			coefficient = 1 / (r.direction.cross(edge2).dot(edge1));
			
			// First check b1, must satisfy b1 >= 0 && b1 + b2 =< 1
			barometric1 = coefficient * r.direction.cross(edge2).dot(s);
			if (barometric1 < 0 || barometric1 > 1) {
				// Return the null intersection
				return inter;
			}
			
			// Next check b2 with the same parameters
			barometric2 = coefficient * s.cross(edge1).dot(r.direction);
			if(barometric2 < 0 || barometric1 + barometric2 > 1) {
				// Return the null intersection
				return inter;
			}
		
			// The ray intersected the face, update the intersection's data
			
			inter.hasNormal = true;
			inter.setNormal(new Vector3d(f.faceNormal.x, f.faceNormal.y, f.faceNormal.z));
			System.out.println("Normal vector:" + inter.getNormal());
			return inter;
		}
		
		return inter;

	}
	
	public Vector3d subtractVertices(VertexGeometric leftSide, VertexGeometric rightSide) {
		
		return new Vector3d(leftSide.x - rightSide.x, leftSide.y - rightSide.y, leftSide.z - rightSide.z);
	}
	
	public Vector3d subtractVertices(Vector3d leftSide, VertexGeometric rightSide) {
		
		return new Vector3d(leftSide.x - rightSide.x, leftSide.y - rightSide.y, leftSide.z - rightSide.z);
	}
}
