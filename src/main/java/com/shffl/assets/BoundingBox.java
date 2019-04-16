package com.shffl.assets;

import org.joml.Vector3d;

import com.shffl.util.Tags;

/**
 * Bounds class for rendering optimization.
 * @author Ethan Wiederspan and Seth Chapman
 */
public class BoundingBox {
	
	 public Vector3d pMin;
	 public Vector3d pMax;
	
	 /**
	 * Empty constructor. Builds a box with bounds that
	 * violate the invariant that pMin < pMax for all coordinates.
	 */
	public BoundingBox() {
		 double min = Double.NEGATIVE_INFINITY;
		 double max = Double.POSITIVE_INFINITY;	 
		 pMin = new Vector3d(max, max, max);
		 pMax = new Vector3d(min, min, min);
	 }
	 
	 /**
	  * Point constructor. pMin and pMax are the same.
	  * The bounds encapsulate a single point.
	 * @param point the encapsulated point.
	 */
	public BoundingBox(Vector3d point) {
		 pMin = new Vector3d(point);
		 pMax = new Vector3d(point);
	 }

	 /**
	 * Constructs a box with bounds based on the mins and maxes of both points. 
	 * @param point1 the first point.
	 * @param point2 the second point.
	 */
	public BoundingBox(Vector3d point1, Vector3d point2) {
		 pMin = new Vector3d(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y), Math.min(point1.z, point2.z));
		 pMax = new Vector3d(Math.max(point1.x, point2.x), Math.max(point1.y, point2.y), Math.max(point1.z, point2.z));
	 }
	 
	 /**
	  * Picks a corner from the cube like so:
	  * 
	  *  3                      pMax (7)
	  *  v                       v
	  *  @ + + + + + + + + + + + @
	  *  +\                      +\
	  *  + \                     + \
	  *  +  \ 2                  +  \ 6
	  *  +   \v                  +   \v
	  *  +    @ + + + + + + + + +++ + @
	  *  +    +                  +    +
	  *  +    +                  +    +
	  *  +    +                  +    +
	  *  +    +                  +    +
	  *  +    +                  +    +
	  *  +    +                  +    +
	  *  @ + +++ + + + + + + + + @    +
	  *  ^\   +                  ^\   +
	  *  1 \  +                  5 \  +
	  *     \ +                     \ +
	  *      \+                      \+
	  *       @ + + + + + + + + + + + @
	  * 	  ^                       ^
	  * 	pMin (0)                  4    
	  * 
	  * @param index the corner of the cube to be returned.
	  * @return the point representation of the corner requested.
	  */
	public Vector3d getCorner(int index) {
		 switch(index) {
		 	case 0:
		 		return new Vector3d(pMin);
		 	case 1:
		 		return new Vector3d(pMin.x(), pMin.y(), pMax.z());
		 	case 2:
		 		return new Vector3d(pMin.x(), pMax.y(), pMin.z());
		 	case 3:
		 		return new Vector3d(pMin.x(), pMax.y(), pMax.z());
		 	case 4:
		 		return new Vector3d(pMax.x(), pMin.y(), pMin.z());
		 	case 5:
		 		return new Vector3d(pMax.x(), pMin.y(), pMax.z());
		 	case 6:
		 		return new Vector3d(pMax.x(), pMax.y(), pMin.z());
		 	case 7:
		 		return new Vector3d(pMax);
		 	default:
		 		return null;
		 }	 
	 }
	 
	 /**
	 * Given a BoundingBox and a point, returns their union. 
	 * @param inputBox
	 * @param point
	 * @return the union of the given BoundingBox and point.
	 */
	public BoundingBox union(BoundingBox inputBox, Vector3d point) {
		 return new BoundingBox(new Vector3d(Math.min(inputBox.pMin.x, point.x),
				 							 Math.min(inputBox.pMin.y, point.y),
				 							 Math.min(inputBox.pMin.z, point.z)),
				 				new Vector3d(Math.max(inputBox.pMax.x, point.x),
				 							 Math.max(inputBox.pMax.y, point.y),
				 							 Math.max(inputBox.pMax.z, point.z))
				 );
	 }
	 
	 /**
	 * Given two BoundingBoxes, returns their union.
	 * @param box1
	 * @param box2
	 * @return the union of the given BoundingBoxes.
	 */
	public BoundingBox union(BoundingBox box1, BoundingBox box2) {
		 return new BoundingBox(new Vector3d(Math.min(box1.pMin.x, box2.pMin.x),
				 							 Math.min(box1.pMin.y, box2.pMin.y),
				 							 Math.min(box1.pMin.z, box2.pMin.z)),
				 				new Vector3d(Math.max(box1.pMax.x, box2.pMax.x),
				 							 Math.max(box1.pMax.y, box2.pMax.y),
				 							 Math.max(box1.pMax.z, box2.pMax.z))
				 );
	 }
	 
	 /**
	 * Given two BoundingBoxes, returns their intersection.
	 * @param box1
	 * @param box2
	 * @return the intersection of the given BoundingBoxes.
	 */
	public BoundingBox intersect(BoundingBox box1, BoundingBox box2) {
		 return new BoundingBox(new Vector3d(Math.max(box1.pMin.x, box2.pMin.x),
				 							 Math.max(box1.pMin.y, box2.pMin.y),
				 							 Math.max(box1.pMin.z, box2.pMin.z)),
				 				new Vector3d(Math.min(box1.pMax.x, box2.pMax.x),
				 							 Math.min(box1.pMax.y, box2.pMax.y),
				 							 Math.min(box1.pMax.z, box2.pMax.z))
				 );
	 }
	 
	 /**
	 * Checks if this BoundingBox overlaps with the input.
	 * @param inputBox the BoundingBox to to check for overlap with this one.
	 * @return true if the two BoundingBoxes overlap, false otherwise.
	 */
	public boolean isOverlappingWith(BoundingBox inputBox) {
		 boolean x = (this.pMax.x >= inputBox.pMin.x) && (this.pMin.x <= inputBox.pMax.x);
		 boolean y = (this.pMax.y >= inputBox.pMin.y) && (this.pMin.y <= inputBox.pMax.y);
		 boolean z = (this.pMax.z >= inputBox.pMin.z) && (this.pMin.z <= inputBox.pMax.z);
		 return x && y && z;
	 }
	 
	 /**
	 * Checks if a point is inside this.
	 * @param point the point to check.
	 * @return true if the given point is inside this BoundingBox, false otherwise.
	 */
	public boolean containsPoint(Vector3d point) {
		 return (point.x >= this.pMin.x && point.x <= this.pMax.x &&
				 point.y >= this.pMin.y && point.y <= this.pMax.y &&
				 point.z >= this.pMin.z && point.z <= this.pMax.z);
	 }
	 
	 /**
	 * Returns a BoundingBox expanded by the given factor.
	 * @param expansionFactor the factor to expand by.
	 * @return a new BoundingBox expanded by the given factor.
	 */
	public BoundingBox expand(double expansionFactor) {
		 Vector3d pMinCopy = new Vector3d(pMin);
		 Vector3d pMaxCopy = new Vector3d(pMax);
		 return new BoundingBox(pMinCopy.sub(new Vector3d(expansionFactor)),
				 				pMaxCopy.add(new Vector3d(expansionFactor)));
	 }
	 
	 /**
	 * @return the diagonal vector of this box.
	 */
	public Vector3d getDiagonal() {
		 Vector3d pMaxCopy = new Vector3d(pMax);
		 return pMaxCopy.sub(this.pMin);
	 }
	 
	 /**
	 * @return the surface area of this box.
	 */
	public double getSurfaceArea() {
		 Vector3d d = this.getDiagonal();
		 return 2 * (d.x * d.y + d.x * d.z + d.y * d.z);
	 }
	 
	 /**
	 * @return the volume of this box.
	 */
	public double getVolume() {
		 Vector3d d = this.getDiagonal();
		 return d.x * d.y * d.z;
	 }
	 
	 /**
	 * Gets the axis with the largest value.
	 * @return 0 for the x axis, 1 for the y axis, and 2 for the z axis.
	 */
	public int furthestExtent() {
		 Vector3d d = this.getDiagonal();
		 if(d.x > d.y && d.x > d.z)
			 return 0;
		 else if(d.y > d.z)
			 return 1;
		 else
			 return 2;
	 }
	
	public double getCenterX() {
		return (this.pMin.x + this.pMax.x) / 2;
	}
	
	public double getCenterY() {
		return (this.pMin.y + this.pMax.y) / 2;
	}
	
	public double getCenterZ() {
		return (this.pMin.z + this.pMax.z) / 2;
	}
	 
	 /**
	 * Does linear interpolation on this box with a point.
	 * @param point the input point.
	 * @return the amount interpolated.
	 */
	public Vector3d lerpBox(Vector3d point) {
		 return new Vector3d(lerp(point.x, this.pMin.x, this.pMax.x),
				 			 lerp(point.y, this.pMin.y, this.pMax.y),
				 			 lerp(point.z, this.pMin.z, this.pMax.z));
	 }
	 
	 /**
	 * Linear interpolation function.
	 * @param t time axis.
	 * @param p0 left point.
	 * @param p1 right point.
	 * @return the amount interpolated across one axis.
	 */
	private double lerp(double t, double p0, double p1) {
		 return (1 - t) * p0 + t * p1;
	 }
		
	public boolean intersectsWith(Ray r) {
		double t1 = (this.pMin.x() - r.origin.x()) / r.direction.x();
		double t2 = (this.pMax.x() - r.origin.x()) / r.direction.x();
		double t3 = (this.pMin.y() - r.origin.y()) / r.direction.y();
		double t4 = (this.pMax.y() - r.origin.y()) / r.direction.y();
		double t5 = (this.pMin.z() - r.origin.z()) / r.direction.z();
		double t6 = (this.pMax.z() - r.origin.z()) / r.direction.z();
		
		double tMin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tMax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
		return tMin < tMax && tMax > 0;
	}
}
