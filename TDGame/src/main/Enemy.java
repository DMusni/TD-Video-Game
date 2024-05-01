package main;

public class Enemy extends Sprite{
	
	private static String[] pose = {"DN", "UP", "RT"};
	private int health; 
	private int speed;
	public int currWaypoint = 0;
	
	public Enemy(String name, int speed) {
		super(name, pose, 0 * Game.TILE_SIZE, 9 * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE, 3, 20);
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	
	
	
	
	
	

}
