package com.shffl.util;
import java.awt.Color;
import java.util.Vector;

import org.joml.Vector3d;

import com.shffl.assets.Intersection;
import com.shffl.assets.Ray;
import com.shffl.control.RenderController;

public class Integrator {
	
	private RenderController rendCon;
	private Intersection inter;
	
	public Integrator(RenderController rCon) {
		
		this.rendCon = rCon;
	}
	
	public Vector3d propagate(Ray r) {
				
		Vector3d rayColor = new Vector3d(1, 1, 1);
		
		inter = new Intersection();
		inter = rendCon.getScene().intersect(r, inter);
		if(inter.hasNormal) {
			
			// If this returned true, we hit an object
			rayColor = getRGBFromIntersection(inter);
			
		}
		
		return rayColor;
	}
	
	public Vector3d getRGBPhong(Intersection inter) {
		
		// WOP VERSRION --------------------
		
		Vector3d diffuse, specular;
		
		Vector3d lightPosition = new Vector3d();
		Vector3d lightDirection = new Vector3d(lightPosition);
		//lightDirection.sub(scene.camera.eye);
		
		double lDotN = lightDirection.dot(inter.getNormal());
		if(lDotN < 0) {
			lDotN = 0;
		}else {
			
		}
		
		// Calculate diffuse 
		// diffuse = new Vector3d(inter.material.diffuse).mul(lDotN);
		
		// Calculate specular
		
		return null;
	}
	
	public Vector3d getRGBFromIntersection(Intersection inter) {
		
		Vector3d n = new Vector3d(inter.getNormal());
		n.normalize();
				
		for(int i = 0; i < 3; i++) {
			n.setComponent(i, (n.get(i)+1)/2 );
			/*
			if(n.get(i) < 0) {
				n.setComponent(i, -n.get(i));
			}else {
				
			}
			*/
		}
			
		return n;
		
	}

}
