package Tetris;

import java.util.*;

public class TetrisGrid {
	// Private Data
	private int numRows = 22;
	private int numCols = 10;
	private final int numAbove = 1;
	private int score = 0;
	private int[][] Grid = new int[numCols][numRows + numAbove];

	// public functions
	/**
	 * 
	 * @param pos
	 * @return
	 */
	public void checkForLines(int[] pos) {
 
		// REMOVE DUPLICATES - using hashset
		int[] fourRows = new int[4];
		List<Integer> rowList = Arrays.asList(pos[1], pos[3], pos[5], pos[7]);
		Set<Integer> rowSet = new HashSet<Integer>(rowList);
		Iterator<Integer> it = rowSet.iterator();
		int rowCount = 0;
		while (it.hasNext()) {
			fourRows[rowCount] = (Integer) it.next();
			rowCount++;
		}

		// Selection Sort Descending Order
		int temp;
		if (rowCount > 1)
			for (int i = 0; i < rowCount - 1; i++)
				for (int j = i + 1; j < rowCount; j++)
					if (fourRows[i] < fourRows[j]) {
						temp = fourRows[i];
						fourRows[i] = fourRows[j];
						fourRows[j] = temp;
					}

		// Check and then Delete Bow
		// Also count number of lines deleted

		for (int i = 0; i < rowCount; i++)
			if (checkRow(fourRows[i])) {
				deleteRow(fourRows[i]);
				score++;
			}

	}

	/**
	 * Check Row
	 * 
	 * @param row
	 * @return true if full line, else false
	 */

	public boolean checkRow(int row) {
		for (int i = 0; i < numCols; i++)
			if (Grid[i][row] == 0)
				return false;
		return true;
	}
	
	public boolean checkFail()  {
		for (int i = 0; i < numCols; i++)
			if (Grid[i][numRows] != 0)
				return true;
		return false;
	}
	
	public void Reset()
	{
		Grid = new int[numCols][numRows + numAbove];
	}

	/**
	 * 
	 * @param blockPos
	 *            set of four X-Y coordinates
	 * @param blockType
	 *            BlockType
	 */
	public void setBlock(int[] blockPos, int blockType) {
		Grid[blockPos[0]][blockPos[1]] = blockType;
		Grid[blockPos[2]][blockPos[3]] = blockType;
		Grid[blockPos[4]][blockPos[5]] = blockType;
		Grid[blockPos[6]][blockPos[7]] = blockType;
	}

	/**
	 * Delete Row
	 * 
	 * @param row
	 */
	public void deleteRow(int row) {
		for (int i = 0; i < numCols; i++)
			for (int j = row; j < (numRows ); j++)
				Grid[i][j] = Grid[i][j + 1];
	}

	// Setters & Getters
	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGridValue(int x, int y) {
		return Grid[x][y];
	}

	public int[][] getGrid() {
		return Grid;
	}

	public void setGrid(int[][] grid) {
		Grid = grid;
	}
}
