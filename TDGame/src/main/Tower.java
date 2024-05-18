package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Tower extends Rect{
	
	private Image towerBase;
	public int towerLvl;
	public int cost = -1;
	
	public Rect range;
	private boolean targeting = false;
	 
	public Animation towerWeapon; //bow animates when it is "shooting" an arrow
	private String weaponDir = "UP";
	private int dmg = 0;
	private int cooldown;
	public int delay;
	
	public int currArrow = 0;
	public double arrowSpeed = 10;
	public int arrowCooldown;
	public int arrowDelay;
	public int arrowVertW;
	public int arrowVertH;
	public int arrowHorzW;
	public int arrowHorzH;
	
	
	public Tower(int x, int y, int towerLvl, String dir) {
		super(x, y, Game.TILE_SIZE, 2 * Game.TILE_SIZE);
		this.towerLvl = towerLvl;
		
		weaponDir = dir;
		/*
		 * tower's range is 2 tile sizes all around the entire tower
		 */
		Color rangeColor = new Color(0, 0, 255, 30); //makes range transparent, default color is blue
		range = new Rect(x - 2*Game.TILE_SIZE, y - 2*Game.TILE_SIZE, 5*Game.TILE_SIZE, 6*Game.TILE_SIZE, rangeColor);
		towerBase = Toolkit.getDefaultToolkit().getImage("img/Tower_01_Level_" + towerLvl + ".png");
		
		
		setLocation(x, y);
		setStats();

		delay = getCooldown();
		arrowDelay = arrowCooldown;
		
		towerWeapon = new Animation("img/Tower_Level_" + towerLvl + "_Bow", 6, getCooldown());
	}
	
	public void setLocation(int x, int y) {
		//method so that range moves with the tower
		setPosition(x, y);
		range.x = x - 2*Game.TILE_SIZE;
		range.y = y - 2*Game.TILE_SIZE;
	}
	
	public void setStats(int cooldown, int arrowCooldown, int arrowSpeed, int dmg, int cost) {
		this.cooldown = cooldown;   //sets the duration of the bow weapon animation
		this.arrowCooldown = arrowCooldown;
		this.arrowSpeed = arrowSpeed;
		this.dmg = dmg;
		this.cost = cost;  //cost of each tower increases at a higher level
		
	}
	
	public void setStats() {
		switch(towerLvl) {
		case(1): 				
			arrowVertW = 8;
			arrowVertH = 40;
			
			arrowHorzW = 40; 
			arrowHorzH = 8;
			
			setStats(20, 85, 10, 10, 150);
			break;
		case(2):
			arrowVertW = 15;
			arrowVertH = 40;
		
			arrowHorzW = 40; 
			arrowHorzH = 15;
			
			setStats(17, 68, 12, 25, 225);
			break;
		case(3):          
			arrowVertW = 22;
			arrowVertH = 40;
	
			arrowHorzW = 40; 
			arrowHorzH = 22;
		
			setStats(14, 30, 20, 40, 400);
			break;
		default:
			cooldown = 0;
			arrowCooldown = 0;
			arrowSpeed = 0;
			dmg = 0;
			cost = 0;
			setStats(0, 0, 0, 0, 0);
		}
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public boolean isCooldownOver() {
		return delay <= 0;
	}
	
	public boolean isArrowCooldownOver() {
		return arrowDelay <= 0;
	}
	
	public void resetCooldown() {
		arrowDelay = arrowCooldown;
	}
	
	public void resetArrowCooldown() {
		arrowDelay = arrowCooldown;
	}
	
	public void updateDelay() {
		arrowDelay--;
		delay--;
	}
	
	public int getDmg() {
		return dmg;
	}
	
	public Tower getCopy() { 
		return new Tower(x, y, towerLvl, getDirection());
	}

	public Rect getRange() {
		return range;
	}
	
	public void setTargeting(boolean b) {
		targeting = b;
	}
	
	public boolean isTargeting() {
		return targeting;
	}
	
	/* Tower base doesn't change direction but the weapon does */
	public String getDirection() {
		return weaponDir;
	}
	
	public void changeDirection(String dir) {
		weaponDir = dir;
	}
	

	public void shootAt(Enemy e) 
	{	
		if(isArrowCooldownOver()) {
			double vDist  = e.y + (e.h / 2) - this.y - (this.h / 2); //vertical displacement between the tower and the enemy
			double hDist  = e.x + (e.w / 2) - this.x - (this.w / 2); //horizontal displacement between the tower and the enemy

			double angle = Math.atan2(vDist, hDist); //angle formed between the player and enemy
			double vx = (arrowSpeed * Math.cos(angle));
			double vy = (arrowSpeed * Math.sin(angle));
			
			double vxAbs = Math.abs(vx);
			double vyAbs = Math.abs(vy);
			
			int arrowW;
			int arrowH;
			String arrowDir;
			
			if(vxAbs > vyAbs) {
				if(vx > 0) arrowDir = "RT";
				else       arrowDir = "LT";
				
				arrowW = arrowHorzW;
				arrowH = arrowHorzH;
			}
			else {
				if(vy > 0) arrowDir = "DN";
				else       arrowDir = "UP";
				
				arrowW = arrowVertW;
				arrowH = arrowVertH;
			}
			
			Arrow arrow = new Arrow(x + 32, y + 32, arrowW, arrowH, towerLvl, arrowDir); 
			arrow.setVelocity(vx, vy);
			arrow.setDamage(getDmg());
			Game.arrows.add(arrow);
			
			resetArrowCooldown();
		}
		updateDelay();
 
	}
	
	
	
	public void draw(Graphics pen) {
		//Draws Tower Base w/ range
		pen.drawImage(towerBase, x, y, Game.TILE_SIZE, 2*Game.TILE_SIZE, null);
		//range.draw(pen);

		
		//Draws idle tower 
		if (!targeting) {
			if (weaponDir.equals("UP")) { 
				switch(towerLvl) { //making sure the weapon sits in the correct place on top of its respective tower
				case 1:
					pen.drawImage(towerWeapon.startImage(), x - 16, y + 5, 96, 96, null);
					break;
				case 2:
					pen.drawImage(towerWeapon.startImage(), x - 16, y - 2, 96, 96, null);
					break;
				case 3:
					pen.drawImage(towerWeapon.startImage(), x - 16, y - 11, 96, 96, null);
					break;
				default:
					pen.drawImage(towerWeapon.startImage(), x - 16, y, 96, 96, null);
				}
				
			}
		}
		else { //if tower is targeting, draw the animation 
			if (weaponDir.equals("UP")) {
				switch(towerLvl) {
				case 1:
					pen.drawImage(towerWeapon.nextImage(), x - 16, y + 5, 96, 96, null);
					break;
				case 2:
					pen.drawImage(towerWeapon.nextImage(), x - 16, y - 2, 96, 96, null);
					break;
				case 3:
					pen.drawImage(towerWeapon.nextImage(), x - 16, y - 11, 96, 96, null);
					break;
				default:
					pen.drawImage(towerWeapon.nextImage(), x - 16, y, 96, 96, null);
				}
			}
		}
		
		pen.setColor(Color.blue);
		//pen.drawRect(x - 16, y + 5, 96, 128); //rect around the entire tower(base & weapon)
		//super.draw(pen);
	}
	
	
}
