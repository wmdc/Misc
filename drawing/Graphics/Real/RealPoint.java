package Graphics.Real;

import java.awt.Color;

/**
 * Created on Jan 21, 2006
 * 
 * @author mwelsman
 *
 * Represents a point in world coordinates.
 * A RealPoint with a null colour is not displayed on the screen, and is probably
 * just a piece of a larger data structure.
 */
public class RealPoint implements java.io.Serializable, RealShape {
    private double x, y;
    private Color colour;
    
    public RealPoint( double x, double y ) {
    	    this.x = x;
    	    this.y = y;
    	    colour = null;
    }
    public RealPoint() {
	    this.x = 0;
	    this.y = 0;
	    colour = null;
    }
    public RealPoint( RealPoint orig ) {
	    this.x = orig.getX();
	    this.y = orig.getY();
	    colour = null;
    }
    public RealPoint( double x, double y, Color colour ) {
    	    this.x = x;
    	    this.y = y;
    	    this.colour = colour;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public Color lineColour() { return colour; }
    public Color fillColour() { return null; }   //fill undefined for a point
    public void setLineColour( Color c ) {
	    colour = c;
    }
    public void setFillColour( Color c ) { }
    
    public void setX( double x ) { this.x = x; }
    public void setY( double y ) { this.y = y; }
    /* Transformation methods*/
    public void translate( double deltaX, double deltaY ) { 
    	    x += deltaX; y += deltaY; 
    	}
    public void translate( RealPoint delta ) {
    	    x += delta.getX(); y += delta.getY(); 
    	}
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
    	    //Translate to the point about which is point will be scaled
    	    RealPoint negPoint = new RealPoint( -aboutPoint.getX(), -aboutPoint.getY() );
    	    translate( negPoint );
    	    
    	    //Apply the scaling
    	    x *= xFactor;
    	    y *= yFactor;
    	    
    	    //Translate back
    	    translate( aboutPoint );
    }
    /** Rotate this point about the given location. */
    public void rotate( double theta, RealPoint aboutPoint ) {
    	    x -= aboutPoint.getX();  //use aboutPoint as origin
    	    y -= aboutPoint.getY();
    	
	    double xNew = Math.cos( Math.toRadians( theta ) ) * x - Math.sin( Math.toRadians( theta ) ) * y;
	    double yNew = Math.sin( Math.toRadians( theta ) ) * x + Math.cos( Math.toRadians( theta ) ) * y;
	    
	    x = xNew + aboutPoint.getX();  //translate back
	    y = yNew + aboutPoint.getY();
    }
    
    /** Clipping of a point (trivial). */
    public RealShape clip( double xMin, double xMax, double yMin, double yMax ) {
    	    if( x < xMin || x > xMax || y < yMin || y > yMax )
    	        return null;
    	    else
    	        return this;
    }
    
    public String toString() {
    	    return "[" + x + ", " + y + "]";
    }
}
