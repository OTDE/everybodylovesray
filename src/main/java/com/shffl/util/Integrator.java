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
		
		inter = rendCon.getScene().intersect(r, inter);
		if(inter != null) {
			
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
