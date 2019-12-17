import java.awt.*;
import javax.swing.*;

public class JumpUp
{
	public static void main(String[] args)
	{
		JFrame jumpup = new JFrame();
		//Overpart overpart = new Overpart(jumpup,true);
		Startpart starttpart = new Startpart(jumpup);
		jumpup.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jumpup.setVisible(true);
		jumpup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}