package graphcalc;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;



@SuppressWarnings("serial")
public class graphcalc extends JPanel  implements  MouseListener
{	
	public graphcalc()
	{
		addMouseListener(this);	 
	}

	public static final int size = 21;

	//these variables store the coordinates of the camera. 
	double camerax = 0;
	double cameray = 0;
	double cameraz = 0;
	double camerayrotation  = 0;
	double cameraxrotation  = 0;
	double camerazrotation  = 0;
	//these variables store the coordinates of the vertices.
	double cube1x =-10;
	double cube1y =-10;
	double cube1z =-10;

	double cube2x = 10;
	double cube2y =-10;
	double cube2z =-10;

	double cube3x = 10;
	double cube3y = 10;
	double cube3z =-10;

	double cube4x =-10;
	double cube4y = 10;
	double cube4z =-10;

	double cube5x =-10;
	double cube5y =-10;
	double cube5z = 10;

	double cube6x = 10;
	double cube6y =-10;
	double cube6z = 10;

	double cube7x = 10;
	double cube7y = 10;
	double cube7z = 10;

	double cube8x =-10;
	double cube8y = 10;
	double cube8z = 10;

	// these three arrays will store the cube vertex coordinates.
	double[] realcubexvalues = {cube1x, cube2x, cube3x, cube4x, cube5x, cube6x, cube7x, cube8x};
	double[] realcubeyvalues = {cube1y, cube2y, cube3y, cube4y, cube5y, cube6y, cube7y, cube8y};
	double[] realcubezvalues = {cube1z, cube2z, cube3z, cube4z, cube5z, cube6z, cube7z, cube8z};

	double[] cubexvalues = {0, 0, 0, 0, 0, 0, 0, 0};
	double[] cubeyvalues = {0, 0, 0, 0, 0, 0, 0, 0};
	double[] cubezvalues = {0, 0, 0, 0, 0, 0, 0, 0};
	//These two arrays will store the screen x and y values that the vertices will have on the screen.
	int[] xpos = {0,0,0,0,0,0,0,0};
	int[] ypos = {0,0,0,0,0,0,0,0};

	double[] gridX = new double[size*size];
	double[] gridZ = new double[size*size];
	double[] gridY= new double[size*size];

	double[] screengridX= new double[size*size];
	double[] screengridY= new double[size*size];
	double[] screengridZ= new double[size*size];


	int[] screenX= new int[size*size];
	int[] screenY= new int[size*size];
        
	
	public void initialize() throws InterruptedException
	{
		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				gridX[y*size + x] = (double)(x - size/2);
				gridZ[y*size + x] = (double)(y - size/2);
			}
		}
		
		while(true)
		{
			functions();
			repaint(10);
			Thread.sleep(10);
		}
	}
	
	
	//   https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
	public static double eval(final String str) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}

			// Grammar:
			// expression = term | expression `+` term | expression `-` term
			// term = factor | term `*` factor | term `/` factor
			// factor = `+` factor | `-` factor | `(` expression `)`
			//        | number | functionName factor | factor `^` factor

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if      (eat('+')) x += parseTerm(); // addition
					else if (eat('-')) x -= parseTerm(); // subtraction
					else return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if      (eat('*')) x *= parseFactor(); // multiplication
					else if (eat('/')) x /= parseFactor(); // division
					else return x;
				}
			}

			double parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')) return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z') nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt")) x = Math.sqrt(x);
					else if (func.equals("sin")) x = Math.sin(x);
					else if (func.equals("cos")) x = Math.cos(x);
					else if (func.equals("tan")) x = Math.tan(x);
					else if (func.equals("asin")) x = Math.asin(x);
					else if (func.equals("acos")) x = Math.acos(x);
					else if (func.equals("atan")) x = Math.atan(x);
					else if (func.equals("abs")) x = Math.abs(x);
					else if (func.equals("log")) x = Math.log(x);
					else throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}


	String thingstring = "0";

	double mousex = 0;
	double mousey = 0;
	double pmousex = 0;
	double pmousey = 0;
	int mouseclickedx = 0;
	int mouseclickedy= 0;
	boolean mousedown = false;

	private void graph()
	{		
		for(int i = 0; i< size; i++)
		{
			for(int a = 0; a< size; a++)
			{
				int x = a-10;
				int z = i-10;
				String stringx = Integer.toString(x);
				String stringz = Integer.toString(z);
				String newthingstring = thingstring.replaceAll("x", stringx);
				newthingstring = newthingstring.replaceAll("y", stringz);
				newthingstring = newthingstring.replaceAll("pi", "3.1415");
				double answer = 0;
				try
				{
					answer = (eval(newthingstring));
				}
				catch(java.lang.ArithmeticException e)
				{
					answer = Math.sqrt(-1);
				}
				catch( java.lang.NullPointerException e)
				{
					answer = 0;
					thingstring = oldthingstring;
				}
				catch(java.lang.RuntimeException e)
				{
					answer = 0;
					thingstring = oldthingstring;
					JOptionPane.showMessageDialog(null, "Invalid input.");
				}
				gridY[size*i +a] = -(double)(answer);
			}
		}
	}


	String oldthingstring = "0";
	@Override
	public void mouseClicked(MouseEvent arg0) {
		mouseclickedx = arg0.getX();
		mouseclickedy = arg0.getY();
		if(mouseclickedx > 710 && mouseclickedx < 970 && mouseclickedy > 20 && mouseclickedy < 50)
		{
			oldthingstring = thingstring;
			thingstring = JOptionPane.showInputDialog ("Input z as a function of x and y.");
			graph();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mousedown = true;

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mousedown = false;

	}

	int[] axisXvalues = {-20,20,0,0,0,0};
	int[] axisYvalues = {0,0,-20,20,0,0};
	int[] axisZvalues = {0,0,0,0,-20,20};


	double[] perspectiveaxisXvalues = {0,0,0,0,0,0};
	double[] perspectiveaxisYvalues = {0,0,0,0,0,0};
	double[] perspectiveaxisZvalues = {0,0,0,0,0,0};
	int[] screenaxisXvalues = {0,0,0,0,0,0};
	int[] screenaxisYvalues = {0,0,0,0,0,0};


	private void functions()
	{
		pmousex = mousex;
		pmousey = mousey;
		mousex = (MouseInfo.getPointerInfo().getLocation().getX() -8);
		mousey = (MouseInfo.getPointerInfo().getLocation().getY() -24);

		render();
	}
	private void render()
	{

		if(mousedown == true && mousex < 700 && mousey < 700)
		{

			/*
			   if(mousex-pmousex > 0)
			   {
			   cameraxrotation -= 0.01;
			   }

			   if(mousex-pmousex < 0)
			   {
			   cameraxrotation += 0.01;
			   }

			   if(mousey-pmousey > 0)
			   {
			   camerayrotation -= 0.01;
			   }

			   if(mousey-pmousey < 0)
			   {
			   camerayrotation += 0.01;
			   }*/
			cameraxrotation -= (mousex-pmousex)/180;
			camerayrotation -= (mousey-pmousey)/180;
			//System.out.println((mousex-pmousex));
		}

		double xdeg = 0;
		double ydeg = 0;
		int FOV = 180;
		double focallength = 700/Math.tan(FOV/2);
		for(int i = 0; i < size*size; i++)
		{
			double realxdif = gridX[i];
			double realydif = gridY[i];
			double realzdif = gridZ[i];
			screengridX[i]= (double)(Math.cos(cameraxrotation)*realxdif +  Math.sin(cameraxrotation)*Math.sin(-camerayrotation)*realydif- Math.sin(cameraxrotation)*Math.cos(-camerayrotation)*realzdif);
			screengridY[i]= (double)(0 +  Math.cos(-camerayrotation)*realydif +  Math.sin(-camerayrotation)*realzdif);
			screengridZ[i]= cameraz +(double)(Math.sin(cameraxrotation)*realxdif  +  Math.cos(cameraxrotation)*(-Math.sin(-camerayrotation))*realydif +  Math.cos(cameraxrotation)* Math.cos(-camerayrotation)*realzdif); 

			double xdif = screengridX[i];
			double ydif = screengridY[i];
			double zdif = screengridZ[i]-30;

			xdeg = (focallength*(xdif/zdif));
			ydeg = (focallength*(ydif/zdif));

			screenX[i] = (int)(xdeg) + 350;
			screenY[i] = (int)(ydeg) + 350;	
		}


		for(int i = 0; i < 8; i++)
		{

			double realxdif = realcubexvalues[i] - 0;
			double realydif = realcubeyvalues[i] - 0;
			double realzdif = realcubezvalues[i] - 0;
			cubexvalues[i]= camerax +(double)(Math.cos(cameraxrotation)*realxdif +  Math.sin(cameraxrotation)*Math.sin(-camerayrotation)*realydif- Math.sin(cameraxrotation)*Math.cos(-camerayrotation)*realzdif);
			cubeyvalues[i]= cameray +(double)(0 +  Math.cos(-camerayrotation)*realydif +  Math.sin(-camerayrotation)*realzdif);
			cubezvalues[i]= cameraz +(double)(Math.sin(cameraxrotation)*realxdif  +  Math.cos(cameraxrotation)*(-Math.sin(-camerayrotation))*realydif +  Math.cos(cameraxrotation)* Math.cos(-camerayrotation)*realzdif); 

			double xdif = cubexvalues[i] - camerax;
			double ydif = cubeyvalues[i] - cameray;
			double zdif = (cubezvalues[i]-30);//cameraz);

			xdeg = (focallength*(xdif/zdif));
			ydeg = (focallength*(ydif/zdif));

			xpos[i] = (int)(xdeg) + 350;
			ypos[i] = (int)(ydeg) + 350;	
		}

		for(int i = 0; i < 6; i++)
		{

			double realxdif = axisXvalues[i] - 0;
			double realydif = axisYvalues[i] - 0;
			double realzdif = axisZvalues[i] - 0;
			perspectiveaxisXvalues[i]= camerax +(double)(Math.cos(cameraxrotation)*realxdif +  Math.sin(cameraxrotation)*Math.sin(-camerayrotation)*realydif- Math.sin(cameraxrotation)*Math.cos(-camerayrotation)*realzdif);
			perspectiveaxisYvalues[i]= cameray +(double)(0 +  Math.cos(-camerayrotation)*realydif +  Math.sin(-camerayrotation)*realzdif);
			perspectiveaxisZvalues[i]= cameraz +(double)(Math.sin(cameraxrotation)*realxdif  +  Math.cos(cameraxrotation)*(-Math.sin(-camerayrotation))*realydif +  Math.cos(cameraxrotation)* Math.cos(-camerayrotation)*realzdif); 

			double xdif = perspectiveaxisXvalues[i];
			double ydif = perspectiveaxisYvalues[i];
			double zdif = (perspectiveaxisZvalues[i]-30);

			xdeg = (focallength*(xdif/zdif));
			ydeg = (focallength*(ydif/zdif));

			screenaxisXvalues[i] = (int)(xdeg) + 350;
			screenaxisYvalues[i] = (int)(ydeg) + 350;	
		}
	}


	@Override

	public void paint(Graphics g) 
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		setBackground(Color.black);
		g2d.setPaint(Color.orange);
		g2d.drawLine(screenaxisXvalues[0],screenaxisYvalues[0],screenaxisXvalues[1],screenaxisYvalues[1]);
		g2d.drawLine(screenaxisXvalues[2],screenaxisYvalues[2],screenaxisXvalues[3],screenaxisYvalues[3]);
		g2d.drawLine(screenaxisXvalues[4],screenaxisYvalues[4],screenaxisXvalues[5],screenaxisYvalues[5]);
		g2d.drawString("X_MINIMUM", screenaxisXvalues[0], screenaxisYvalues[0]);
		g2d.drawString("X_MAXIMUM", screenaxisXvalues[1], screenaxisYvalues[1]);
		g2d.drawString("Z_MINIMUM", screenaxisXvalues[3], screenaxisYvalues[3]);
		g2d.drawString("Z_MAXIMUM", screenaxisXvalues[2], screenaxisYvalues[2]);
		g2d.drawString("Y_MINIMUM", screenaxisXvalues[4], screenaxisYvalues[4]);
		g2d.drawString("Y_MAXIMUM", screenaxisXvalues[5], screenaxisYvalues[5]);
		g2d.setPaint(Color.red);
		for(int i = 0; i<size*size; i++)
		{
			//g2d.fillOval(screenX[i], screenY[i], 2, 2);
			for(int a = 0; a<size*size; a++)
			{
				if(gridX[i] == gridX[a]&& (gridZ[i]+1 == gridZ[a]) && screengridZ[i] < 50 && screengridZ[a] < 50)
				{
					g2d.drawLine(screenX[a], screenY[a], screenX[i], screenY[i]);
				}
				if(gridZ[i] == gridZ[a]&& (gridX[i]+1 == gridX[a]) && screengridZ[i] < 50 && screengridZ[a] < 50)
				{
					g2d.drawLine(screenX[a], screenY[a], screenX[i], screenY[i]);
				}
			}

		}
		g2d.setPaint(Color.blue);
		//This connects the points of the cube together.
		g2d.drawLine(xpos[0],ypos[0],xpos[1],ypos[1]);
		g2d.drawLine(xpos[0],ypos[0],xpos[3],ypos[3]);
		g2d.drawLine(xpos[0],ypos[0],xpos[4],ypos[4]);
		g2d.drawLine(xpos[1],ypos[1],xpos[2],ypos[2]);
		g2d.drawLine(xpos[1],ypos[1],xpos[5],ypos[5]);
		g2d.drawLine(xpos[2],ypos[2],xpos[3],ypos[3]);
		g2d.drawLine(xpos[2],ypos[2],xpos[6],ypos[6]);
		g2d.drawLine(xpos[3],ypos[3],xpos[7],ypos[7]);
		g2d.drawLine(xpos[7],ypos[7],xpos[4],ypos[4]);
		g2d.drawLine(xpos[7],ypos[7],xpos[6],ypos[6]);
		g2d.drawLine(xpos[5],ypos[5],xpos[4],ypos[4]);
		g2d.drawLine(xpos[5],ypos[5],xpos[6],ypos[6]);
		g2d.setPaint(Color.gray);
		g2d.fillRect(700, -2000, 4000, 4000);


		g2d.setPaint(Color.white);
		g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
		g2d.fillRect(710, 20, 260, 20);
		g2d.setPaint(Color.black);
		g2d.drawString("INPUT_FUNCTIONS", 713, 35);


	}
	
	public static void main(String[] args) throws InterruptedException 
	{
		JFrame frame = new JFrame("3d graphing calculator");
		graphcalc game = new graphcalc();
		frame.add(game);
		frame.setSize(1000, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.initialize();
	}
}

