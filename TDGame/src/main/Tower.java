package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Tower extends Rect{
	
	private Image towerBase;
	public int towerLvl;
	
	public Rect range;
	public boolean inRange = false;
	 
	public Animation towerWeapon;
	private String weaponDir = "UP";
	
	public int currArrow = 0;
	
	
	public Tower(int x, int y, int towerLvl, String dir) {
		super(x, y, Game.TILE_SIZE, 2 * Game.TILE_SIZE);
		this.towerLvl = towerLvl;
		
		weaponDir = dir;
		/*
		 * tower's range is a tile size all around the entire tower
		 */
		
		Color rangeColor = new Color(0, 0, 255, 30); //makes range transparent
		range = new Rect(x - Game.TILE_SIZE, y - Game.TILE_SIZE, 3*Game.TILE_SIZE, 4*Game.TILE_SIZE, rangeColor);
		towerBase = Toolkit.getDefaultToolkit().getImage("img/Tower_01_Level_" + towerLvl + ".png");
		towerWeapon = new Animation("img/Tower_Level_" + towerLvl + "_Bow", 6, 10);
		setLocation(x, y);
	}
	
	public void setLocation(int x, int y) {
		//method so that range moves with the tower
		setPosition(x, y);
		range.x = x - Game.TILE_SIZE;
		range.y = y - Game.TILE_SIZE;
	}
	
	public Tower getCopy() {
		return new Tower(x, y, towerLvl, getDirection());
	}

	public Rect getRange() {
		return range;
	}
	
	public boolean isInRange() {
		return inRange;
	}
	
	/* Tower base doesn't change direction but the weapon does */
	public String getDirection() {
		return weaponDir;
	}
	
	public void changeDirection(String dir) {
		weaponDir = dir;
	}
	
	
	
	public void shoot(Rect[] arrow) {
		/*
		 * Something is wrong with this method
		 */
		arrow[currArrow].setVelocity(1.8, 0.8);
		
		arrow[currArrow].x = x;
		arrow[currArrow].y = x;
		
		currArrow++;
		
		if(currArrow == arrow.length) {
			currArrow = 0;
		}
		
	}
	
	public void draw(Graphics pen) {
		//Draws Tower Base w/ range
		pen.drawImage(towerBase, x, y, Game.TILE_SIZE, 2*Game.TILE_SIZE, null);
		//range.draw(pen);

		
		//Drawing weapon animation
		if (!inRange) {
			if (weaponDir.equals("UP")) { 
				pen.drawImage(towerWeapon.startImage(), x - 16, y + 5, 96, 96, null);
			}
		}
		else {
			if (weaponDir.equals("UP")) {
				pen.drawImage(towerWeapon.nextImage(), x - 16, y + 5, 96, 96, null);
			}
		}
		
		pen.setColor(Color.blue);
		pen.drawRect(x - 16, y + 5, 96, 128); //rect around the entire tower(base & weapon)
		super.draw(pen);
	}
	
	
}
