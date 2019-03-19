package com.shffl.util;
import java.awt.Color;

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
	
	public Color propagate(Ray r) {
				
		Color rayColor = Color.GRAY;
		
		inter = new Intersection();
		rendCon.getScene().initializeFaces();
		inter = rendCon.getScene().intersect(r, inter);
		if(inter.hasNormal) {
			
			// If this returned true, we hit an object
			rayColor = getColorFromIntersection(inter);
			
		}
		
		return rayColor;
	}
	
	public Color getColorFromIntersection(Intersection inter) {
		
		Vector3d n = new Vector3d(inter.getNormal());
		double[] normal = {n.x, n.y, n.z};
		
		for(int i = 0; i < 3; i++){
			if(normal[i] < 0){
				normal[i] = -normal[i];
			}
			if(normal[i] > 1){
				normal[i] = 1/normal[i];
			} 
		}
		
		Color c = new Color(0,0,0);
		try {
			 c = new Color((float)normal[0], (float)normal[1], (float)normal[2]);
		} catch(IllegalArgumentException e) {
			System.out.println("Bad Color: ("+normal[0]+","+normal[1]+","+normal[2]+")");
		}
		
		return c;
		
	}

}
