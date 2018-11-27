import java.io.IOException;

import com.owens.oobjloader.builder.*;
import com.owens.oobjloader.parser.*;

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
	
	public void build() {
		objData = new Build();
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
