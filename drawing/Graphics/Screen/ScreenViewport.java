package Graphics.Screen;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created on Jan 21, 2006
 * 
 * @author mwelsman
 * 
 * Represents the viewport at the screen (pixel) level.
 * The viewport works in right hand coordinates, meaning
 * that the origin is in the bottom left-hand corner
 * of the screen.
 */
public class ScreenViewport extends Canvas {
	public static final int DEFAULT_SCREEN_WIDTH = 500;
	public static final int DEFAULT_SCREEN_HEIGHT = 500;
	private ArrayList pixels;  //list of pixels to be "turned on"
	private ScreenPoint target; //where the currentPosition is to be drawn
	
	public ScreenViewport() {
		super();
		this.setSize( DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT );
		pixels = new ArrayList();
		repaint();
		target = new ScreenPoint();
	}
	//Initialize the viewport to some specific size
	public ScreenViewport( int width, int height ) {
		super();
		this.setSize( width, height );
		pixels = new ArrayList();
		repaint();
		target = new ScreenPoint();
	}
	/* Pass in new pixels to be coloured. */
    public void addPixels( ArrayList thePixels ) {
    	    pixels.addAll( thePixels );
    }
    public ArrayList getPixels() {
    	    return pixels;
    }
    /** Change the location of the currentPosition on the screen */
    public void setTarget( ScreenPoint point ) {
    	    //Must convert to left hand coordinates for drawing.
    	    target = new ScreenPoint( point.getX(), this.getHeight() - point.getY() );
    }
    /* Method that redraws the contents of the canvas,
     * which at this level are simply defined as pixels. */
    public void paint( Graphics g ) {
    	    g.setColor( Color.BLACK );
    	    g.fillRect( 0, 0, getWidth(), getHeight() );
        //Draw in pixels that are coloured
    	    g.setColor( Color.WHITE );
	    for( int i = 0; i < pixels.size(); i++ ) {
	    	    if( pixels.get( i ) instanceof ColouredPixel ) { //there is colour data
	    	    	    ColouredPixel pixel = (ColouredPixel)pixels.get( i );
	    	    	    
	    	    	    g.setColor( pixel.getColor() );
	    	    	    setPixel( g, pixel.getX(), pixel.getY() );
	    	    }
	    	    else if( pixels.get( i ) instanceof ScreenPoint ) { //no colour data
	    	    	    if( g.getColor() != Color.WHITE ) g.setColor( Color.WHITE ); //default
	    	    	    
	    	    	    setPixel( g, ((ScreenPoint)pixels.get(i)).getX(), 
	   	    			     ((ScreenPoint)pixels.get(i)).getY());
	    	    }
	    	    	
	    	    
	    }
	    //Draw the "cross-hair" that represents the current position
	    g.setColor( Color.RED );
	    g.drawLine( target.getX() - 2, target.getY(), target.getX() + 2, target.getY() );
	    g.drawLine( target.getX(), target.getY() - 2, target.getX(), target.getY() + 2 );
    }
    /** Add this data to the screen and trigger a repaint */
    public void refresh( ArrayList data ) {
    	    pixels.clear();  //clear the current data
    	    
    	    for( int i = 0; i < data.size(); i++ ) {
    	    	    if( data.get(i) instanceof Drawable ) {
    	    	    	    pixels.addAll( ((Drawable)data.get(i)).pixels() );
    	    	    }
    	    }
    	
    	    repaint();
    }
    
    /* Pixel drawing routine, not provided by Graphics class.
     * Note that this is where the conversion from left hand
     * to right hand coordinates takes place. */
    private void setPixel( Graphics g, int x, int y ) {
    	    g.drawLine( x, this.getHeight() - y, x, this.getHeight() - y );
    }
    /** Clear the screen. */
    public void clear() {
    	    pixels.clear();
    }
}
