package com.shffl.assets;
import org.joml.Vector3d;

public class Light {
	public double[] intensity;
	public double[] location;
	
	public transient Vector3d i;
	public transient Vector3d loc;
	
	public void convert() {
		i = new Vector3d(intensity[0], intensity[1], intensity[2]);
		loc = new Vector3d(intensity[0], intensity[1], intensity[2]);
	}
}
