package com.shffl.assets;

import java.awt.Color;

import org.joml.Vector3d;

public class PixelColor {
	
	private Vector3d color;
	private double weight;
	
	public PixelColor(Vector3d color, double weight) {
		this.color = new Vector3d(color);
		this.weight = weight;
	}
	
	public PixelColor() {
		this.color = new Vector3d(0,0,0);
		this.weight = 0;
	}

	public void updateColor(Vector3d color, double weight) {
		
		this.color.add(color.mul(weight));
		this.weight += weight;
	}
	
	public Color getColor() {
		
		float r = (float) (this.color.x / weight);
		float g = (float) (this.color.y / weight);
		float b = (float) (this.color.z / weight);
			
		return new Color(r, g, b);
	}
	
	

}
