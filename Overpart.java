import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.RadialGradientPaint;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class Overpart extends JPanel implements Runnable, MouseListener, MouseMotionListener
{
	JFrame frame;
	BufferedImage offImage, overpanel, win, winp1, winp2, congratulations;
    Graphics offGraphics;
	Thread overTimer = new Thread(this);
	int animeCounter = 0;
	boolean winner = false;
	
	public Overpart(JFrame frame, boolean winner)
	{
		frame.add(this);
		this.frame = frame;
		this.winner = winner;
		addMouseListener(this);
		addMouseMotionListener(this);	
		setSize(Global.WINDOWWIDTH, Global.WINDOWHEIGHT);
		
		offImage = new BufferedImage(Global.WINDOWWIDTH,Global.WINDOWHEIGHT, BufferedImage.TYPE_INT_ARGB);
		offGraphics = offImage.getGraphics();

		try
		{
			overpanel = ImageIO.read(new File("overpanel.png"));
			win = ImageIO.read(new File("win.png"));
			winp1 = ImageIO.read(new File("winp1.png"));
			winp2 = ImageIO.read(new File("winp2.png"));
			congratulations = ImageIO.read(new File("congratulations.png"));
		} 
		catch (IOException e)
		{
		}
		overTimer.start();
	}
	public void run()
	{
		while (true)
		{
			offGraphics.drawImage(overpanel, 0, 0, Global.WINDOWWIDTH, Global.WINDOWHEIGHT, null);
			if(animeCounter<20)
			{
				animeCounter++;
				offGraphics.drawImage(congratulations, (int)(Global.WINDOWWIDTH* (-0.4+animeCounter*0.0375)), (int)(Global.WINDOWHEIGHT* 0.05),null);
				
			}
			else if(animeCounter<40)
			{
				animeCounter++;
				offGraphics.drawImage(congratulations, (int)(Global.WINDOWWIDTH* 0.35), (int)(Global.WINDOWHEIGHT* 0.05), null);
				offGraphics.drawImage(win, (int)(Global.WINDOWWIDTH* (1-(animeCounter-20)*0.0325)), (int)(Global.WINDOWHEIGHT* 0.4), null);
			}
			else if(animeCounter<60)
			{
				animeCounter++;
				offGraphics.drawImage(congratulations, (int)(Global.WINDOWWIDTH* 0.35), (int)(Global.WINDOWHEIGHT* 0.05), null);
				offGraphics.drawImage(win, (int)(Global.WINDOWWIDTH* 0.35), (int)(Global.WINDOWHEIGHT* 0.4), null);
				if(winner == true)
				{
					offGraphics.drawImage(winp1, (int)(Global.WINDOWWIDTH* 0.45), (int)(Global.WINDOWHEIGHT* 0.2),(int)(winp1.getWidth()*(double)(animeCounter-40)/20),(int)(winp1.getHeight()*(double)(animeCounter-40)/20), null);
				}					
				else
				{
					offGraphics.drawImage(winp2, (int)(Global.WINDOWWIDTH* 0.45), (int)(Global.WINDOWHEIGHT* 0.2),(int)(winp2.getWidth()*(double)(animeCounter-40)/20),(int)(winp2.getHeight()*(double)(animeCounter-40)/20), null);
				}
			}
			else
			{
				offGraphics.drawImage(congratulations, (int)(Global.WINDOWWIDTH* 0.35), (int)(Global.WINDOWHEIGHT* 0.05), null);
				offGraphics.drawImage(win, (int)(Global.WINDOWWIDTH* 0.35), (int)(Global.WINDOWHEIGHT* 0.4), null);
				if(winner == true)
				{
					offGraphics.drawImage(winp1, (int)(Global.WINDOWWIDTH* 0.45), (int)(Global.WINDOWHEIGHT* 0.2), null);
				}					
				else
				{
					offGraphics.drawImage(winp2, (int)(Global.WINDOWWIDTH* 0.45), (int)(Global.WINDOWHEIGHT* 0.2), null);
				}
			}
			repaint();
			try { Thread.sleep(10); }
			catch (InterruptedException e) {}
		}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(offImage, 0, 0, null);
	}
	public void mousePressed(MouseEvent event){
		
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseClicked(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
	public void mouseMoved(MouseEvent event){
		
	}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
}