package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class Game extends GameBase implements Runnable {
	
	//monitor dimensions: 1920 x 1080
	//laptop dimensions: 1536 x 864
	
	int mx = -1; //mouse coordinates
	int my = -1;

	
	GameScreen screen;
	static final int screenW = 1536;
	static final int screenH = 864;
	static int toolBarHeight = 0;
	
	int x = 200;
	int y = 200;
	
	ResizeRect[] waypoints;
	int currWaypoint = 0;
	
	/*
	 * Tilemap Test 
	 */
	
	public static final int tileS = 32; //scaling factor for the map
	
	Rect r = new Rect(10, 20, tileS, tileS);
	
	
	String[] pose = {"DN", "UP", "RT"};
	Sprite fireBug = new Sprite("img/GreenSnail", pose, 16, 460, 48, 48, 3, 20);
	
	Image map = Toolkit.getDefaultToolkit().getImage("img/Map.png");
	
	public Game() {
		screen = new GameScreen(screenW, screenH, this);
		
		//setting up the JFrame
		setSize(screenW, screenH);
		setTitle("My Tower Defense Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		//setUndecorated(true);
		add(screen);
		pack();
		setVisible(true);
		toolBarHeight = getInsets().top;
		
		init();
		
	}
	
	public final void init() {		
		addKeyListener(this);
		requestFocus();
		
		addMouseListener(this);	
		addMouseMotionListener(this);
	
		Thread t = new Thread(this);
		t.start();
		
		waypoints = new ResizeRect[]{
				new ResizeRect(306, 410, 26, 96),
				new ResizeRect(259, 175, 114, 30),
				new ResizeRect(920, 205, 32, 89),
				new ResizeRect(853, 407, 69, 34),
				new ResizeRect(972, 355, 41, 66),
				new ResizeRect(924, 711, 74, 34),
				new ResizeRect(1176, 646, 36, 63),
				new ResizeRect(1128, 527, 107, 34)		
		};

	}
	
	
	public void run() {
		// Game Loop
		while(true)
		{
			update();
			
			screen.repaint();
						
			try
			{			
				Thread.sleep(15);
			}
			catch(Exception x) {};
		}
	}
	
	
	public void update() {
		//where any movements and key presses/mouse movements happen/are calculated

		fireBug.moving = false;
		
//		if(pressing[UP]) fireBug.moveUP(2);
//		if(pressing[DN]) fireBug.moveDN(2);
//		if(pressing[RT]) fireBug.moveRT(2);
//		
//		for(int i = 0; i < waypoints.length; i++) {
//			if(fireBug.overlaps(waypoints[i])) {
//				fireBug.pushOutOf(waypoints[i]);
//			}
//		}
		
		
		if(currWaypoint < waypoints.length) {
			fireBug.chase(waypoints[currWaypoint], 2);
			
			if (fireBug.overlaps(waypoints[currWaypoint])) {
				fireBug.pushOutOf(waypoints[currWaypoint]);
				currWaypoint++;
			}
		}
		else {
			fireBug.moveRT(2);
		}
	
	}
	
	public void draw(Graphics pen) {
		pen.drawImage(map, 0, 0, screenW, screenH, null);
	
		pen.setColor(Color.RED);
		
		fireBug.draw(pen);
	
		
		for(ResizeRect waypoint : waypoints) {
			waypoint.draw(pen);
		}
			
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		//new x and y 
		int nx = e.getX();
		int ny = e.getY() - toolBarHeight;
		
		int dx = nx - mx;
		int dy = ny - my;
//		
//		for(int i = 0; i < waypoints.length; i++)
//		{
//			if(waypoints[i].resizer.held) {
//				waypoints[i].resizeBy(dx, dy);
//			}
//			else if(waypoints[i].held) {
//				waypoints[i].moveBy(dx, dy);
//			}
//			
//		}
		//"For each resize rect waypoint in waypoints array..."
		for(ResizeRect waypoint : waypoints) {
			if(waypoint.resizer.held) {
				waypoint.resizeBy(dx, dy);
			}
			else if(waypoint.held) {
				waypoint.moveBy(dx, dy);
			}
		}
		
		//set mouse coordinates to new coordinate
		mx = nx;
		my = ny;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		mx = e.getX();
		my = e.getY() - toolBarHeight;
		
		//System.out.println(waypoints[0].resizer.toString());
		System.out.println("" + mx + "," + my);
		
		for(int i = 0; i < waypoints.length; i++)
		{
			if(waypoints[i].contains(mx,  my))  waypoints[i].grabbed();
			if(waypoints[i].resizer.contains(mx,  my))  waypoints[i].resizer.grabbed();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		for(int i = 0; i < waypoints.length; i++)
		{
			waypoints[i].dropped();
			waypoints[i].resizer.dropped();
		}
	}
	
	public void keyTyped(KeyEvent e) {
		char keyChar = Character.toLowerCase(e.getKeyChar());
		if(keyChar == 'p') {
			for(int i = 0; i < waypoints.length; i++)
			{
				System.out.println(waypoints[i]);
			}
		}
	}
	

	public static void main(String[] args) {
		
		new Game();
		
		
	}
	

}
