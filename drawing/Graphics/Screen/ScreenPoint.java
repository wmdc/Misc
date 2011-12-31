package Graphics.Screen;

import Graphics.Real.*;
import java.util.ArrayList;

/**
 * Created on Jan 21, 2006
 * 
 * @author mwelsman
 *
 * Represents a point in screen coordinates.  This is also where a point in real coordinates is converted to
 * a point in screen coordinates.
 */
public class ScreenPoint implements Drawable {
    private int x, y;
    
    public ScreenPoint() {
    	    x = 0;
    	    y = 0;
    }
    public ScreenPoint( int x, int y ) {
    	    this.x = x;
    	    this.y = y;
    }
    /** Construct a ScreenPoint from a point in real world coordinates. */
    public ScreenPoint( RealPoint real, RealViewingArea worldView, ScreenViewport screen ) {
    	    //Simply scale the new point by the scale factor, which depends on the dimensions of the world
    	    //area that we can see and the number of pixels we have on the screen
    	    this.x = (int)Math.round(( real.getX() - worldView.getX()) * screen.getWidth() / worldView.getWidth() );
        this.y = (int)Math.round(( real.getY() - worldView.getY()) * screen.getHeight() / worldView.getHeight() );
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public void setX( int x ) { this.x = x; }
    public void setY( int y ) { this.y = y; }
    
    public ArrayList pixels() {
    	    ArrayList pixels = new ArrayList(1);
        pixels.add( this );
    	    return pixels;
    }
    
    public boolean equals( Object o ) {
        if( !( o instanceof ScreenPoint ) ) 
        	   return false;
        ScreenPoint p = (ScreenPoint)o;
    	    return x == p.getX() && y == p.getY();
    }
    
    public String toString() {
    	    return "[" + x + ", " + y + "]";
    }
}
