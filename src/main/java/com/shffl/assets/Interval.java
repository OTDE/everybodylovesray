package com.shffl.assets;

public class Interval {
	
	public double min;
	public double max;
	
	public Interval(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public Interval(double minAndMax) {
		this.min = minAndMax;
		this.max = minAndMax;
	}

}
