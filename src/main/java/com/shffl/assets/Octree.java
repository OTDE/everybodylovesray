package com.shffl.assets;

import java.util.ArrayList;

import org.joml.Vector3d;

import com.owens.oobjloader.builder.Face;
import com.owens.oobjloader.builder.FaceVertex;

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
	
	public Octree(Face[] faces, BoundingBox bounds) {
		this(bounds);
		for(Face f : faces) {
			insert(f);
		}
	}
	
	public Octree(BoundingBox bounds) {
		this.bounds = bounds;
		faces = new ArrayList<Face>();
		this.hasSubdivided = false;
		numObjects++;
	}
	
	public boolean insert(Face face) {
		if(!isInside(face, bounds))
			return false;
		if(!hasSubdivided) {
			if(faces.size() > MAX_FACES) {
				this.subdivide(); // Subdivide if this node is over capacity and hasn't already
				ArrayList<Face> facesCopy = new ArrayList<Face>(faces);
				for(Face f : facesCopy) {
					if(insert(f)) { //if this is successfully inserted into a lower level
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
    
    public ArrayList<Face> getFacesWithinRange(BoundingBox range) { 
        ArrayList<Face> facesInRange = new ArrayList<Face>(); 
        for (Face f : faces) { 
            if (isInside(f, range)) { 
                facesInRange.add(f); 
            } 
        } 
        if (hasSubdivided) { 
            for (Octree octree : children) { 
                if (octree.bounds.isOverlappingWith(range)) { 
                    facesInRange.addAll(octree.getFacesWithinRange(range)); 
                } 
            }  
        } 
        return facesInRange; 
    }
	
	public boolean isInside(Face face, BoundingBox bounds) {
		 for(FaceVertex vertex: face.vertices) {
			 if(!bounds.containsPoint(vertex.v)) {
				 return false;
			 }
		 }
		 return true;
	}
}
