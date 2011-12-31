package Graphics.Real;

import java.util.ArrayList;

/**
 * Created on Apr 2, 2006
 * @author mwelsman
 *
 * Takes the data structure and world window parameters and clips the data structure contents 
 * so that only objects visible within the window are contained in the resultant structure.
 * This structure is then passed along for drawing.
 */
public class RealClipper {
	/** Take a data structure (the ArrayList) and outer edges of the world window and create a new
	 *  data structure where the objects from the first are clipped.  The original structure remains
	 *  untouched. 
	 * 
	 *  World window information is given in terms of X and Y min/max values for easy use with region
	 *  codes.
	 * 
	 *  This method relies on the Clippable interface.  Clippable objects can be clipped and contain
	 *  methods for clipping themselves to a given viewport. */
    public static ArrayList clip( ArrayList data, double xMin, double xMax, double yMin, double yMax ) {
    	    ArrayList result = new ArrayList();
    	    
    	    for( int i = 0; i < data.size(); i++ ) {
    	    	    Object curObj = data.get( i );
    	    	    
    	    	    if( curObj instanceof Group ) //it's a group item so recurse
    	    	    	    result.addAll( clip( (ArrayList)curObj, xMin, xMax, yMin, yMax ));
    	    	    else if( 	curObj instanceof RealSegment || 
    	    	    				curObj instanceof RealString ||
						curObj instanceof RealCircle ) {  //polygons not supported yet
    	    	    	    RealShape clipObj = (RealShape)curObj;
    	    	    	    
    	    	    	    	clipObj = clipObj.clip( xMin, xMax, yMin, yMax );  //perform the clipping on the object
    	    	    	    	//check for null since sometimes the object won't exist at all inside the world window
    	    	    	    	if( clipObj != null ) result.add( clipObj );
    	    	    }
    	    	    else result.add( data.get( i ));  //it can't be clipped so it stays in
    	    }
    	    
    	    return result;
    }
    /** Return the intersection point of the given edge with the given side of the
        world window.
        Side is given using RegionCode constants such as RegionCode.LEFT. */
    public static RealPoint intersect( RealPoint p1, RealPoint p2, int side, double xMin, double xMax, double yMin, double yMax ) {
    	double slope = ((double)( p1.getY() - p2.getY()))/( p1.getX() - p2.getX() );
    	
    	if( side == RegionCode.LEFT ) {  //intersection with left edge
    		double x = xMin;
    		double y = p1.getY() + ( xMin - p1.getX() ) * slope;
    		return new RealPoint( x, y );
    	}
    	else if( side == RegionCode.RIGHT ) {  //intersection with right edge
    		double x = xMax;
    		double y = p1.getY() + ( xMax - p1.getX() ) * slope;
    		return new RealPoint( x, y );
    	}
    	else if( side == RegionCode.ABOVE ) {  //intersection with top edge
    		double x = p1.getX() + ( yMax - p1.getY() ) * slope;;
    		double y = yMax;
    		return new RealPoint( x, y );
    	}
    	else if( side == RegionCode.BELOW ) {  //intersection with bottom edge
    		double x = p1.getX() + ( yMin - p1.getY() ) * slope;
    		double y = yMin;
    		return new RealPoint( x, y );
    	}
    	else
    		return null;  //this is an error case
    }
}