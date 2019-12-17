import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Client implements Runnable{
	public DataInputStream input;
	public DataOutputStream output;
	Ball[] ball;
	Socket skt;
	public Client(Ball[] ball){
		this.ball = ball ;
		try{
			skt = new Socket("localhost",8000);
			input = new DataInputStream(skt.getInputStream());
			output = new DataOutputStream(skt.getOutputStream());
			
		}
		catch(IOException ex){System.err.println(ex);}
	}
	public void run()
	{
		try
		{
			while(true)
			{
				for(int i = 0 ; i < 8 ; i ++)
				{
					output.writeDouble(ball[i].pX);
					//ball[i].pX = input.readDouble();
					output.writeDouble(ball[i].pY);
					//ball[i].pY = input.readDouble();
					output.writeDouble(ball[i].vX);
					//ball[i].vX = input.readDouble();
					output.writeDouble(ball[i].vY);
					//ball[i].vY = input.readDouble();
					output.writeDouble(ball[i].hp);
					//ball[i].hp = input.readDouble();
					output.writeDouble(ball[i].cd);
					//ball[i].cd = input.readDouble();
				}
				try { Thread.sleep(3000); }
				catch (InterruptedException ex) { System.err.println(ex);}
			}
		}
		catch(IOException ex){System.err.println(ex);}
		
		
	}
}