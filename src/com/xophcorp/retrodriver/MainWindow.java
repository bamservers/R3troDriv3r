package com.xophcorp.retrodriver;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MainWindow extends JFrame
{

	public MainWindow()
	{
		super("RetroDriver"); setBounds(120,60,800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});

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
		//KeyboardFocusManager.getCurrentKeyboardFocusManager()
		new Thread (gs).start();
	}

	public static void main(String[] args) {new MainWindow();}
}
