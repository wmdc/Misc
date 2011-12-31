package Graphics.Screen;

import java.util.ArrayList;
import java.awt.Color;

/**
 * Created on Apr 2, 2006
 * @author mwelsman
 *
 * Defines a polygon in screen coordinates.  This is needed to maintain some information for the
 * fill routines.
 */
public class ScreenPolygon implements Drawable {
    private boolean filled;
    private Color lineColour, fillColour;
    private ArrayList segments;
    /** ScreenPolygon constructors. */
    public ScreenPolygon() {
    	    super();
    	    segments = new ArrayList();
    	    lineColour = Color.WHITE;
    	    filled = true;
    }
    public ScreenPolygon( ArrayList edges, boolean filled, Color lineColour, Color fillColour ) {
       	segments = edges;
    	    this.filled = filled;
    	    this.lineColour = lineColour;
    	    this.fillColour = fillColour;
    }
    /** Return an ordered list of line segments that make up this polygon that have non-zero slopes.
     *  This is useful for clipping. */
    public ArrayList getNonZeroSegments() {
    	    ArrayList result = new ArrayList();
    	    
    	    for( int i = 0; i < segments.size(); i++ )
    	    	    if( ((ScreenSegment)segments.get(i)).getSlope() != 0 ) result.add( segments.get(i) );
    	    	    
    	    return result;
    }
    /** Is this a filled polygon? */
    public boolean isFilled() {
    	    return filled;
    }
    /** Return the segments that make up this polygon. */
    public ArrayList getSegments() {
    	    return segments;
    }
    /** Return the points associated with this polygon.  Note that this will only provide the polygon outline.
     *  In order to fill the polygon it must be passed through the fill routine. */
    public ArrayList pixels() {
    	    ArrayList points = new ArrayList();
    	    
    	    for( int i = 0; i < segments.size(); i++ ) {
    	    	    points.addAll(((ScreenSegment)segments.get(i)).pixels());
    	    }
    	    
    	    return points;
    }
    
    public Color getLineColour() { return lineColour; }
    public Color getFillColour() { return fillColour; }
}
