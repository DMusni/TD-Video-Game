package main;

import java.awt.*;

import javax.swing.JPanel;

public class GameScreen extends JPanel{
	
	Game game;
	Dimension size;
	
	public GameScreen(int w, int h, Game game) {
		
		setDoubleBuffered(true); //removes flickering
		size = new Dimension(w, h);
		setLayout(null);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		requestFocus();
		
		this.game = game;
		
	}
	
	public void paintComponent(Graphics pen) {
		super.paintComponent(pen);
		
		game.draw(pen);
	}
 
}
