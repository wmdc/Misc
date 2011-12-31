package Interface;

import java.util.*;
import Graphics.Real.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Rectangle;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * Provides a command-line interface for the graphics package.  This can be used in 
 * conjunction with a GUI.
 * 
 * Commands are given in the following format:
 * 
 * <commandName> <arg1> <arg2> <arg3> ...
 * 
 * This class largely acts as a parser, calling methods of the RealWorld class that
 * are derived from command line text.
 * 
 * Updated so that the runs inside of a window and not just with stdin.
 */
public class CommandEngine extends JFrame implements KeyListener {
	//Window parameters
	public static final String DEFAULT_WINDOW_TITLE = "Graphics Terminal";
	public static final int DEFAULT_WINDOW_WIDTH = 300;
	public static final int DEFAULT_WINDOW_HEIGHT = 60;
	public static final int MAX_WIDTH = 800;
	public static final int MAX_HEIGHT = 60;
	//Error messages
	public static final String ERR_INVALID_ARGS = "Wrong number of arguments.";
	public static final String ERR_UNKNOWN_CMD = "Unknown command.";
	/* COMMANDS */
	//Commands for drawing shapes
    public static final String POLY_REL = "polyrel";
    public static final String POLY_ABS = "polyabs";
    public static final String LINE_ABS = "lineabs";
    public static final String LINE_REL = "linerel";
    public static final String LINE_TO  = "lineto";
    public static final String CIRC_ABS = "circabs";
    public static final String CIRC_REL = "circrel";
    public static final String STR_ABS = "strabs";
    public static final String STR_REL = "strrel";
    //Commands for selecting or altering shapes
    public static final String SELECT = "select";
    public static final String TRANSLATE = "translate";
    public static final String SCALE = "scale";
    public static final String ROTATE = "rotate";
    public static final String POLY_REMOVE = "polyremove";
    public static final String POLY_INSERT = "polyinsert";
    public static final String STR_CHAR = "strchar";
    //Commands for affecting world params such as fill colour or current position
    public static final String MOVE_ABS = "moveabs";
    public static final String MOVE_REL = "moverel";
    public static final String CENTRE = "centre";
    public static final String LINE_COL = "linecol";
    public static final String FILL_COL = "fillcol";
    public static final String FILL = "fill";
    public static final String CURPOS = "curpos";
    public static final String INIT = "init";
    //Camera and window commands
    public static final String REFRESH = "refresh";
    public static final String PAN = "pan";
    public static final String ZOOM = "zoom";
    public static final String STRUCT = "struct";
    public static final String WIN_SIZE = "winsize";
    public static final String MOVE_CLIPPING = "moveclip";
    public static final String RESTORE_CLIPPING = "restoreclip";
    //IO Commands and quit
    public static final String CLEAR = "clear";
    public static final String SAVE = "save";
    public static final String SAVE_SELECTION = "saveselection";
    public static final String LOAD = "load";
    public static final String QUIT = "quit";
    
    private JTextField field;  //the field that is written in
    private RealWorld world;
    
    /** Construct a CommandEngine of the given size. */
    public CommandEngine( RealWorld world, int width, int height ) {
    	    super();
    	    
    	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	    
    	    field = new JTextField();
    	    field.addKeyListener( this );  //listen for changes
    	    this.getContentPane().add( field );
    	    this.world = world;
    	    this.setTitle( DEFAULT_WINDOW_TITLE );
    	    
    	    this.setSize( (int)screenSize.getHeight(), DEFAULT_WINDOW_HEIGHT );
    	    this.setMaximizedBounds( new Rectangle( (int)screenSize.getWidth(), MAX_HEIGHT ));
    	    this.setLocation( 0, (int)screenSize.getHeight() - DEFAULT_WINDOW_HEIGHT - 50 );
    }
    /* Listen for text changes */
    public void keyTyped( KeyEvent e ) {
    	    if( e.getKeyChar() == '\r'  || e.getKeyChar() == '\n' ) processCommand();
    	}
    public void keyPressed( KeyEvent e ){}   	//not used
    	public void keyReleased( KeyEvent e ){}  	//not used
    	/** Process the text in the field and clear it. */
    	private void processCommand() {
    		String command = field.getText();
    		field.setText("");
    		
    		StringTokenizer st = new StringTokenizer( command );
    		if( st.hasMoreTokens() ) {
    			String commandName = st.nextToken();
    			
    			String args[] = new String[ st.countTokens() ];
    			int counter = 0;
    			while( st.hasMoreTokens() ) {
    				args[counter] = st.nextToken();
    				counter++;
    			}
    			//Execute the given command with the args that were read in
    			//and print out any input errors.
    			try { 
    				executeCommand( commandName, args );
    			} catch( BadCmdException e ) { System.out.println( e ); }
    		}
    	}
    	/** Execute the parsed command. */
    	private void executeCommand( String command, String args[] ) throws BadCmdException {
    		if( command.equalsIgnoreCase( POLY_REL ) ) {
    			if( !( args.length % 2 == 0 || args.length < 2 ) )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPolygon poly = RealPolygon.polyRel( 
    					toPointList( args ), 
                     world.getCurrentPosition(), 
					 	world.getFillColour(),
    						world.getLineColour());
    			
    			world.addObject( poly );
    		}
    		else if( command.equalsIgnoreCase( POLY_ABS ) ) {
    			if( !( args.length % 2 == 0 || args.length < 2 ) )
    				throw new BadCmdException( ERR_INVALID_ARGS );

    			RealPolygon poly = new RealPolygon(
    					toPointList( args ),
					world.getFillColour(),
					world.getLineColour());
    			
    			world.moveCurrentPositionAbs(	 toDouble( args[ 0 ] ),
    					                      toDouble( args[ 1 ] ));

    			world.addObject( poly );
    		}
    		//Draw a line at the given position
    		else if( command.equalsIgnoreCase( LINE_ABS ) ) {
    			if( args.length != 4 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			world.addObject(
    					new RealSegment(
    							toDouble( args[0] ),  //x1
							toDouble( args[1] ),  //y1
							toDouble( args[2] ),  //x2
							toDouble( args[3] ),  //y2
							world.getLineColour()));
    			
    			world.moveCurrentPositionAbs( toDouble( args[2] ), toDouble( args[3] ) );
    		}
    		//Draw a line at a position relative to the current position
    		else if( command.equalsIgnoreCase( LINE_REL) ) {
    			if( args.length != 4 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPoint ptRel1 = new RealPoint( world.getCurrentPosition() );
    			ptRel1.translate( toDouble(args[0]), toDouble(args[1]) );
    			
    			RealPoint ptRel2 = new RealPoint( world.getCurrentPosition() );
    			ptRel2.translate( toDouble(args[2]), toDouble(args[3]) );
    			
    			world.addObject(
    					new RealSegment(
    							ptRel1,   //(x1, y1)
							ptRel2,   //(x2, y2)
							world.getLineColour()));
    			
    			world.moveCurrentPositionAbs( ptRel2.getX(), ptRel2.getY() );
    		}
        //Draw a line from the current position to a given position
    		else if( command.equalsIgnoreCase( LINE_TO) ) {
    			if( args.length != 2 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPoint ptEnd = new RealPoint( toDouble(args[0]), toDouble(args[1]) );
    			
    			world.addObject(
    					new RealSegment(
    							world.getCurrentPosition(),
							ptEnd,
							world.getLineColour()));
    			
    			world.moveCurrentPositionAbs( ptEnd.getX(), ptEnd.getY() );
    		}
    		//Draw a circle at the given position
    		else if( command.equalsIgnoreCase( CIRC_ABS ) ) {
			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
			
			Color fill;
			if( world.fillEnabled() ) fill = world.getFillColour();
			else fill = null;  //don't fill
			
    			world.addObject( 
    					new RealCircle(
    					new RealPoint( toDouble(args[0]), toDouble(args[1])),
					toDouble( args[2] ),
					world.getLineColour(),
					fill));
    			
    			world.moveCurrentPositionAbs( toDouble(args[0]), toDouble(args[1]) );
    		}
    		//Draw a circle at the position relative to the current position
    		else if( command.equalsIgnoreCase( CIRC_REL ) ) {
    			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPoint ptRel = new RealPoint( world.getCurrentPosition() );
    			ptRel.translate( toDouble(args[0]), toDouble(args[1]) );
			
			Color fill;
			if( world.fillEnabled() ) fill = world.getFillColour();
			else fill = null;  //don't fill
			
    			world.addObject( 
    					new RealCircle(
    					ptRel,
					toDouble( args[2] ),
					world.getLineColour(),
					fill));
    			
    			world.moveCurrentPositionAbs( ptRel.getX(), ptRel.getY() );
    		}
    		//Add a string at the given location with the given width/height
    		else if( command.equalsIgnoreCase( STR_ABS ) ) {
    			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.addObject( 
    					new RealString( args[0], 
    					new RealPoint( toDouble(args[1]), toDouble(args[2])),
					world.getLineColour()));
    			
    			world.moveCurrentPositionAbs( toDouble(args[1]), toDouble(args[2]) );
    		}
    		//Add text to a location relative to the current position
    		else if( command.equalsIgnoreCase( STR_REL ) ) {
    			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPoint ptRel = new RealPoint( world.getCurrentPosition() );
    			ptRel.translate( toDouble(args[1]), toDouble(args[2]) );
    			
    			world.addObject( 
    					new RealString( args[0], 
    					ptRel,
					world.getLineColour()));
    			
    			world.moveCurrentPositionAbs( ptRel.getX(), ptRel.getY() );
    		}
    		else if( command.equalsIgnoreCase( SELECT ) ) {
             //use the toolbar!
    		}
    		//Translate the currently selected objects
    		else if( command.equalsIgnoreCase( TRANSLATE ) ) {
    			if( args.length != 2 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPoint delta = new RealPoint( toDouble( args[0] ), toDouble( args[1] ) );
    			
             world.translateSelectedObjects( delta );
             System.out.println("Translated " + world.getSelectedObjects() + 
             		" by " + delta );
    		}
    		//Scale the currently selected objects
    		else if( command.equalsIgnoreCase( SCALE ) ) {
    			if( args.length != 4 && args.length != 2 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			double xFactor = toDouble( args[0] );
    			double yFactor = toDouble( args[1] );
    			
    			if( args.length == 2 ) { //scale about curpos
    				world.scaleSelectedObjects( xFactor, yFactor, 
    						   world.getCurrentPosition() );
    			} else {
    			    RealPoint about = 
    			    	            new RealPoint( toDouble( args[2] ), toDouble( args[3] ) );
    			    world.scaleSelectedObjects( xFactor, yFactor, about );
    			}
    		}
    		//Rotate the currently selected objects
    		else if( command.equalsIgnoreCase( ROTATE ) ) {
    			if( args.length != 3 && args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			double theta = toDouble( args[0] );
    			
    			if( args.length == 1 ) { //rotate about curpos
    				world.rotateSelectedObjects( theta, world.getCurrentPosition() );
    			} else {
    			    RealPoint about = 
    			    	            new RealPoint( toDouble( args[1] ), toDouble( args[2] ) );
    			    world.rotateSelectedObjects( theta, about );
    			}
    		}
    		//Remove a point from the selected polygon, if one is selected.
    		else if( command.equalsIgnoreCase( POLY_REMOVE ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			int index = toInt( args[0] );
    			
    			world.removePolyVertex( index );
    		}
    		//Insert a new vertex into the selected polygon.  (do nothing if none selected).
    		else if( command.equalsIgnoreCase( POLY_INSERT ) ) {
    			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			RealPoint p = new RealPoint( toDouble( args[0] ), toDouble( args[1] ) );
    			int index = toInt( args[2] );
    			
    			world.insertPolyVertex( index, p );
    		}
    		//Change a character in the currently-selected string
    		else if( command.equalsIgnoreCase( STR_CHAR ) ) {
             if( args.length != 2 )
             	throw new BadCmdException( ERR_INVALID_ARGS );
             
             char c = args[0].charAt( 0 );
             int index = toInt( args[ 1 ] );
             
             world.changeCharacter( index, c );
    		}
    		else if( command.equalsIgnoreCase( MOVE_ABS ) ) {
    			if( args.length != 2 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.moveCurrentPositionAbs( toDouble( args[0] ), toDouble( args[1] ) );
    			System.out.println("Current position is now: " + world.getCurrentPosition());
    		}
    		else if( command.equalsIgnoreCase( MOVE_REL ) ) {
    			if( args.length != 2 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.moveCurrentPositionRel( toDouble( args[0] ), toDouble( args[1] ) );
    			System.out.println("Current position is now: " + world.getCurrentPosition());
    		}
    		//Move the current position to the middle of the viewport
    		else if( command.equalsIgnoreCase( CENTRE ) ) {
    			if( args.length != 0 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.centrePosition();
    			System.out.println("Current position is now: " + world.getCurrentPosition());
    		}
    		//Change the current line colour
    		else if( command.equalsIgnoreCase( LINE_COL ) ) {
    			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.setLineColour( new Color( 	toInt( args[0] ), 
    					                       	toInt( args[1] ), 
											toInt( args[2] ) ) );
    			System.out.println("New line colour: " + world.getLineColour() );
    		}
    		//Change the current fill colour
    		else if( command.equalsIgnoreCase( FILL_COL ) ) {
    			if( args.length != 3 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.setFillColour( new Color( 	toInt( args[0] ), 
    					                       	toInt( args[1] ), 
											toInt( args[2] ) ) );
    			System.out.println("New fill colour: " + world.getFillColour() );
    		}
    		//Toggle filling on or off
    		else if( command.equalsIgnoreCase( FILL ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.toggleFilling( toBoolean( args[0] ));
    		}
    		//Print out the current position
    		else if( command.equalsIgnoreCase( CURPOS ) ) {
    			System.out.println( world.getCurrentPosition() );
    		}
    		//Resize the viewport to default values and set the curpos to (0,0);
    		else if( command.equalsIgnoreCase( INIT ) ) {
    			world.init();
    		}
    		//Redraw the contents of the world (normally this is automatic)
    		else if( command.equalsIgnoreCase( REFRESH ) ) {
    			world.redraw();
    		}
    		//Pan the window by the given amount
    		else if( command.equalsIgnoreCase( PAN ) ) {
    			if( args.length != 2 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.pan( toDouble( args[0] ), toDouble( args[1] ) );
    		}
    		//Zoom in the window by the given factor
    		else if( command.equalsIgnoreCase( ZOOM ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.zoom( toDouble( args[0] ) );
    		}
    		//Print out the contents of the data structure
    		else if( command.equalsIgnoreCase( STRUCT ) ) {
    			System.out.println( world );
    		}
    		//Set the size of the window (1 arg to preserve 1-1 aspect ratio)
    		else if( command.equalsIgnoreCase( WIN_SIZE ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.setWindowSize( toInt( args[0] ), toInt( args[0] ) );
    		}
    		//Move the clipping boundaries.  Useful for testing.
    		else if( command.equalsIgnoreCase( MOVE_CLIPPING ) ) {
    			if( args.length != 4 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			world.moveClipping(
    					toDouble( args[0] ),
					toDouble( args[1] ),
					toDouble( args[2] ),
					toDouble( args[3] ));
    		}
    		//Restore clipping boundaries to viewport boundaries
    		else if( command.equalsIgnoreCase( RESTORE_CLIPPING ) ) {
    			if( args.length != 0 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			
    			world.restoreClipping();
    		}
    		//Clear the contents of the data structure
    		else if( command.equalsIgnoreCase( CLEAR ) ) {
    			int result = -1;
    			if( world.modifiedSinceSaved() ) {
    			    result = JOptionPane.showConfirmDialog( 
    					new JFrame(), "Do you really want to clear the data structure?" );
    			}
    			
    			if( result == JOptionPane.YES_OPTION || result == -1 ) {
    				world.clear();
    				System.out.println( "Data structure cleared." );
    			}
    			else {
    				System.out.println("Data structure clear cancelled.");
    			}
    		}
    		//Save the contents of the data structure to a file.
    		else if( command.equalsIgnoreCase( SAVE ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.saveWorldToFile( args[0] );
    			System.out.println( "Data structure written to file: " + args[0] );
    		}
    		//Save the currently-selected objects to a file.
    		else if( command.equalsIgnoreCase( SAVE_SELECTION ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.saveSelectedObjectsToFile( args[0] );
    			System.out.println( "Data structure written to file: " + args[0] );
    		}
    		//Append a new file's contents to the current data structre
    		else if( command.equalsIgnoreCase( LOAD ) ) {
    			if( args.length != 1 )
    				throw new BadCmdException( ERR_INVALID_ARGS );
    			world.loadWorldFromFile( args[0] );
    		}
    		//Quit the graphics program
    		else if( command.equalsIgnoreCase( QUIT ) ) {
    			int result = -1;
    			if( world.modifiedSinceSaved() ) {
    			    result = JOptionPane.showConfirmDialog( 
    					new JFrame(), "Do you really want to quit?" );
    			}
    			
    			if( result == JOptionPane.YES_OPTION || result == -1 ) {
    				System.exit( 0 );
    			}
    			else {
    				System.out.println( "Quit cancelled" );
    			}
    			
    		}
    		else {  //don't know what the command is
    			throw new BadCmdException( ERR_UNKNOWN_CMD );
    		}
    	}
    	/* Utility methods to make the code a bit clearer.
    	 * Each individual command execution block above should be as simple as possible. */
    	private int toInt( String str ) throws BadCmdException {
    		try {
    		    return Integer.parseInt( str );
    		} catch( Exception e ) { throw new BadCmdException(
    				"This command requires an integer argument.");
    		}
    	}
    	private double toDouble( String str ) throws BadCmdException {
    		try {
    		    return Double.parseDouble( str );
    		} catch( Exception e ) { throw new BadCmdException(
			"This command requires a double argument.");
	    }
    	}
    	private boolean toBoolean( String str ) throws BadCmdException {
    		if( str.equalsIgnoreCase( "true" ) ||
    			str.equalsIgnoreCase( "t" ) ||
    			str.equalsIgnoreCase( "yes" ) ||
    			str.equalsIgnoreCase( "y" ) )
				return true;
    		if( str.equalsIgnoreCase( "false" ) ||
        			str.equalsIgnoreCase( "f" ) ||
        			str.equalsIgnoreCase( "no" ) ||
        			str.equalsIgnoreCase( "n" ) )
    				return false;
    		//If the string can't be parsed, throw an exception
    		throw new BadCmdException( "This command requires a boolean argument." );
    	}
    	private ArrayList toPointList( String args[] ) throws BadCmdException {
    		ArrayList result = new ArrayList();
    		
    		for( int i = 0; i < args.length - 1; i += 2 )
    			result.add( new RealPoint( toDouble( args[i]), toDouble(args[i+1]) ) );

    		return result;
    	}
}
class BadCmdException extends Exception {
	public BadCmdException( String s ) { super(s); }
	}