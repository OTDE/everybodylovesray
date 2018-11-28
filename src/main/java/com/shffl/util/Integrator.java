package com.shffl.util;
import java.awt.Color;

import com.shffl.assets.Ray;
import com.shffl.control.RenderController;

public class Integrator {
	
	private RenderController rendCon;
	private Intersection inter;
	
	public Integrator(RenderController rCon) {
		
		this.rendCon = rCon;
	}
	
	public Color propagate(Ray r) {
		
		
		return Color.PINK;
		
	}

}
