package Graphics.Real;

/** An object to store region codes used for clipping.  Used for points, line segments, and polygons. */
public class RegionCode {
	//Refer to "bits" by name for clarity
	public static final int LEFT = 0;
	public static final int ABOVE = 1;
	public static final int RIGHT = 2;
	public static final int BELOW = 3;
	public static final int NUM_REGION_CODES = 4;
	
	private boolean[] code;  //the actual codes
	/** Constructors */
	public RegionCode() {
		code = new boolean[4];
	}
	public RegionCode( boolean left, boolean above, boolean right, boolean below ) {
		code = new boolean[4];
		
		code[ LEFT ] = left;
		code[ RIGHT ] = right;
		code[ ABOVE ] = above;
		code[ BELOW ] = below;
	}
	/** Generate the region codes for a point.  This is the "encode" method. */
	public RegionCode( RealPoint p, double xMin, double xMax, double yMin, double yMax  ) {
		code = new boolean[4];
		
		if( p.getX() < xMin ) code[ LEFT ] = true;
		if( p.getX() > xMax ) code[ RIGHT ] = true;
		if( p.getY() < yMin ) code[ BELOW ] = true;
		if( p.getY() > yMax ) code[ ABOVE ] = true;
	}
	/** Get region code values (use constants such as RegionCode.BELOW). */
	public boolean getCode( int rc ) {
		return code[ rc ];
	}
	/** Return TRUE if it is inside the world window and FALSE otherwise. */
	public boolean insideViewport() {
		return !code[ LEFT ] && !code[ RIGHT ] && !code[ ABOVE ] && !code[ BELOW ];
	}
	/** How many region code bits? */
	public int size() {
		return NUM_REGION_CODES;
	}
	
	public String toString() {
		return "RegionCode[" + "left=" + code[LEFT] + " right=" + code[RIGHT]
		                     + "above=" + code[ABOVE] + " below=" + code[BELOW] +"]";
	}
}