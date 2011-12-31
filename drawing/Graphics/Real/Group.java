package Graphics.Real;

import java.awt.Color;
import java.io.Serializable;
import java.util.*;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * Allows entities in the data structure to be grouped and share attributes.
 * A Group object is basically an ArrayList.  These are naturally recursive and
 * Groups can be nested to create subsets.  It is also possible for object to 
 * belong to disjoint groups.  Basically, you can structure groups in any way
 * ArrayLists can be structured.
 * 
 * A Group object is a RealShape but does not support everything a RealShape does
 * and so must be checked for explicitly.
 */
public class Group extends ArrayList implements Serializable, RealShape {
    //All functionality except for transformations is inherited from ArrayList.
	/** Recursively search for an item to delete. */
	public void removeRecursive( Object theObject ) {
		for( int i = 0; i < size(); i++ ) {
			//We could be removing a group, so check this first
			if( get(i).equals( theObject ) ) {
				remove( i );
			}
			else if( get(i) instanceof Group ) {
				Group g = (Group)get(i);
				g.removeRecursive( theObject );
			}
		}
	}
	
	/* Transformation methods-> just call methods of Transformable children. */
	public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
		for( int i = 0; i < size(); i++ ) {
			if( get( i ) instanceof RealShape )
				((RealShape)get(i)).scale( xFactor, yFactor, aboutPoint );
			else if( get( i ) instanceof Group )
				((Group)get(i)).scale( xFactor, yFactor, aboutPoint );
		}
	}
    public void rotate( double theta, RealPoint aboutPoint ) {
    	    for( int i = 0; i < size(); i++ ) {
    	    	    if( get( i ) instanceof RealShape )
    				((RealShape)get(i)).rotate( theta, aboutPoint );
    			else if( get( i ) instanceof Group )
    				((Group)get(i)).rotate( theta, aboutPoint );
		}
    }
    public void translate( RealPoint delta ) {
    	    for( int i = 0; i < size(); i++ ) {
    	    	    if( get( i ) instanceof RealShape )
    				((RealShape)get(i)).translate( delta );
    			else if( get( i ) instanceof Group )
    				((Group)get(i)).translate( delta );
		}
    }
    /** Clip everything in this group, which might include another group. */
    public RealShape clip( double xMin, double xMax, double yMin, double yMax ) {
    	   Group clippedGroup = new Group();
    	   
    	   for( int i = 0; i < size(); i++ ) {
    	       clippedGroup.add( ((RealShape)get(i)).clip( xMin, xMax, yMin, yMax ) );
    	   }
    	   
    	   return clippedGroup;
    }
    /* Color accessors, meaningless for groups */
    public Color lineColour() { return null; }
    public Color fillColour() { return null; }
    /** Recurse down, setting line or fill when possible */
    public void setLineColour( Color lineColour ) { 
    	    for( int i = 0; i < size(); i++ ) {
    	        if( get(i) instanceof RealShape )
    	    	        ((RealShape)get(i)).setLineColour( lineColour );
        }
    	}
    public void setFillColour( Color fillColour ) { 
    	    for( int i = 0; i < size(); i++ ) {
    	    	    if( get(i) instanceof RealShape )
    	    	    	    ((RealShape)get(i)).setFillColour( fillColour );
    	    }
    	}
    /** Return a string representation of this group.  Used for the tree display. */
    public String toString() { return "Group" + super.toString(); }
}
