package com.shffl.util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * Film Class holds the Buffered Image that acts as 
 * the picture being developed. In Sprint01, Film 
 * simply holds a Buffered Image with the dimensions 
 * given by the JSON. 
 *
 */
public class Film {
	
	private BufferedImage renderedImage;
	public Graphics2D g2d;
	
	private int width;
	private int height;
	
	/**
	 * Constructor for Film Class. Builds the Buffered Image 
	 * with the dimensions given.
	 * 
	 * @param width the width specified by the input JSON
	 * @param height the height specified by the input JSON
	 */
	public Film(int w, int h) {

		this.width = w;
		this.height = h;
		renderedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}//constructor
	
	/**
	 * Getter for the BufferedImage thatholds the image in process
	 * @return
	 */
	public BufferedImage getRenderedImage() {
		return renderedImage;
	}// getRenderedImage
	
	//Accessors	
	
	public int getWidth() {	return this.width; }
	
	public int getHeight() { return this.height; }

	/**
	 * Develops the image at a given pixel location.
	 * @param x the pixel's x-coordinate
	 * @param y the pixel's y-coordinate
	 * @param color the color to develop the film at
	 */
	public void develop(int x, int y, Color color) {
		renderedImage.setRGB(x,y,color.getRGB());
	}//develop
	
}//class
