package Demos;

import Graphics.Real.*;
import Graphics.Screen.*;
import Interface.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * The final entry point for the graphics package.
 */
public class Final extends JFrame implements ComponentListener {
	private RealWorld world;
	
	public static void main( String args[] ) {  
        //Get screen resolution -> we want the canvas to fill most of the screen.
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    //Build the world and make sure the viewport/canvas is attached to this applet's window.
	    ScreenViewport screen = new ScreenViewport( (int)(screenSize.getHeight()), (int)(screenSize.getHeight() ));
	    
	    RealWorld theWorld = new RealWorld( screen );          //the world model
	    
	    Final demo = new Final( theWorld );
	    demo.setSize(  screen.getWidth(), screen.getHeight()  );
	    demo.getContentPane().add( screen );


	    ViewportControl vc = new ViewportControl( theWorld );  //handle mouse zoom/pan
	    
	    CommandEngine commands = new CommandEngine( theWorld, screen.getWidth() - screen.getHeight(),
	    		                                        screen.getHeight() );
	    

	    Toolbar tools = new Toolbar( theWorld );
	    
	    
	    //Show the UI
	    demo.show();
	    tools.show();
	    commands.show();
	}
	/* Some window code */
	public Final( RealWorld theWorld ) {
		super();
		addComponentListener( this );
		world = theWorld;
	}
	/** Resized, etc. -> maintain aspect ratio and redraw. */
	public void componentResized( ComponentEvent e ) {
		setSize( getHeight(), getHeight() );
		world.redraw();
	}
	public void componentShown( ComponentEvent e ) {}  //unused
	public void componentMoved( ComponentEvent e ) {}
	public void componentHidden( ComponentEvent e ) {}
}