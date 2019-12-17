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

public class Battlepart extends JPanel implements Runnable , MouseListener , MouseMotionListener
{
	JFrame frame;
	BufferedImage offImage, battlepanel , arrow0, arrow1, arrow2, light, p1 , p2 , skillbackground , explosion ;
    Graphics offGraphics;
	int moveBall = -1, arrowFlag = 0, ballFlag = 0, turn = 0, animeCounter = 1 ,clickX = 0 , clickY = 0, animeCounter1 = 0;
	int[] moveTurn = new int[6] ; 
	double angle = 0;
	Thread ballTimer = new Thread(this);
	Table table = new Table(0,0,Global.WINDOWWIDTH / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 / Global.DRAWSCALE, Global.WINDOWEDGE / Global.DRAWSCALE);
	Ball ball[] = new Ball[Global.BALLCOUNT];
	AffineTransform identity = new AffineTransform();
	AffineTransform trans = new AffineTransform();
	Color tableColor = new Color(50,50,50);
	Random rand = new Random();
	public void mousePressed(MouseEvent event){
		if(animeCounter == 100 && checkStop(ball) && event.getY() < Global.WINDOWHEIGHT * 0.8 )
		{
			ballFlag = 1;
			clickX = event.getX();
			clickY = event.getY();
		}
		else
		{
			int j;
			for(int i = 0 ; i < 6 ; i ++)
			{
				if(i < 3)
					j = i + 3;
				else
					j = i - 3; 
				if(ball[i].cd == ball[i].maxcd && ball[i].owner == ball[moveBall].owner && animeCounter == 100 && checkStop(ball) && event.getX() > Global.WINDOWWIDTH * j / 6 + 70 && event.getX() < Global.WINDOWWIDTH * j / 6 + 220 && event.getY() > Global.WINDOWHEIGHT * 0.87 && event.getY() < Global.WINDOWHEIGHT * 0.87 + 40)
				{
					skill(i);
					ball[i].cd = 0;
				}
			}
		}
	}
	public void mouseReleased(MouseEvent event){
		if (ballFlag == 1)
		{
			double tempX = clickX - event.getX();
			double tempY = clickY - event.getY();
			if(distance(tempX,tempY,0,0) > 400)
			{
				ball[moveBall].vX = -400 * Math.cos(angle) /10;
				ball[moveBall].vY = -400 * Math.sin(angle) /10;
			}
			else
			{
				ball[moveBall].vX = tempX /10;
				ball[moveBall].vY = tempY /10;
			}
			arrowFlag = 0;
			
			ballFlag = 0;
		}
		
		
	}
	public void mouseClicked(MouseEvent event){}
	public void mouseDragged(MouseEvent event)
	{
		if(ballFlag == 1)
		{
			double tempX = ball[moveBall].pX + ball[moveBall].radius;
			double tempY = ball[moveBall].pY + ball[moveBall].radius;
			double temp = (double)clickX - (double)event.getX();
			angle = arrowAngle((double)(event.getY()- clickY),(double)(event.getX() - clickX));
			if(distance((double)clickX,(double)clickY,(double)event.getX(),(double)event.getY()) < 100 )
				arrowFlag = 1;
			else if(distance((double)clickX,(double)clickY,(double)event.getX(),(double)event.getY()) < 200 )
				arrowFlag = 2;
			else
				arrowFlag = 3;
			trans = new AffineTransform();
			trans.translate(Global.DRAWSCALE * tempX,Global.DRAWSCALE * tempY);
			trans.rotate(angle);
			if(distance((double)clickX,(double)clickY,(double)event.getX(),(double)event.getY()) > 200)
				trans.scale(- 400.0 / arrow2.getWidth(),0.1);
			else 
				trans.scale(-2 * distance((double)clickX,(double)clickY,(double)event.getX(),(double)event.getY()) / arrow1.getWidth(),0.1);			
		}	
	}
	public void mouseMoved(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	
	public Battlepart(JFrame frame,int[][] info)
	{
		frame.add(this);
		this.frame = frame;
		addMouseListener(this);
		addMouseMotionListener(this);	
		setSize(Global.WINDOWWIDTH, Global.WINDOWHEIGHT);
		
		
		offImage = new BufferedImage((int)Global.WINDOWWIDTH,(int)Global.WINDOWHEIGHT,BufferedImage.TYPE_INT_ARGB);
		offGraphics = offImage.getGraphics();
		try
		{
			skillbackground = ImageIO.read(new File("skillbackground.png"));
			battlepanel = ImageIO.read(new File("battlepanel.png"));
			arrow0 = ImageIO.read(new File("arrow0.png"));
			arrow1 = ImageIO.read(new File("arrow1.png"));
			arrow2 = ImageIO.read(new File("arrow2.png"));
			light = ImageIO.read(new File("ballLight2.png"));
			p1 = ImageIO.read(new File("p1.png"));
			p2 = ImageIO.read(new File("p2.png"));
			explosion = ImageIO.read(new File("explosion.png"));
		} 
		catch (IOException e)
		{
		}
		
		ball[0] = new Ball(Global.WINDOWWIDTH * 5 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 1 / 4 / Global.DRAWSCALE , info[3] , false);
		ball[1] = new Ball(Global.WINDOWWIDTH * 6 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 2 / 4 / Global.DRAWSCALE , info[4] , false);
		ball[2] = new Ball(Global.WINDOWWIDTH * 5 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 3 / 4 / Global.DRAWSCALE , info[5] , false);
		ball[3] = new Ball(Global.WINDOWWIDTH * 3 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 1 / 4 / Global.DRAWSCALE , info[0] , true);
		ball[4] = new Ball(Global.WINDOWWIDTH * 2 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 2 / 4 / Global.DRAWSCALE , info[1] , true);
		ball[5] = new Ball(Global.WINDOWWIDTH * 3 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 3 / 4 / Global.DRAWSCALE , info[2] , true);
		ball[6] = new Ball(Global.WINDOWWIDTH * 7 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 2 / 4 / Global.DRAWSCALE , Global.KINGSTATUS , false);
		ball[7] = new Ball(Global.WINDOWWIDTH * 1 / 8 / Global.DRAWSCALE, Global.WINDOWHEIGHT * 0.8 * 2 / 4 / Global.DRAWSCALE , Global.KINGSTATUS , true);
		
		for(int i = 0 ; i < Global.BALLCOUNT ; i++){
			if(ball[i].owner == false)
				ball[i].ballColor = Global.p1Color;
			else
				ball[i].ballColor = Global.p2Color;
		}
		
		moveTurn = calcMoveTurn();
		moveBall = moveTurn[turn];
		
		Client c = new Client(ball);
		Thread t = new Thread(c);
		t.start();
		
		ballTimer.start();
	}
	public void run()
	{	
		int i = 0;
		int x = 0;
		int y = 0;
		int j=0;


		while (true)
		{
			offGraphics.setColor(Color.black);
			offGraphics.fillRect(0,0,Global.WINDOWWIDTH,Global.WINDOWHEIGHT);
		//	offGraphics.drawImage(battlepanel, 0, 0, Global.WINDOWWIDTH, Global.WINDOWHEIGHT, null);
			drawHp(offGraphics);
			table.draw(offGraphics,tableColor);
			
			offGraphics.setColor(Color.black);
			for(i = 0; i < Global.BALLCOUNT; i++)
			{
				if (ball[i].vX != 0 || ball[i].vY != 0 && ball[i].alive == true)
				{
					ball[i].addFriction(.05);
				}
				if (ball[i].alive == true)
					ball[i].move(.05);
			}

			for(i = 0; i < Global.BALLCOUNT; i++)
			{
				if (ball[i].alive == true)
					checkWallBounce(ball[i]);
			}

			for(i = 0; i < Global.BALLCOUNT; i++)
			{
				if(ball[i].alive == true)
					ball[i].draw(offGraphics);
			}
			
			for(x = 0; x < Global.BALLCOUNT; x++)//calculate damage
			{
				for(y = x + 1; y < Global.BALLCOUNT; y++)
				{
					if (separation(ball[x],ball[y]) <= radSum(ball[x],ball[y]) && ball[x].alive == true && ball[y].alive == true)
					{
						collide(ball[x],ball[y]);
						offGraphics.drawImage(explosion,(int)(Global.DRAWSCALE * (ball[x].pX+ball[y].pX)/2),(int)(Global.DRAWSCALE * (ball[x].pY+ball[y].pY)/2),80,80,null);
						if(ball[x].owner != ball[y].owner)
						{
							if(ball[x].owner == ball[moveBall].owner)
								ball[y].hp = ball[y].hp - ball[x].damage;
							else
								ball[x].hp = ball[x].hp - ball[y].damage;
						}
					}
				}
			}
			
			
			
			for(i = 0; i < Global.BALLCOUNT - 2; i++)
			{
				ball[i].drawBallStatus(offGraphics,i);
			}
			
			table.drawBorder(offGraphics);
			
			if(checkStop(ball))
			{
				for(i = 0 ; i < Global.BALLCOUNT ; i++)
				{
					if (ball[i].hp <= 0 )
						ball[i].alive = false;
				}
				if(animeCounter == 0)
				{
					do
					{
						turn += 1 ;
						turn = turn % 6;
					}while(ball[moveTurn[turn]].alive != true);
					
					moveBall = moveTurn[turn];
					for(i = 0 ; i < 6 ; i++)
						if(ball[i].cd!=ball[i].maxcd)
							ball[i].cd += 1;
				}
				if(animeCounter < 75)
				{
					animeCounter ++;
					if(moveBall < 3)
						drawPlayer(offGraphics,p1);
					else
						drawPlayer(offGraphics,p2);
				}
				else if(animeCounter <= 100)
				{
					if(animeCounter < 100)
						animeCounter ++;
					ball[moveBall].drawLight(offGraphics,light,animeCounter-75);
				}
				if(ball[6].hp <= 0)
				{
					frame.remove(this);
					Overpart overpart = new Overpart(frame,true);
					try
					{
						ballTimer.wait();
					} 
					catch (InterruptedException e)
					{
					}
				}
				else if(ball[7].hp <= 0)
				{
					frame.remove(this);
					Overpart overpart = new Overpart(frame,false);
					try
					{
						ballTimer.wait();
					} 
					catch (InterruptedException e)
					{
					}
				}
				else if(ball[0].hp <= 0 && ball[1].hp <= 0 && ball[2].hp <= 0)
				{
					frame.remove(this);
					Overpart overpart = new Overpart(frame,true);
					try
					{
						ballTimer.wait();
					} 
					catch (InterruptedException e)
					{
					}
				}
				else if(ball[3].hp <= 0 && ball[4].hp <= 0 && ball[5].hp <= 0)
				{
					frame.remove(this);
					Overpart overpart = new Overpart(frame,false);
					try
					{
						ballTimer.wait();
					} 
					catch (InterruptedException e)
					{
					}
				}
			}
			else
				animeCounter = 0;
			
			repaint();
			try { Thread.sleep(13); }
			catch (InterruptedException e) { }
		}
	}
	public void paintComponent(Graphics g)
	{
			super.paintComponent(g);
			g.setColor(Color.white);
			g.drawImage(offImage, 0, 0, null);
			if (arrowFlag != 0 )
			{ 
				Graphics2D g2d = (Graphics2D)g;
				g2d.setTransform(trans);
				switch(arrowFlag)
				{
					case 1:
						g2d.drawImage(arrow0, (int)(-0.2 * arrow0.getWidth()) ,  (int)(-0.5 * arrow0.getHeight()) , null);	
						break;
					case 2:
						g2d.drawImage(arrow1, (int)(-0.2 * arrow0.getWidth()) ,  (int)(-0.5 * arrow0.getHeight()) , null);	
						break;
					case 3:
						g2d.drawImage(arrow2, (int)(-0.2 * arrow0.getWidth()) ,  (int)(-0.5 * arrow0.getHeight()) , null);	
						break;
				}	
				g2d.setTransform(identity);
			}
	}
	
	public Color randColor()
	{
		int r,g,ball;
		r = (int)(Math.random() * 256);
		g = (int)(Math.random() * 256);
		ball = (int)(Math.random() * 256);
		return new Color(r,g,ball);
	}
	
	public int posNegRand()
	{
		double a = Math.random() * 2;
		if (a >= 1)
			a = 1;
		if (a < 1)
			a = -1;

		return (int)a;
	}
	public void drawPlayer(Graphics g,BufferedImage img)//animtation of player turn 
	{
		float opacity = (float)(animeCounter/50.);
		trans = new AffineTransform();
		trans.translate(Global.WINDOWWIDTH / 2,Global.WINDOWHEIGHT * 0.8 / 2 - 100);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setTransform(trans);
		if (animeCounter < 50)
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		else 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		g.drawImage(img,-400,-80,800,160,null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setTransform(identity);
	}

	public void drawHp(Graphics g)
	{
		g.setColor(Global.p2Color);
		g.fillRect(0,0,(int)(200 * ball[7].hp / ball[7].maxhp),(int)(Global.WINDOWHEIGHT * 0.8));
		g.setColor(Global.p1Color);
		g.fillRect(Global.WINDOWWIDTH - (int)(200 * ball[6].hp / ball[6].maxhp),0,(int)(200 * ball[6].hp / ball[6].maxhp),(int)(Global.WINDOWHEIGHT * 0.8));
		g.setColor(Color.black);
	}
	
	public double arrowAngle(double y,double x)
	{
		if (x > 0 && y > 0)
			return Math.atan2(y,x);
		else if (x < 0 && y > 0)
			return Math.atan2(y,x);
		else if (x < 0 && y < 0)
			return Math.toRadians(360) + Math.atan2(y,x);
		else
			return Math.toRadians(360) + Math.atan2(y,x);
	}
	public int[] calcMoveTurn()
	{
		int array[] = {0,1,2,3,4,5};
		for (int i = 5 ; i > 0; i--)
		{
		  int index = rand.nextInt(i + 1);
		  int a = array[index];
		  array[index] = array[i];
		  array[i] = a;
		}
		return array;
	}
	public boolean checkStop(Ball[] b)
	{
		for (int i = 0 ; i < b.length ; i++){
			if(b[i].vX!=0 || b[i].vY!=0)
				return false;
		}
			return true;
		
	}
	public void checkWallBounce(Ball ball)
	{
		double xLeft = ball.pX;
		double yTop = ball.pY;
		double yBottom = ball.pY + 2 * ball.radius;
		double xRight = ball.pX + 2 * ball.radius;
		double sqrtRadius = Math.sqrt(2)*ball.radius;
		
		if (xLeft < table.minX())
		{
			ball.pX = table.minX();
			ball.wallBounceX();
		} 

		if (yTop < table.minY())
		{
			ball.pY = table.minY();
			ball.wallBounceY();
		}

		if (xRight > table.maxX())
		{
			ball.pX = table.maxX() - 2 * ball.radius;
			ball.wallBounceX();
		} 

		if (yBottom > table.maxY())
		{
			ball.pY = table.maxY() - 2 * ball.radius;
			ball.wallBounceY();
		}
		
		if((xLeft-yTop)>(table.maxX()-table.edge()-sqrtRadius))
		{
			ball.pX = table.maxX()-table.edge()-sqrtRadius + yTop;
			ball.wallBounceXY1();
		}
		
		if((yTop-xLeft)>(table.maxY()-table.edge()-sqrtRadius))
		{
			ball.pY = table.maxY()-table.edge()-sqrtRadius + xLeft;
			ball.wallBounceXY1();
		}
		
		if((xLeft+yTop)>(table.maxX()+table.maxY()-table.edge()-sqrtRadius-2*ball.radius))
		{
			ball.pX = table.maxX()+table.maxY()-table.edge()-sqrtRadius-2*ball.radius-yTop;
			ball.wallBounceXY2();
		}
		
		if((xLeft+yTop)<(table.edge()+sqrtRadius-2*ball.radius))
		{
			ball.pX = table.edge()+sqrtRadius- 2*ball.radius-yTop;
			ball.wallBounceXY2();
		}
	}
	public double separation(Ball b1, Ball b2)
	{ 
		double x1, x2, y1, y2;
		x1 = b1.pX + b1.radius;
		x2 = b2.pX + b2.radius;
		y1 = b1.pY + b1.radius;
		y2 = b2.pY + b2.radius;

		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	public double radSum(Ball b1, Ball b2) //Sum of the radii of two balls
	{
		return (b1.radius + b2.radius);
	}
	public void collide(Ball b1, Ball b2) //Collides two balls and changes their velocities accordingly
	{

		/* These collisions should all be perfectly (of very close to it) elastic.
		 * Final velocities are calculated on the premise that the impulse from a
		 * collision acts along a line through the center of the ball and through the
		 * collision point, therefore the component of velocity perpendicular to the
		 * collision cannot be changed by it, therefore the component of velocity headed
		 * straight into the collision can be treated as a single dimensional collison*/

		double dx1, dx2, dy1, dy2; //Distances to the collision point
		double x1, x2, y1, y2; //distances to bewteen centers and collision
		double dxr, dyr; //real distances (collision overlap)
		double dx, dy; //imaginary distances (no overlap)
		double vp1, vp2, vs1, vs2; //normal and perpendicular velocities to collision
		double newvs1, newvs2; //for storing new straigth velocites during calculations
		double distance; //real distance between ball centers

		x1 = b1.pX + b1.radius;
		x2 = b2.pX + b2.radius;
		y1 = b1.pY + b1.radius;
		y2 = b2.pY + b2.radius;

		//the first thing that we need to do is move one of the balls to make it a perfect collision on the edges
		//since there should always be at least a very slight overlap from non actual smooth movement
		//(NOTE: This probably should later be replaced by a method inside of the move method to prevent overlap instead of correcting it)
		dxr = (x2 - x1); 
		dyr = (y2 - y1);

		distance = Math.sqrt(dxr * dxr + dyr * dyr); //gets the ACTUAL distance between the two balls

		//radSum adds radii, this gives the ideal distance
		dx = radSum(b1,b2) * dxr / distance; //this is how much we much change X and Y coords to get a perfect collision
		dy = radSum(b1,b2) * dyr / distance;


		//so that things dont look screwy when we have a fast tiny ball collide with a slow big one
		//we want it to be the small ball that will have its position adjusted

		if (b1.mass < b2.mass)
		{
			x1 = (x1 - (dx - dxr));
			y1 = (y1 - (dy - dyr));

			b1.pX = x1 - b1.radius;
			b1.pY = y1 - b1.radius;
		}
		else
		{
			x2 = (x2 + (dx - dxr));
			y2 = (y2 + (dy - dyr));

			b2.pX = x2 - b2.radius;
			b2.pY = y2 - b2.radius;
		}		

		//Find the x and y distances from the centers of each ball to the collision point
		dx1 = (b1.radius / radSum(b1,b2)) * (x2 - x1);
		dx2 = (b2.radius / radSum(b1,b2)) * (x2 - x1);

		dy1 = (b1.radius / radSum(b1,b2)) * (y2 - y1);
		dy2 = (b2.radius / radSum(b1,b2)) * (y2 - y1);

		//calculate the components of velocity of each ball headed towards the collision point and perpendicular to it
		vs1 = straightVelocity(b1.vX, b1.vY, dx1, dy1, b1.radius);
		vp1 = perpendicularVelocity(b1.vX, b1.vY, dx1, dy1, b1.radius);

		vs2 = straightVelocity(b2.vX, b2.vY, dx2, dy2, b2.radius);
		vp2 = perpendicularVelocity(b2.vX, b2.vY, dx2, dy2, b2.radius);

		//use the formulas in the method to find new straight velocities for each ball
		newvs1 = collisionVelocity(vs1, vs2, b1.mass, b2.mass);
		newvs2 = collisionVelocity(vs2, vs1, b2.mass, b1.mass);

		//now we get new X and Y velocities for each, using the new straight velocity and the
		//unaffected perpendicular velocity component

		b1.vX = xVelocity(newvs1, vp1, dx1, dy1, b1.radius);
		b1.vY = yVelocity(newvs1, vp1, dx1, dy1, b1.radius);

		b2.vX = xVelocity(newvs2, vp2, dx2, dy2, b2.radius);
		b2.vY = yVelocity(newvs2, vp2, dx2, dy2, b2.radius);
	}
	public double straightVelocity(double vX, double vY, double dx, double dy, double r)
	{
		return vX * dx / r + vY * dy / r;
	} //velocity directed towards collision
	public double perpendicularVelocity(double vX, double vY, double dx, double dy, double r)
	{
		return vY * dx / r - vX * dy / r;
	}//velocity perpendicular to collision
	public double xVelocity(double vs, double vp, double dx, double dy, double r)
	{
		return vs * dx / r - vp * dy / r;
	} //x velocity from S and P
	public double yVelocity(double vs, double vp, double dx, double dy, double r)
	{
		return vs * dy / r + vp * dx / r;
	} //y velocity from S and P
	public double collisionVelocity(double v1, double v2, double m1, double m2) //Returns velocity of a ball after collision
	{
		return v1 * (m1-m2) / (m1+m2) + v2 * (2 * m2) / (m1 + m2);
	}
	public double distance(double x1,double y1,double x2,double y2)
	{
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	public void skill(int n){
			
		switch (ball[n].skill)
		{
			case 0:
				for(int i = 0 ; i < 8 ; i ++){
					if(ball[n].owner != ball[i].owner)
						ball[i].hp /= 2;
				}
				break;
			case 1:
				for(int i = 0 ; i < 6 ; i ++){
					if(ball[n].owner != ball[i].owner)
						ball[i].cd = 0;
				}
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
		}
	}
}