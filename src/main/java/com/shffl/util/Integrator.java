package com.shffl.util;

import org.joml.Vector3d;

import com.shffl.assets.Intersection;
import com.shffl.assets.Light;
import com.shffl.assets.Ray;
import com.shffl.control.RenderController;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * Integrator class. Used for calculating the color of a pixel on intersection.
 * Accounts for reflections and refractions.
 */
public class Integrator {

	private RenderController rendCon;

	/**
	 * Initializes Integrator and gives it access to Scene data
	 * @param rCon the RenderController containing scene data.
	 */
	public Integrator(RenderController rCon) {
		this.rendCon = rCon;
	}//constructor

	/**
	 * Takes in ray and propagates it across the input scene to check if it 
	 * intercepts any objects.
	 * 
	 * @param r the Ray to propagate
	 * @return Vector3d containing the rgb values to be displayed on the pixel
	 */
	public Vector3d propagate(Ray r, int depth) {
		
		//Set default values
		Vector3d rayColor = new Vector3d(0, 0, 0);
		Vector3d reflectColor = new Vector3d(0, 0, 0);
		Vector3d refractColor = new Vector3d(0, 0, 0);
		
		// Check for max depth
		if(depth >= 10) {
			return rayColor;
		}
		
		Intersection inter = new Intersection();
		inter = rendCon.getScene().intersect(r, inter);
		
		double mirror = 0.0;
		double opacity = 1.0;
		
		//If this returned true, we hit an object
		if(inter.hasNormal) {
		
			//rayColor = getRGBNormal(inter); // NORMAL INTEGRATOR
			rayColor = getRGBPhong(inter); // PHONG MODEL INTEGRATOR
			mirror = inter.material.mirror;
			opacity = inter.material.opacity;
			
			if(mirror != 0) {//Reflection propagation
				
				//Get reflection ray
				double rDotN = r.direction.dot(inter.getNormal());
				double nDotI = inter.getNormal().dot(r.direction);
				
				//Inside-Outside Check
				if (nDotI < 0) { // Outside
					//Generate reflection ray
					Vector3d delta = (new Vector3d(inter.getNormal())).mul(2.0);
					delta = delta.mul(rDotN);
					Vector3d reflectDirection = (new Vector3d(r.direction)).sub(delta);
					reflectDirection = reflectDirection.normalize();
					
					Ray reflection = new Ray(inter.getPosition(), reflectDirection);
					reflection.nudgeOrigin(.01);
					reflectColor = new Vector3d(propagate(reflection,depth+1));
					reflectColor = reflectColor.mul(mirror);
				} else {
					//We're inside the object -- don't reflect!
				}
			}//reflection
			
			if(opacity < 1.0) {// Refraction propagation
				double outsideIOR = 1;
				double insideIOR = inter.material.indexOfRefraction;
				Vector3d incidence = new Vector3d(r.direction);
				Vector3d normal = new Vector3d(inter.getNormal());
				
				double nDotI = normal.dot(incidence);
				
				//Clamp nDotI's value between -1 and 1
				if(nDotI < -1.0)
					nDotI = -1.0;
				if(nDotI > 1.0)
					nDotI = 1.0;
				
				if(nDotI < 0) {//Outside surface
					nDotI = -nDotI;
				} else {//Inside surface
					opacity = 0.0;
					normal.mul(-1.0);
					double temp = outsideIOR;
					outsideIOR = insideIOR;
					insideIOR = temp;
				}
				double eta = outsideIOR / insideIOR;
				double k = 1 - eta * eta * (1 - nDotI * nDotI);
				if(k < 0) {
					return rayColor;
				}
				//Should be: eta * I + (eta + nDotI - sqrt(k)) * N
				Vector3d refractionDir = new Vector3d(normal);
				Vector3d incidenceCopy = new Vector3d(incidence);
				incidenceCopy.mul(eta);
				double normScalar = eta * nDotI - Math.sqrt(k);
				refractionDir = refractionDir.mul(normScalar);
				refractionDir = refractionDir.add(incidenceCopy);
				refractionDir = refractionDir.normalize();
				Ray refraction = new Ray(inter.getPosition(), refractionDir);
				refraction.nudgeOrigin(0.001);
				refractColor = new Vector3d(propagate(refraction, depth + 1));
				refractColor = refractColor.mul((1 - opacity) * (1 - mirror));
			}//refraction
		} else {
			//Didn't hit anything
		}
		rayColor = rayColor.mul((1 - mirror) * opacity);
		rayColor = rayColor.add(reflectColor);
		rayColor = rayColor.add(refractColor);
		
		// Check for values that are OOB for Color Object
		for(int i = 0; i < 3; i++) {
			if(rayColor.get(i) > 1) {
				rayColor.setComponent(i, 1.0);
			}
		}
		return rayColor;
	}//propagate
	
	
	/**
	 * Determines if a ray intersects any objects in the direction of a light 
	 * sources, used to determine if a shadow is being cast onto a point.
	 * 
	 * @param r Ray with a direction towards a light source.
	 * @return boolean describing whether the point is within a shadow.
	 */
	public boolean inHardShadow(Ray r) {
		return rendCon.getScene().shadowIntersect(r);
	}//inHardShadow

	/**
	 * Calculates the RGB values to display to the pixel. Uses the Phong 
	 * reflection model to determine how lights in the scene effect the 
	 * visible light coming off of the material of the object. 
	 * 
	 * @param inter the Intersection holding the data of the Ray-Triangle intersection point
	 * @return Vector3d containing the rgb values to be displayed on the pixel
	 */
	public Vector3d getRGBPhong(Intersection inter) {
		
		Vector3d ambient, diffuse, specular, rgb = new Vector3d(0,0,0), lightIntensity;
		
		// Apply ambient no matter what
		ambient = new Vector3d(inter.material.ambient);
		
		for(Light light: rendCon.getScene().lightSources) {
		
			lightIntensity = new Vector3d(0,0,0);
			
			// Check if the light is hitting the surface
			Vector3d lightPosition = new Vector3d(light.pos);//.mulPosition(rendCon.cam.viewMatrix());			
			Vector3d lightDirection = new Vector3d(lightPosition).sub(inter.getPosition());
			lightDirection.normalize();
			double lDotN =  Math.max(lightDirection.dot(inter.getNormal()), 0.0);
			
			if( lDotN > 0.0 ) {
				
				// There is light cast in this direction, check for hard shadow
				Ray shadowRay = new Ray(inter.getPosition(), lightDirection);
				shadowRay.tMax = inter.getPosition().distance(lightPosition);
				
				
				shadowRay.nudgeOrigin(0.001);
				if (!inHardShadow(shadowRay)) {
					
					// Not in shadow, calculate reflectivity 
					diffuse = new Vector3d(inter.material.diffuse).mul(lDotN);
					
					Vector3d cameraDirection = new Vector3d(rendCon.getScene().eye).sub(inter.getPosition());
					cameraDirection.normalize();
					Vector3d halfway = cameraDirection.add(lightDirection);
					halfway.normalize();
					double hDotN = Math.max(halfway.dot(inter.getNormal()), 0.0);
					double shine = Math.pow(hDotN, inter.material.shiny);
					specular = new Vector3d(inter.material.specular).mul(shine);
					
					// Get final color
					Vector3d reflection = diffuse.add(specular);
					lightIntensity = new Vector3d(light.i);
					lightIntensity.mul(reflection);
					
				} else {
					//In shadow
				}//Shadow Check
			} else { 
				//Not in light's path
			} //lDotN check
			rgb = rgb.add(lightIntensity);
		}
		
		// After all light sources are factored, add ambient light of scene and material
		rgb.add(rendCon.getScene().getAmbient());
		rgb.add(ambient);
		
		// Check for values that are OOB for Color Object
		for(int i = 0; i < 3; i++) {
			if(rgb.get(i) > 1) {
				rgb.setComponent(i, 1.0);
			}
		}
		return rgb;
	}//getRGBPhong

	/**
	 * Calculates the RGB values to display to the pixel. Determines color 
	 * based only on the point normal of intersection point of the ray.
	 * 
	 * @param inter 
	 * @return Vector3d containing the rgb values to be displayed on the pixel
	 */
	public Vector3d getRGBNormal(Intersection inter) {

		Vector3d n = new Vector3d(inter.getNormal());
		n.normalize();

		// Translate the value to fit Color Object specifications
		for(int i = 0; i < 3; i++) {
			n.setComponent(i, (n.get(i)+1)/2 );
		}
		return n;
	}// getRGBNormal
	
}//class
