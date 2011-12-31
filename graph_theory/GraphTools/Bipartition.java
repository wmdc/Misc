package GraphTools;

import java.util.Vector;

/** A simple class for storing the bipartition of a graph */
public class Bipartition {
	public Vector x, y; //the two sets of the bipartition
	/** Construct a bipartition from two sets. */
	public Bipartition( Vector x, Vector y ) {
		this.x = x;
		this.y = y;
	}
	
	/** String representation of the bipartition. */
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
}