import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.RadialGradientPaint;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class Startpart extends JPanel implements Runnable, MouseListener, MouseMotionListener
{
	JFrame frame;
	BufferedImage offImage, startpanel, start1, start2, up;
    Graphics offGraphics;
	Thread startTimer = new Thread(this);
	int animeCounter = 0;
	boolean bigstart = false;
	
	public Startpart(JFrame frame)
	{
		frame.add(this);
		this.frame = frame;
		addMouseListener(this);
		addMouseMotionListener(this);	
		setSize(Global.WINDOWWIDTH, Global.WINDOWHEIGHT);
		
		offImage = new BufferedImage(Global.WINDOWWIDTH * Global.DRAWSCALE, Global.WINDOWHEIGHT * Global.DRAWSCALE, BufferedImage.TYPE_INT_ARGB);
		offGraphics = offImage.getGraphics();

		try
		{
			startpanel = ImageIO.read(new File("startpanel.png"));
			start1 = ImageIO.read(new File("start1.png"));
			start2 = ImageIO.read(new File("start2.png"));
			up = ImageIO.read(new File("up.png"));
		} 
		catch (IOException e)
		{
		}
		startTimer.start();
	}
	public void run()
	{
		while (true)
		{
			offGraphics.drawImage(startpanel, 0, 0, Global.WINDOWWIDTH, Global.WINDOWHEIGHT, null);
			if(bigstart)
			{
				offGraphics.drawImage(start2, (int)(Global.WINDOWWIDTH * 0.32), (int)(Global.WINDOWHEIGHT *0.63), (int)(Global.WINDOWWIDTH * 0.36), (int)(Global.WINDOWHEIGHT*0.24), null);
			}
			else
			{
				offGraphics.drawImage(start1, (int)(Global.WINDOWWIDTH * 0.35), (int)(Global.WINDOWHEIGHT *0.65), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT*0.2), null);
			}
			
			if(animeCounter<10)
			{
				animeCounter++;
				offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *(0.1+(double)(animeCounter)/100)), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT * (0.4-(double)(animeCounter)/100)), null);
			}
			else if(animeCounter<12)
			{
				animeCounter++;
				offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *0.2), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT*0.3), null);
			}
			else if(animeCounter<22)
			{
				animeCounter++;
				offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *(0.2-(double)(animeCounter-12)/100)), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT * (0.3+(double)(animeCounter-12)/100)), null);
			}
			else if(animeCounter<42)
			{
				animeCounter++;
				offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *(0.1-2*(double)(animeCounter-22)/100+ 5*(double)((animeCounter-22)*(animeCounter-22))/10000 )), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT*0.4), null);
			}
			else if(animeCounter<45)
			{
				animeCounter++;
				offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *-0.1), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT*0.4), null);
			}
			else if(animeCounter<65)
			{
				animeCounter++;
				offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *(-0.1 + 5*(double)((animeCounter-45)*(animeCounter-45))/10000)), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT*0.4), null);
			}
			else
			{
					animeCounter = 0;
					offGraphics.drawImage(up, (int)(Global.WINDOWWIDTH * 0.55), (int)(Global.WINDOWHEIGHT *0.1), (int)(Global.WINDOWWIDTH * 0.3), (int)(Global.WINDOWHEIGHT*0.4), null);
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
		if(event.getX()>Global.WINDOWWIDTH * 0.35 && event.getX()<Global.WINDOWWIDTH * 0.65 && event.getY()>Global.WINDOWHEIGHT *0.65 && event.getY()<Global.WINDOWHEIGHT *0.85)
		{
			frame.remove(this);
			Selectpart selectpart = new Selectpart(frame);
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseClicked(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
	public void mouseMoved(MouseEvent event){
		if(event.getX()>Global.WINDOWWIDTH * 0.35 && event.getX()<Global.WINDOWWIDTH * 0.65 && event.getY()>Global.WINDOWHEIGHT *0.65 && event.getY()<Global.WINDOWHEIGHT *0.85)
			bigstart = true;
		else
			bigstart = false;

	}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
}