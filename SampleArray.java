import java.util.Random;

public class SampleArray {

	private int pixelX;
	private int pixelY;
	private int sampleSize;
	private Random rng;
	
	public Sample[] samples;
	
	public SampleArray(int pX, int pY, int size) {
		pixelX = pX;
		pixelY = pY;
		sampleSize = size;
		samples = new Sample[sampleSize];
		rng = new Random();
	}
	
	public SampleArray(int size) {
		sampleSize = size;
		samples = new Sample[sampleSize];
		rng = new Random();
	}
	
	public void setPixelX(int pixelX) {
		this.pixelX = pixelX;
	}

	public void setPixelY(int pixelY) {
		this.pixelY = pixelY;
	}

	public void fill() {
		for(int i = 0; i < sampleSize; i++) {
			samples[i] = new Sample(rng.nextDouble(), rng.nextDouble());
		}
	}

	public int getPixelX() {
		return pixelX;
	}

	public int getPixelY() {
		return pixelY;
	}

	public int getSampleSize() {
		return sampleSize;
	}
}
