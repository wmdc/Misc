package Graphics.Real;

import java.util.ArrayList;
import java.awt.Color;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * Encapsulates a single character.  Also includes the definition of line-stroke
 * characters.  Implementation may change in the future.
 */
public class RealCharacter implements java.io.Serializable, RealShape {
	public static final double DEFAULT_CHARACTER_WIDTH = 1.5;
	public static final double DEFAULT_WIDTH_WITH_PADDING = 2;
	public static final double DEFAULT_CHARACTER_HEIGHT = 2;
	
	public static boolean initialized = false;
	
    private char character;      //the character represented, 'a', 'b', etc.
    private ArrayList segments;  //the segments that make up the character
    
    private Color lineColour;
    
    private static ArrayList[] charDefinitions; //character definitions.
    
    /** Construct a character object from a given char located at the
     *  given position in world coordinates. */
    public RealCharacter( char character, double x, double y, Color lineColour ) {
    	    if( !RealCharacter.initialized() ) RealCharacter.initialize();
    	    this.character = character;
    	    segments = charDefinitions[character];
    	    
    	    for( int i = 0; i < segments.size(); i++ ) {
    	    	    RealSegment cur = (RealSegment)segments.get(i);
    	    	    cur.translate( new RealPoint( x, y) );
    	    	    cur.setLineColour( lineColour );
    	    }

    	    this.lineColour = lineColour;
    }
    /** Return true IFF the char set has been built */
    public static boolean initialized() {
    	    return initialized;
    }
    
    /** Build the character list.  This must be called before constructing a character
     *  so that the line segments can be retrieved. */
    public static void initialize() {
    	    initialized = true;
    	    charDefinitions = new ArrayList[ 256 ];
    	    
    	    ArrayList charA = new ArrayList();
    	    charA.add( new RealSegment( 0, 0, 0, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charA.add( new RealSegment( DEFAULT_CHARACTER_WIDTH, 0, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charA.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charA.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT / 2, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT / 2, Color.WHITE ) );
    	    charDefinitions['A'] = charA;

    	    ArrayList charB = new ArrayList();
    	    charB.add( new RealSegment( 0, 0, 0, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charB.add( new RealSegment( DEFAULT_CHARACTER_WIDTH, 0, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charB.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charB.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT / 2, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT / 2, Color.WHITE ) );
    	    charB.add( new RealSegment( 0, 0, DEFAULT_CHARACTER_WIDTH, 0 , Color.WHITE ));
    	    charDefinitions['B'] = charB;
    	    
    	    ArrayList charC = new ArrayList();
    	    charC.add( new RealSegment( 0, 0, 0, DEFAULT_CHARACTER_HEIGHT , Color.WHITE) );
    	    charC.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charC.add( new RealSegment( 0, 0, DEFAULT_CHARACTER_WIDTH, 0, Color.WHITE ) );
    	    charDefinitions['C'] = charC;
    	    
    	    ArrayList charD = new ArrayList();
    	    charD.add( new RealSegment( 0, 0, 0, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charD.add( new RealSegment( DEFAULT_CHARACTER_WIDTH, 0, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charD.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charD.add( new RealSegment( 0, 0, DEFAULT_CHARACTER_WIDTH, 0, Color.WHITE ) );
    	    charDefinitions['D'] = charD;
    	    
    	    ArrayList charE = new ArrayList();
    	    charE.add( new RealSegment( 0, 0, 0, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charE.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charE.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT / 2, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT / 2, Color.WHITE ) );
    	    charE.add( new RealSegment( 0, 0, DEFAULT_CHARACTER_WIDTH, 0, Color.WHITE ) );
    	    charDefinitions['E'] = charE;
    	    
    	    ArrayList charF = new ArrayList();
    	    charF.add( new RealSegment( 0, 0, 0, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charF.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT, Color.WHITE ) );
    	    charF.add( new RealSegment( 0, DEFAULT_CHARACTER_HEIGHT / 2, DEFAULT_CHARACTER_WIDTH, DEFAULT_CHARACTER_HEIGHT / 2, Color.WHITE ) );
    	    charDefinitions['F'] = charF;
    }
    
    /** Return the list of segments that make up this character.  These are converted to real world coordinates
     *  based on the location of the character.
     * 
     *  For invalid characters, an empty set of segments is returned so this should not cause errors. */
    public ArrayList getSegments() {
    	    ArrayList result = new ArrayList();
    	    
    	    if( segments == null ) return result; //empty
    	    
    	    for( int i = 0; i < segments.size(); i++ ) {
    	    	    RealSegment seg = new RealSegment( (RealSegment)segments.get(i), lineColour );
    	    	    result.add( seg );
    	    }

    	    return result;
    }
    
    public Color lineColour() { return lineColour; }
    public Color fillColour() { return null; }  //only lines
    
    public void setLineColour( Color c ) {
    	    lineColour = c;
    	    
    	    for( int i = 0; i < segments.size(); i++ ) {
    	    	    RealSegment cur = (RealSegment)segments.get(i);
    	    	    cur.setLineColour( c );
    	    }
    }
    public void setFillColour( Color c ) { }
    
    /* Transformation methods -> just transform the segments */
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
    	    if( segments == null ) return;
       	for( int i = 0; i < segments.size(); i++ )
    	        ((RealSegment)(segments.get(i))).scale( xFactor, yFactor, aboutPoint );
    }
    public void rotate( double theta, RealPoint aboutPoint ) {
    	    if( segments == null ) return;
    	    for( int i = 0; i < segments.size(); i++ )
	        ((RealSegment)(segments.get(i))).rotate( theta, aboutPoint );
    }
    public void translate( RealPoint delta ) {
      	for( int i = 0; i < segments.size(); i++ ) {
    	        RealSegment cur = (RealSegment)segments.get(i);
    	        cur.translate( new RealPoint( delta.getX(), delta.getY() ) );
        }
    }
    
    /** Clip the character -> just pass the segments to the line clipper. */
    public RealShape clip( double xMin, double xMax, double yMin, double yMax ) {
    	    Group charSegs = new Group();
    	    for( int i = 0; i < segments.size(); i++ )
    	    	    charSegs.add( ((RealSegment)segments.get(i)).clip( xMin, xMax, yMin, yMax ) );
    	    return charSegs;
    }
    
    public String toString() {
    	    return "Char[" + character + " " + segments;
    }
}
