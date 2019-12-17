import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.RadialGradientPaint;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Selectpart extends JPanel implements Runnable , MouseListener , MouseMotionListener
{
	JFrame frame;
	BufferedImage offImage , select , p1l1 , p1l2 , p1r1 , p1r2 , p2l1 , p2l2 , p2r1 , p2r2 , status , icon ,fight;
    Graphics offGraphics;
	int nextPartFlag = 0;
	Thread selectTimer = new Thread(this);
	AffineTransform identity = new AffineTransform();
	AffineTransform trans = new AffineTransform();
	int[][] info = new int[6][5];
	public void mousePressed(MouseEvent event)
	{
		int x = event.getX();
		int y = event.getY();
		if(x > (Global.WINDOWWIDTH * 8.7 / 10) && x < (Global.WINDOWWIDTH * 9.9 / 10) && y < (Global.WINDOWHEIGHT * 0.1) && nextPartFlag == 6)
		{
			frame.remove(this);
			Battlepart battlepart = new Battlepart(frame,info);
		}
		for(int i = 1 ; i <= 3 ; i++)
		{
			if( Math.pow(x - (Global.WINDOWWIDTH * 0.05) - 15 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.28 * i) - 15 , 2.0) < 900)
			{
				info[i-1][0] = (info[i-1][0] + 6) % 7;
			}
			else if( Math.pow(x - (Global.WINDOWWIDTH * 0.1) - 15 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.28 * i) - 15 , 2.0) < 900)
			{
				info[i-1][0] = (info[i-1][0] + 1) % 7;
			}
			else if( Math.pow(x - (Global.WINDOWWIDTH * 0.55) - 15 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.28 * i) - 15 , 2.0) < 900)
			{
				info[i+2][0] = (info[i+2][0] + 6) % 7;
			}
			else if( Math.pow(x - (Global.WINDOWWIDTH * 0.6) - 15 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.28 * i) - 15 , 2.0) < 900)
			{
				info[i+2][0] = (info[i+2][0] + 1) % 7;
			}
			for(int j = 0 ; j < 4 ; j++)
			{
				if( Math.pow(x - (Global.WINDOWWIDTH * 0.21) - 10 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.29 * i - j * 40) - 10 , 2.0) < 400)
				{
					if(info[i-1][4-j] != 0)
						info[i-1][4-j] -= 1;
				}
				else if( Math.pow(x - (Global.WINDOWWIDTH * 0.45) - 10 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.29 * i - j * 40) - 10 , 2.0) < 400)
				{
					if(sumStatus(info[i-1])&& info[i-1][4-j] != 10)
						info[i-1][4-j] += 1;
				}
				else if( Math.pow(x - (Global.WINDOWWIDTH * 0.71) - 10 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.29 * i - j * 40) - 10 , 2.0) < 400)
				{
					if(info[i+2][4-j] != 0)
						info[i+2][4-j] -= 1;
				}
				else if( Math.pow(x - (Global.WINDOWWIDTH * 0.95) - 10 , 2.0 ) + Math.pow(y - (Global.WINDOWHEIGHT * 0.29 * i - j * 40) - 10 , 2.0) < 400)
				{
					if(sumStatus(info[i+2])&& info[i+2][4-j] != 10)
						info[i+2][4-j] += 1;
				}
			}
		}
	}
	public void mouseReleased(MouseEvent event){}
	public void mouseClicked(MouseEvent event){}
	public void mouseDragged(MouseEvent event){}
	public void mouseMoved(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	
	public Selectpart(JFrame frame)
	{
		frame.add(this);
		this.frame = frame;
		addMouseListener(this);
		addMouseMotionListener(this);	
		setSize(Global.WINDOWWIDTH, Global.WINDOWHEIGHT);
		
		for(int i = 0 ; i < 6 ; i ++)
		{
			for(int j = 0 ; j < 5 ; j++)
				if(j == 0)
					info[i][j] = 0;
				else
					info[i][j] = 5;
		}
		
		
		offImage = new BufferedImage(Global.WINDOWWIDTH,Global.WINDOWWIDTH,BufferedImage.TYPE_INT_ARGB);
		offGraphics = offImage.getGraphics();
		try
		{
			select = ImageIO.read(new File("select.png"));
			p1r1 = ImageIO.read(new File("1pright1.png"));
			p1l1 = ImageIO.read(new File("1pleft1.png"));
			p1r2 = ImageIO.read(new File("1pright2.png"));
			p1l2 = ImageIO.read(new File("1pleft2.png"));
			p2r1 = ImageIO.read(new File("2pright1.png"));
			p2l1 = ImageIO.read(new File("2pleft1.png"));
			p2r2 = ImageIO.read(new File("2pright2.png"));
			p2l2 = ImageIO.read(new File("2pleft2.png"));
			status = ImageIO.read(new File("status.png"));
			fight = ImageIO.read(new File("fight.png"));
		} 
		catch (IOException e)
		{
		}
		selectTimer.start();
	}
	public void run()
	{	
		while (true)
		{
			nextPartFlag = 0;
			offGraphics.setColor(Color.white);
			offGraphics.fillRect(0,0,Global.WINDOWWIDTH,Global.WINDOWHEIGHT);
			offGraphics.drawImage(select, Global.WINDOWWIDTH /10 , 0, (int)(Global.WINDOWWIDTH * 0.8) , (int)(Global.WINDOWHEIGHT * 0.15) , null);
			drawMidLine(offGraphics);
			drawButton(offGraphics);
			for(int i = 0 ; i < 6 ; i ++)
			{
				if(sumStatus(info[i]))
					break;
				else
					nextPartFlag += 1;
			}
			if(nextPartFlag == 6)
				offGraphics.drawImage(fight,(int)(Global.WINDOWWIDTH * 8.7 / 10),0,(int)(Global.WINDOWWIDTH * 0.12),(int)(Global.WINDOWHEIGHT * 0.15),null);
			repaint();
			try { Thread.sleep(30); }
			catch (InterruptedException e) { }
		}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.white);
		g.drawImage(offImage, 0, 0, null);
	}
	public void drawMidLine(Graphics g)
	{
		g.setColor(new Color(0,0,0));
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(20));
		g2d.drawLine((Global.WINDOWWIDTH -20) / 2 , (int)(Global.WINDOWWIDTH * 0.1) , (Global.WINDOWWIDTH -20) / 2 ,(int)(Global.WINDOWHEIGHT * 0.87));
		g2d.setStroke(new BasicStroke(1));
	}
	public void drawButton(Graphics g)
	{
		for(int i = 0 ; i < 6 ;i ++)
		{
			try
			{
				icon = ImageIO.read(new File("char0" + info[i][0] + ".png"));
			} 
			catch (IOException e)
			{
			}
			if(i < 3)
				g.drawImage(icon,(int)(Global.WINDOWWIDTH * 0.055),(int)(Global.WINDOWHEIGHT * 0.29 * (i + 1) - 110),90,90,null);
			else
				g.drawImage(icon,(int)(Global.WINDOWWIDTH * 0.555),(int)(Global.WINDOWHEIGHT * 0.29 * (i - 2) - 110),90,90,null);
		}
		for(int i = 1 ; i <= 3 ; i++)
		{
			g.drawImage(status,(int)(Global.WINDOWWIDTH * 0.13),(int)(Global.WINDOWHEIGHT * 0.29 * i - 130),100,170,null);
			g.drawImage(status,(int)(Global.WINDOWWIDTH * 0.63),(int)(Global.WINDOWHEIGHT * 0.29 * i - 130),100,170,null);
			g.drawImage(p2l1,(int)(Global.WINDOWWIDTH * 0.05),(int)(Global.WINDOWHEIGHT * 0.28 * i),30,30,null);
			g.drawImage(p2r1,(int)(Global.WINDOWWIDTH * 0.1),(int)(Global.WINDOWHEIGHT * 0.28 * i),30,30,null);
			g.drawImage(p1l1,(int)(Global.WINDOWWIDTH * 0.55),(int)(Global.WINDOWHEIGHT * 0.28 * i),30,30,null);
			g.drawImage(p1r1,(int)(Global.WINDOWWIDTH * 0.6),(int)(Global.WINDOWHEIGHT * 0.28 * i),30,30,null);
			for(int j = 0 ; j < 4 ; j++)
			{
				g.setColor(Color.black);
				g.drawRect((int)(Global.WINDOWWIDTH * 0.235),(int)(Global.WINDOWHEIGHT * 0.29 * i - j * 40),(int)(Global.WINDOWWIDTH * 0.2),20);
				g.drawRect((int)(Global.WINDOWWIDTH * 0.735),(int)(Global.WINDOWHEIGHT * 0.29 * i - j * 40),(int)(Global.WINDOWWIDTH * 0.2),20);
				g.setColor(Color.red);
				g.fillRect((int)(1 + Global.WINDOWWIDTH * 0.235),(int)(1 + Global.WINDOWHEIGHT * 0.29 * i - j * 40),(int)(-1 + Global.WINDOWWIDTH * 0.2 * info[i-1][4-j] / 10 ),19);
				g.fillRect((int)(1 + Global.WINDOWWIDTH * 0.735),(int)(1 + Global.WINDOWHEIGHT * 0.29 * i - j * 40),(int)(-1 + Global.WINDOWWIDTH * 0.2 * info[i+2][4-j] / 10 ),19);
				g.drawImage(p2l1,(int)(Global.WINDOWWIDTH * 0.21),(int)(Global.WINDOWHEIGHT * 0.29 * i - j * 40),20,20,null);
				g.drawImage(p2r1,(int)(Global.WINDOWWIDTH * 0.45),(int)(Global.WINDOWHEIGHT * 0.29 * i - j * 40),20,20,null);
				g.drawImage(p1l1,(int)(Global.WINDOWWIDTH * 0.71),(int)(Global.WINDOWHEIGHT * 0.29 * i - j * 40),20,20,null);
				g.drawImage(p1r1,(int)(Global.WINDOWWIDTH * 0.95),(int)(Global.WINDOWHEIGHT * 0.29 * i - j * 40),20,20,null);
			}
		}
	}
	public boolean sumStatus(int[] a)
	{
		int sum = 0;
		for(int i = 1 ; i < 5 ; i++){
			sum += a[i];
		}
		if(sum == 20)
			return false;
		else
			return true;
	}
}