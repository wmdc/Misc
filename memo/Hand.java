/*
 * Created on Mar 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ActivePrototype;

/**
 * @author mwelsman
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.*;
import java.awt.event.*;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.*;

public class Hand
{
	
	public boolean move;
	public boolean resize;
	public boolean rotate;
	public boolean shrinkingOn;
	
	public boolean leftArrow;
	public boolean rightArrow;
	public boolean upArrow;
	public boolean downArrow;
	
	private double[][]  points;
	
	private double[] center;
	private double angle;
	private double[] size;
	private double[] moveChange;
	private double diameter;
	
	//GRAPHICS
	
	private Line2D.Double[] lines;
	private Ellipse2D.Double circle;
	
	private Tabletop inputPanel;
	private Manipulatable object;
	
	
	public Hand()
	{
		
		points = new double[4][2];
		center = new double[2];
		size = new double[4];
		moveChange = new double[2];
		
		center[0] = 100.0;
		center[1] = 100.0;
		diameter = 4.0;
		
		lines = new Line2D.Double[4];
		
		resetSquare();
	}
	
	public void setInputPanel(Tabletop _inputPanel)
	{
		inputPanel = _inputPanel;
	}
	
	public void setObject(ArrayList memos, PersonalSpace personalArea)
	{
		Iterator memoList = memos.iterator();
		PersonalSpace personalAreaIgnore;
	    
	    
	    while( memoList.hasNext() ) {
    	    Memo curMemo = (Memo)memoList.next();
     	    
    	    if( curMemo.contains( (float) center[0], (float) center[1] ))
    	    {
	    	
    	        object = (Manipulatable) curMemo;
    	    }
        }
        
        if( personalArea.contains( (float) center[0], (float) center[1] ))
    	{	
    	    object = (Manipulatable) personalArea;
    	}
    	else 
    	{
    	//	object =  (Manipulatable) personalArea;
    	}
        
        
	}
	
	public void resetSquare()
	{
		angle = 0.0;
		size[0] = 10.0;
		size[1] = 10.0;
		size[2] = 10.0;
		size[3] = 10.0;
		
		center[0] += moveChange[0];
		moveChange[0] = 0;
		center[1] += moveChange[1];
		moveChange[1] = 0;
		
		points[0][0] = center[0] + 10.0;
		points[0][1] = center[1];
		points[1][0] = center[0];
		points[1][1] = center[1] + 10.0;
		points[2][0] = center[0] - 10.0;
		points[2][1] = center[1];
		points[3][0] = center[0];
		points[3][1] = center[1] - 10.0;
		
		setGraphics();
	}
	
	public void moveHand(double x, double y)
	{
		center[0] += x;
		center[1] += y;
		
		resetSquare();
		inputPanel.repaint();
	}
	
	public void moveObject(double x, double y)
	{
		moveChange[0] += x;
		moveChange[1] += y;
		
		for(int i = 0; i < 4; i++)
		{
			points[i][0] += x;
			points[i][1] += y;
		}
		
		setGraphics();
		
		object.translate( (float) x, (float) y );
						
		inputPanel.repaint();
	}
	
	public void resizeObject(double x, double y)
	{
		int i = 0;
		
		if(x > 0) i = 0;
		if(y > 0) i = 1;
		if(x < 0) i = 2;
		if(y < 0) i = 3;
		
		double[][] oldPoints = new double[4][2];
		
		for(int j = 0; j < 4; j++)
		{
			oldPoints[j][0] = points[j][0];
			oldPoints[j][1] = points[j][1];
		}
		
		if(!shrinkingOn)
		{
			points[i][0] += x;
			points[i][1] += y;
		}
		else
		{
			///i = 0 --> i = 2 and so on. This allows to change the value on an intuitive side
			
			points[(i+2)%4][0] += x;
			points[(i+2)%4][1] += y;
		}
				
		setGraphics();
		object.scale( (float) (points[0][0] - oldPoints[0][0]),
					  (float) (points[1][1] - oldPoints[1][1]),
					  -(float) (points[2][0] - oldPoints[2][0]),
					  -(float) (points[3][1] - oldPoints[3][1]));

		inputPanel.repaint();
	}
	
	public void rotateObject(double alfa)
	{
		angle += alfa;
		
		for(int i = 0; i < 4; i++)
		{
			points[i][0] = center[0] + Math.cos(Math.PI*(angle+90*i)/180.0)*size[i];
			points[i][1] = center[1] + Math.sin(Math.PI*(angle+90*i)/180.0)*size[i];
		}
		
		setGraphics();
		object.rotate( (float) alfa);
		
		inputPanel.repaint();
		
	}
	
	public void setGraphics()
	{
		for(int i = 0; i < 4; i++)
		{
			lines[i] = new Line2D.Double(new Point2D.Double(points[i][0], points[i][1]),
										 new Point2D.Double(points[(i+1)%4][0], points[(i+1)%4][1]));
		}
		
		circle = new Ellipse2D.Double(center[0] - diameter/2.0, center[1]-diameter/2.0, diameter, diameter);
	}
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(Color.green);
		if(move == true) g2.setColor(Color.blue);
		if(resize == true) g2.setColor(Color.black);
		if(rotate == true) g2.setColor(Color.red);
		
		g2.draw(circle);
		
		for(int i = 0; i < 4; i++)
		{
			g2.draw(lines[i]);
		}
	}
}