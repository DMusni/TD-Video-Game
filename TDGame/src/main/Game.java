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
	
	static int toolBarHeight = 0; //used for mouse events since mouse coordinates are for the entire window, not just the JPanel
	
	public static int TILE_SIZE = 1920 / 30;
	
	ResizeRect[] waypoints;
	ResizeRect[] noZones;
	
	ArrayList<Enemy> enemies;
	ArrayList<Tower> towers; //towers that are added as the player places them on the map
	
	public Enemy[] enemyTypes;
	public int[] enemyCount = {7, 6, 4, 2};
	
	public static ArrayList<Arrow> arrows;
	ArrayList<Enemy> enemyList;
	
	Color validColor = new Color(0, 255, 0, 30); //range color is green 
	Color invalidColor = new Color(255, 0, 0, 30); //range color is red
	
	Tower tower;
	TowerShop towerShop;
	World world;

	Image map = Toolkit.getDefaultToolkit().getImage("img/Map.png");
	
	public Game() {
		screen = new GameScreen(1920, 1080, this);
		
		//setting up the JFrame
		setSize(1920, 1080);
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
		
		//initializing the rectangles that the enemies will follow 
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
		
	
		enemyTypes = new Enemy[] {
				new Enemy("img/YellowSnail", 1, 1),
				new Enemy("img/GreenSnail", 2, 2),
				new Enemy("img/RedSnail", 3, 3),
				new Enemy("img/Firebug", 4, 4),
		};
		
		arrows = new ArrayList<>();
		enemyList = new ArrayList<>();
		
	
		tower = new Tower(pixelOfTile(3), pixelOfTile(6), 1, "UP");
		towerShop = new TowerShop(pixelOfTile(25), pixelOfTile(1), pixelOfTile(4) + 32, pixelOfTile(7));
		world = new World(this, towerShop, enemyCount, enemyTypes);
		
		towers = new ArrayList<>();
		
		initializeNoZones();
		

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
		world.startEnemyWave();
		
		for(Enemy enemy : enemyList) {
			if(enemy.currWaypoint < waypoints.length) { //condition for enemy to chase the "waypoint" at their speed
				enemy.chase(waypoints[enemy.currWaypoint], enemy.getSpeed());

				if (enemy.overlaps(waypoints[enemy.currWaypoint])) { //if the enemy overlaps the waypoint their chasing,
					enemy.pushOutOf(waypoints[enemy.currWaypoint]);  //enemy gets pushed out of the current waypoint
					enemy.currWaypoint++;							 //update the current waypoint their following/chasing
				}
				
			}
			else { //handles when the enemy reaches the last waypoint
				enemy.moveRT(enemy.getSpeed());	
			}	
		}
		
		for(Enemy e : enemyList) {
			tower.setTargeting(false);
			if(e.overlaps(tower.range)) {
				tower.setTargeting(true);
				tower.shootAt(e);
				break;
			}
		}
		
		for(Tower t : towers) {
			t.setTargeting(false); //each tower is set to false before the inner for loop
			for(Enemy e: enemyList) { 
				if(e.overlaps(t.getRange())) {
					t.setTargeting(true); //tower weapon starts animating if enemy is in its range
					t.shootAt(e);
					break;
				}
			}
		}
		

		for(Arrow arrow : arrows) {
			arrow.move();
		}
		
		arrowsDoDamage();
		
		
		gameOver();
		
	}
	
	public void draw(Graphics pen) {
		pen.drawImage(map, 0, 8, 1920, 1080, null);
		
		
		for(Enemy enemy : enemyList) {
			if(enemy.isAlive()) {
				enemy.draw(pen);
			}
		}
		
//		for(ResizeRect nono : noZones) {
//			nono.draw(pen);
//		}
		
//		for(ResizeRect waypoint : waypoints) {
//			waypoint.draw(pen);
//		}
		
		tower.draw(pen);
		towerShop.draw(pen);

		world.draw(pen);
		

		if(towerShop.selectedTower != null) { 
			towerShop.selectedTower.range.fill(pen); //draw range as the player is dragging it around 
			towerShop.selectedTower.draw(pen);	
		}
		
		for(Tower playTowers : towers) { 
			playTowers.draw(pen);
		}		
		
		for(Arrow arrow : arrows) {
			arrow.draw(pen);
		}
	}
	
	public int pixelOfTile(int tile) { //converts tile position to it's corresponding pixel location
		return tile * TILE_SIZE;
	}
	
	public void arrowsDoDamage()
	{
		Iterator<Enemy> iterator = enemyList.iterator();
		while (iterator.hasNext()) 
		{
			Enemy bug = iterator.next();
			
			Iterator<Arrow> arrowIterator = arrows.iterator();
			while(arrowIterator.hasNext())
			{
				Arrow arrow = arrowIterator.next();
				if (arrow.overlaps(bug))
				{
					bug.takeDamage(arrow.damage); 
					if (!bug.isAlive())
					{	
						world.increaseMoney(); //gain money each time you kill an enemy
						arrowIterator.remove();
						iterator.remove();
						break;
					}
					arrowIterator.remove();
				}
			}	
		}
	}
	
	public void gameOver() {
		Iterator<Enemy> iterator = enemyList.iterator();
		while (iterator.hasNext())  //iterator goes through the arraylist so it can safely remove the enemy once it exits the screen
		{
			Enemy enemy = iterator.next();
			if (enemy.x >= 1920) //enemy moves out of the screen
			{	
				world.loseHealth(); 
				iterator.remove(); 
			}
		}
		world.gameOver();
	}
	
	public void initializeNoZones() {
		noZones = new ResizeRect[] {
				new ResizeRect(1, 585, 377, 54),
				new ResizeRect(326, 268, 52, 318),
				new ResizeRect(382, 268, 764, 48),
				new ResizeRect(1095, 322, 50, 190),
				new ResizeRect(1159, 460, 48, 498),
				new ResizeRect(1209, 840, 313, 53),
				new ResizeRect(1414, 714, 52, 124),
				new ResizeRect(1468, 712, 443, 53),
				new ResizeRect(330, 657, 92, 25),
				new ResizeRect(754, 319, 88, 123),
				new ResizeRect(449, 202, 243, 57),
				new ResizeRect(563, 593, 152, 57),
				new ResizeRect(501, 508, 282, 81),
				new ResizeRect(1222, 724, 111, 25),
				new ResizeRect(1230, 895, 294, 54),
				new ResizeRect(788, 669, -618, -366),
				new ResizeRect(270, 399, 39, 47),
				new ResizeRect(762, 3, 81, 283),
				new ResizeRect(976, 158, 24, 25),
				new ResizeRect(1294, 265, 37, 185),
				new ResizeRect(1096, 649, 48, 51),
				new ResizeRect(1609, 774, 297, 39),
				new ResizeRect(1555, 648, 347, 55),
				new ResizeRect(1220, 589, 41, 48),
				new ResizeRect(566, 445, 274, 57),
				new ResizeRect(396, 521, 26, 134),
				new ResizeRect(982, 473, 23, 24),
				new ResizeRect(1361, 600, 30, 25),
				new ResizeRect(913, 348, 95, 13),
				new ResizeRect(1154, 898, -919, -20),
				new ResizeRect(651, 332, 44, 57),
				new ResizeRect(69, 843, 102, 163),
				new ResizeRect(785, 908, 28, 46),
				new ResizeRect(590, 76, 37, 51),
				new ResizeRect(1106, 81, 29, 42),
				new ResizeRect(1687, 855, 20, 21),
				new ResizeRect(914, 526, 27, 44),
				new ResizeRect(400, 781, 31, 45),
				new ResizeRect(978, 718, 27, 40),
				new ResizeRect(1109, 792, 22, 23),
				new ResizeRect(785, 908, 28, 46),
				new ResizeRect(590, 76, 37, 51),
				new ResizeRect(1106, 81, 29, 42),
				new ResizeRect(1687, 855, 20, 21),
				new ResizeRect(914, 526, 27, 44),
				new ResizeRect(400, 781, 31, 45),
				new ResizeRect(978, 718, 27, 40),
				new ResizeRect(1109, 792, 22, 23),
		};
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
		
		for(ResizeRect nono : noZones) {
			if(nono.resizer.held) {
				nono.resizeBy(dx, dy);
			}
			else if(nono.held) {
				nono.moveBy(dx, dy);
			}
		}

		
		if(towerShop.selectedTower != null) {
			if(towerShop.selectedTower.held) { 
				towerShop.selectedTower.setLocation((mx/TILE_SIZE) * TILE_SIZE, (my/TILE_SIZE) * TILE_SIZE); //"snaps" tower in place 
			}
		}
		
		boolean isValid = true;
		if(towerShop.selectedTower != null) { 
			for(ResizeRect nono : noZones) { 
				if(towerShop.selectedTower.overlaps(nono)) {
					isValid = false;
					System.out.println("overlapping");
				}
			}
			for(Tower t : towers) { //condition for preventing placing towers on top of each other
				if(towerShop.selectedTower.contains(t)) {
					isValid = false;
					break;
				}
			}
			
			if(isValid) { //tests to see if the location on the map is a valid placement
				towerShop.selectedTower.range.setColor(validColor); //green means placeable
			}
			else {
				towerShop.selectedTower.range.setColor(invalidColor); //red means unplaceable
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
		
		for(int i = 0; i < noZones.length; i++)
		{
			if(noZones[i].contains(mx,  my))  noZones[i].grabbed();
			if(noZones[i].resizer.contains(mx,  my))  noZones[i].resizer.grabbed();
		}
		
		
		for(Tower currTower : towerShop.towers) { //checks each tower in the shop
			if(currTower.contains(mx, my) && (e.getButton() == MouseEvent.BUTTON1)) { //left click
				if(currTower.cost <= world.money) {              //can only select a tower if you have enough money for it
					towerShop.setSelected(currTower.getCopy());  //copy of the towerShop tower is now the selectedTower
					towerShop.getSelected().grabbed();			 //selected tower is held
					return;
				}
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
		
		for(int i = 0; i < noZones.length; i++)
		{
			noZones[i].dropped();
			noZones[i].resizer.dropped();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mx = e.getX();
		my = e.getY() - toolBarHeight;
		
		if((e.getButton() == MouseEvent.BUTTON3) && towerShop.selectedTower != null) { //right click
			boolean validPlacement = true;
			for(Tower t : towers) { //condition for preventing placing towers on top of each other
				if(towerShop.selectedTower.contains(t)) {
					validPlacement = false;
					break;
				}
			}
			for(ResizeRect nono : noZones) {
				if(towerShop.selectedTower.overlaps(nono)) {
					validPlacement = false;
					break;
				}
			}
			if(validPlacement && (world.money >= towerShop.selectedTower.cost)) { //can only select tower if you have enough money
				towers.add(towerShop.selectedTower); //add the selectedTower to the arraylist
				towerShop.selectedTower.dropped();   //selectedTower is no longer held, can't move it anymore
				world.buy(towerShop.selectedTower);  //money decreases once tower is placed
				towerShop.selectedTower = null;      //set the selectedTower to null, there can only be one selectedTower at a time
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		mx = e.getX();
		my = e.getY() - toolBarHeight;
		
		//can move tower even if not pressing/dragging
		if(towerShop.selectedTower != null) {
			if(towerShop.selectedTower.held) { 
				towerShop.selectedTower.setLocation((mx/TILE_SIZE) * TILE_SIZE, (my/TILE_SIZE) * TILE_SIZE); //"snaps" tower in place 
			}
		}
		
		//same logic as mouseDragged
		boolean isValid = true;
		if(towerShop.selectedTower != null) {
			for(ResizeRect nono : noZones) {
				if(towerShop.selectedTower.overlaps(nono)) {
					isValid = false;
					System.out.println("overlapping");
				}
			}
			for(Tower t : towers) { //condition for preventing placing towers on top of each other
				if(towerShop.selectedTower.contains(t)) {
					isValid = false;
					break;
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

		if(keyChar == 'p') {
			for(int i = 0; i < noZones.length; i++)
			{
				System.out.println("new ResizeRect(" + noZones[i] + "),");
			}
			for(int i = 0; i < waypoints.length; i++)
			{
				System.out.println("new ResizeRect(" + waypoints[i] + "),");
			}
		}
		
		if(pressing[ESC]) { //ability to deselect tower 
			towerShop.selectedTower = null;
		}
	}
	

	public static void main(String[] args) {
		
		new Game();
		
	}
	

}
