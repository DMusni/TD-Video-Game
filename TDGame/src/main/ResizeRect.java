package main;
import java.awt.Graphics;

public class ResizeRect extends Rect{
	/*
	 * Taken from CMP428 Rect2 class
	 * Rect class for the purpose of being a tool to be able to resize and drag to move rectangles 
	 */
	Rect resizer;

	public ResizeRect(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		//will be in the bottom left corner of rectangle, 10 by 10
		resizer = new Rect(x + w - 20, y + h - 20, 20, 20);
	}
	
	public void draw(Graphics pen) {
		super.draw(pen);
		
		resizer.draw(pen);
	}
	
	public void moveBy(int dx, int dy) {
		super.moveBy(dx, dy);
		
		resizer.moveBy(dx, dy);
	}
	
	public void resizeBy(int dx, int dy) {
		super.resizeBy(dx, dy);
		
		resizer.moveBy(dx, dy);
		
	}
	
}
