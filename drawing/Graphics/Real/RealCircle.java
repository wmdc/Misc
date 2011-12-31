package Graphics.Real;

import java.awt.Color;

/**
 * Created on Mar 12, 2006
 * @author mwelsman
 *
 * Holds data that defines a circle in real world coordinates.
 */
public class RealCircle extends RealPoint implements java.io.Serializable, RealShape {
    private double radius;
    private Color lineColour, fillColour;
    
    /** Create a new circle. */
    public RealCircle( RealPoint position, double radius, Color lineColour ) {
    	    super( position );
    	    this.radius = radius;
    	    this.lineColour = lineColour;
    	    this.fillColour = null;
    }
    /** Construct a new circle with the given colour. */
    public RealCircle( RealPoint position, double radius, Color lineColour, Color fillColour ) {
    	    super( position );
	    this.radius = radius;
	    this.lineColour = lineColour;
	    this.fillColour = fillColour;
    }
    /** Get the circle's radius. */
    public double getRadius() {
    	    return radius;
    }
    /** Get the circle's position. */
    public RealPoint getPosition() {
    	    return (RealPoint)this;
    }
    /** Get the circle's line colour. */
    public Color lineColour() {
    	    return lineColour;
    }
    /** Get the circle's fill colour. */
    public Color fillColour() {
    	    return fillColour;
    }
    /** Set this circle's line colour. */
    public void setLineColour( Color lineColour ) {
    	    this.lineColour = lineColour;
    }
    /** Set this circle's fill colour. */
    public void setFillColour( Color fillColour ) {
    	    this.fillColour = fillColour;
    }

    /** Circle clipping.  Only partially implemented. */
    public RealShape clip( double xMin, double xMax, double yMin, double yMax ) {
    	    //Throw out the circle if it's entirely outside of the viewing area.
    	    //Otherwise, just leave it in unchanged.
    	    if( getX() + radius < xMin || getX() - radius > xMax ||
    		    getY() + radius < yMin || getY() - radius > yMax )
    		    return null;
    	    return this;
    }
    
    /* Translation method - inherit rotation and translation but scale involves radius. */
    /** Scale the circle about a given point.  This means that the position is scaled
     *  as well as the radius are scaled.
     * 
     *  If a circle is scaled about the centre, only the radius will be affected.
     * 
     *  A circle has only one value that can be scaled (radius), so take the average
     *  of the x and y factors.  For proper circle scaling, xFactor = yFactor = rFactor. */
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
    	   super.scale( xFactor, yFactor, aboutPoint ); //scale the point
    	   radius *=  ( xFactor + yFactor ) / 2;
    }
    
    public String toString() {
    	    return "Circ[ r= " + radius + " pos= " + super.toString() + " lineColour= " + lineColour + " fillColour= " + fillColour + "]";
    }
}
