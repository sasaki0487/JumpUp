import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
public class Ball
{
	double vX = 0;
	double vY = 0;
	double pX = 0;
	double pY = 0;
	double density = 1;
	double radius;
	double mass;
	double maxhp;
	double speed;
	double hp;
	double damage;
	double maxcd,cd = 0;
	int skill;
	Boolean owner , alive = true;
	BufferedImage icon,skill1;
	AffineTransform identity = new AffineTransform();
	AffineTransform trans = new AffineTransform();
	Color ballColor = new Color(0,0,0);
	public Ball(double x, double y, int[] info , Boolean owner)
	{
		try
		{
			if(info[0] == 10 && owner == false)
				icon = ImageIO.read(new File("char12.png"));
			else if(info[0] == 10 && owner == true)
				icon = ImageIO.read(new File("char11.png"));
			else
				icon= ImageIO.read(new File("char0" + info[0] + ".png"));
			skill1 = ImageIO.read(new File("skill.png"));
		} 
		catch (IOException e)
		{
		}
		skill = info[0];
		radius = 3 + info[2] / 2;
		double volume = 4.0/3.0 * Math.PI * radius * radius * radius;
		pX = x - radius;
		pY = y - radius;
		
		hp = info[1] * 50 + 100 ;
		maxhp = hp;
		damage = info[4] * 5 ;
		this.owner = owner;
		speed = 3 - info[3] / 5;
		mass = density * volume;
		if(info[0] < 10)
			maxcd = Global.CDDATA[info[0]];
		else
			maxcd = 0;
	}
	public void printData()
	{
		System.out.println("pX:" + pX);
		System.out.println("pY:" + pY);
		System.out.println("mass:" + mass);
		System.out.println("vX:  " + vX);
		System.out.println("vY:  " + vY);
		System.out.println("V:   " + velocity());
		System.out.println("Ek:  " + kinetic());
		System.out.println("Mom: " + momentum());
	}
	public void addVelocity(double x, double y)
	{
		vX+=x;
		vY+=y;
	}
	public double velocity()
	{
		return Math.sqrt(vX * vX + vY * vY);
	}
	public double momentum()
	{
		return mass * vX + mass * vY;
	}
	public double kinetic()
	{
		return .5 * mass * velocity() * velocity();
	}
	public void wallBounceX()
	{
		vX = -vX;
	}
	public void wallBounceY()
	{
		vY = -vY;
	}
	public void wallBounceXY1()
	{
		double Temp;
		Temp = vX;
		vX = vY;
		vY = Temp;
	}
	public void wallBounceXY2()
	{
		double Temp;
		Temp = vX;
		vX = -vY;
		vY = -Temp;
	}

	public void addFriction(double t)
	{
		double a,aX,aY;
		a = Global.FRICTION/* * speed*/;
		aY = (Math.abs(vY)/ vY) * Math.sqrt( a * a /(1+(vX * vX / vY / vY)));
		aX = (Math.abs(vX)/ vX) * Math.sqrt( a * a /(1+(vY * vY / vX / vX)));
		if (vX < 0.05 && vX > -0.05)
			vX = 0;
		else 
			vX = vX - aX * t;
		
		if (vY < 0.05 && vY > -0.05)
			vY = 0;
		else 
			vY = vY - aY * t;
	}
	public void move(double t)
	{
		pX = pX + vX * t;
		pY = pY + vY * t;
	}
	public void draw(Graphics g)
	{
		int top, left, size;
		left = (int)(pX * Global.DRAWSCALE);
		top = (int)(pY * Global.DRAWSCALE);
		size = (int)(2 * radius * Global.DRAWSCALE);
		g.setColor(ballColor);
		g.fillOval(left, top, size, size);
		g.drawImage(icon , left + (int)((radius*10) - size * 0.3 ), top + (int)((radius*10) - size * 0.3 ), (int)(size * 0.6) , (int)(size * 0.6) , null);
	}
	
	public void drawBallStatus(Graphics g,int i) //draw the ball status on the buttom of screen
	{
		if( i > 2 )
			i = i - 3;
		else
			i = i + 3;
		Graphics2D g2d = (Graphics2D)g;
		g.drawImage(icon,Global.WINDOWWIDTH * i / 6,(int)(Global.WINDOWHEIGHT*0.83),50,50,null);
		g.setColor(new Color(40,255,40));
		g2d.setStroke(new BasicStroke(1));
		g.drawRect(Global.WINDOWWIDTH * i / 6 + 70,(int)(Global.WINDOWHEIGHT * 0.83),150,10);
		g.fillRect(Global.WINDOWWIDTH * i / 6 + 70,(int)(Global.WINDOWHEIGHT * 0.83),(int)(150 * (hp / maxhp)),10);
		g.setColor(new Color(255,88,9));
		g.drawRect(Global.WINDOWWIDTH * i / 6 + 70,(int)(Global.WINDOWHEIGHT * 0.86),150,10);
		g.fillRect(Global.WINDOWWIDTH * i / 6 + 70,(int)(Global.WINDOWHEIGHT * 0.86),(int)(150 * (cd / maxcd)),10);
		if(cd == maxcd)
			g.drawImage(skill1,Global.WINDOWWIDTH * i / 6 + 70 ,(int)(Global.WINDOWHEIGHT*0.87),150,40,null);
	}

	public void drawLight(Graphics g,BufferedImage img,int animeCounter)
	{
		trans = new AffineTransform();
		int counter = animeCounter * 4;
		trans.translate(Global.DRAWSCALE * (pX + radius),Global.DRAWSCALE * (pY + radius));
		Graphics2D g2d = (Graphics2D)g;
		g2d.setTransform(trans);
		if(counter < 100)
			g.drawImage(img,-counter,-counter,2*counter,2*counter,null);
		else
			g.drawImage(img , -100 , -100 , 200 , 200 , null);
		g2d.setTransform(identity);
	}
	public void clear(Graphics g, Color TableColor)
	{
		int top, left, size;
		left = (int)(pX * Global.DRAWSCALE);
		top = (int)(pY * Global.DRAWSCALE);
		size = (int)(2 * radius * Global.DRAWSCALE);

		g.setColor(TableColor);
		g.fillOval(left-1, top-1, size+2, size+2);
	}
}