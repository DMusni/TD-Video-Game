package main;

public class Enemy extends Sprite{
	
	private static String[] pose = {"DN", "UP", "RT"};
	private int health; 
	
	public Enemy(String name) {
		super(name, pose, 100, 400, 48, 48, 3, 20);
	}
	
	
	
	

}
