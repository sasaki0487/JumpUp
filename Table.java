import java.awt.*;
import javax.swing.*;

class Table
{
	double left, top, width, height, edgeLength;
	int[] polx = new int[8];
	int[] poly = new int[8];

	public Table(double l, double t, double w, double h, double e)
	{
		left = l;
		top = t;
		width = w;
		height = h;
		edgeLength = e;
		
		int[] polx = {(int)((left+edgeLength) * Global.DRAWSCALE),
					(int)((left+width-edgeLength) * Global.DRAWSCALE),
					(int)((left+width) * Global.DRAWSCALE),
					(int)((left+width) * Global.DRAWSCALE),
					(int)((left+width-edgeLength) * Global.DRAWSCALE),
					(int)((left+edgeLength) * Global.DRAWSCALE),
					(int)((left) * Global.DRAWSCALE),
					(int)((left) * Global.DRAWSCALE)
					};
		int[] poly = {(int)((top) * Global.DRAWSCALE),
					(int)((top) * Global.DRAWSCALE),
					(int)((top+edgeLength) * Global.DRAWSCALE),
					(int)((top+height-edgeLength)* Global.DRAWSCALE),
					(int)((top+height) * Global.DRAWSCALE),
					(int)((top+height) * Global.DRAWSCALE),
					(int)((top+height-edgeLength) * Global.DRAWSCALE),
					(int)((top+edgeLength) * Global.DRAWSCALE)
					};
		this.polx = polx;
		this.poly = poly;
	}
	public double minX()
	{
		return left;
	}
	public double minY()
	{
		return top;
	}
	public double maxX()
	{
		return left + width;
	}
	public double maxY()
	{
		return top + height;
	}
	public double  edge()
	{
		return edgeLength;
	}
	public void draw(Graphics g, Color tableColor)
	{
		g.setColor(tableColor);
		g.fillPolygon(polx,poly,8);	
		drawBorder(g);
	}
	public void drawBorder(Graphics g)
	{
		g.setColor(new Color(100,100,100));
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(new GradientPaint(0,0,new Color(255,255,80),Global.WINDOWWIDTH,Global.WINDOWHEIGHT,new Color(130,170,210)));
		g2d.setStroke(new BasicStroke(20));
		g2d.drawPolygon(polx,poly,8);
	}
} 