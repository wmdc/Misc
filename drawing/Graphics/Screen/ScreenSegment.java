package Graphics.Screen;

import java.util.ArrayList;
import java.awt.Color;

/**
 * Created on Jan 21, 2006
 * 
 * @author mwelsman
 *
 * Represents a line segment with two end points given in
 * screen coordinates.
 * 
 * This object is immutable once constructed.
 */
public class ScreenSegment implements Drawable {
    private ScreenPoint start, end;
    private Color lineColour;
    
    //Some useful pre-computed values.
    private double slope;
    private int minY, maxY, minX;

    /** ScreenSegment constructors */
    public ScreenSegment( int startX, int startY, int endX, int endY, Color lineColour ) {
    	    this.start = new ScreenPoint( startX, startY );
    	    this.end = new ScreenPoint( endX, endY );
    	    
    	    this.lineColour = lineColour;
    	    
    	    //Precompute values used for the filling routine
    	    if( start.getY() < end.getY() ) {
    	    	    minY = start.getY();
    	    	    minX = start.getX();
    	    	    maxY = end.getY();
    	    }
     	else {
     		minY = end.getY();
    	        	minX = end.getX();
    	        	maxY = start.getY();
     	}
    	    //Precompute the slope of the line segment -> this is a fractional value
    	    slope = ((double)( start.getY() - end.getY() )) / ( start.getX() - end.getX() );
    }
    public ScreenSegment( ScreenPoint start, ScreenPoint end, Color lineColour ) {
	    this.start = start;
	    this.end = end;
	    
	    this.lineColour = lineColour;
	    
        //Precompute the lowest Y value
	    if( start.getY() < end.getY() ) minY = start.getY();
	    else minY = end.getY();
	    //Precompute the highest Y value
	    if( start.getY() > end.getY() ) maxY = start.getY();
	    else maxY =  end.getY();
	    //Precompute the X value associated with the lowest endpoint
	    if( start.getY() < end.getY() ) minX = start.getX();
 	    else minX = end.getX();
	    //Precompute the slope of the line segment -> this is a fractional value
	    slope = ((double)( start.getY() - end.getY() )) / ( start.getX() - end.getX() );
    }
    
    public ScreenPoint getStart() { return start; }
    public ScreenPoint getEnd() { return end; }
    
    /** 
     * Returns the pixels that should be coloured in to draw this line.
     * Pixels are calculated using Bresenham's line drawing algorithm.
     * */
    public ArrayList pixels() {
    	    //Quick check.. is it just one pixel?  If so, return.
    	    if( start.equals( end ) ) {
    	    	    ArrayList result = new ArrayList();
    	    	    result.add( start );
    	    	    return result;
    	    }
    	    
    	    int dx, dy, x, y, xEnd, p, const1, const2;
    	    boolean steep = false; //is the line's |slope| > 1?

    	    if( Math.abs( end.getY() - start.getY() ) > Math.abs( end.getX() - start.getX() )) {
    	    	    steep = true;  //swap
    	    	    ScreenPoint temp = new ScreenPoint( start.getX(), start.getY() );
    	    	    start = new ScreenPoint( temp.getY(), temp.getX() );
    	    	
    	    	    temp = new ScreenPoint( end.getX(), end.getY() );
    	    	    end = new ScreenPoint( temp.getY(), temp.getX() );
    	    }
    	    
    	    dx = Math.abs( start.getX() - end.getX() );
    	    dy = Math.abs( start.getY() - end.getY() );
    	    p = 2 * dy - dx;
    	    
    	    const1 = 2 * dy;              //stored for convenience
    	    const2 = 2 * ( dy - dx );     //and performance
    	    
    	    x = start.getX();
	    	y = start.getY();
	    	xEnd = end.getX();
    	    
    	    ArrayList result = new ArrayList();
    	    
    	    do {
    	    	//If the line's slope is steep we swapped coordinates
    	    	//earlier and must swap them back now.
    	    	if( steep ) result.add( new ColouredPixel( y, x, lineColour ));
    	    	else        result.add( new ColouredPixel( x, y, lineColour ) );
    	    	
    	    	if( start.getX() > end.getX() ) x--;
    	    	else x++;
    	    	    //Do we make a step in Y?
    	    	    if( p <  0 )
    	    	    	    p += const1;
    	    	    else {
    	    	    	    p += const2;
    	    	    	    if( start.getY() > end.getY() ) y--;
    	    	    	    else y++;
    	    	    }
    	    } while( x != xEnd );
    	    
    	    return result;
    }
    
    //Useful methods for polygon filling.  These are pre-computed so they can be called
    //multiple times fairly efficiently.
    /** Get the lowest Y value for the line segment. */
    public int getMinimumY() {
    	    return minY;
    }
    /** Get the highest Y value for the line segment. */
    public int getMaximumY() {
    	    return maxY;
    }
    /** Get the X value of the lowest point on the line segment. */
    public int getLowestX() {
    	   return minX;
    }
    /** Get the X coordinate on the line corresponding to the given Y.  Don't call this
     *  with horizontale lines (slope 0). */
    public int getXIntercept( int yValue ) {
    	    if( start.getY() == end.getY() ) { //horizontal
    	    	    if( start.getX() < end.getX() ) return end.getX();
    	    	    else return start.getX();
    	    }
    	    return (int)( start.getX()  + ( yValue - start.getY() ) / slope );
    }
    /** Get the slope of this line segment. */
    public double getSlope() {
    	    return slope;
    }
    
    public String toString() {
    	   return "SSeg[" + start +", " + end + "]";
    }
}
