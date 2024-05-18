package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class World {
	
	public Game game;
	public TowerShop shop;
	
	public int money = 1000;
	public final int maxhealth = 15;
	public int health;
	
	public int enemySpawnTimer = 60 * 2; //spawn enemy every 2 seconds
	public int enemySpawnDelay;
	public int[] enemyCount;
	public Enemy[] enemies; 
	public Enemy currEnemy = null;
	public int enemyIndex =0;
	
	
	public World(Game game, TowerShop shop, int[] enemyCount, Enemy[] enemies) {
		this.game = game;
		this.shop = shop;
		health = maxhealth;
		enemySpawnDelay = enemySpawnTimer;
		this.enemyCount = enemyCount;
		this.enemies = enemies;
	}
	
	//method for increasing money as the round plays 
	public void increaseMoney() {
		money += 50;
	}
	
	public void buy(Tower tower) { 
		if(tower.cost <= money) {
			money = money - tower.cost;
		}
	}
	
	public void loseHealth() {
		if(health > 0) {
			health--;
		}
	}
	
	public void gameOver() {
		if(health <= 0) {
			System.out.println("Game Over");
		}
	}
	
	public String getTowerStats(Tower tower) { 
		String stats = "Price: $" + tower.cost;
		return stats;
	}
	
	public void startEnemyWave() {
		if(enemySpawnDelay <= 0) {
			if(enemyIndex >= enemyCount.length) return; //first check for enemyCount out of bounds
			
			if(enemyCount[enemyIndex] == 0) enemyIndex++;  //move on to the next enemy
			
			if(enemyIndex >= enemyCount.length) return; //second check for enemies out of bounds 

			currEnemy = enemies[enemyIndex].getCopy();
			game.enemyList.add(currEnemy);	
			enemyCount[enemyIndex] -= 1;

			enemySpawnDelay = enemySpawnTimer;
		}
		enemySpawnDelay--;

	}
	
	public void draw(Graphics pen) {
		
		//draw money
		Color myColor = new Color(0, 175, 0);
		pen.setColor(myColor);
		Font myFont = new Font("DialogInput", Font.BOLD, 32);
		pen.setFont(myFont);
		String totalMoney = "$" + money;
		int moneyCenter = pen.getFontMetrics().stringWidth(totalMoney) / 2;
		pen.drawString(totalMoney, (shop.x + (shop.w/2)) - moneyCenter, shop.y + 32);
		
		//draw tower stats
		pen.setColor(Color.WHITE);
		Font statFont = new Font("SansSerif", Font.BOLD, 17);
		for(Tower t : shop.towers) {
			pen.setFont(statFont);
			int statHeight = pen.getFontMetrics().getHeight();
			pen.drawString(getTowerStats(t), (t.x + t.w) + 15, t.y + 48);
			
			//visual for not being able to buy the tower
			if (money < t.cost) { 
				Font m = new Font("SansSerif", Font.PLAIN, 17);
				pen.setFont(m);
				pen.drawString("Not Enough Money", (t.x + t.w) + 15, (t.y + 48) + statHeight);
			}
		}
		
		//draw health 
		Color c = new Color(0, 0, 0, 190);
		pen.setColor(c);
		pen.fillRoundRect(10, 10, 132, 64, 25, 25);
		
		Color outline = new Color(255, 255, 255);
		pen.setColor(outline);
		pen.drawRoundRect(10, 10, 132, 64, 25, 25);
		
		Font h = new Font("Monospaced", Font.BOLD, 26);
		pen.setFont(h);
		pen.drawString("" + health + "/" + maxhealth, 53, 50);
		
		Image heart = Toolkit.getDefaultToolkit().getImage("img/FullHeart.png");
		pen.drawImage(heart, 15, 25, 32, 32, null);
		
		
		pen.setColor(Color.black);
	}
	
}
