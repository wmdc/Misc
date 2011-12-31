package Graphics.Real;

import java.util.ArrayList;
import java.io.*;
import Graphics.Screen.*;

import java.awt.Color;

/**
 * Created on Jan 22, 2006
 * 
 * @author mwelsman
 *
 * Represents a 2-D "world" containing objects in real coordinates.
 */
public class RealWorld {
	//Kinds of messages for listeners
	public static final int CONTENTS_CHANGED = 0;
	public static final int FILL_COL_CHANGED = 1;
	public static final int LINE_COL_CHANGED = 2;
	public static final int CURPOS_CHANGED = 3;
	public static final int VIEWPORT_CHANGED = 4;
	
    private Group contents;   //everything in the world
    private Group selection;  //everything selected in the world
    private Group listeners;  //objects to be updated when world changes
    
    private RealPoint currentPosition;
    private ScreenViewport screen;    //the actual canvas on the screen
    private RealViewingArea viewArea; //a helper class to keep track of world size
    
    private boolean dirty;    //the "dirty" bit
    
    private Color lineColour, fillColour; //the current colours
    private boolean fill;  //should filling be on for shapes that are drawn?
    
    private double xMin, xMax, yMin, yMax; //clipping plane values
    
    public RealWorld( ScreenViewport screen ) { 
    	    contents = new Group();  //empty data structure
    	    selection = new Group();
    	    listeners = new Group();
    	    this.screen = screen;
    	    viewArea = new RealViewingArea();
    	    currentPosition = new RealPoint();
    	    dirty = false; //no changes made yet
    	    fill = true;
    	    lineColour = Color.WHITE;
    	    fillColour = Color.WHITE;
    	    
    	    restoreClipping();
    	    
    	    redraw();
    }
    /** Used to re-initialize some world window params and to set the current position
     *  back to the origin. */
    public void init() {
    	    viewArea = new RealViewingArea();  //new defaults
    	    moveCurrentPositionAbs( 0, 0 ); //this triggers redraws, etc.
    	    restoreClipping();
    	    redraw();
    }
    /** Move the clipping edges to arbitrary locations.  Useful for testing. */
    public void moveClipping( double xMin, double xMax, double yMin, double yMax ) {
    	    this.xMin = xMin;
    	    this.xMax = xMax;
    	    this.yMin = yMin;
    	    this.yMax = yMax;
    	    
    	    redraw();
    }
    /** Move the clipping boundaries back to the edge of the viewport. */
    public void restoreClipping() {
    	    xMin = viewArea.getX();
	    xMax = viewArea.getX() + viewArea.getWidth();
	    yMin = viewArea.getY();
	    yMax = viewArea.getY() + viewArea.getHeight();
	    
	    redraw();
    }
    /** Move the current position to the middle of the viewing area */
    public void centrePosition() {
    	    currentPosition = new RealPoint( viewArea.getX() + viewArea.getWidth() / 2, 
    	    		                             viewArea.getY() + viewArea.getHeight() / 2 );
    	    alertListeners( CURPOS_CHANGED );
    	    redraw();
    }
    /** Move the current position by the given dx/dy. */
    public void moveCurrentPositionRel( double dx, double dy ) {
    	    currentPosition.translate( dx, dy );
    	    alertListeners( CURPOS_CHANGED );
    	    redraw();
    }
    /** Move the current position to the given point. */
    public void moveCurrentPositionAbs( double x, double y ) {
	    currentPosition = new RealPoint( x, y );
	    alertListeners( CURPOS_CHANGED );
	    redraw();
    }
    /** Must be called when changing the view, since the screen coordinates change. */
    public void updateScreenTarget() {
    	    screen.setTarget( new ScreenPoint( currentPosition, viewArea, screen ));
    }
    /** Set the list of selected objects. */
    public void setSelectedObjects( Group selection ) {
    	    this.selection = selection;
    }
    /** Get the list of selected objects. */
    public ArrayList getSelectedObjects() {
    	    return (ArrayList)selection;
    }
    /* Operations on selected objects. */
    public void deleteSelectedObjects() {
    	    contents.removeAll( selection );
    	    selection = new Group(); //clear
    	    dirty = true;
    	    alertListeners( CONTENTS_CHANGED );
    	    redraw();
    }
    /** Rotate the currently selected objects. */
    public void rotateSelectedObjects( double theta, RealPoint aboutPoint ) {
	    selection.rotate( theta, aboutPoint );
	    dirty = true;
	    alertListeners( CONTENTS_CHANGED );
	    redraw();
    }
    /** Translate the currently selected objects. */
    public void translateSelectedObjects( RealPoint aboutPoint ) {
	    selection.translate( aboutPoint );
	    dirty = true;
	    alertListeners( CONTENTS_CHANGED );
	    redraw();
    }
    /** Scale the currently selected objects. */
    public void scaleSelectedObjects( double xFactor, 	double yFactor, 
    		                                              	RealPoint aboutPoint ) {
	    selection.scale( xFactor, yFactor, aboutPoint );
	    dirty = true;
	    alertListeners( CONTENTS_CHANGED );
	    redraw();
    }
    /** Insert a vertex into the selected polygon. */
    public void insertPolyVertex( int index, RealPoint vertex ) {
    	    if( selection.size() == 1 && selection.get(0) instanceof RealPolygon ) {
    	    	    RealPolygon poly = (RealPolygon)selection.get(0);
    	    	    poly.insertPoint( vertex, index );
    	    	    dirty = true;
    	    	    redraw();
    	    }
    }
    /** Remove a vertex from the selected polygon. */
    public void removePolyVertex( int index ) {
        	if( selection.size() == 1 && selection.get(0) instanceof RealPolygon ) {
    	        RealPolygon poly = (RealPolygon)selection.get(0);
    	        poly.removePoint(  index );
    	        dirty = true;
    	        redraw();
        }
    }
    /** Insert a substring into the selected RealString. */
    public void changeCharacter( int index, char c ) {
      	if( selection.size() == 1 && selection.get(0) instanceof RealString ) {
	        RealString string = (RealString)selection.get(0);
	        string.changeCharacter( index, c );
	        dirty = true;
	        redraw();
        }
    }
    /** Set the current line colour. */
    public void setLineColour( Color c ) {
    	    lineColour = c;
    	    
    	    //Change the colour of selected objects
    	    for( int i = 0; i < selection.size(); i++ )
    	    	    if( selection.get(i) instanceof RealShape ) {
    	    	    	    RealShape cur = (RealShape)selection.get(i);
    	    	    	    cur.setLineColour( lineColour );
    	    	    }
    	    
    	    alertListeners( LINE_COL_CHANGED );
    	    redraw();
    }
    /** Return the current line colour. */
    public Color getLineColour() {
    	    return lineColour;
    }
    /** Set the current fill colour. */
    public void setFillColour( Color c ) {
    	    fillColour = c;
    	    
        //Change the colour of selected objects
    	    for( int i = 0; i < selection.size(); i++ )
    	    	    if( selection.get(i) instanceof RealShape ) {
    	    	    	    RealShape cur = (RealShape)selection.get(i);
    	    	    	    cur.setFillColour( fillColour );
    	    	    }
    	    	    
    	    alertListeners( FILL_COL_CHANGED );
    	    redraw();
    }
    /** Return the current fill colour. */
    public Color getFillColour() {
    	    return fillColour;
    }
    /** Toggle filling. */
    public void toggleFilling( boolean fillEnabled ) {
    	    fill = fillEnabled;
    }
    /** Are new shapes being filled? */
    public boolean fillEnabled() {
    	    return fill;
    }
    /** Set the size of the window */
    public void setWindowSize( int x, int y ) {
    	    screen.setSize( x, y );
    	    screen.getParent().setSize( x, y );
    	    redraw();
    }
    /** Set the dirty bit and send out a message to listeners, then redraw. */
    public void triggerRefresh() {
    	    dirty = true;  //we've altered the world
	    alertListeners( CONTENTS_CHANGED );
	    redraw();
    }
    /** Add something to the data structure.  It will be drawn if it is recognised. */
    public void addObject( Object theObject ) {
    	    contents.add( theObject );
    	    dirty = true;  //we've altered the world
    	    alertListeners( CONTENTS_CHANGED );
    	    redraw();
    }
    /** Remove an object from the data structure. */
    public void removeObject( Object theObject ) {
    	    contents.removeRecursive( theObject );  //recursive with groups

    	    dirty = true;  //we've altered the world
    	    alertListeners( CONTENTS_CHANGED );
    	    redraw();
    }
    /** Get the world contents */
    public Group getContents() {
    	    return contents;
    }
    /** Remove all elements in the world */
    public void clear() {
    	    contents.clear();
    	    dirty = true;
    	    redraw();
    }
    /** Get the canvas associated with this world. */
    public ScreenViewport getDrawingArea() {
    	    return screen;
    }
    /** Get the attributes related to the world view. */
    public RealViewingArea getViewingArea() {
    	    return viewArea;
    }
    /** Get the current position. */
    public RealPoint getCurrentPosition() {
    	    return currentPosition;
    }
    /** Redraw the world. */
    public void redraw() {
    	    screen.clear();
    	    
    	    //Stage 1 - Clipping
    	    ArrayList clippedData = RealClipper.clip( contents, xMin, xMax, yMin, yMax ); //RealClipper...  //REMOVED UNTIL IT WORKS
    	    
    	    //Stage 2 - Screen coordinate conversion
    	    ArrayList screenData = getScreenData( clippedData );//new ArrayList( contents.size() );
    	    
    	    //Stage 3 - Filling (only polygons for now)
    	    ArrayList fillData = ScreenFiller.fill( screenData );
    	    
    	    //Screen update
    	    screen.setTarget( new ScreenPoint( currentPosition, viewArea, screen) );
    	    screen.refresh( fillData );
    }
    /* Convert to screen coordinates. */
    private ArrayList getScreenData( ArrayList originalData ) {
    	    Group result = new Group();
    	    
    	    //Loop through the contents
   	    for( int i = 0; i < originalData.size(); i++ ) {
   	    	    Object o = originalData.get( i );
   	    	    
   	    	    if( o instanceof Group ) {  //recurse
   	    	    	    result.addAll( getScreenData( (Group)o ) );
   	    	    }
	        if( o instanceof RealSegment ) {  //we have a line segment
	        	    RealSegment seg = (RealSegment)o;
	        	    
	        	    //Convert to screen coordinates
	        	    ScreenPoint start = new ScreenPoint( seg.getStart(), viewArea, screen );
	        	    ScreenPoint end = new ScreenPoint( seg.getEnd(), viewArea, screen );
	        	    
	        	    result.add( new ScreenSegment( start, end, seg.lineColour() ) );
	        }
	        else if( o instanceof RealPolygon ) {  //we have a polygon
        	    RealPolygon poly = (RealPolygon)o;
        	    
        	    ArrayList segs = poly.getSegments();
        	    ScreenPolygon sPoly = new ScreenPolygon( new ArrayList(),
        	    		                                     poly.isFilled(),
												   poly.lineColour(),
												   poly.fillColour());
        	    
        	    for( int j = 0; j < segs.size(); j++ ) {
        	    	    RealSegment seg = (RealSegment)segs.get(j);
        	    	    
                 //Convert to screen coordinates
    	        	    ScreenPoint start = new ScreenPoint( seg.getStart(), viewArea, screen );
    	        	    ScreenPoint end = new ScreenPoint( seg.getEnd(), viewArea, screen );
                 
                 sPoly.getSegments().add(new ScreenSegment( start, end, seg.lineColour() ));
        	    }
        	    result.add( sPoly );
            }
	        else if( o instanceof RealString ) {  //we have a character
        	    RealString text = (RealString)o;
        	    
        	    ArrayList segs = text.getSegments();
        	    
        	    for( int j = 0; j < segs.size(); j++ ) {
        	    	    RealSegment seg = (RealSegment)segs.get(j);
        	    	    
                 //Convert to screen coordinates
    	        	    ScreenPoint start = new ScreenPoint( seg.getStart(), viewArea, screen );
    	        	    ScreenPoint end = new ScreenPoint( seg.getEnd(), viewArea, screen );
                 result.add(new ScreenSegment( start, end, text.lineColour() ));
        	    }
        }
	        else if( o instanceof RealCircle ) { //we have a circle
	        	    RealCircle circle = (RealCircle)o;
	        	    
	        	    //Create a new circle object in screen coordinates
	        	    ScreenPoint screenPosition = new ScreenPoint( circle.getPosition(), viewArea, screen );
	        	    
	        	    //Must convert the radius length to screen coordinates
	        	    int radius = (int)( (circle.getRadius() / viewArea.getHeight() ) * 
	        	    		                          screen.getHeight() );
	        	    
	        	    result.add( new ScreenCircle( screenPosition, radius, 
	        	    		                circle.lineColour(), circle.fillColour() ) );
	        }
	    }
   	    return result;
    }
    
    /** Save the current world to a file. */
    public void saveWorldToFile( String fileName ) {
    	    try {
    	        ( new ObjectOutputStream ( new FileOutputStream ( fileName ) )).writeObject( contents );
    	        
    	        dirty = false; //reset the dirty bit.
    	    }
    	    catch( IOException e ) {
    	    	    System.out.println("Could not save file: " + e );
    	    }
    }
    /** Save the current world to a file. */
    public void saveSelectedObjectsToFile( String fileName ) {
    	    try {
    	        ( new ObjectOutputStream ( new FileOutputStream ( fileName ) )).writeObject( selection );
    	        
    	        dirty = false; //reset the dirty bit.
    	    }
    	    catch( IOException e ) {
    	    	    System.out.println("Could not save file: " + e );
    	    }
    }
    /** Load a new world from a file in append mode. */
    public void loadWorldFromFile( String fileName ) {
	    try {
	        contents.addAll((Group)( new ObjectInputStream ( new FileInputStream ( fileName ) )).readObject());
	        
	        dirty = false;  //reset the dirty bit.
		    alertListeners( CONTENTS_CHANGED );
		    redraw();
	    }
	    catch( Exception e ) {
	    	    System.out.println("Could not open file: " + e );
	    }
    }
    /** Have we done something since saving, i.e. is dirty bit set? */
    public boolean modifiedSinceSaved() {
    	    return dirty;
    }
    /** Zoom the world window in or out. */
    public void zoom( double factor ) {
    	    viewArea.zoom( factor );
    	    alertListeners( VIEWPORT_CHANGED );
    	    restoreClipping();
    	    redraw();
    }
    /** Pan the world window. */
    public void pan( double dx, double dy ) {
    	    viewArea.translate( dx, dy );
    	    alertListeners( VIEWPORT_CHANGED );
    	    restoreClipping();
    	    redraw();
    }
    
    /* Support for listeners. */
    private void alertListeners( int changeType ) {
    	    for( int i = 0; i < listeners.size(); i++ ) {
    	    	    WorldListener l = (WorldListener)listeners.get(i);
    	    	    l.worldChanged( changeType );  //trigger an update
    	    }
    }
    public void addWorldListener( WorldListener l ) { listeners.add( l ); }
    public void removeWorldListener( WorldListener l ) { listeners.remove( l ); }
    
    public String toString() {
    	    return "World contents: " + contents;
    }
}
