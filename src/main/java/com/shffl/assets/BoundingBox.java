package com.shffl.assets;

import org.joml.Vector3d;

public class BoundingBox {
	
	 public Vector3d pMin;
	 public Vector3d pMax;
	
	 //Empty constructor
	 public BoundingBox() {
		 double min = Double.MIN_VALUE;
		 double max = Double.MAX_VALUE;
		 
		 pMin = new Vector3d(max, max, max);
		 pMax = new Vector3d(min, min, min);
	 }
	 
	 //Point constructor
	 public BoundingBox(Vector3d point) {
		 pMin = new Vector3d(point);
		 pMax = new Vector3d(point);
	 }

	 //Two-point constructor
	 public BoundingBox(Vector3d point1, Vector3d point2) {
		 pMin = new Vector3d(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y), Math.min(point1.z, point2.z));
		 pMax = new Vector3d(Math.max(point1.x, point2.x), Math.max(point1.y, point2.y), Math.max(point1.z, point2.z));
	 }
	 
	 public Vector3d getCorner(int index) {
		 switch(index) {
		 	case 0:
		 		return pMin;
		 	case 1:
		 		return new Vector3d(pMin.x, pMax.y, pMin.z);
		 	case 2:
		 		return new Vector3d(pMax.x, pMax.y, pMin.z);
		 	case 3:
		 		return new Vector3d(pMax.x, pMin.y, pMin.z);
		 	case 4:
		 		return new Vector3d(pMax.x, pMin.y, pMax.z);
		 	case 5:
		 		return new Vector3d(pMin.x, pMin.y, pMax.z);
		 	case 6:
		 		return new Vector3d(pMin.x, pMax.y, pMax.z);
		 	case 7:
		 		return pMax;
		 	default:
		 		return null;
		 }	 
	 }
	 
	 //Union of a bounding box and a point
	 public BoundingBox union(BoundingBox inputBox, Vector3d point) {
		 return new BoundingBox(new Vector3d(Math.min(inputBox.pMin.x, point.x),
				 							 Math.min(inputBox.pMin.y, point.y),
				 							 Math.min(inputBox.pMin.z, point.z)),
				 				new Vector3d(Math.max(inputBox.pMax.x, point.x),
				 							 Math.max(inputBox.pMax.y, point.y),
				 							 Math.max(inputBox.pMax.z, point.z))
				 );
	 }
	 
	 //Union of two bounding boxes
	 public BoundingBox union(BoundingBox box1, BoundingBox box2) {
		 return new BoundingBox(new Vector3d(Math.min(box1.pMin.x, box2.pMin.x),
				 							 Math.min(box1.pMin.y, box2.pMin.y),
				 							 Math.min(box1.pMin.z, box2.pMin.z)),
				 				new Vector3d(Math.max(box1.pMax.x, box2.pMax.x),
				 							 Math.max(box1.pMax.y, box2.pMax.y),
				 							 Math.max(box1.pMax.z, box2.pMax.z))
				 );
	 }
	 
	 //Intersect of two bounding boxes
	 public BoundingBox intersect(BoundingBox box1, BoundingBox box2) {
		 return new BoundingBox(new Vector3d(Math.max(box1.pMin.x, box2.pMin.x),
				 							 Math.max(box1.pMin.y, box2.pMin.y),
				 							 Math.max(box1.pMin.z, box2.pMin.z)),
				 				new Vector3d(Math.min(box1.pMax.x, box2.pMax.x),
				 							 Math.min(box1.pMax.y, box2.pMax.y),
				 							 Math.min(box1.pMax.z, box2.pMax.z))
				 );
	 }
	 
	 //Checks if this bounding box overlaps with the input
	 public boolean isOverlappingWith(BoundingBox inputBox) {
		 boolean x = (this.pMax.x >= inputBox.pMin.x) && (this.pMin.x <= inputBox.pMax.x);
		 boolean y = (this.pMax.y >= inputBox.pMin.y) && (this.pMin.y <= inputBox.pMax.y);
		 boolean z = (this.pMax.z >= inputBox.pMin.z) && (this.pMin.z <= inputBox.pMax.z);
		 return x && y && z;
	 }
	 
	 //Checks if a point is inside a bounding box
	 public boolean containsPoint(Vector3d point) {
		 return (point.x >= this.pMin.x && point.x <= this.pMax.x &&
				 point.y >= this.pMin.y && point.y <= this.pMax.y &&
				 point.z >= this.pMin.z && point.z <= this.pMax.z);
	 }
	 
	 //Expands bounding box by expansion
	 public BoundingBox expand(double expansionFactor) {
		 Vector3d pMinCopy = new Vector3d(pMin);
		 Vector3d pMaxCopy = new Vector3d(pMax);
		 return new BoundingBox(pMinCopy.sub(new Vector3d(expansionFactor)),
				 				pMaxCopy.add(new Vector3d(expansionFactor)));
	 }
	 
	 //Get box diagonal vector
	 public Vector3d getDiagonal() {
		 Vector3d pMinCopy = new Vector3d(pMin);
		 return pMinCopy.sub(this.pMin);
	 }
	 
	 //Get surface area of box
	 public double getSurfaceArea() {
		 Vector3d d = this.getDiagonal();
		 return 2 * (d.x * d.y + d.x * d.z + d.y * d.z);
	 }
	 
	 //Get box volume
	 public double getVolume() {
		 Vector3d d = this.getDiagonal();
		 return d.x * d.y * d.z;
	 }
	 
	 //Gets axis possessing this box's largest value
	 public int furthestExtent() {
		 Vector3d d = this.getDiagonal();
		 if(d.x > d.y && d.x > d.z)
			 return 0;
		 else if(d.y > d.z)
			 return 1;
		 else
			 return 2;
	 }
	 
	 public Vector3d lerpBox(Vector3d point) {
		 return new Vector3d(lerp(point.x, this.pMin.x, this.pMax.x),
				 			 lerp(point.y, this.pMin.y, this.pMax.y),
				 			 lerp(point.z, this.pMin.z, this.pMax.z));
	 }
	 
	 private double lerp(double t, double p0, double p1) {
		 return (1 - t) * p0 + t * p1;
	 }
	 
}
