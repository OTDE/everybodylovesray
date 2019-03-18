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
		
		// FOR TESTING PURPOSES
		g2d = renderedImage.createGraphics();
        g2d.setColor(Color.RED);
	}// Film
	
	/**
	 * Getter for the BufferedImage thatholds the image in process
	 * @return
	 */
	public BufferedImage getRenderedImage() {

		return renderedImage;
	}// getRenderedImage
	
		
	public int getWidth() {	return this.width; }
	
	public int getHeight() { return this.height; }

	public void develop(Sample s, int pixelX, int pixelY, Color c) {
		
		double rX = s.getOffsetX() + pixelX;
		double rY = s.getOffsetX() + pixelY;
		double pixelCenterX = 0.5 + pixelX;
		double pixelCenterY = 0.5 + pixelY;
						
		if(renderedImage.getRGB(pixelX, pixelY) == Color.GRAY.getRGB() || renderedImage.getRGB(pixelX, pixelY) == 0 ) {
			renderedImage.setRGB(pixelX,pixelY,c.getRGB());	
		}else {
			//System.out.println("color there");
		}
	}
	
	public Color blendColor(double weight, Color inFilm, Color fromRay) {
		return null;
	}
}
