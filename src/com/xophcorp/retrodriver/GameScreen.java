package com.xophcorp.retrodriver;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


class GameScreen extends Canvas implements Runnable, KeyListener
{
	private static int MAP_UNITS_PER_SCREEN = 50;//50;//this could control elevation?

	int begin_horizon = 300;//y location of the beginning of the horizon

	private static final int TURNING_DEFINITION = 0;
	private static final int FEATURE_DEFINITION = 1;

	int Map1 [][] = new int [][]
	{
		//Turning map (negative #s mean turning left, posative #s mean turning right)
		//{0, 0, 0, 20, 20, 20, 40, 40, 40, 60, 60, 60, 80, 80, 80, 100, 100, 100, 120, 120, 120, 140, 140, 140, 160, 160, 160, 180, 180, 180},
		//{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 75, 74, 73, 72, 71, 70, 69, 68, 67, 66, 65, 64, 63, 62, 61, 60, 59, 58, 57, 56, 55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
		{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, 155, 160, 165, 170, 175, 180, 185, 190, 195, 200, 205, 210, 215, 220, 225, 230, 235, 240, 245, 250, 255, 260, 265, 270, 275, 280, 285, 290, 295, 300, 295, 290, 285, 280, 275, 270, 265, 260, 255, 250, 245, 240, 235, 230, 225, 220, 215, 210, 205, 200, 195, 190, 185, 180, 175, 170, 165, 160, 155, 150, 145, 140, 135, 130, 125, 120, 115, 110, 105, 100, 95, 90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0, 0},
		//Feature map (tunnel, bridge, puddle, pitstop, etc)
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
	};

	BufferStrategy strat;
	double map_progress = 0;
	double speed = 0;

	java.text.DecimalFormat df = new java.text.DecimalFormat ();

	GameScreen()
	{
		setVisible(true);
		this.setIgnoreRepaint(true);

		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingSize(0);
	}

	public void run ()
	{
		// create an accelerated image of the right size to store our sprite in
		//GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		//Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);

		createBufferStrategy(2);
		strat = getBufferStrategy();

		long lastmili = System.currentTimeMillis();
		while (this.isVisible())
		{
			long deltamili = System.currentTimeMillis() - lastmili;
			lastmili = System.currentTimeMillis();

			for (int cnt = 0;cnt < (deltamili / 10);cnt++)
			{
				Render((int)deltamili);
			}
			Paint();
			try{Thread.currentThread().sleep(10);}
			catch (Exception e){}
		}

	}

	private void Paint() //display text on canvas
	{
		Graphics2D g = (Graphics2D) strat.getDrawGraphics(); // cast to 2D
		g.clearRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setBackground(Color.white);

		//g.drawString("Hello World!", 1, 15);


		//g.drawOval((int)oldpos.getX() - 5, (int)oldpos.getY() - 5, 10, 10);



		//all of below belongs in its own method: DrawScene (g, map_progress, Map1);

		int map_progress_i = (int)Math.floor(map_progress);
		int row_count = 0;
		int last_y_level = 0;
		int last_x1 = 0;
		int last_x2 = 0;

		double bar_height_modifier = 0;
		//double bar_height_modifier = ((MAP_UNITS_PER_SCREEN / 2) * 0.02);

		while (row_count < MAP_UNITS_PER_SCREEN)
		{
			bar_height_modifier += 0.15;
			//bar_height_modifier *= 1.2;
			double bar_height = (begin_horizon / MAP_UNITS_PER_SCREEN) * bar_height_modifier;

			double bar_adjust = ((map_progress - Math.floor(map_progress))) * (bar_height);
//			if (row_count == 1)
//			{
//				System.out.println(bar_adjust);
//			}

			int y_level = (int)((row_count * bar_height) + (bar_adjust * 2));

			y_level += begin_horizon;

			//g.drawLine (0, y_level, 800, y_level);

			if (map_progress_i % 2 == 0)	g.setColor(new Color (0, 0.9f, 0));
			else							g.setColor(new Color (0, 1f, 0));
			g.fillRect (0, y_level, getWidth(), last_y_level);

			g.setColor(Color.blue);
			g.drawString("map_progress: " + map_progress_i + " map: " + Map1 [TURNING_DEFINITION][map_progress_i] + " adj: " + df.format(bar_adjust), 0, y_level + 15);
			int x1 = (int)(((double)getWidth() / 2d) - ((row_count * bar_height) * 1) - (bar_adjust * 2) - 100) + Map1 [TURNING_DEFINITION] [map_progress_i];
			int x2 = (int)(((double)getWidth() / 2d) + ((row_count * bar_height) * 1) + (bar_adjust * 2) + 100) + Map1 [TURNING_DEFINITION] [map_progress_i];

			if (last_x1 == 0 && last_x2 == 0 && last_y_level == 0)
			{
				last_x1 = x1;
				last_x2 = x2;
				last_y_level = y_level;
			}
			//draw track here
//			g.drawOval(x1, y_level, 3, 3);
//			g.drawOval(x2, y_level, 3, 3);

			g.drawLine(x1, y_level, last_x1, last_y_level);
			g.drawLine(x2, y_level, last_x2, last_y_level);

			last_x1 = x1;
			last_x2 = x2;
			last_y_level = y_level;


			if (map_progress_i <  Map1 [FEATURE_DEFINITION].length)
			{
				if (Map1 [FEATURE_DEFINITION][map_progress_i] == 1)
				{
					g.drawOval(x1, y_level, 3, 3);
				}
				else if (Map1 [FEATURE_DEFINITION][map_progress_i] == 2)
				{
					g.drawString("Pit Stop", x1, y_level);
				}
				//tunnel
				else if (Map1 [FEATURE_DEFINITION][map_progress_i] == 3 && y_level < this.getHeight())
				{
					g.setColor(Color.black);

					int tunnel_height = (int)(300 + (bar_height * 10));

					g.fillRect(x1 - 500, y_level - tunnel_height, 500, tunnel_height);
					g.fillRect(x2, y_level - tunnel_height, 500, tunnel_height);

					g.fillRect(x1 - 50, y_level - tunnel_height - 300, (x2 - x1) + 100, 300);
				}


			}



			map_progress_i--;
			row_count++;
			if (map_progress_i < 0)
			{
				map_progress_i += Map1 [TURNING_DEFINITION].length;
			}
		}

		g.dispose();
		strat.show();
	}

	//Render!
	//here, mili tells us how much to render a scene. mili is the amount of time that has passed since the last render
	private void Render(int mili)
	{
		//System.out.print(mili + ", ");


		//oldpos = (java.awt.Point.Double)pos.clone();


		//move pos towards newpos

		//double angle = Math.atan2(newpos.getX() - pos.getX(), newpos.getY() - pos.getY());

		//speed = Math.min(1d/(Math.abs((angle + 3.2d) - (old_angle + 3.2d))), 1d);
//		double new_speed = (Math.abs ((angle + 3.2d) - (old_angle + 3.2d))) * 10;
//		//System.out.println ((angle) + "\t\t" + df.format  (new_speed));
//		if (new_speed > 0.001)
//		{
//			//System.out.println ((angle) + "\t\t" + df.format  (1/new_speed));
//			speed = Math.max(Math.min(1/new_speed, 2), 0.1);
//		}
//		else if (new_speed == 0)
//		{
//			speed = Math.min(speed + 0.05, 2);
//		}
//		//System.out.println(df.format(0.0001d/(Math.abs((angle + 3.2d) - (old_angle + 3.2d)))));
//
//		double xmove = Math.sin (angle) * speed;
//		double ymove = Math.cos (angle) * speed;
//		//System.out.println(angle + ", " + xmove + ", " + ymove);
//		//pos.setLocation((double)pos.getX() + xmove, (double)pos.getY() + ymove);
//
//		old_angle = angle;
		//pos = newpos;


//		//try to draw the normal
//		double deltax = (-pts [0] + oldpts [0]);
//		double deltay = (-pts [1] + oldpts [1]);
//
//		double deltadist =Math.sqrt (((deltax * deltax) + (deltay *deltay)));
//
//		g.setColor(Color.red);
//		int NormalLength = 20;
//		g.drawLine((int)pts [0], (int)pts [1], (int)(pts [0] + ((-deltay/deltadist) * NormalLength)), (int)(pts [1] + ((deltax/deltadist) * NormalLength)));
//		g.drawLine((int)pts [0], (int)pts [1], (int)(pts [0] - ((-deltay/deltadist) * NormalLength)), (int)(pts [1] - ((deltax/deltadist) * NormalLength)));

		//System.out.println(type + " " + nrmpts [0] + " " + nrmpts [1] + " " + deltadist + " " + deltax + " " + deltay);

		//this is the start of a new line. reset the old position here.
//		if (type == 1)
//		{
//			oldpos = (java.awt.Point)pos.clone();
//		}

		map_progress += speed * ((double)mili / 1000d);

		//int MAX_SPEED = 1;
		//if (speed > MAX_SPEED)speed = MAX_SPEED;

		if (map_progress >= Map1 [TURNING_DEFINITION].length)
		{
			map_progress -= Map1 [TURNING_DEFINITION].length;
		}
		else if (map_progress < 0)
		{
			map_progress += Map1 [TURNING_DEFINITION].length;
		}

	}
	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == e.VK_ESCAPE)System.exit(0);
		else if (e.getKeyCode() == e.VK_LEFT);
		else if (e.getKeyCode() == e.VK_RIGHT);
		else if (e.getKeyCode() == e.VK_UP)speed += 0.1;
		else if (e.getKeyCode() == e.VK_DOWN)speed -= 0.1;

		else if (e.getKeyCode() == e.VK_A)begin_horizon += 1;
		else if (e.getKeyCode() == e.VK_Z)begin_horizon -= 1;

	}

	public void keyReleased(KeyEvent e)
	{
	}
}
