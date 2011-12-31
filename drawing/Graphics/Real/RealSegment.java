package Graphics.Real;
import java.awt.Color;

/**
 * Created on Jan 21, 2006
 * 
 * @author mwelsman
 *
 * Represents a line segment with two end points given in
 * world coordinates.
 */
public class RealSegment implements java.io.Serializable, RealShape {
    private RealPoint start, end;
    private double slope;
    private Color lineColour;
    
    /** Construct a new RealSegment with the given endpoints. */
    public RealSegment( RealPoint start, RealPoint end, Color lineColour ) {
    	    this.start = start;
    	    this.end = end;
    	    this.lineColour = lineColour;
    	    
    	    //precompute the slope
    	    this.slope = ((double)( start.getY() - end.getY()))/( start.getX() - end.getX() );
    }
    /** Clone another RealSegment. */
    public RealSegment( RealSegment original, Color lineColour ) {
	    this.start = new RealPoint( original.getStart().getX(), original.getStart().getY() );
	    this.end = new RealPoint( original.getEnd().getX(), original.getEnd().getY() );
	    this.lineColour = lineColour;
	    
	    this.slope = ((double)( start.getY() - end.getY()))/( start.getX() - end.getX() );
    }
    /** Construct a RealSegment from endpoints given as primitives. */
    public RealSegment( double startX, double startY, double endX, double endY, Color lineColour ) {
    	    this.start = new RealPoint( startX, startY );
    	    this.end = new RealPoint( endX, endY );
    	    this.lineColour = lineColour;
			
    	    this.slope = ((double)( start.getY() - end.getY()))/( start.getX() - end.getX() );
    }
    /* Get the endpoints of the line segment */
    public RealPoint getStart() { return start; }
    public RealPoint getEnd() { return end; }
    
    public Color lineColour() { return lineColour; }
    public Color fillColour() { return null; }  //undefined
    
    public void setLineColour( Color c ) {
	    lineColour = c;
    }
    public void setFillColour( Color c ) { }
    

    /* Transformation methods. */
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
    	    start.scale( xFactor, yFactor, aboutPoint );
    	    end.scale( xFactor, yFactor, aboutPoint );
    }
    public void rotate( double theta, RealPoint aboutPoint ) {
        start.rotate( theta, aboutPoint );
        end.rotate( theta, aboutPoint );
    }
    public void translate( RealPoint delta ) {
    	    start.translate( delta );
    	    end.translate( delta );
    }
    
	/** Clip this line to the given viewing area.
	 *  Cohen-Sutherland line clipping algorithm. */
	public RealShape clip(  double xMin, double xMax, double yMin, double yMax ) {
		if( accept( xMin, xMax, yMin, yMax ) )
			return this; //doesn't need to be clipped
		else if( reject( xMin, xMax, yMin, yMax ))
			return null; //doesn't appear at all
		else {  //Can't trivial accept or reject.  Begin actual clipping.
			double x1 = start.getX();
			double y1 = start.getY();
			double x2 = end.getX();
			double y2 = end.getY();
			RegionCode c1 = new RegionCode( start, xMin, xMax, yMin, yMax );
			RegionCode c2 = new RegionCode( end, xMin, xMax, yMin, yMax );
			
			for( int i = 0; i < 2; i++ ) {
				//Do both points
			    if( c1.getCode( RegionCode.LEFT )) { //outside to the left
			    	    y1 += ( xMin - x1 ) * slope;
			    	    x1 = xMin;
			    }
			    else if( c1.getCode( RegionCode.RIGHT )) {
			        	y1 += ( xMax - x1 ) * slope;
			    	    x1 = xMax;
			    }
			    else if( c1.getCode( RegionCode.ABOVE )) {
			    	    y1 = yMax;
			    	    x1 += ( yMax - y1 ) * slope;
			    }
			    else if( c1.getCode( RegionCode.BELOW )) {
			    	    y1 = yMin;
			    	    x1 += ( yMin - y1 ) * slope;
			    }
			    
			    if( c2.getCode( RegionCode.LEFT )) { //outside to the left
		    	        y2 += ( xMin - x2 ) * slope;
		    	        x2 = xMin;
		        }
		        else if( c2.getCode( RegionCode.RIGHT )) {
		            	y2 += ( xMax - x2 ) * slope;
		    	        x2 = xMax;
		        } 
		        else if( c2.getCode( RegionCode.ABOVE )) {
		    	        y2 = yMax;
		    	        x2 += ( yMax - y2 ) * slope;
		        }
		        else if( c2.getCode( RegionCode.BELOW )) {
		    	        y2 = yMin;
		    	        x2 += ( yMin - 21 ) * slope;
		        }
			}
			
			RealPoint newStart = new RealPoint( x1, y1 );
			RealPoint newEnd = new RealPoint( x2, y2 );
			
			return new RealSegment( newStart, newEnd, lineColour() ); //the clipped line
		}
	}
	/* Line clipper helper methods */
	/** Return true if and only if the point with this region code can be trivially accepted
	 *  within the given clipping area. */
	private boolean accept( double xMin, double xMax, double yMin, double yMax ) {
		RegionCode c1 = new RegionCode(  start, xMin, xMax, yMin, yMax  );
		RegionCode c2 = new RegionCode(  end, xMin, xMax, yMin, yMax  );
		
		if( c1.insideViewport() && c2.insideViewport() )
		    return true; //trivial accept
		return false; //can't trivially accept
	}
	/** Return true if and only if the point with this region code can be trivially rejected
	 *  within the given clipping area. */
	private boolean reject( double xMin, double xMax, double yMin, double yMax ) {
		RegionCode c1 = new RegionCode(  start, xMin, xMax, yMin, yMax  );
		RegionCode c2 = new RegionCode(  end, xMin, xMax, yMin, yMax  );
		
		for( int i = 0; i < c1.size(); i++ ) {
			if( c1.getCode( i ) && c2.getCode( i ) ) return true; //trivially reject
		}
		
		return false; //can't trivially reject
	}
	/** Do we need to swap?  i.e. if there is a single point outside, is it c1?  */
	private boolean swapNeeded( double xMin, double xMax, double yMin, double yMax ) {
		RegionCode c1 = new RegionCode( start, xMin, xMax, yMin, yMax );
	    RegionCode c2 = new RegionCode( end, xMin, xMax, yMin, yMax );
	    
	    if( c1.insideViewport() && !c2.insideViewport() ) return true;
	    else return false;
	}
	
    public String toString() {
	    return "Seg[" + start + ", " + end + "]";
    }
}
