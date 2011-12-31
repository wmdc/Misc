package Interface;

import Graphics.Real.*;
import java.awt.event.*;

/**
 * Created on Feb 12, 2006
 * @author mwelsman
 *
 * Allows the user to zoom and pan with the mouse.
 */
public class ViewportControl implements MouseListener, MouseMotionListener {
	//How long until a double click becomes 2 clicks?  In ms.
	public static final long MAX_DOUBLE_CLICK_WAIT = 250;
	//How much is one step (proportional to screen).
	public static final double ZOOM_FACTOR = 0.5;
	
	private RealWorld view;
	private boolean pressed;
	private long lastClick;
	private int lastX, lastY;
	/** Construct a new ViewportControl. */
	public ViewportControl( RealWorld theView ) {
		view = theView;
		view.getDrawingArea().addMouseListener( this );
		view.getDrawingArea().addMouseMotionListener( this );
		
		lastX = -1; lastY = -1;
		pressed = false;
	}
	
	/* Pan when the user drags. */
	public void mouseDragged( MouseEvent e ) {
		if( lastX != -1 && lastY != -1 ) {
			int dx = e.getX() - lastX;
			int dy = -(e.getY() - lastY); //convert to right-hand
			
			view.pan( toWorldX( dx ), toWorldY( dy ) );
		}
		
		lastX = e.getX();
		lastY = e.getY();
	}
	/* Clear delta x/y (dragging) data. */
	public void mouseReleased( MouseEvent e ) {
		lastX = -1;
		lastY = -1;
	}
	/* Zoom when the user double clicks. */
	public void mouseClicked( MouseEvent e ) {
		long time = System.currentTimeMillis();
		
		if( time <= lastClick + MAX_DOUBLE_CLICK_WAIT ) {
			//Must convert to right-hand coordinates
			view.pan(	toWorldX( e.getX() - view.getDrawingArea().getWidth() / 2 ),
						toWorldY( ( view.getDrawingArea().getHeight() - e.getY() )
								- view.getDrawingArea().getHeight() / 2 ));
			
			if( !e.isAltDown() )
			    view.zoom( ZOOM_FACTOR );
			else
				view.zoom( 1 / ZOOM_FACTOR );
		}
		else
			lastClick = time;
	}
	/* Not used. */
	public void mouseEntered( MouseEvent e ) {}
	public void mouseExited( MouseEvent e ) {}
	public void mouseMoved( MouseEvent e ) {}
	public void mousePressed( MouseEvent e ) {}
	
	/** Return the width of the screen. */
	
	/** Convert a screen X value to world X value. */
	private double toWorldX( int xVal ) {
		return xVal * (view.getViewingArea().getWidth() /
					 view.getDrawingArea().getWidth());
	}
	/** Convert a screen Y value to world Y value. */
	private double toWorldY( int yVal ) {
		return yVal * ( view.getViewingArea().getWidth() /
					 view.getDrawingArea().getHeight());
	}
}
