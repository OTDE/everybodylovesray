import java.awt.image.BufferedImage;

public class Film {
	
	private BufferedImage renderedImage;
	
	public Film(int width, int height) {
		renderedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage getRenderedImage() {
		return renderedImage;
	}

}