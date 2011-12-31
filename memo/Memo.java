/*
 * Created on Mar 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ActivePrototype;

import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.*;

/**
 * @author mwelsman
 *
 * Encapsulates a single element ("container") created on the 
 */
public class Memo implements Manipulatable {
    private double width, height;  //width and height of the container
    private double x, y;           //location of the container
    private double orientation;    //orientation in degrees
    private GeneralPath strokes;   //defines the strokes the user has drawn
    private ArrayList links;       //an arralist of memos that this is linked to
    
    private boolean locked;        //when this is set, no new strokes can be added
    private boolean penDown;       //is the stylus being held down over this memo?
    
    /** Construct a basic memo. */
    public Memo( double x, double y, double width, double height, double orientation ) {
    	    this.x = x;
    	    this.y = y;
    	    this.width = width;
    	    this.height = height;
    	    this.orientation = orientation;
    	    
    	    locked = false;
    	    
    	    strokes = new GeneralPath();
    	    links = new ArrayList();
    }
    /** Construct a memo that is automatically linked  to another source. */
    public Memo( double x, double y, double width, double height, double orientation, Memo source ) {
    	    this.x = x;
    	    this.y = y;
	    this.width = width;
	    this.height = height;
	    this.orientation = orientation;
	    
	    locked = false;
	    
	    strokes = new GeneralPath();
	    
	    //Create the link
	    links = new ArrayList();
	    source.linkToThisSource( this );
    }
    
    /** Link this memo to another memo.  The other memo will now display this memo's
     *  strokes. */
    public void linkToThisSource( Memo destination ) {
        links.add( destination );
    }
    /** Draw this memo.  This will be called automatically by the Tabletop class. */
    public void paint( Graphics2D g ) {
    	    //Need to translate the drawing on the memo back into screen coordinates
	    Shape screenPath = strokes.createTransformedShape( AffineTransform.getTranslateInstance( x, y ) );
    	    g.draw( screenPath );
    	    
    	    //Draw the memo boundary
    	    g.setColor( Color.WHITE );
    	    g.fillRect( (int)x - 1, (int)y - 1, (int)width, (int)height );
    	    g.setColor( Color.GRAY );
    	    g.drawRect( (int)x - 1, (int)y - 1, (int)width, (int)height );
    	    g.setColor( Color.BLACK );
    	    g.drawRect( (int)x, (int)y, (int)width, (int)height );
    }
    /** If the user is holding down the mouse, then "pen" is down. */
    public void penDown() {
    	    penDown = true;
    }
    public void penUp() {
    	    penDown = false;
    }
    /** 
     *  First, check to see if the mouse is within bounds. If no, return.
     *  Otherwise, the pen is inside this memo and drawing may happen.
     * 
     *  If the pen is down and it moves, a bit more of the path is drawn.  If not,
     *  don't draw anything, just move the current position. */
    public void penMove( int penX, int penY ) {
    	    //Is the pen in the right area?  If not, just return.
    	    if( penX < x || (penX > x + width ) || penY > y || (penY > penY + height )) return;
    	
    	    //Are we drawing?
    	    if( penDown ) strokes.lineTo( penX, penY );
    	    else strokes.moveTo( (int)(penX - x), (int)(penY - y) ); //coordinates local to the memo
    }
    /** Manipulatable Interface methods */
    public void translate( float dx, float dy ) {
    	    x += dx;
    	    y += dy;
    	}
    public void rotate( float degrees ) {
    	    orientation += degrees;
    	}
    public void scale( float right, float bottom, float left, float top ) {
    	    //remember that the hand scaled out each side individually.
    	    width += left + right;
    	    height += top + bottom;
    	    x -= left;
    	    y -= top;
    	}
    
    /*
     * I chenged one x to xTest, to point difference between variables 
     */	
    
    public boolean contains( float xTest, float yTest ) {
    	    return xTest > x && xTest < x + width && yTest > y && yTest < y + height;
    	}
}
