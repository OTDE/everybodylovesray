package com.shffl.util;


/**
 * Sample class for storing sample data.
 * @author Ethan Wiederspan and Seth Chapman
 *
 */
public class Sample {
	
	private double offsetX;
	private double offsetY;
	
	// These additional pieces of information regarding camera info have yet to be implemented.
	// private int time;
	// private double camX;
	// private double camY;
	
	/**
	 * Constructor for the sample class. Initializes sample information.
	 * @param offX the amount by which a ray is offset in the x direction in a given pixel
	 * @param offY the amount by which a ray is offset in the y direction in a given pixel
	 */
	public Sample(double offX, double offY) {
		
		offsetX = offX;
		offsetY = offY;
	}

	/**
	 * Accessors and mutators
	 */
	public double getOffsetX() { return offsetX; }

	public double getOffsetY() { return offsetY; }
}
