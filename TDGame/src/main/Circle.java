package main;

import java.awt.Graphics;

public class Circle {
	
	double x;
	double y;
	
	double r;
	
	int A; //angle rotating by
	double cosA;
	double sinA;
	
	double speed = 0;
	double vx = 0;
	double vy = 0;
	
	public Circle(double x, double y, double r, int A) {
		this.x = x;
		this.y = y;
		
		this.r = r;
		this.A = A;
		
		if (A < 0)   A = (A % 360) + 360;
		if (A > 359) A-= 360;
		
		cosA = Lookup.cos[A]; 
		sinA = Lookup.sin[A];
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setVelocity(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public void movex() {
		x += speed * cosA;
		y += speed * sinA;
	}
	
	public void move() {
		x += vx;
		y += vy;
	}
	
	public void turnLT(int dA) {
		A -= dA;
		
		if(A < 0) A += 360;
		
		cosA = Lookup.cos[A];
		sinA = Lookup.sin[A];
	}
	
	public void turnRT(int dA) {
		A += dA;
		
		if(A >= 360) A -= 360;
		
		cosA = Lookup.cos[A];
		sinA = Lookup.sin[A];
	}
	
	public void moveForward(int d) {
		x += d*cosA;
		y += d*sinA;
	}
	
	public void moveBackward(int d) {
		x -= d*cosA;
		y -= d*sinA;
	}
	
	public boolean overlaps(Circle c) {
		double dx = x - c.x;
		double dy = y - c.y;
		
		double d = Math.sqrt(dx*dx + dy*dy); //distance formula
		
		return d < r + c.r; //true when the distance is less than the radius of the two circles added together
	}
	
	public void draw(Graphics pen) {
		
		pen.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
		
		pen.drawLine((int)x, (int)y, (int)(x+r*cosA), (int)(y+r*sinA)); //draws the radius
	}
	
}
