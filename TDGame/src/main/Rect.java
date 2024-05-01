package main;

import java.awt.Color;
import java.awt.Graphics;

public class Rect {
	/*
	 * This code is taken from CMP428 Rect class 
	 */
	int x;
	int y;
	int w;
	int h;
	
	double vx = 0;
	double vy = 0;
	
	int old_x;//where rectangle was before the game loop
	int old_y;
	
	boolean held = false; //is the rect grabbed by the mouse
	Color c;
	
	public Rect(int x, int y, int w, int h) {
		this(x, y, w, h, Color.black);
	}
	
	public Rect(int x, int y, int w, int h, Color c) {
		setPosition(x, y);
		
		this.w = w;
		this.h = h;
		
		old_x = x;
		old_y = y;
		
		this.c = c;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVelocity(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public void moveBy(int dx, int dy) {
		//method to move a rectangle around 
		//if you change x and y then ur moving the rectangle
		old_x = x;
		old_y = y;
		
		x += dx;
		y += dy;
	}
	
	public void grabbed() {
		held = true;
	}
	
	public void dropped() {
		held = false;
	}
	
	public void moveLT(int dx) {
		old_x = x;
		
		x -= dx;

	}
	
	public void moveRT(int dx) {
		old_x = x;
		
		x += dx;

	}
	
	public void moveUP (int dy) {
		old_y = y;
		
		y -= dy;

	}
	
	public void moveDN(int dy) {
		old_y = y;
		
		y += dy;
	}
	
	public void move() {
		x += vx;
		y += vy;
		
	}
	
	public void resizeBy(int dw, int dh) {
		//if you change w and h, ur changing its size
		w += dw; 
		h += dh;
	}
	
	public boolean cameFromLeftOf(Rect r) {
		return old_x + w < r.x;
	}
	
	public boolean cameFromRightOf(Rect r) {
		return r.x + r.w < old_x;
	}
	
	public boolean cameFromAbove(Rect r) {
		return old_y + h < r.y;
	}
	
	public boolean cameFromBelow(Rect r) {
		return r.y + r.h < old_y;
	}
	
	public void pushBackLeftFrom(Rect r) {
		x = r.x - w - 1;
	}
	
	public void pushBackRightFrom(Rect r) {
		x = r.x + r.w + 1;
	}
	
	public void pushBackUpFrom(Rect r) {
		y = r.y - h - 1;
	}
	
	public void pushBackDownFrom(Rect r) {
		y = r.y + r.h + 1;
	}
	
	
	public boolean isLeftOf(Rect r) {
		return x + w < r.x;
	}
	
	public boolean isRightOf(Rect r) {
		return r.x + r.w < x;
	}
	
	public boolean isAbove(Rect r) {
		return y + h < r.y;
	}
	
	public boolean isBelow(Rect r) {
		return r.y + r.h < y;
	}
	
	public boolean contains(int mx, int my) { //mx, my being the coordinates for the mouse point
		return (mx >= x)     && 
			   (mx <= x + w) &&
			   (my <= y + h) &&
			   (my >= y);
	}
	
	public boolean overlaps(Rect r) {
		return (x + w >= r.x      )&& 
			   (x     <= r.x + r.w)&& 
			   (y + h >= r.y      )&& 
			   (y     <= r.y + r.h);
	}
	
	public void pushOutOf(Rect r) {
		if(cameFromLeftOf(r))  pushBackLeftFrom(r);
		if(cameFromRightOf(r)) pushBackRightFrom(r);
		if(cameFromBelow(r))   pushBackDownFrom(r);
		if(cameFromAbove(r))   pushBackUpFrom(r);
	}
	
	public void chase(Rect r, int dx)
	{
		if(isLeftOf(r))   moveRT(dx); 
		if(isRightOf(r))  moveLT(dx); 
		if(isAbove(r))    moveDN(dx); 
		if(isBelow(r))    moveUP(dx); 
	}
	
	public void setColor(Color c) {
		this.c = c;
	}
	
	public void fill(Graphics pen) {
		//everything (any object) you want drawn to the screen will have a draw method
		pen.setColor(c);
		pen.fillRect(x, y, w, h); //doesn't need pen bc its pen object 
	}
	
	public void draw(Graphics pen) {
		//everything (any object) you want drawn to the screen will have a draw method
		pen.setColor(c);
		pen.drawRect(x, y, w, h); //doesn't need pen bc its pen object 
	}
	
	public String toString() { //for figuring out where your rects are
		return "" + x + ", " + y + ", " + w + ", " + h;
	}
	
}