package com.shffl.util;
import java.util.Random;

/**
 * Container class for holding samples and a reference to a pixel.
 * @author Ethan Wiederspan and Seth Chapman
 *
 */
public class SampleArray {

	private int pixelX;
	private int pixelY;
	private int sampleSize;
	private Random rng;
	
	public Sample[] samples;
	
	/**
	 * Constructor for the sample array with a specified pixel
	 * @param pX the specified x coordinate for a pixel
	 * @param pY the specified y coordinate for a pixel
	 * @param size the size of the sample array
	 */
	public SampleArray(int pX, int pY, int size) {
		
		pixelX = pX;
		pixelY = pY;
		sampleSize = size;
		samples = new Sample[sampleSize];
		rng = new Random();
	}//constructor (3-param)
	
	/**
	 * Constructor for the sample array without a specified pixel
	 * @param size the size of the sample array
	 */
	public SampleArray(int size) {
		
		sampleSize = size;
		samples = new Sample[sampleSize];
		rng = new Random();
	}//constructor (1-param)
	
	/**
	 * Fills the array of samples with new samples
	 */
	public void fill() {
		
		for(int i = 0; i < sampleSize; i++) {
			samples[i] = new Sample(rng.nextDouble(), rng.nextDouble());
		}
	}//fill
	
	//Accessors and Mutators
	
	public void setPixelX(int pixelX) { this.pixelX = pixelX; }

	public void setPixelY(int pixelY) { this.pixelY = pixelY; }

	public int getPixelX() { return pixelX; }

	public int getPixelY() { return pixelY; }

	public int getSampleSize() { return sampleSize; }
	
}//class
