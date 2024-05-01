package main;

import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;



public class Game extends GameBase implements Runnable {
	
	int mx = -1; //mouse coordinates
	int my = -1;

	
	GameScreen screen;
	//ask system for its screen width and height
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int SCREEN_W = device.getDisplayMode().getWidth ();
	public static final int SCREEN_H = device.getDisplayMode().getHeight();
	
	static int toolBarHeight = 0;
	
	public static int TILE_SIZE = SCREEN_W / 30;
	
	ResizeRect[] waypoints;
	Rect[] arrow = new Rect[15];

	ArrayList<Enemy> enemies;
	ArrayList<Tower> towers;
	
	Color validColor = new Color(0, 255, 0, 30);
	Color invalidColor = new Color(255, 0, 0, 30);
	
	Tower tower;
	TowerShop towerShop;

	Image map = Toolkit.getDefaultToolkit().getImage("img/Map.png");
	
	public Game() {
		screen = new GameScreen(SCREEN_W, SCREEN_H, this);
		
		//setting up the JFrame
		setSize(SCREEN_W, SCREEN_H);
		setTitle("My Tower Defense Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		add(screen);
		pack();
		setVisible(true);
		toolBarHeight = getInsets().top;
		
		init();
		
	}
	
	public void init() {		
		super.init();
		
		waypoints = new ResizeRect[]{
				new ResizeRect(6 *TILE_SIZE, 8 *TILE_SIZE,   TILE_SIZE, 2*TILE_SIZE),
				new ResizeRect(5 *TILE_SIZE, 3 *TILE_SIZE, 2*TILE_SIZE,   TILE_SIZE),
				new ResizeRect(18*TILE_SIZE, 4 *TILE_SIZE,   TILE_SIZE, 2*TILE_SIZE),
				new ResizeRect(17*TILE_SIZE, 8 *TILE_SIZE,   TILE_SIZE,   TILE_SIZE),
				new ResizeRect(19*TILE_SIZE, 7 *TILE_SIZE,   TILE_SIZE, 2*TILE_SIZE),
				new ResizeRect(18*TILE_SIZE, 14*TILE_SIZE,   TILE_SIZE, 2*TILE_SIZE),
				new ResizeRect(23*TILE_SIZE, 13*TILE_SIZE,   TILE_SIZE,   TILE_SIZE),
				new ResizeRect(22*TILE_SIZE, 10*TILE_SIZE, 2*TILE_SIZE,   TILE_SIZE)		
		};
		
		enemies = new ArrayList<>();
		enemies.add(new Enemy("img/YellowSnail", 1));
		enemies.add(new Enemy("img/GreenSnail", 2));
		enemies.add(new Enemy("img/RedSnail", 3));
		enemies.add(new Enemy("img/Firebug", 4));
		
	
		tower = new Tower(pixelOf(6), pixelOf(5), 1, "UP");
		towerShop = new TowerShop(pixelOf(25), pixelOf(1), pixelOf(4), pixelOf(7));
		
		towers = new ArrayList<>();
		
		for (int i = 0; i < arrow.length; i++) {
			arrow[i] = new Rect(-1000, 0, 8, 8);
		}

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
		tower.inRange = false;
		for(Enemy enemy : enemies) {
			if(enemy.currWaypoint < waypoints.length) {
				enemy.chase(waypoints[enemy.currWaypoint], enemy.getSpeed());

				if (enemy.overlaps(waypoints[enemy.currWaypoint])) {
					enemy.pushOutOf(waypoints[enemy.currWaypoint]);
					enemy.currWaypoint++;
				}
				
				if (enemy.overlaps(tower.getRange())) {
					tower.inRange = true;
				}
			}
			else {
				enemy.moveRT(enemy.getSpeed());	
			}
			
			/*-----Not sure how to handle this part-------*/
			if (tower.inRange) {
				tower.shoot(arrow);
			}
			
			for (int i = 0; i < arrow.length; i++) {
				arrow[i].move();
				if(arrow[i].overlaps(enemy)) {
					enemy.x = -1000;
				}
			}
		}
		
		gameOver();
		
	}
	
	public void draw(Graphics pen) {
		pen.drawImage(map, 0, 8, SCREEN_W, SCREEN_H, null);
		
		
		for(Enemy enemy : enemies) {
			enemy.draw(pen);
		}
		
		for(Rect waypoint : waypoints) {
			waypoint.draw(pen);
		}
		
		tower.draw(pen);
		towerShop.draw(pen);
		
		for(int i = 0; i < arrow.length; i++) {
			arrow[i].draw(pen);;
		}
		
		if(towerShop.selectedTower != null) {
			towerShop.selectedTower.range.fill(pen);
			towerShop.selectedTower.draw(pen);
			
		}
		
		for(Tower playTowers : towers) {
			playTowers.draw(pen);
		}		
	}
	
	public int pixelOf(int tile) { //converts tile position to it's corresponding pixel location
		return tile * TILE_SIZE;
	}
	
	public void gameOver() {
		Iterator<Enemy> iterator = enemies.iterator();
		while (iterator.hasNext()) 
		{
			Enemy enemy = iterator.next();
			if (enemy.x >= SCREEN_W)
			{	
				System.out.println("lives lost");
				iterator.remove();
			}
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

		//"For each resize rect waypoint in waypoints array..."
		for(ResizeRect waypoint : waypoints) {
			if(waypoint.resizer.held) {
				waypoint.resizeBy(dx, dy);
			}
			else if(waypoint.held) {
				waypoint.moveBy(dx, dy);
			}
		}

		
		if(towerShop.selectedTower != null) {
			if(towerShop.selectedTower.held) { 
				towerShop.selectedTower.setLocation((mx/TILE_SIZE) * TILE_SIZE, (my/TILE_SIZE) * TILE_SIZE); //"snaps" tower in place 
			}
		}
		
		boolean isValid = true;
		if(towerShop.selectedTower != null) {
			for(Rect waypoint : waypoints) {
				if(towerShop.selectedTower.overlaps(waypoint)) {
					isValid = false;
					System.out.println("overlapping");
				}
			}
			if(isValid) {
				towerShop.selectedTower.range.setColor(validColor);
			}
			else {
				towerShop.selectedTower.range.setColor(invalidColor);
			}
			System.out.println(towerShop.selectedTower);
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
		
		System.out.println("" + mx + "," + my);
		
		for(int i = 0; i < waypoints.length; i++)
		{
			if(waypoints[i].contains(mx,  my))  waypoints[i].grabbed();
			if(waypoints[i].resizer.contains(mx,  my))  waypoints[i].resizer.grabbed();
		}
		
//		for(Tower currTower : towerShop.towers) {
//			if(currTower.contains(mx, my) && (e.getButton() == MouseEvent.BUTTON1)) { //left click
//				towerShop.setSelected(currTower.getCopyOf(currTower));
//				towerShop.selectedTower.grabbed();
//				return;
//			}
//		}
		
		for(Tower currTower : towerShop.towers) {
			if(currTower.contains(mx, my) && (e.getButton() == MouseEvent.BUTTON1)) { //left click
				towerShop.setSelected(currTower.getCopy());
				towerShop.getSelected().grabbed();
				return;
			}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		mx = e.getX();
		my = e.getY() - toolBarHeight;
		
		if((e.getButton() == MouseEvent.BUTTON3) && towerShop.selectedTower != null) { //right click
			towers.add(towerShop.selectedTower);
			towerShop.selectedTower.dropped();
			towerShop.selectedTower = null;
		}
	
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		mx = e.getX();
		my = e.getY() - toolBarHeight;

		if(towerShop.selectedTower != null) {
			if(towerShop.selectedTower.held) { 
				towerShop.selectedTower.setLocation((mx/TILE_SIZE) * TILE_SIZE, (my/TILE_SIZE) * TILE_SIZE); //"snaps" tower in place 
			}
		}
		
		boolean isValid = true;
		if(towerShop.selectedTower != null) {
			for(Rect waypoint : waypoints) {
				if(towerShop.selectedTower.overlaps(waypoint)) {
					isValid = false;
					System.out.println("overlapping");
				}
			}
			if(isValid) {
				towerShop.selectedTower.range.setColor(validColor);
			}
			else {
				towerShop.selectedTower.range.setColor(invalidColor);
			}
			System.out.println(towerShop.selectedTower);
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
