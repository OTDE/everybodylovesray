import java.util.Map;
/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * Environment class. GSON parses into this,
 * which represents a 3D scene and everything
 * it contains within it.
 */
public class Environment {

	public int height;
	public int width;
	
	private transient Map<String, LightSource> lightMap;
	private transient Map<String, Model> modelMap;
	private transient Map<String, Material> matMap;
	
}
