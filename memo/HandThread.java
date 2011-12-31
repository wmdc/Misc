package ActivePrototype;

import java.awt.event.*;


class HandThread extends Thread
{
	private Hand hand;
	
	private double moveJump = 5.0;
	private double rotateJump = 5.0;
	private int sleepTime = 50;
	
	public HandThread(Hand _hand)
	{
		hand = _hand;
	}
	
	public void run()
	{
		try
		{
			while(hand.leftArrow || hand.rightArrow || hand.downArrow || hand.upArrow)
			{
				if(hand.move)
				{
					if(hand.leftArrow) hand.moveObject(-moveJump, 0.0);
					if(hand.rightArrow) hand.moveObject(moveJump, 0.0);
					if(hand.downArrow) hand.moveObject(0.0, moveJump);
					if(hand.upArrow) hand.moveObject(0.0, -moveJump);
					
					Thread.sleep(sleepTime);
					continue;
				}
				
				if(hand.resize)
				{
					if(hand.leftArrow) hand.resizeObject(-moveJump, 0.0);
					if(hand.rightArrow) hand.resizeObject(moveJump, 0.0);
					if(hand.downArrow) hand.resizeObject(0.0, moveJump);
					if(hand.upArrow) hand.resizeObject(0.0, -moveJump);
					
					Thread.sleep(sleepTime);
					continue;
				}
				
				if(hand.rotate)
				{
					if(hand.leftArrow) hand.rotateObject(-rotateJump);
					if(hand.rightArrow) hand.rotateObject(rotateJump);
					
					Thread.sleep(sleepTime);
					continue;
				}
				
				if(hand.leftArrow) hand.moveHand(-moveJump, 0.0);
				if(hand.rightArrow) hand.moveHand(moveJump, 0.0);
				if(hand.downArrow) hand.moveHand(0.0, moveJump);
				if(hand.upArrow) hand.moveHand(0.0, -moveJump);
				
				Thread.sleep(sleepTime);
			}
		}
		catch (InterruptedException exp) {}
	}
}