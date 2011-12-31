package Graphics.Real;

import java.util.ArrayList;
import java.awt.Color;

/**
 * Created on Jan 22, 2006
 * @author mwelsman
 *
 * Represents a polygon defined in world coordinates.
 * Polygons are also closed by definition, so this is done implicitly.
 */
public class RealPolygon
                         implements java.io.Serializable, RealShape {
    private Color lineColour, fillColour;     //colours of the polygon
    private ArrayList points;   //vertices in the polygon
    
    /** Construct an empty polygon. */
    public RealPolygon( Color lineColour, Color fillColour ) {
    	    points = new ArrayList();
        this.lineColour = lineColour;
        this.fillColour = fillColour;
    }
    /** Construct an unfilled polygon with the given real-world points. */
    public RealPolygon( ArrayList vertices, Color lineColour ) {
       	points = vertices;
	    this.lineColour = lineColour;
	    this.fillColour = null;
    }
    /** Construct a filled polygon. */
    public RealPolygon( ArrayList vertices, Color lineColour, Color fillColour ) {
    	    points = vertices;
    	    this.lineColour = lineColour;
    	    this.fillColour = fillColour;
    }
    
    /** Returns the line segments (RealSegments) that make up this 
     *  polygon.  Used for drawing the polygon. */
    public ArrayList getSegments() {
    	    ArrayList result = new ArrayList();

    	    //Add the points defined in the structure
    	    for( int i = 0; i < points.size(); i++ ) {
    	    	    RealPoint start = new RealPoint( ((RealPoint)points.get( i )).getX(), ((RealPoint)points.get( i )).getY() );
    	    	    RealPoint end;
    	    	    
    	    	    if( i == points.size() - 1 ) { //implicit ("wrap around") edge
    	    	    	    end = new RealPoint( ((RealPoint)points.get( 0 )).getX(), ((RealPoint)points.get( 0 )).getY() );
    	    	    }
    	    	    else end = new RealPoint( ((RealPoint)points.get( i + 1 )).getX(), ((RealPoint)points.get( i + 1 )).getY() );
    	    	    
    	    	    result.add( new RealSegment( start, end, lineColour ));
    	    }
    	    
    	    return result;
    }
    
    /** Is this polygon filled? */
    public boolean isFilled() {
    	    return fillColour == null;
    }
    /** Get this polygon's line colour. */
    public Color lineColour() {
    	    return lineColour;
    }
    /** Get this polygon's fill colour. */
    public Color fillColour() {
	    return fillColour;
    }
    /** Set this polygon's line colour. */
    public void setLineColour( Color c ) {
	    lineColour = c;
    }
    /** Get this polygon's fill colour. */
    public void setFillColour( Color c ) {
    	    fillColour = c;
    }
    /** Polygon clipping.
     *  This is the Sutherland-Hodgman clipping algorithm. */
    /* NOT WORKING CORRECTLY */
    public RealShape clip( double xMin, double xMax, double yMin, double yMax ) {
    	    ArrayList clippedPoints = new ArrayList( points.size() );
    	
    	    //Make four passes over the polygon.  One single pass per side.
    	    for( int i = RegionCode.LEFT; i <= RegionCode.BELOW; i++ ) {
    		    //Go through each implicit edge in the polygon.
    		    for( int j = 0; j < points.size(); j++ ) {
    			    RealPoint p1, p2;
    			    if( j == points.size() - 1 ) { //loop around (close polygon)
    				    p1 = (RealPoint)points.get( j );
    			        p2 = (RealPoint)points.get( 0 );
    			    } else {
    			        p1 = (RealPoint)points.get( j );
    			        p2 = (RealPoint)points.get( j + 1 );
    			    }
    			
    			    RegionCode c1 = new RegionCode( p1, xMin, xMax, yMin, yMax );
    			    RegionCode c2 = new RegionCode( p2, xMin, xMax, yMin, yMax );
    			    //Four cases to consider...
    			
    			    //1. Edge goes from outside current side to outside.  Don't include.
    			    if( c1.getCode( i ) && c2.getCode( i ) ); //go to the next edge
    			    //2. Edge goes from outside current side to inside.
    			    //   Save intersection and inside vertex.
    			    else if( c1.getCode( i ) && !c2.getCode( i ) ) {
    				    clippedPoints.add( RealClipper.intersect( p1, p2, i, xMin, xMax, yMin, yMax) );
    				    clippedPoints.add( p2 );
    			    }
    			    //3. Edge goes from inside to outside.  Save intersection point.
    			    else if( !c1.getCode( i ) && c2.getCode( i ) ) {
    				    clippedPoints.add( RealClipper.intersect( p1, p2, i, xMin, xMax, yMin, yMax) );
    			    }
    			    //4. Edge goes from inside to inside.
    			    //   Save the second point (first was saved previously).
    			    else if( !c1.getCode( i ) && !c2.getCode( i ) ) {
    				    clippedPoints.add( p2 );
    			    }
    		    }
    	    }
    	    //Build the new polygon
    	    return new RealPolygon( clippedPoints, lineColour, fillColour );
    }
    
    /** Get the position of the polygon, which is by definition the first point in the
     *  polygon. */
    public RealPoint getPosition() {
    	    if( points.isEmpty() )
    	    	    return null;
    	    
    	    RealPoint position = (RealPoint)points.get( 0 );
    	    return position;
    }
    /** Insert a new vertex into the polygon. */
    public void insertPoint( RealPoint p, int index ) {
    	    points.add( index, p );
    }
    /** Remove the vertex at the given index from the polygon. */
    public void removePoint( int index ) {
    	    points.remove( index );
    }
    
    /** Build a Polygon from PolyRel type input. */
    public static RealPolygon polyRel( ArrayList points, RealPoint origin, Color fillColour, Color lineColour ) {
    	    ArrayList result = new ArrayList();
    	    result.add( new RealPoint( origin ) );
    	    
    	    RealPoint last = new RealPoint( origin );
    	    
    	    //Each point in the arraylist contains dy/dx from last point
    	    for( int i = 0; i < points.size(); i++ ) {
    	    	    RealPoint curDelta = (RealPoint)points.get(i);
    	    	    curDelta.translate( last );
    	    	    result.add( new RealPoint( curDelta ) );
    	    	    
    	    	    last = curDelta;
    	    }
    	    return new RealPolygon( result, lineColour, fillColour );
    }
    
    /* Transformation methods.
     * These are implemented by the RealPoint class. */
    /** Scale the polygon's vertices about the given point. */
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
    	    for( int i = 0; i < points.size(); i++ )
    	    	   ((RealPoint)points.get(i)).scale( xFactor, yFactor, aboutPoint );
    }
    /** Rotate the polygon's vertices about the given point. */
    public void rotate( double theta, RealPoint aboutPoint ) {
    	    for( int i = 0; i < points.size(); i++ )
	    	   ((RealPoint)points.get(i)).rotate( theta, aboutPoint );
    }
    /** Scale the polygon's vertices. */
    public void translate( RealPoint delta ) {
    	    for( int i = 0; i < points.size(); i++ )
    	    	    ((RealPoint)points.get(i)).translate( delta );
    }
    
    public String toString() {
    	   return "Poly[ " + points + " lineCol: " + lineColour + " fillCol: " + fillColour + "]";
    }
}
