


public class Sampler {

	public int samplesPerPixel;
	private SampleArray sampleSet;
	
	public Sampler(int sampNum) {
		samplesPerPixel = sampNum;
		sampleSet = new SampleArray(samplesPerPixel);
	}
	
	public SampleArray getPixelSamples(int pixelX, int pixelY) {
		sampleSet.setPixelX(pixelX);
		sampleSet.setPixelY(pixelY);
		sampleSet.fill();
		return sampleSet;
	}
}
