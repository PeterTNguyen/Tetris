package Tetris;

//BlockType
//1 - Line
//2 - Square
//3 - L
//4 - Backward L
//5 - Z
//6 - Backward Z
//7 - T

//Direction
//0 - Right
//1 - Down
//2 - Left
//3 - Up
import java.util.Random;

public class TetrisBlock {
	private int posX, posY, type, nextType, direction;
	private int[] currPos = new int[8], spacePos = new int[8];
	Random randomGen = new Random();

	public TetrisBlock(TetrisGrid grid) {
		direction = 0;
		posX = 4;
		posY = grid.getNumRows() - 1;
		type = randomGen.nextInt(7) + 1;
		nextType = randomGen.nextInt(7) + 1;
		currPos = getPos(false, posX, posY, direction, type);
		spacePos = calcSpacePos(grid, currPos);
	}

	public boolean Reset(TetrisGrid grid) {
		direction = 0;
		posX = 4;
		posY = grid.getNumRows() - 1;
		type = nextType;
		nextType = randomGen.nextInt(7) + 1;

		currPos = getPos(false, posX, posY, direction, type);
		spacePos = calcSpacePos(grid, currPos);

		if (grid.checkFail() || checkCollision(grid, currPos))
			return false;

		return true;
	}

	public void rotate(TetrisGrid grid) {
		int newDirec = (direction + 1) % 4;
		int[] newPos = getPos(true, posX, posY, newDirec, type);
		if (checkBoundary(grid, newPos))
			if (!checkCollision(grid, newPos)) {
				direction++;
				direction %= 4;
				currPos = getPos(false, posX, posY, direction, type);
				spacePos = calcSpacePos(grid, currPos);
			}
	}

	public void moveRight(TetrisGrid grid) {
		int newPosX = posX + 1;
		int[] newPos = getPos(true, newPosX, posY, direction, type);
		if (checkBoundary(grid, newPos))
			if (!checkCollision(grid, newPos)) {
				posX++;
				currPos = getPos(false, posX, posY, direction, type);
				spacePos = calcSpacePos(grid, currPos);
			}

	}

	public void moveLeft(TetrisGrid grid) {
		int newPosX = posX - 1;
		int[] newPos = getPos(true, newPosX, posY, direction, type);
		if (checkBoundary(grid, newPos))
			if (!checkCollision(grid, newPos)) {
				posX--;
				currPos = getPos(false, posX, posY, direction, type);
				spacePos = calcSpacePos(grid, currPos);
			}

	}

	public int moveDown(TetrisGrid grid) {
		int newPosY = posY - 1;
		int[] newPos = getPos(true, posX, newPosY, direction, type);
		if (checkBoundary(grid, newPos))
			if (checkCollision(grid, newPos)) {

				// SetBlock
				grid.setBlock(currPos, type);

				// Check for Lines
				grid.checkForLines(currPos);
				if (!Reset(grid))
					return 0;
				return 2;
			} else {
				posY--;
				currPos = getPos(false, 0, 0, 0, 0);
			}

		return 1;
	}

	public int[] calcSpacePos(TetrisGrid grid, int[] downPos) {
		int[] tempPos = new int[8];
		for (int i = 0; i < 8; i++)
			tempPos[i] = downPos[i];

		while (!checkCollision(grid, tempPos)) {
			tempPos[1]--;
			tempPos[3]--;
			tempPos[5]--;
			tempPos[7]--;
		}
		tempPos[1]++;
		tempPos[3]++;
		tempPos[5]++;
		tempPos[7]++;
		return tempPos;
	}

	private boolean checkBoundary(TetrisGrid grid, int[] newPos) {

		for (int i = 0; i < 8; i += 2) {
			if (newPos[i] < 0 || newPos[i] >= grid.getNumCols())
				return false;
			// if (newPos[i + 1] < 0 || newPos[i + 1] > (grid.getNumRows() + 1))
			// return false;
		}
		return true;
	}

	public boolean checkCollision(TetrisGrid grid, int[] allPos) {
		// Check collision with Ground
		if (allPos[1] < 0 || allPos[3] < 0 || allPos[5] < 0 || allPos[7] < 0) {
			return true;
		}

		// Check Collision with block
		if (grid.getGridValue(allPos[0], allPos[1]) != 0
				|| grid.getGridValue(allPos[2], allPos[3]) != 0
				|| grid.getGridValue(allPos[4], allPos[5]) != 0
				|| grid.getGridValue(allPos[6], allPos[7]) != 0) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param next
	 *            false if using current coordinates
	 * @param Xin
	 *            next X coord value
	 * @param Yin
	 *            next Y coord value
	 * @param Directin
	 *            next Direction value
	 * @return return array of four X-Y coord pairs
	 */
	public int[] getPos(boolean next, int Xin, int Yin, int Directin,
			int blockType) {
		int X, Y, direc;
		if (next) {
			X = Xin;
			Y = Yin;
			direc = Directin;
		} else {
			X = posX;
			Y = posY;
			direc = direction;
			blockType = type;
		}

		switch (blockType) {
		// Line
		case 1:
			switch (direc) {
			case 0:
				int[] pos0 = { X - 1, Y, X, Y, X + 1, Y, X + 2, Y };
				return pos0;
			case 1:
				int[] pos1 = { X, Y + 1, X, Y, X, Y - 1, X, Y - 2 };
				return pos1;
			case 2:
				int[] pos2 = { X - 1, Y, X, Y, X + 1, Y, X + 2, Y };
				return pos2;
			case 3:
				int[] pos3 = { X, Y + 1, X, Y, X, Y - 1, X, Y - 2 };
				return pos3;
			}
			break;

		// Square
		case 2:
			int[] pos = { X, Y, X, Y - 1, X - 1, Y, X - 1, Y - 1 };
			return pos;

			// L
		case 3:
			switch (direc) {
			case 0:
				int[] pos0 = { X + 1, Y, X, Y, X - 1, Y, X - 1, Y - 1 };
				return pos0;
			case 1:
				int[] pos1 = { X, Y - 1, X, Y, X, Y + 1, X - 1, Y + 1 };
				return pos1;
			case 2:
				int[] pos2 = { X - 1, Y, X, Y, X + 1, Y, X + 1, Y + 1 };
				return pos2;
			case 3:
				int[] pos3 = { X, Y + 1, X, Y, X, Y - 1, X + 1, Y - 1 };
				return pos3;
			}
			break;
		// Backward L
		case 4:
			switch (direc) {
			case 0:
				int[] pos0 = { X - 1, Y, X, Y, X + 1, Y, X + 1, Y - 1 };
				return pos0;
			case 1:
				int[] pos1 = { X, Y + 1, X, Y, X, Y - 1, X - 1, Y - 1 };
				return pos1;
			case 2:
				int[] pos2 = { X + 1, Y, X, Y, X - 1, Y, X - 1, Y + 1 };
				return pos2;
			case 3:
				int[] pos3 = { X, Y - 1, X, Y, X, Y + 1, X + 1, Y + 1 };
				return pos3;
			}
			break;
		// Z
		case 5:
			switch (direc) {
			case 0:
				int[] pos0 = { X - 1, Y, X, Y, X, Y - 1, X + 1, Y - 1 };
				return pos0;
			case 1:
				int[] pos1 = { X, Y + 1, X, Y, X - 1, Y, X - 1, Y - 1 };
				return pos1;
			case 2:
				int[] pos2 = { X - 1, Y, X, Y, X, Y - 1, X + 1, Y - 1 };
				return pos2;
			case 3:
				int[] pos3 = { X, Y + 1, X, Y, X - 1, Y, X - 1, Y - 1 };
				return pos3;
			}
			break;
		// Backward Z
		case 6:
			switch (direc) {
			case 0:
				int[] pos0 = { X + 1, Y, X, Y, X, Y - 1, X - 1, Y - 1 };
				return pos0;
			case 1:
				int[] pos1 = { X, Y + 1, X, Y, X + 1, Y, X + 1, Y - 1 };
				return pos1;
			case 2:
				int[] pos2 = { X + 1, Y, X, Y, X, Y - 1, X - 1, Y - 1 };
				return pos2;
			case 3:
				int[] pos3 = { X, Y + 1, X, Y, X + 1, Y, X + 1, Y - 1 };
				return pos3;
			}
			break;
		// T
		case 7:
			switch (direc) {
			case 0:
				int[] pos0 = { X, Y, X + 1, Y, X - 1, Y, X, Y - 1 };
				return pos0;
			case 1:
				int[] pos1 = { X, Y, X, Y + 1, X - 1, Y, X, Y - 1 };
				return pos1;
			case 2:
				int[] pos2 = { X, Y, X - 1, Y, X, Y + 1, X + 1, Y };
				return pos2;
			case 3:
				int[] pos3 = { X, Y, X, Y + 1, X + 1, Y, X, Y - 1 };
				return pos3;
			}
			break;
		}
		// return pos;
		return new int[] {};
	}

	// Setters and Getters
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNextType() {
		return nextType;
	}

	public void setNextType(int nextType) {
		this.nextType = nextType;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int[] getCurrPos() {
		return currPos;
	}

	public void setCurrPos(int[] currPos) {
		this.currPos = currPos;
	}

	public int[] getSpacePos() {
		return spacePos;
	}

	public void setSpacePos(int[] spacePos) {
		this.spacePos = spacePos;
	}
}
