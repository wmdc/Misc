package Graphics.Real;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * Represents a string of characters, defined by RealCharacters.
 * Characters can be added or removed.
 */
public class RealString implements java.io.Serializable, RealShape {
    private String text;
    private RealPoint position;
    private Color lineColour;
    private Group characters;
    
    public RealString( String text, RealPoint where, Color lineColour ) {
    	    this.text = text;
    	    this.position = where;
    	    this.lineColour = lineColour;
    	    
    	    characters = new Group();
    	    
    	    //Build the character set...
    	    if( !RealCharacter.initialized() )  //initialize the char set if it hasn't been
    	    	    RealCharacter.initialize();
    	    
    	    for( int i = 0; i < text.length(); i++ ) {
    	    	    char cur = text.charAt( i );
    	    	    //Build a character
    	    	    RealCharacter curChar = new RealCharacter( 
    	    	    		cur, 
    	    	    		position.getX() + i * 
				RealCharacter.DEFAULT_WIDTH_WITH_PADDING, 
                 position.getY(), 
				lineColour );
    	    	    characters.add( curChar );
    	    }
    }
    /** Get all line segments that make up this RealString. */
    public Group getSegments() {
    	    Group result = new Group();
    	    
    	    for( int i = 0; i < characters.size(); i++ ) {
    	    	    RealCharacter cur = (RealCharacter)characters.get(i);
    	    	    result.addAll( cur.getSegments() );
    	    }
    	    
    	    return result;
    }
    
    /** Get the RealCharacters that make up this RealString */
    public Group getCharacters() {
    	    return characters;
    }
    /* Accessor methods */
    public RealPoint getPosition() { return position; }
    public double getX() { return position.getX(); }
    public double getY() { return position.getY(); }
    public String getText() { return text; }
    public Color lineColour() { return lineColour; }
    public Color fillColour() { return null; } //undefined
    /** Set the line colour of this string object and of all characters. */
    public void setLineColour( Color c ) {
	    lineColour = c;
	    
	    for( int i = 0; i < characters.size(); i++ ) {
	    	    RealCharacter cur = (RealCharacter)characters.get(i);
	    	    cur.setLineColour( c );
	    }
    }
    public void setFillColour( Color c ) { }
    
    /** Build a new RealString with characters inserted. */
    public void changeCharacter( int index, char c ) {
    	    //Rebuild the string with the inserted character
    	    char[] chars = text.toCharArray();
    	    chars[ index ] = c;
    	    text = new String( chars );
    	    //Rebuild the characcter
    	    characters = new Group();
    	    for( int i = 0; i < text.length(); i++ ) {
	    	    char cur = text.charAt( i );
	    	    //Build a character
	    	    RealCharacter curChar = new RealCharacter( cur, 0, 0, lineColour );
	    	    characters.add( curChar );
	    }
    }
    /** Build a new RealString with characters removed. */
    public RealString removeSubstring( int start, int end ) {
    	    text = text.substring( 0, start ) + text.substring( end, text.length() - 1 );
    	    return new RealString( text, position, lineColour );
    }
    
    /* Transformation methods -> simply transform the characters. */
    public void scale( double xFactor, double yFactor, RealPoint aboutPoint ) {
    	    for( int i = 0; i < characters.size(); i++ ) {
    	    	    RealCharacter cur = (RealCharacter)characters.get(i);
    	    	    cur.scale( xFactor, yFactor, aboutPoint );
    	    }
    }
    public void rotate( double theta, RealPoint aboutPoint ) {
    	    for( int i = 0; i < characters.size(); i++ ) {
    	        RealCharacter cur = (RealCharacter)characters.get(i);
    	        cur.rotate( theta, aboutPoint );
        }
    }
    public void translate( RealPoint delta ) {
    	    for( int i = 0; i < characters.size(); i++ ) {
    	        RealCharacter cur = (RealCharacter)characters.get(i);
    	        cur.translate( delta );
        }
    }
    
    /** Clip the string, i.e. just clip each character */
    public RealShape clip( double xMin, double xMax, double yMin, double yMax ) {
    	    Group clippedChars = new Group();
	    for( int i = 0; i < characters.size(); i++ ) {
	    	    RealCharacter cur = (RealCharacter)characters.get(i);
	    	    
	    	    RealShape clippedData = cur.clip( xMin, xMax, yMin, yMax );
	    	    
	    	    if( clippedData != null ) {
	    	    	    Group clippedGroup = (Group)clippedData;
	    	    	    clippedChars.addAll( clippedGroup );
	    	    }
	    }
	    return clippedChars;
    }
    /** Print out the values contained by this string. */
    public String toString() {
    	    return "RealString[" + "text=" + text + " position=" + position + " lineCol=" + lineColour + "]";
    }
}
