package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Arrow extends Rect{
	
	public Image img;
	public int damage;
	public int lvl;
	
	public Arrow(int x, int y, int w, int h, int towerLvl, String dir) {
		super(x, y, w, h);
		
		img = Toolkit.getDefaultToolkit().getImage("img/Tower_Level_" + towerLvl + "_Arrow_" + dir + ".png");
		lvl = towerLvl;
	}
	
	public void setDamage(int dmg) {
		damage = dmg;
	}
	
	public void draw(Graphics pen) {
		pen.drawImage(img, x, y, w, h, null);
		//super.draw(pen);
	}
}
