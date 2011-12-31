package Graphics.Real;

/**
 * Created on Jan 22, 2006
 * @author mwelsman
 *
 * Encapsulates information about the viewport in real world coordinates.
 */
public class RealViewingArea {
	//Constants to define the default viewing area size and location
	public static final int DEFAULT_VIEWING_AREA_WIDTH = 20;
	public static final int DEFAULT_VIEWING_AREA_HEIGHT = 20;
	public static final int DEFAULT_VIEWING_AREA_X = -10;
	public static final int DEFAULT_VIEWING_AREA_Y = -10;
	//Constants to define the step sizes when not setting the viewport
	//location or zoom to an absolute value.
	public static final double VIEWING_AREA_PAN_STEP = 0.1;
	public static final double VIEWING_AREA_ZOOM_STEP = 0.9;
	//Constants used as arguments to the pan by default step
	//method translate()
	public static final int VIEW_DIRECTION_UP = 1;
	public static final int VIEW_DIRECTION_DOWN = 2;
	public static final int VIEW_DIRECTION_LEFT = 3;
	public static final int VIEW_DIRECTION_RIGHT = 4;
	
    private double x, y;  //where is the bottom left of this viewport located?
    private double width, height;  //how large is the viewport?

    /** Construct a new viewing area with defaults. */
    public RealViewingArea() {
    	    x = DEFAULT_VIEWING_AREA_X;
    	    y = DEFAULT_VIEWING_AREA_Y;
    	    width = DEFAULT_VIEWING_AREA_WIDTH;
    	    height = DEFAULT_VIEWING_AREA_HEIGHT;
    }
    /** Construct a new viewing area with the given parameters. */
    public RealViewingArea( double x, double y, double width, double height ) {
    	    this.x = x;
    	    this.y = y;
    	    this.width = width;
    	    this.height = height;
    }
    /** Move the viewport to the given location. */
    public void setLocation( double newX, double newY ) {
    	    x = newX;
    	    y = newY;
    }
    /** Translate the viewport by the given amounts. */
    public void translate( double deltaX, double deltaY ) {
    	    x += deltaX;
    	    y += deltaY;
    }
    /** Translate the viewport in the given direction by one step size,
     *  which is a fraction of the viewport size.
     *  Directions are defined by constants like VIEW_DIRECTION_UP. */
    public void translate( int direction ) {
    	    if( direction == VIEW_DIRECTION_UP )
    	    	    y += y * VIEWING_AREA_PAN_STEP;
    	    else if( direction == VIEW_DIRECTION_DOWN )
    	    	    y -= y * VIEWING_AREA_PAN_STEP;
    	    else if( direction == VIEW_DIRECTION_LEFT ) 
    	    	    x -= x * VIEWING_AREA_PAN_STEP;
    	    else if( direction == VIEW_DIRECTION_RIGHT ) 
    	    	    x += x * VIEWING_AREA_PAN_STEP;
    }
    /** Set the viewport size to the given amount. */
    public void setSize( double newWidth, double newHeight ) {
    	    width = newWidth;
    	    height = newHeight;
    }
    /** Change the viewing area size by some factor. */
    public void zoom( double factor ) {
    	    double newWidth = width * factor;
    	    double newHeight = height * factor;
    	    //Make sure that the screen remains centred on the same point as
    	    //before.
    	    translate( ( width - newWidth ) / 2, ( height - newHeight ) / 2);
    	    //Change the size of the viewing area.
    	    width = newWidth;
    	    height = newHeight;
    }
    //Accessor methods
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public RealPoint getPosition() { return new RealPoint( x, y ); }
    public double getX() { return x; }
    public double getY() { return y; }
}
