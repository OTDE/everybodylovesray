package com.shffl.util;
import java.awt.Color;
import java.util.Vector;

import org.joml.Vector3d;

import com.shffl.assets.Intersection;
import com.shffl.assets.Light;
import com.shffl.assets.Ray;
import com.shffl.control.RenderController;

public class Integrator {

	private RenderController rendCon;
	private Intersection inter;

	public Integrator(RenderController rCon) {

		this.rendCon = rCon;
	}

	/**
	 * Takes in ray and propagates it across the input scene to check if it 
	 * intercepts any objects.
	 * 
	 * @param r the Ray to propagate
	 * @return Vector3d containing the rgb values to be displayed on the pixel
	 */
	public Vector3d propagate(Ray r) {

		Vector3d rayColor = new Vector3d(1, 1, 1);

		inter = new Intersection();
		inter = rendCon.getScene().intersect(r, inter);
		
		// If this returned true, we hit an object
		if(inter.hasNormal) {

			// Get a color from the Integrator method
			
			//rayColor = getRGBNormal(inter); // NORMAL INTEGRATOR
			rayColor = getRGBPhong(inter); // PHONG MODEL INTEGRATOR

		}

		return rayColor;
	}// propagate
	
	public boolean inHardShadow(Ray r) {
		
		return rendCon.getScene().shadowIntersect(r);
	}

	/**
	 * Calculates the RGB values to display to the pixel. Uses the Phong 
	 * reflection model to determine how lights in the scene effect the 
	 * visible light coming off of the material of the object. 
	 * 
	 * @param inter the Intersection holding the data of the Ray-Triangle intersection point
	 * @return Vector3d containing the rgb values to be displayed on the pixel
	 */
	public Vector3d getRGBPhong(Intersection inter) {
		
		Vector3d diffuse, specular, rgb = new Vector3d(0,0,0), lightIntensity = new Vector3d(0,0,0);
		
		for(Light light: rendCon.getScene().lightSources) {
			
			// Calculate diffuse 
			Vector3d lightPosition = new Vector3d(light.pos);//.mulPosition(rendCon.cam.viewMatrix());			
			Vector3d lightDirection = new Vector3d(lightPosition).sub(inter.getPosition());
			lightDirection.normalize();
			
			double lDotN =  Math.max(lightDirection.dot(inter.getNormal()), 0.0);
			diffuse = new Vector3d(inter.material.diffuse).mul(lDotN);
			
			// Calculate specular
			if( lDotN > 0.0 ) {
				
				// There is light cast in this direction, check for hard shadow
				
				// optimize?
				Vector3d sOrigin = new Vector3d(inter.getPosition());
				Vector3d sDir = new Vector3d(lightDirection);
				
				Ray shadowRay = new Ray(sOrigin, sDir);
				shadowRay.nudgeOrigin();
				Vector3d d =new Vector3d(shadowRay.origin).add(shadowRay.direction);
				//System.out.println("SR with origin: "+shadowRay.origin+"and dir: "+d);
				if (!inHardShadow(shadowRay)) {
					
					
					// Not in shadow, calculate reflectivity
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
					
				}else{
					// In shadow, only ambient light
				}// Shadow Check
				
			}else {
				lightIntensity = diffuse;
			} // lDotN check
			
			
			
			rgb = rgb.add(lightIntensity);
		}
		
		// After all light sources are factored, add ambient light of scene
		rgb.add(rendCon.getScene().getAmbient());
		
		// Check for values that are OOB for Color Object
		for(int i = 0; i < 3; i++) {
			if(rgb.get(i) > 1) {
				rgb.setComponent(i, 1.0);
			}
		}
	
		return rgb;
	}// getRGBPhong

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

}
