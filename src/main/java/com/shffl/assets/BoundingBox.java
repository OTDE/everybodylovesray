package com.shffl.assets;

import org.joml.Vector3d;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * Bounds class for rendering optimization.
 * This is an axis-aligned bounding box (AABB):
 * the normals of its faces align with the x, y, and z-axis.
 */
public class BoundingBox {

	//Minimum and maximum points.
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
	}//default constructor

	/**
	 * Point constructor. pMin and pMax are the same.
	 * The bounds encapsulate a single point.
	 * @param point the encapsulated point.
	 */
	public BoundingBox(Vector3d point) {
		pMin = new Vector3d(point);
		pMax = new Vector3d(point);
	}//one-point constructor

	/**
	 * Constructs a box with bounds based on the mins and maxes of both points. 
	 * @param point1 the first point.
	 * @param point2 the second point.
	 */
	public BoundingBox(Vector3d point1, Vector3d point2) {
		pMin = new Vector3d(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y), Math.min(point1.z, point2.z));
		pMax = new Vector3d(Math.max(point1.x, point2.x), Math.max(point1.y, point2.y), Math.max(point1.z, point2.z));
	}//two-point constructor

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
	}//getCorner

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
	}//isOverlappingWith

	/**
	 * @return The x coordinate of the center of the bounding box.
	 */
	public double getCenterX() {
		return (this.pMin.x + this.pMax.x) / 2;
	}//getCenterX

	/**
	 * @return The y coordinate of the center of the bounding box.
	 */
	public double getCenterY() {
		return (this.pMin.y + this.pMax.y) / 2;
	}//getCenterY

	/**
	 * @return The z coordinate of the center of the bounding box.
	 */
	public double getCenterZ() {
		return (this.pMin.z + this.pMax.z) / 2;
	}//getCenterZ

	/**
	 * Intersection test for this BoundingBox and a ray.
	 * Uses the "slab" test. Adapted from the Game Physics Cookbook.
	 * @param r The ray to check against.
	 * @return True if this BoundingBox intersects with the given ray, false otherwise.
	 */
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
	}//intersectsWith

}//class
