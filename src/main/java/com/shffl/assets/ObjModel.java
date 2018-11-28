package com.shffl.assets;
import java.io.IOException;

import org.joml.Matrix4d;
import org.joml.Vector3d;

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
			objData.doneParsingObj(filepath);
		} catch(IOException e) {
			System.out.println("File not found");
		}
	}
}