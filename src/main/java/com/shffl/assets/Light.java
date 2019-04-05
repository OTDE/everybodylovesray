package com.shffl.assets;
import org.joml.Vector3d;

public class Light {
	public double[] intensity;
	public double[] position;
	
	public transient Vector3d i;
	public transient Vector3d pos;
	
	public void convert() {
		i = new Vector3d(intensity[0], intensity[1], intensity[2]);
		pos = new Vector3d(position[0], position[1], position[2]);
	}
}
