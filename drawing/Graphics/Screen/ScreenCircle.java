package Graphics.Screen;

import java.util.ArrayList;
import java.awt.Color;

/**
 * Created on March 10, 2006
 * @author mwelsman
 *
 * Represents a circle as it is drawn on the screen.  Includes the Bresenham circle algorithm
 * which is used to determine which pixels the circle intersects.
 */
public class ScreenCircle implements Drawable {
    private ScreenPoint centre;
    private int radius;
    private Color lineColour, fillColour;
    /** Construct a new ScreenCircle at the given location on the screen
     *  with the given radius.  Screen coordinates. */
    public ScreenCircle( ScreenPoint location, int radius ) {
    	    this.centre = location;
    	    this.radius = radius;
    	    lineColour = Color.WHITE;
    	    fillColour = null;
    }
    /** Construct a circle with the given colour. */
    public ScreenCircle( ScreenPoint location, int radius, Color lineColour, Color fillColour ) {
	    this.centre = location;
	    this.radius = radius;
	    this.lineColour = lineColour;
	    this.fillColour = fillColour;
    }
    /** Return the pixels that must be drawn to draw this circle.
     *  This is the Bresenham circle implementation.  */
    public ArrayList pixels() {
    	    ArrayList result = new ArrayList();
    	    
    	    //Consider only 1/8 of the circle's radius, or 45 degrees.
    	    //Pass this point off to addCirclePixels, which adds in the
    	    //other points (circle is symmetric).
    	    int x = 0;
    	    int y = radius;
    	    double d = 5.0 / ( 4.0 - radius );
    	    
    	    addCirclePixels( new ScreenPoint( x, y ), result );  //draw in the first pixel
    	    
    	    while( y > x ) {
    	    	    if( d < 0 )
    	    	    	    d += 2.0 * x + 3.0;
    	    	    else {
    	    	    	    d += 2.0 * ( x - y ) + 5.0;
    	    	    	    y--;
    	    	    }
    	    	    x++;
    	    	    addCirclePixels( new ScreenPoint( x, y ), result );
    	    }
    	    return result;
    }
    /* Take advantage of circle symmetry. 
     * This method also translates the points as they are created so we
     * don't have to do it again later.
     * Note: don't call this when X == Y. */
    private void addCirclePixels( ScreenPoint where, ArrayList container ) {
    	    //Also include the current colour.
    	    container.add( new ColouredPixel(  where.getX()  + centre.getX(),  where.getY()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel(  where.getY()  + centre.getX(),  where.getX()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel(  where.getY()  + centre.getX(), -where.getX()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel(  where.getX()  + centre.getX(), -where.getY()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel( -where.getX()  + centre.getX(), -where.getY()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel( -where.getY()  + centre.getX(), -where.getX()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel( -where.getY()  + centre.getX(),  where.getX()  + centre.getY(), lineColour ));
    	    container.add( new ColouredPixel( -where.getX()  + centre.getX(),  where.getY()  + centre.getY(), lineColour ));
    }
    /* Accessor methods */
    public int getRadius() { return radius; }
    public int getX() { return centre.getX(); }
    public int getY() { return centre.getY(); }
    public Color getLineColour() { return lineColour; }
    public Color getFillColour() { return fillColour; }
}
