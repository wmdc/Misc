package Graphics.Screen;

import java.awt.Color;

/**
 * Created on March 12, 2006.
 * @author mwelsman
 *
 * Pixel class that allows a colour to be set.
 */
public class ColouredPixel extends ScreenPoint {
    private Color c;
    /** Construct a new pixel with the given colour. */
    public ColouredPixel( int x, int y, Color c ) {
    	    super( x, y );
    	    this.c = c;
    }
    
    public void setColor( Color c ) {
    	    this.c = c;
    }
    public Color getColor() { 
    	    return c;
    }
}
