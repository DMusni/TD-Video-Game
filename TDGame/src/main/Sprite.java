package main;
import java.awt.Graphics;

public class Sprite extends Rect {
	
	Animation[] animation;
	int action = 0;
	
	boolean moving = false;
	

	public Sprite(String name, String[] pose, int x, int y, int w, int h, int count, int duration) {
		super(x, y, w, h);
		
		animation = new Animation[pose.length];
		
		for(int i = 0; i < animation.length; i++) {
			animation[i] = new Animation(name + "_" + pose[i], count, duration);
		}
	}
	
	public void draw(Graphics pen) {
		if (!moving) {
			pen.drawImage(animation[action].startImage(), x, y, w, h, null);
		}
		else{
			pen.drawImage(animation[action].nextImage(), x, y, w, h, null);
		}
	
		
		//draws a rectangle around the sprite so you can see what space it takes up
		//comment out when game is finished
		pen.drawRect(x, y, w, h);
	}
	
//	public void moveLT(int dx) {
//		super.moveLT(dx);
//		
//		action = 2;
//		
//	}
	
	public void moveRT(int dx) {
		super.moveRT(dx);
		
		action = 2;
		
		moving = true;

	}
	
	public void moveUP (int dy) {
		super.moveUP(dy);
		
		action = 1;
		
		moving = true;
		
	}
	
	public void moveDN(int dy) {
		super.moveDN(dy);
		
		action = 0;
		
		moving = true;
		
	}
}
