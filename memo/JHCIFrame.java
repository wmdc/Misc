package ActivePrototype;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;


public class JHCIFrame extends JFrame
{
	
	private Hand hand;
	private HandThread handThread;	
	
	private Tabletop mainPanel;
	private ArrayList memos;
	private PersonalSpace personalArea;
	    
    /**
     * The constructor.
     */  
     public JHCIFrame(Hand _hand, Tabletop _mainPanel) {
   
        hand = _hand;
        mainPanel = _mainPanel;
              

         
        //setTitle("JHCI");

             
        handThread = new HandThread(hand);
        
        
        this.addKeyListener(new KeyListener()
		{  
			public void keyTyped(KeyEvent e)
			{
				
			}
					
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyChar() == 'z' || e.getKeyChar() == 'Z')
				{
					hand.setObject(memos, personalArea);
					hand.move = true;
				}
				
				if (e.getKeyChar() == 'x' || e.getKeyChar() == 'X')
				{
					hand.setObject(memos, personalArea);
					hand.resize = true;
				}
				
				if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C')
				{
					hand.setObject(memos, personalArea);
					hand.rotate = true;
				}
				
				if (e.getKeyChar() == ' ')
				{
					hand.shrinkingOn = true;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
				{
					hand.leftArrow = true;
					if(!handThread.isAlive())
					{
						handThread = new HandThread(hand);
						handThread.start();
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					hand.rightArrow = true;
					if(!handThread.isAlive())
					{

						handThread = new HandThread(hand);
						handThread.start();
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					hand.upArrow = true;
					if(!handThread.isAlive())
					{
						handThread = new HandThread(hand);
						handThread.start();
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					hand.downArrow = true;
					if(!handThread.isAlive())
					{
						
						handThread = new HandThread(hand);
						handThread.start();
					}
				}
				
				mainPanel.repaint();
			}
			
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyChar() == 'z' || e.getKeyChar() == 'Z')
				{
					hand.move = false;
					hand.resetSquare();
				}
				
				if (e.getKeyChar() == 'x' || e.getKeyChar() == 'X')
				{
					hand.resize = false;
					hand.resetSquare();
				}
				
				if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C')
				{
					hand.rotate = false;
					hand.resetSquare();
				}
				
				if (e.getKeyChar() == ' ')
				{
					hand.shrinkingOn = false;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
				{
					hand.leftArrow = false;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					hand.rightArrow = false;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					hand.upArrow = false;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					hand.downArrow = false;
				}
				
				mainPanel.repaint();
			}
		});
    }
    
    public void addMemos(ArrayList _memos)
    {
    	memos = _memos;
    }
    public void addPersonalArea(PersonalSpace _personalArea)
    {
    	personalArea = _personalArea;
    }
    
    
    /**
     * Shutdown procedure when run as an application.
     */
    protected void windowClosed() {
    	
    	// TODO: Check if it is safe to close the application
    	
        // Exit application.
        System.exit(0);
    }
}
