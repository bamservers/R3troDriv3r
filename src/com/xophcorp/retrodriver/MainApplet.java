package com.xophcorp.retrodriver;

import javax.swing.*;
import java.awt.Container;

public class MainApplet extends JApplet
{
	public void start()
	{
		//setBounds(120,60,800,600);

		Container con = this.getContentPane(); //inherit main frame
//		this.setUndecorated(true);
		this.setVisible(true);
		con.setVisible(true);
		//this.getContentPane().setLayout(null);
		//con.setLayout(new FlowLayout(FlowLayout.CENTER));

		GameScreen gs = new GameScreen();
		gs.setSize(this.getWidth(), this.getHeight());
		con.add(gs);
		con.addKeyListener(gs);
		addKeyListener(gs);
		gs.addKeyListener(gs);
		con.requestFocus();
		con.requestFocusInWindow();//makes sure we're focused all the time
		new Thread (gs).start();
	}

}
