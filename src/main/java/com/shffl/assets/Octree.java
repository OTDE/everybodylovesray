package com.shffl.assets;

import java.util.ArrayList;

import org.joml.Vector3d;

import com.owens.oobjloader.builder.Face;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * Octree data structure for optimizing Face processing.
 */
public class Octree {
	
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int EIGHT = 8;
	private static final int MAX_DEPTH = 30;
	protected static final int MAX_FACES = 900;
	public ArrayList<Face> faces;
	private boolean hasSubdivided = false;
	public boolean isEmpty = true;
	private Octree[] children;
	public BoundingBox bounds;
	public int curDepth;
	
	/**
	 * Constructor with a given set of Faces.
	 * @param faces the set of faces to add to this Octree
	 * @param bounds the global bounds.
	 */
	public Octree(ArrayList<Face> faces, BoundingBox bounds) {
		this(bounds);
		this.faces = faces;
	}
	
	/**
	 * Default constructor.
	 * @param bounds the global bounds.
	 */
	public Octree(BoundingBox bounds) {
		this.bounds = bounds;
		faces = new ArrayList<Face>();
		this.hasSubdivided = false;
	}
	
	/**
	 * Recursive method for building an Octree.
	 * Exit conditions:
	 * --Octree is built to the max depth
	 * --There are not enough faces in a leaf to necessitate division
	 * @param depth The current depth of the tree.
	 */
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
	}//build
	
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
    }//subdivide
    
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
    }//getFacesWithin
    
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
	}//areOverlapping
	
	//Accessor
	
	public BoundingBox getBounds() {
		return this.bounds;
	}
	
	/**
	 * Method for testing contents of Octree.
	 */
	public void testNodes() {
		if(!hasSubdivided) {
			System.out.println(this.faces.size());
		} else {
			for(Octree kids: children) {
				kids.testNodes();
			}
		}
	}//testNodes
	
}//class

