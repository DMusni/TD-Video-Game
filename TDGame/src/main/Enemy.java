package main;

public class Enemy extends Sprite{
	
	static String[] pose = {"DN", "UP", "RT"};
	private int health = 4; 
	
	public Enemy(String name) {
		super(name, pose, 100, 400, 64, 64, 3, 20);
	}
	
	
	
	

}
