package main;

public class Enemy extends Sprite{
	
	private static String[] pose = {"RT", "UP", "DN"};
	private int health = 0; 
	private int speed;
	private int enemyType = 0;
	private boolean alive = true;
	private String name;
	
	public int currWaypoint = 0; 
	
	
	public Enemy(String name, int speed, int enemyType) {
		super(name, pose, -2 * Game.TILE_SIZE, 9 * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE, 3, 20);
		this.speed = speed;
		this.name = name;
		
		if((enemyType > 0) && (enemyType <= 4)) { //there are only 4 enemy types
			this.enemyType = enemyType;
		}
		setEnemyHealth();
	}
	
	public void setEnemyHealth() { //each enemy type has different health
		switch(enemyType) {
		case 1:
			health = 20;
			break;
		case 2:
			health = 50;
			break;
		case 3: 
			health = 75;
			break;
		case 4:
			health = 100;
			break;
		default:
			health = 0;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getEnemyType() {
		return enemyType;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public Enemy getCopy() { 
		return new Enemy(getName(), getSpeed(), getEnemyType());
	}
	
	public void takeDamage(int dmg) { 
		health -= dmg;      //health decreases by the dmg of the tower
		if (health <= 0) {  
			alive = false;
		}
	}

}
