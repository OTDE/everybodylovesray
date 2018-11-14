/**
 * Sampler class for fetching arrays of samples for input
 * @author Ethan Wiederspan and Seth Chapman
 *
 */
public class Sampler {

	public int samplesPerPixel;
	private SampleArray sampleSet;
	
	/**
	 * Constructor for the sampler.
	 * @param sampNum number of samples per pixel.
	 */
	public Sampler(int sampNum) {
		samplesPerPixel = sampNum;
		sampleSet = new SampleArray(samplesPerPixel);
	}
	
	/**
	 * Method to get an array of samples for a pixel
	 * @param pixelX the x coordinate of the import pixel
	 * @param pixelY the y coordinate of the import pixel
	 * @return an array of samples for that pixel
	 */
	public SampleArray getPixelSamples(int pixelX, int pixelY) {
		sampleSet.setPixelX(pixelX);
		sampleSet.setPixelY(pixelY);
		sampleSet.fill();
		return sampleSet;
	}
}
