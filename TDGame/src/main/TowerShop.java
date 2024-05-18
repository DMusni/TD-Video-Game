package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class TowerShop{
	
	public Tower[] towers;
	public int x;
	public int y;
	public int w;
	public int h;
	
	public Tower selectedTower = null;
	
	public TowerShop(int x, int y, int w, int h) {
		this.x = x;
		this.y = y; 
		this.w = w;
		this.h = h;
		initTowers();
	}
	
	public void initTowers() {
		towers = new Tower[3];
	
		int shopX = x + Game.TILE_SIZE; //x coordinate of the shop
		int shopY = y + 32;           //y coordinate of the shop
		
		//only three towers 
		towers[0] = new Tower(shopX - 32, shopY, 1, "UP");
		towers[1] = new Tower(shopX - 32, shopY + 2*Game.TILE_SIZE, 2, "UP");
		towers[2] = new Tower(shopX - 32, shopY + 4*Game.TILE_SIZE, 3, "UP");
	}
	
	public void setSelected(Tower tower) {
		this.selectedTower = tower;
	}
	
	public Tower getSelected() {
		return selectedTower;
	}
	
	public void draw(Graphics pen) {
		//background
		Color c = new Color(0, 0, 0, 190);
		pen.setColor(c);
		pen.fillRoundRect(x, y, w, h, 20, 20);
		pen.setColor(Color.white);
		pen.drawRoundRect(x, y, w, h, 20, 20);
		
		
		for(int i = 0; i < towers.length; i++) {
			towers[i].draw(pen);
		}
	}
}
