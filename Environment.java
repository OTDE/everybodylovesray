import java.util.Map;

public class Environment {

	public int height;
	public int width;
	
	private transient Map<String, LightSource> lightMap;
	private transient Map<String, Model> modelMap;
	private transient Map<String, Material> matMap;
	
}
