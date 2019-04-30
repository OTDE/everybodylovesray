package com.shffl.assets;

import java.util.ArrayList;

import org.joml.Vector3d;

import com.owens.oobjloader.builder.Face;

/**
 * Octree data structure for optimizing Face processing.
 * @author Ethan Wiederspan and Seth Chapman
 */
public class Octree {
	
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int FOUR = 4;
	private static final int EIGHT = 8;
	private static final int THIRTEEN = 13;
	private static final int MAX_DEPTH = 8;
	protected static final int MAX_FACES = 5;
	public ArrayList<Face> faces;
	private boolean hasSubdivided = false;
	public boolean isEmpty = true;
	private Octree[] children;
	public BoundingBox bounds;
	public int numObjects = 0;
	public int curDepth;
	
	/**
	 * Constructor with a given set of Faces.
	 * @param faces the set of faces to add to this Octree
	 * @param bounds the global bounds.
	 */
	public Octree(ArrayList<Face> faces, BoundingBox bounds) {
		this(bounds);
		this.faces = faces;
		/*
		for(Face f: faces) {
			insert(f);
			if(faces.indexOf(f) % 100 == 0)
				System.out.printf("%d faces loaded!\n", faces.indexOf(f));
		}
		*/
	}
	
	/**
	 * Default constructor.
	 * @param bounds the global bounds.
	 */
	public Octree(BoundingBox bounds) {
		this.bounds = bounds;
		faces = new ArrayList<Face>();
		this.hasSubdivided = false;
		numObjects++;
	}
	
	public void build(int depth) {
		if(this.faces.size() > MAX_FACES && !hasSubdivided && depth < MAX_DEPTH) {
			this.subdivide();
			ArrayList<Face> fCopy = new ArrayList<Face>(faces);
			for(Face f: fCopy) {
				for(Octree kids: this.children) {
					if(kids.areOverlapping(f, kids.getBounds())) {
						kids.faces.add(f);
						this.faces.remove(f);
					}
				}
			}
			for(Octree kids: this.children) {
				kids.build(depth + 1);
			}
		}
		if(!this.faces.isEmpty() && hasSubdivided) {
			System.out.printf("This node contains extra stuff at level %d\n", depth);
			
		}
		
	}
	/**
	 * Recursively inserts Face into Octree.
	 * @param face the Face to be inserted.
	 * @return True for successful insertion at a given depth, false otherwise
	 */
	public boolean insert(Face face) { //(probably not the problem)
		if(!areOverlapping(face, bounds))
			return false;
		if(!hasSubdivided) {
			if(this.faces.size() > MAX_FACES) {//if it's subdividing time
				this.subdivide(); //do it
				ArrayList<Face> facesCopy = new ArrayList<Face>(faces);
				for(Face f : facesCopy) { //make a copy of all the faces you have
					if(insert(f)) {       //if inserted into the leaf
						faces.remove(f);  //pop it out of this one
					}
				}
			} else {
				faces.add(face); //otherwise add to the leaf
				isEmpty = false; //let 'em know it isn't empty
			} 
		} else { //octree has subdivided

			boolean inLowerLevel = false;
			for(Octree thisOctreeChild : children) { //look at children
				if(thisOctreeChild.insert(face)) { //successful insertion in lower level
					inLowerLevel = true;
				}
			}
			if(!inLowerLevel) {  //if this intersects with none of the children:
				faces.add(face); //this should never happen. a face will intersect
			}					 //with one of the octants if it's in the bounding box for all 8
			isEmpty = false;     //parent node always has leaves with children
		}
		return true;
	}
	
    /**
     * Divides a leaf node into 8 leaves, with the original node as the root.
     */
    private void subdivide() { //this is not the problem.
        if (hasSubdivided) { 
            System.err.println("We have already subdivided this."); 
            System.exit(1); 
        } 
        hasSubdivided = true; 

        Vector3d center = new Vector3d(bounds.getCenterX() / TWO, bounds.getCenterY() / TWO, bounds.getCenterZ() / TWO); 
 
        BoundingBox[] newBounds = new BoundingBox[EIGHT]; 
        children = new Octree[EIGHT]; 
        
        for (int i = ZERO; i < EIGHT; i++) { 
            newBounds[i] = new BoundingBox(this.bounds.getCorner(i), center);
            children[i] = new Octree(newBounds[i]);  
        } 
    } 
    
    /**
     * Query method for accessing elements in an Octree based on proximity.
     * @param range the BoundingBox to check for faces inside.
     * @return a list of all Faces within range of the input BoundingBox.
     */
    public ArrayList<Face> getFacesWithin(Ray r) { 
        ArrayList<Face> facesInRange = new ArrayList<Face>(); 
        if(this.bounds.intersectsWith(r)) {
        	if(hasSubdivided) {
        		for(Octree kids : children) {
        			facesInRange.addAll(kids.getFacesWithin(r));
        		}
        	} else {
        		return this.faces;
        	}
        }
        return facesInRange;
    }
    
    /*
    public ArrayList<Face> getFacesIntersectingWith(Vector3d originInput, Vector3d direction, int layer) {
    	if(!this.bounds.containsPoint(originInput)) {
    		double tAtBounds = this.getIntersectPoint(origin, inverse)
    	}
    		
    	if(this.isEmpty && this.children == null) {
    		System.out.println("nope");
    		return null;
    	} // swing and a miss, no intersection
    	Vector3d originCopy = new Vector3d(originInput); // copy origin
    	if(!this.faces.isEmpty()) {
			System.out.println("we hit something"); // if we're at a leaf, get all its faces
    		return this.faces;
    	}
    	//make bounding box planes thru center of box
    	Plane xP = new Plane(new Vector3d(1.0, 0.0, 0.0), this.bounds.getCenterX()); 
    	Plane yP = new Plane(new Vector3d(0.0, 1.0, 0.0), this.bounds.getCenterY());
    	Plane zP = new Plane(new Vector3d(0.0, 0.0, 1.0), this.bounds.getCenterZ());
    	
    	//determine side of each plane origin is on (true if on or in front of plane, false if behind)
    	boolean sideX = xP.pointDistance(originCopy) >= ZERO;
    	boolean sideY = yP.pointDistance(originCopy) >= ZERO;
    	boolean sideZ = zP.pointDistance(originCopy) >= ZERO;
    	
    	double xDist, yDist, zDist;                               //for each axis
    	if(sideX == (direction.x < 0)) {                          //if the direction points toward the plane
    		xDist = xP.intersectsWith(originCopy, direction);
    		System.out.println(xDist);
    	} //get the point where it intersects
    	else													  //otherwise
    		xDist = Double.POSITIVE_INFINITY;					  //set to positive infinity
    	if(sideY == (direction.y < 0)) {
    		yDist = yP.intersectsWith(originCopy, direction);
    	}
    	else
    		yDist = Double.POSITIVE_INFINITY;
    	if(sideZ == (direction.z < 0)) {
    		zDist = zP.intersectsWith(originCopy, direction);
    	}
    	else
    		zDist = Double.POSITIVE_INFINITY;
    	//set octant id values
    	int oidX, oidY, oidZ;

    	for(int i = ZERO; i < FOUR; i++) {
        	oidX = ZERO;
        	oidY = ZERO;
        	oidZ = ZERO;
        	if(sideX)
        		oidX = FOUR;
        	if(sideY)
        		oidY = TWO;
        	if(sideZ)
        		oidZ = ONE;
    		int idx = oidX + oidY + oidZ; //pick the octant you're looking at
    		System.out.printf("We're in octant %d, layer %d\n", idx, layer);
    		printChildStatus();
    		ArrayList<Face> result = this.children[idx].getFacesIntersectingWith(originCopy, direction, layer + 1); //get faces
    		if(result != null) // if it's an actual result, return it
    			return result;
    		double minDist = Math.min(Math.min(xDist, yDist), zDist); //find shortest distance for origin to any three planes
    		if(minDist == Double.POSITIVE_INFINITY) {
    			System.out.println("Is pointing away");//if none intersect return null
    			return null;
    		}
    		Vector3d adjustedOrigin = new Vector3d();
    		direction.mul(minDist, adjustedOrigin);
    		adjustedOrigin.add(originInput);
    		originCopy = adjustedOrigin;
    		if(!this.bounds.containsPoint(originCopy)) {
    			System.out.println("new origin isn't in this node");
    			return null;
    		}
    		if(minDist == xDist) {
    			sideX = !sideX;
    			xDist = Double.POSITIVE_INFINITY;
    		} else if(minDist == yDist) {
    			sideY = !sideY;
    			yDist = Double.POSITIVE_INFINITY;
    		} else if(minDist == zDist) {
    			sideZ = !sideZ;
    			zDist = Double.POSITIVE_INFINITY;
    		}   		
    	}//end for
    	System.out.println("oh no we got here");
    	return null;
    } */
    
	
	/**
	 * Method for checking if a face is completely inside a bounding box.
	 * Checks if all of the face's vertices are inside the bounding box.
	 * @param face the Face to check against.
	 * @param bounds the BoundingBox to check that the face is inside of.
	 * @return if the Face intersects with the bounding box.
	 */
	public boolean areOverlapping(Face f, BoundingBox bounds) {
		double x1 = f.vertices.get(ZERO).v.x();
		double x2 = f.vertices.get(ONE).v.x();
		double x3 = f.vertices.get(TWO).v.x();
		

		double y1 = f.vertices.get(ZERO).v.y();
		double y2 = f.vertices.get(ONE).v.y();
		double y3 = f.vertices.get(TWO).v.y();
		
		double z1 = f.vertices.get(ZERO).v.z();
		double z2 = f.vertices.get(ONE).v.z();
		double z3 = f.vertices.get(TWO).v.z();
		
		double xMin = Math.min(x1, Math.min(x2, x3));
		double xMax = Math.max(x1, Math.max(x2, x3));

		double yMin = Math.min(y1, Math.min(y2, y3));
		double yMax = Math.max(y1, Math.max(y2, y3));
		
		double zMin = Math.min(z1, Math.min(z2, z3));
		double zMax = Math.max(z1, Math.max(z2, z3));
		
		Vector3d min = new Vector3d(xMin, yMin, zMin);
		Vector3d max = new Vector3d(xMax, yMax, zMax);
		
		BoundingBox tBox = new BoundingBox(min, max);
		return tBox.isOverlappingWith(bounds);
	}
	

	
	private double getIntersectPoint(Vector3d origin, Vector3d inverse) {
		double tMin = Double.NEGATIVE_INFINITY;
		double tMax = Double.POSITIVE_INFINITY;
		
		double t1 = (this.bounds.pMin.x - origin.x) * inverse.x;
		double t2 = (this.bounds.pMax.x - origin.x) * inverse.x;
		
		tMin = Math.max(tMin, Math.min(t1, t2));
		tMax = Math.min(tMax, Math.max(t1, t2));
		
		t1 = (this.bounds.pMin.y - origin.y) * inverse.y;
		t2 = (this.bounds.pMax.y - origin.y) * inverse.y;

		tMin = Math.max(tMin, Math.min(t1, t2));
		tMax = Math.min(tMax, Math.max(t1, t2));

		t1 = (this.bounds.pMin.z - origin.z) * inverse.z;
		t2 = (this.bounds.pMax.z - origin.z) * inverse.z;
		
		tMin = Math.max(tMin, Math.min(t1, t2));
		tMax = Math.min(tMax, Math.max(t1, t2));
		
		if(!(tMax > Math.max(tMin, 0.0)))
			return -1;
		if(tMax < 0.0)
			return -1;
		if(tMin < 0.0)
			return tMax;
		return tMin;
	}
	
	private Interval getInterval(Face f, Vector3d axis) {
		Vector3d firstVertex = new Vector3d(f.vertices.get(ZERO).v);
		double firstMinAndMaxValue = firstVertex.dot(axis);
		Interval result = new Interval(firstMinAndMaxValue);
		int numVertices = f.vertices.size();
		
		for(int i = ONE; i < numVertices; i++) {
			Vector3d vertex = new Vector3d(f.vertices.get(i).v);
			double projection = vertex.dot(axis);
			result.min = Math.min(result.min, projection);
			result.max = Math.max(result.max, projection);
		}	
		return result;
	}
	
	private Interval getInterval(BoundingBox bounds, Vector3d axis) {
		Vector3d firstVertex = new Vector3d(bounds.getCorner(ZERO));
		double firstMinAndMaxValue = firstVertex.dot(axis);
		Interval result = new Interval(firstMinAndMaxValue);
		
		for(int i = ONE; i < EIGHT; i++) {
			Vector3d vertex = new Vector3d(bounds.getCorner(i));
			double projection = vertex.dot(axis);
			result.min = Math.min(result.min, projection);
			result.max = Math.max(result.max, projection);
		}
		return result;
	}
	
	private boolean overlapsOnAxis(BoundingBox bounds, Face f, Vector3d axis) {
		Interval a = this.getInterval(bounds, axis);
		Interval b = this.getInterval(f, axis);
		return ((b.min <= a.max) && (a.min <= b.max));
	}
	
	private void printChildStatus() {
		for(Octree kids: children) {
			if(kids.isEmpty)
				System.out.print("N ");
			else
				System.out.print("Y ");
		}
		System.out.println();
	}
	
	public BoundingBox getBounds() {
		return this.bounds;
	}
	
	public void testNodes() {
		if(!hasSubdivided) {
			System.out.println(this.faces.size());
		} else {
			for(Octree kids: children) {
				kids.testNodes();
			}
		}
	}
}
