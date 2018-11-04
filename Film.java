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
	
	/**
	 * Constructor for Film Class. Builds the Buffered Image 
	 * with the dimensions given.
	 * 
	 * @param width the width specified by the input JSON
	 * @param height the height specified by the input JSON
	 */
	public Film(int width, int height) {
		renderedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}// Film
	
	/**
	 * Getter for the BufferedImage thatholds the image in process
	 * @return
	 */
	public BufferedImage getRenderedImage() {
		return renderedImage;
	}// getRenderedImage
	
	/**
	 * Draws the entire buffered image red, used to test 
	 * various systems.
	 */
	public void makeTestImage() {
		
		System.out.println("making a test Image");
		Graphics2D g2d = renderedImage.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, renderedImage.getWidth(), renderedImage.getHeight());
        g2d.dispose();
        
        System.out.println("done with Test image!");
	}// makeTestImage

}
