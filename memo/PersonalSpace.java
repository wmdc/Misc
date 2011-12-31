package ActivePrototype;

import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.*;

/**
 * Created on Mar 16, 2006
 * @author mwelsman
 *
 * Encapsulates a single personal editing area, of which we will only have one in the demo.
 * This will maintain a position, side, and orientation, and will also include a toolbar
 * that can be clicked on to change the stylus mode.
 */
public class PersonalSpace implements Manipulatable{
	private double width, height, maxWidth, maxHeight, minWidth, minHeight;  //width and height of the personal space
    private double x, y;           //location of the personal space of user
    private GeneralPath strokes;   //defines the strokes the user has drawn
    private double orientation;    //orientation in degrees
    private ArrayList links;       //an arralist of personal spaces that this is linked to
    
    private boolean locked;        //when this is set, no new strokes can be added
    private boolean penDown;       //is the stylus being held down over this personal space?
	private boolean maxWidthE;
	private boolean maxHeightE;
	private boolean minWidthE;
	private boolean minHeightE;
	
	
    /** Construct a personal space. */
    public PersonalSpace( double x, double y, double width, double height, double orientation ) {
    	    this.x = x;
    	    this.y = y;
    	    this.width = width;
    	    this.height = height;
    	    this.orientation = orientation;
    	    
    	    locked = false;
    	    
    	    strokes = new GeneralPath();
    	    links = new ArrayList();
    }
    
    /** Draw this personal space.*/
    public void paint( Graphics2D g ) {
    	    //Need to translate the drawing on the personal space back into screen coordinates
	    Shape screenPath = strokes.createTransformedShape( AffineTransform.getTranslateInstance( x, y ) );
    	    g.draw( screenPath );
    	    
    	    //Draw the personalspace
    	    g.setColor( Color.black );
    	    g.fillRect( (int)x - 4, (int)y - 4, (int)width+7, (int)height+7 );
    	    g.setColor( Color.lightGray );
    	    g.fillRect( (int)x - 1, (int)y - 1, (int)width, (int)height );
    	    g.setColor( Color.GRAY );
    	    g.drawRect( (int)x - 1, (int)y - 1, (int)width, (int)height );
    	    g.setColor( Color.black );
    	    g.fillRect( (int)x-2, (int)y-2, 24, 24 );
    	    g.setColor( Color.red );
    	    g.fillRect( (int)x, (int)y, 20, 20 );
    	        	    
    	    //g.drawRect( (int)x, (int)y, (int)width, (int)height );
    }
    
    
    //TD:dont translate if lower & right bound reached
    public void translate( float dx, float dy ) {
    	if (x+dx > 0){
        	x += dx;
        }
   	    if (y+dy > 0){
   	    	y += dy;
   	    }
   	}
   	
   	public void scale( float right, float bottom, float left, float top ) {
    	if (right > -1 && bottom > -1 && left > -1 && top > -1){ //Checks max 
    		maxWidth = 750;
    		maxHeight = 400;
    		minWidth = 500;
    		minHeight = 200;
    		if (maxWidth < width){
    			maxWidthE = false;
    		}
    		else{
    			maxWidthE = true;
    		}
    		if (maxWidthE || (left < 0) || (right < 0)){
    			width += left + right;
    			x -= left;
    		}
    		if (maxHeight < height){
    			maxHeightE = false;
    		}
    		else{
    			maxHeightE = true;
    		}
    		if (maxHeightE){
    			height += top + bottom;
    			y -= top;
    		}
    	}
    	else{		//Checks minHigth & min Width
    		if (minWidth < (width+left+right)){
    			width += left + right;
    			x -= left;
    		}
    		if (maxHeight < (height+top+bottom)){
    			height += top + bottom;
    			y -= top;
    		}
    	}
    	//****************
    	/*if (minWidth > width){
    		minWidthE = false;
    	}
    	else{
    		minWidthE = true;
    	}
    	if (minWidthE || (left < 0) || (right > 0)){
    		width += left + right;
    		x -= left;
    	}
    	
    	if (minHeight > height){
    		minHeightE = false;
    	}
    	else{
    		minHeightE = true;
    	}
    	if (minHeightE){
    		height += top + bottom;
    		y -= top;
    	}*/    	
    	
    	//width += left + right;
    	//height += top + bottom;
    	//x -= left;
    	//y -= top;
    }
    
    public boolean contains( float xTest, float yTest ) {
    	//return xTest > x && xTest < x + width && yTest > y && yTest < y + height;
        return xTest > x && xTest < x + 20 && yTest > y && yTest < y + 20; 
    }    
    
    public void rotate( float degrees ) {
    	orientation += degrees;
    }

}
