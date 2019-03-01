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
		
		System.out.println("propping this damn ray, bingus: "+r.direction);
		
		Color rayColor = Color.GRAY;
		
		inter = new Intersection();
		inter = rendCon.getScene().intersect(r, inter);
		if(inter.hasNormal) {
			
			// If this returned true, we hit an object
			rayColor = getColorFromIntersection(inter);
			
		}
		
		return rayColor;
	}
	
	public Color getColorFromIntersection(Intersection inter) {
		
		Vector3d normal = new Vector3d(inter.getNormal());
		return new Color((float)normal.x, (float)normal.y, (float)normal.z);
	}

}
