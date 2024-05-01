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
	
		int col = x + Game.TILE_SIZE;
		int shopY = y + 32;
		towers[0] = new Tower(col, shopY, 1, "UP");
		towers[1] = new Tower(col, shopY + 2*Game.TILE_SIZE, 2, "UP");
		towers[2] = new Tower(col, shopY + 4*Game.TILE_SIZE, 3, "UP");
	}
	
	public void setSelected(Tower tower) {
		this.selectedTower = tower;
	}
	
	public Tower getSelected() {
		return selectedTower;
	}
	
	public void draw(Graphics pen) {
		//background
		pen.setColor(Color.LIGHT_GRAY);
		pen.fillRoundRect(x, y, w, h, 20, 20);
		
		for(int i = 0; i < towers.length; i++) {
			towers[i].draw(pen);
		}
	}
}
