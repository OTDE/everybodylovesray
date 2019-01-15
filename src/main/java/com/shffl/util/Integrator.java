package com.shffl.util;
import java.awt.Color;

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
		
		if( rendCon.getEnvironement().intersect(r, inter)) {
			
			// If this returned true, we hit an object
			return Color.RED;
		}
		
		return Color.BLUE;
	}

}
