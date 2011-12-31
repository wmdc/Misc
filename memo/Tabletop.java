package ActivePrototype;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Created on Mar 16, 2006
 * @author mwelsman
 *
 * This is the canvas which displays the tabletop surface for the prototype.
 * This class can also be executed to run the demo.
 */
 
 	/*Marek:
	 * I changed JComponent to JPanel, hope it doesn't change a lot, and it's helpful for me.
	 */
 
public class Tabletop extends JPanel implements Runnable, MouseListener, MouseMotionListener {
	private boolean running;
	
	/*Marek:
	 * I changed this variables to static - I have to pass it to frame within the main function
	 */
	
	static private ArrayList memos;
	static private PersonalSpace personalArea;
	//static private ArrayList personalArea;
	
	static private Hand hand; 
	
	/** Construct a new tabletop display area. */
	public Tabletop( Dimension size ) {
		super();
		setSize( size );
		
		memos = new ArrayList();
		//del this line 
		//personalArea = new ArrayList();
		
		memos.add( new Memo( 10, 10, 200, 200, 0 ));
		//Just a single personal space in the demo
		personalArea = new PersonalSpace(100, 400, 600, 300, 0);
		//personalArea.add(new PersonalSpace(100, 400, 600, 300, 0));
	}
	/** Run the tabletop display/active prototype.
	 *  This takes up the entire screen. */
    public static void main( String args[] ) {
    	
    		hand = new Hand();
    	
    	    //Get the current screen resolution
    	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	    
    	    Tabletop theTabletop = new Tabletop( screenSize );
    	    
    	    JHCIFrame theWindow = new JHCIFrame(hand, theTabletop);
    	    theWindow.setResizable( false );
    
    	    
    	    theTabletop.setBackground(Color.GRAY);
    	    theWindow.getContentPane().add( theTabletop );
    	    
    	    hand.setInputPanel(theTabletop);
    	    theWindow.addMemos(memos);
    	    theWindow.addPersonalArea(personalArea);
    	    
    	    theWindow.setSize( screenSize );
    	    theWindow.setLocation( 0, 0 );
    	    //Start up the tabletop in another thread just in case
    	    Thread main = new Thread( theTabletop );
    	    
    	    main.start();
    	    theWindow.show();
    }
    /** Perform redraw operations, etc. */
    public void run() {
    	    running = true;
    	    while( running ) {
    	    	    //doSomething
    	    }
    }
    
    
    /*Marek:
	 * I added paintComponent function.
	 *
	 * It doesn't work on my computer without it, how did it work before?
	 * Maybe Eclipse adds something. 
	 */
    
    public void paintComponent(Graphics g) {
    	
    	super.paintComponent(g);
    	
    	Graphics2D g2 = (Graphics2D)g;
	
	    personalArea.paint(g2);
	      	
    	Iterator memoList = memos.iterator();
    	while( memoList.hasNext() ) {
    	    Memo curMemo = (Memo)memoList.next();
    	    
    	    curMemo.paint(g2);
	    }
	 
	    hand.draw(g2);
	}
    	
    
    /** Mouse input is handled here. */
    public void mouseEntered( MouseEvent e ){}
    public void mouseExited( MouseEvent e ) {}
    public void mouseClicked( MouseEvent e ) {
    	    //did the user click on a PersonalSpace button?
    	}
    public void mouseReleased( MouseEvent e ) {
    	    Iterator memoList = memos.iterator();
    	    
    	    while( memoList.hasNext() ) {
    	    	    Memo curMemo = (Memo)memoList.next();
    	    	    
    	    	    if( curMemo.contains( e.getX(), e.getY() ))
    	    	        curMemo.penUp();
    	    }
    	}
    public void mousePressed( MouseEvent e ) {
	    Iterator memoList = memos.iterator();
	    
	    while( memoList.hasNext() ) {
    	    Memo curMemo = (Memo)memoList.next();
    	    
    	    if( curMemo.contains( e.getX(), e.getY() ))
    	        curMemo.penDown();
        }
    	}
    public void mouseMoved( MouseEvent e ) {
    }
    public void mouseDragged( MouseEvent e ) {
    	
    }
    
    
}
