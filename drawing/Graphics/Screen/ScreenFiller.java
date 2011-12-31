package Graphics.Screen;

import java.util.ArrayList;

/**
 * Created on Apr 2, 2006
 * @author mwelsman
 *
 * Line filler that operates on polygons and circles contained in the given data structure.  A new
 * structure is generated with extra line segments that fill in the shapes.  This is done in screen
 * coordinates since we want to fill in every pixel.
 * 
 * By the time the data structure is passed to the filler it should have already been clipped.
 */
public class ScreenFiller {
	/** All we are looking for here are line segments which share X values within */
    public static ArrayList fill( ArrayList data ) {
    	    ArrayList result = new ArrayList();
    	    
    	    //loop through the data.  We're interested in polygons and circles
    	    for( int i = 0; i < data.size(); i++ ) {
    	    	    if( data.get(i) instanceof ScreenPolygon )  //polygon fill routine
    	    	    	    result.addAll( fill( (ScreenPolygon)data.get(i)) );
    	    	    else if( data.get(i) instanceof ScreenCircle )  //circle fill routine
    	    	    	    result.addAll( fill( (ScreenCircle)data.get(i)) );
    	    	    else
    	    	    	    result.add( data.get(i) );  //some other item that doesn't get filled (e.g. point or line)
    	    }
    	    return result;
    }
    /** "Fill" a single polygon by returning a bunch of segments.  The outer (non-fill) 
     *  segments should be last in the list so that they are drawn overtop.  This is important
     *  if the line colour is not the same as the fill colour. */
    public static ArrayList fill( ScreenPolygon polygon ) {
    	    //If the polygon does not have three vertices or more, it cannot be filled.
    	    if( polygon.getSegments().size() < 3 ) return new ArrayList();
    	    
        ArrayList verticalSegs = polygon.getSegments(); //polygon.getNonZeroSegments(); //don't count horizontal
        
        int minY = 0, maxY = 0;
        boolean beginMin = false, beginMax = false;
        /* Get the range of Y values to scan over. */
        for( int i = 0; i < verticalSegs.size(); i++ ) {
        	   if( !beginMin ) {
        	   	   minY = ((ScreenSegment)verticalSegs.get(i)).getMinimumY();
        	   	   beginMin = true;
        	   }
        	   if( !beginMax ) {
     	   	   maxY = ((ScreenSegment)verticalSegs.get(i)).getMaximumY();
     	   	   beginMax = true;
     	   }
        	   
        	   ScreenSegment cur = (ScreenSegment)verticalSegs.get(i);
        	   
        	   if( cur.getMinimumY() < minY ) minY = cur.getMinimumY();
        	   if( cur.getMaximumY() > maxY ) maxY = cur.getMaximumY();
        }
        
        ArrayList fillSegments = new ArrayList(); //the scanlines to draw

    	    /* Scan over the range of Y values. */
    	    for( int i = minY; i <= maxY; i++ ) {
    	    	    ArrayList segs = sortByXIntercept( getActiveSegments( verticalSegs, i ), i);

    	    	    //Loop through the sorted segments, connecting edges
    	    	    for( int j = 0; j < segs.size() - 1; j += 2 ) {
    	    	    	    ScreenSegment even = (ScreenSegment)segs.get( j );
    	    	    	    ScreenSegment odd = (ScreenSegment)segs.get( j + 1 );
    	    	    	    
    	    	    	    int startX = even.getXIntercept( i );
    	    	    	    int endX = odd.getXIntercept( i );
    	    	    	    //Add the scan line for filling
    	    	    	    fillSegments.add( new ScreenSegment( startX, i, endX, i, polygon.getFillColour() ) );
    	    	    }
    	    	    
    	    }
    	    fillSegments.add( polygon.getSegments() );  //add the outline
    	    return fillSegments;
    }
    /** Return a list of ScreenSegments that include the given Y value. */
    public static ArrayList getActiveSegments( ArrayList segments, int y ) {
    	    ArrayList result = new ArrayList();
    	    
    	    for( int i = 0; i < segments.size(); i++ ) {
    	    	    ScreenSegment seg = (ScreenSegment)segments.get( i );

    	    	    if( seg.getMinimumY() <= y && seg.getMaximumY() >= y )
    	    	    	    result.add( seg );  //y is within range, so add this segment
    	    }
    	    return result;
    }
    /** Order the list so that the first segment has the smallest X intercept, etc. */
    public static ArrayList sortByXIntercept( ArrayList segments, int y ) {
    	    ArrayList source = new ArrayList();
	    source.addAll( segments ); //copy since we will be altering
	    
    	    ArrayList result = new ArrayList();
    	    
    	    int xInt = 0;
    	    ScreenSegment smallest = null;
        //Sort by X intercept.  Assume no horizontal segments.
    	    while( !source.isEmpty() ) {
    	    	    for( int i = 0; i < source.size(); i++ ) {
    	    	        	ScreenSegment seg = (ScreenSegment)source.get( i );
    	    	    	    if( smallest == null ) {
    	    	    	        smallest = seg;
    	    	    	        xInt = seg.getXIntercept( y );
    	    	    	    } else {
    	    	    	    	    if( seg.getXIntercept( y ) < xInt ) {
    	    	    	    	    	    smallest = seg;
    	    	    	    	    	    xInt = seg.getXIntercept( y );
    	    	    	    	    } /*else if( seg.getXIntercept( y ) == xInt ) {
    	    	    	    	    	    source.remove(smallest);
	    	    	    	    	    smallest = seg;
	    	    	    	    	    xInt = seg.getXIntercept( y );
	    	    	    	    }*/
    	    	    	    }
    	    	    }
    	    	    result.add( smallest );
    	    	    source.remove( smallest );
    	    	    smallest = null; //start over
    	    }
    	    //This is a sorted ArrayList of segments, from left (x) to right given a certain
    	    //scan line
    	    return result;
    }
    /** Fill routine for a single circle.  Step down the circle and turn it into a bunch of line
     *  segments.  Include the circle outline at the end in case the line colour is different
     *  from the fill colour. */
    public static ArrayList fill( ScreenCircle circle ) {
    	    ArrayList points = new ArrayList();
    	    
    	    int yMin = circle.getY() - circle.getRadius(); //lowest point
    	    int yMax = circle.getY() + circle.getRadius(); //highest point
    	    
    	    for( int i = yMin; i <= yMax; i++ ) {
    	    	    //Move up the scan lines, finding the two intersection points with
    	        //the circle.
    	    	
    	    	    int dx = (int)Math.sqrt( circle.getRadius() * circle.getRadius() - 
    	    	    		                     ( circle.getY() - i ) * ( circle.getY() - i ) );
    	    	    int x = circle.getX();
    	    	    
    	    	    for( int j = x - dx; j <= x + dx; j++ ) {
    	    	    	    points.add( new ColouredPixel( j, i, circle.getFillColour() ));
    	    	    }
    	    }
    	    
      	points.add( circle );
	    return points;
    }
}