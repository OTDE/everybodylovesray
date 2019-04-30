package com.shffl.assets;

import java.awt.Color;

import org.joml.Vector3d;

/**
 * @author Ethan Weiderspan and Seth Chapman
 * Information storage class for information regarding a pixel's color.
 */
public class PixelColor {
	
	private Vector3d color;
	private double weight;
	
	/**
	 * Creates a new PixelColor object with the given color and weight.
	 * @param color the color to input
	 * @param weight the weight to input
	 */
	public PixelColor(Vector3d color, double weight) {
		this.color = new Vector3d(color);
		this.weight = weight;
	}//constructor
	
	/**
	 * Empty constructor. Sets values to their defaults.
	 * (That is, zero.)
	 */
	public PixelColor() {
		this.color = new Vector3d(0,0,0);
		this.weight = 0;
	}//empty constructor

	//Fancy accessors and mutators
	
	public void updateColor(Vector3d color, double weight) {
		this.color.add(color.mul(weight));
		this.weight += weight;
	}//updateColor
	
	public Color getColor() {
		
		float r = (float) (this.color.x / weight);
		float g = (float) (this.color.y / weight);
		float b = (float) (this.color.z / weight);
			
		return new Color(r, g, b);
	}//getColor
	
}//class
