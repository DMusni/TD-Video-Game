package main;

public class GameGrid {
	
	private int[][] grid;
	private int numRows;
	private int numCols;
	
	public GameGrid(int row, int col) {
		if (row > 0 && col > 0) {
			this.numRows = row;
			this.numCols = col;
			grid = new int[row][col];
		}
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public int getPosition(int row, int col) {
		//calculation for the tilespot in grid 
		return (row * numCols) + col;
	}
	
	public int getLocation(int row, int col, int tileSize) {
		//converts position in grid to pixel location on screen
		return getPosition(row, col) * tileSize;
	}
	
	public void setEnemyStart(Enemy enemy, int row, int col, int tileSize) {
		enemy.x = getLocation(row, col, tileSize);
		enemy.y = getLocation(row, col, tileSize);
	}
	
	
}
