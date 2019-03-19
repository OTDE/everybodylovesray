package com.shffl.assets;

import java.util.ArrayList;

import org.joml.Vector3d;

import com.owens.oobjloader.builder.Face;
import com.owens.oobjloader.builder.FaceVertex;

/**
 * Octree data structure for optimizing Face processing.
 * @author Ethan Wiederspan and Seth Chapman
 */
public class Octree {
	
	private static final int ZERO = 0;
	private static final int TWO = 2;
	private static final int EIGHT = 8;
	protected static final int MAX_FACES = 10;
	public ArrayList<Face> faces;
	private boolean hasSubdivided = false;
	private Octree[] children;
	public BoundingBox bounds;
	public int numObjects = 0;
	
	/**
	 * Constructor with a given set of Faces.
	 * @param faces the set of faces to add to this Octree
	 * @param bounds the global bounds.
	 */
	public Octree(Face[] faces, BoundingBox bounds) {
		this(bounds);
		for(Face f : faces) {
			insert(f);
		}
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
	
	/**
	 * Recursively inserts Face into Octree.
	 * @param face the Face to be inserted.
	 * @return True for successful insertion at a given depth, false otherwise
	 */
	public boolean insert(Face face) {
		if(!isInside(face, bounds))
			return false;
		if(!hasSubdivided) {
			if(faces.size() > MAX_FACES) {
				this.subdivide();
				ArrayList<Face> facesCopy = new ArrayList<Face>(faces);
				for(Face f : facesCopy) {
					if(insert(f)) {
						faces.remove(f);
					}
				}
			} else {
				faces.add(face);
			} 
		} else {
			boolean inLowerLevel = false;
			for(Octree thisOctree : children) {
				if(thisOctree.insert(face)) {
					inLowerLevel = true;
				}
			}
			if(!inLowerLevel) {
				faces.add(face);
			}
		}
		return true;
	}
	
    /**
     * Divides a leaf node into 8 leaves, with the original node as the root.
     */
    private void subdivide() { 
        if (hasSubdivided) { 
            System.err.println("We have already subdivided this."); 
            System.exit(1); 
        } 
        hasSubdivided = true; 
 
        Vector3d minBoxMin = new Vector3d(bounds.pMin);
        Vector3d minBoxMax = new Vector3d(bounds.pMax.x / TWO, bounds.pMax.y / TWO, bounds.pMax.z / TWO); 
        Vector3d maxBoxMin = new Vector3d(minBoxMax);
        Vector3d maxBoxMax = new Vector3d(bounds.pMax);
        
        BoundingBox minBox = new BoundingBox(minBoxMin, minBoxMax);
        BoundingBox maxBox = new BoundingBox(maxBoxMin, maxBoxMax);
 
        BoundingBox[] newBounds = new BoundingBox[EIGHT]; 
        children = new Octree[8]; 
        
        for (int i = ZERO; i < EIGHT; i++) { 
            newBounds[i] = new BoundingBox(minBox.getCorner(i), maxBox.getCorner(i));
            children[i] = new Octree(newBounds[i]);  
        } 
    } 
    
    /**
     * Query method for accessing elements in an Octree based on proximity.
     * @param range the BoundingBox to check for faces inside.
     * @return a list of all Faces within range of the input BoundingBox.
     */
    public ArrayList<Face> getFacesWithinRange(BoundingBox range) { 
        ArrayList<Face> facesInRange = new ArrayList<Face>(); 
        for(Face f : faces) { 
            if (isInside(f, range)) { 
                facesInRange.add(f); 
            } 
        } 
        if(hasSubdivided) { 
            for(Octree octree : children) { 
                if (octree.bounds.isOverlappingWith(range)) { 
                    facesInRange.addAll(octree.getFacesWithinRange(range)); 
                } 
            }  
        } 
        return facesInRange; 
    }
    
    public ArrayList<Face> getFacesIntersectingWith(Ray r) {
    	ArrayList<Face> facesIntersectingWithRay = new ArrayList<Face>();
    	if(hasSubdivided) {
    		for(Octree octree : children) {
    			if(octree.intersectsWith(r)) {
    				facesIntersectingWithRay.addAll(octree.getFacesIntersectingWith(r));
    			}
    		}
    	}
    	if(this.intersectsWith(r))
    		facesIntersectingWithRay.addAll(faces);
    	return facesIntersectingWithRay;
    }
	
	/**
	 * Method for checking if a face is completely inside a bounding box.
	 * Checks if all of the face's vertices are inside the bounding box.
	 * @param face the Face to check against.
	 * @param bounds the BoundingBox to check that the face is inside of.
	 * @return if the Face is completely inside the BoundingBox.
	 */
	public boolean isInside(Face face, BoundingBox bounds) {
		 for(FaceVertex vertex: face.vertices) {
			 if(!bounds.containsPoint(vertex.v)) {
				 return false;
			 }
		 }
		 return true;
	}
	
	public boolean intersectsWith(Ray r) {
		double tMin = Double.NEGATIVE_INFINITY;
		double tMax = Double.POSITIVE_INFINITY;
		
		double t1 = (this.bounds.pMin.x - r.origin.x) * r.inverse.x;
		double t2 = (this.bounds.pMax.x - r.origin.x) * r.inverse.x;
		
		tMin = Math.max(tMin, Math.min(t1, t2));
		tMax = Math.min(tMax, Math.max(t1, t2));
		
		t1 = (this.bounds.pMin.y - r.origin.y) * r.inverse.y;
		t2 = (this.bounds.pMax.y - r.origin.y) * r.inverse.y;

		tMin = Math.max(tMin, Math.min(t1, t2));
		tMax = Math.min(tMax, Math.max(t1, t2));

		t1 = (this.bounds.pMin.z - r.origin.z) * r.inverse.z;
		t2 = (this.bounds.pMax.z - r.origin.z) * r.inverse.z;
		
		return tMax > Math.max(tMin, 0.0);
	}
}
