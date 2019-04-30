package com.shffl.assets;

import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * Class containing information about the scene's light sources.
 * These are point lights.
 */
public class Light {
	
	//Read by GSON parser
	public double[] intensity;
	public double[] position;
	
	//Values used by program
	public transient Vector3d i;
	public transient Vector3d pos;
	
	/**
	 * Transfers information from fields parsed by GSON
	 * directly to values used inside the program.
	 */
	public void convert() {
		i = new Vector3d(intensity[0], intensity[1], intensity[2]);
		pos = new Vector3d(position[0], position[1], position[2]);
	}//convert
	
}//class
