package main;

import java.awt.*;


public class Game extends GameBase implements Runnable {
	
	//monitor dimensions: 1920 x 1080
	//laptop dimensions: 1536 x 864
	
	GameScreen screen;
	static final int screenW = 1536;
	static final int screenH = 864;
	
	int x = 200;
	int y = 200;
	
	
	/*
	 * Tilemap Test 
	 */
	
	public static final int tileS = 32; //scaling factor for the map
	
//	String[] map = 
//		{
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"...####################.........................",
//			"......................#.........................",
//			"...##################.#.........................",
//			"....................#.#.........................",
//			"....................#.#.........................",
//			"....................#.#.........................",
//			"....................#.#.........................",
//			"....................#.#.........................",
//			"....................#.##########################",
//			"....................#...........................",
//			"....................############################",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................",
//			"................................................"
//			
//		};
//	
	Rect r = new Rect(10, 20, tileS, tileS);
	
	
	
	String[] snailPose = {"DN", "UP", "RT"};
	Sprite fireBug = new Sprite("img/Firebug", snailPose, 100, 432, 80, 80, 3, 20);
	
	Image map = Toolkit.getDefaultToolkit().getImage("img/Map.png");
	
	Circle c = new Circle(300, 200, 40, 0);
	
	public Game() {
		screen = new GameScreen(screenW, screenH, this);
		
		//setting up the JFrame
		setSize(screenW, screenH);
		setTitle("My Tower Defense Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		add(screen);
		pack();
		setVisible(true);
		
		init();
		
	}
	
	public final void init() {		
		addKeyListener(this);
		requestFocus();
		
		addMouseListener(this);	
		addMouseMotionListener(this);
	
		Thread t = new Thread(this);
		t.start();
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
	
//		//First tilemap video cont from 49:18
//		if (pressing[_W]) {
//			//y-= 5;
//			r.moveBy(0, -tileS/8);
//		}
//		if (pressing[_S]) {
//			//y-= 5;
//			r.moveBy(0, tileS/8);
//		}
//		if (pressing[_A]) {
//			//y-= 5;
//			r.moveBy(-tileS/8, 0);
//		}
//		if (pressing[_D]) {
//			//y-= 5;
//			r.moveBy(tileS/8, 0);
//		}
		fireBug.moving = false;
		
		if(pressing[UP]) fireBug.moveUP(2);
		if(pressing[DN]) fireBug.moveDN(2);
		if(pressing[RT]) fireBug.moveRT(2);
		
		
		
	
	}
	
	public void draw(Graphics pen) {
		pen.drawImage(map, 0, 0, screenW, screenH, null);
	
		pen.setColor(Color.RED);
		
		fireBug.draw(pen);
		
		c.draw(pen);
		
		
		
//		for(int row = 0; row < map.length; row++) {
//			for (int col = 0; col < map[row].length(); col++) {
//				char c = map[row].charAt(col);
//				
//				if (c == '.') {
//					pen.setColor(Color.LIGHT_GRAY);
//				}
//				if (c == '#') {
//					pen.setColor(Color.BLACK);
//				}
//				pen.fillRect(tileS*col, tileS*row, tileS, tileS);
//				
//			}
//		}
//		
//		r.draw(pen);
		
		
	}
	

	public static void main(String[] args) {
		
		new Game();
		
		
	}
	

}
