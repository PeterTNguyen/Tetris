package Tetris;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;
import java.net.URL;



public class TetrisGame extends Applet implements Runnable, KeyListener {
	private int numPixels = 25, maxRow, speed, timer, score;
	private TetrisGrid GameGrid;
	private Image image, emptyBlock;
	private Image[] blockImage;
	private URL base;
	private Graphics second;
	private TetrisBlock block;
	private Font scoreFont;
	private int[] blockPos = new int[8], spacePos = new int[8];


	
	@Override
	public void init() {
		setFocusable(true);
		addKeyListener(this);
		setBackground(Color.GRAY);
		scoreFont = new Font("Arial", Font.PLAIN, 30);

		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Blockles");
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		blockImage = new Image[7];
		blockImage[0] = getImage(base, "data/redBlock.png");
		blockImage[1] = getImage(base, "data/pinkBlock.png");
		blockImage[2] = getImage(base, "data/greenBlock.png");
		blockImage[3] = getImage(base, "data/blueBlock.png");
		blockImage[4] = getImage(base, "data/orangeBlock.png");
		blockImage[5] = getImage(base, "data/purpleBlock.png");
		blockImage[6] = getImage(base, "data/yellowBlock.png");
		emptyBlock = getImage(base, "data/emptyBlock.png");
	}

	@Override
	public void start() {
		GameGrid = new TetrisGrid();
		block = new TetrisBlock(GameGrid);
		setSize(numPixels * GameGrid.getNumCols() + 250,
				numPixels * GameGrid.getNumRows() + 4);
		speed = 30;
		timer = 0;
		score = 0;
		GameGrid.getNumCols();
		maxRow = GameGrid.getNumRows();

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			// update grid
			// check if block hit something
			timer++;
			if (timer % speed == 0) {
				timer = 0;
				if (block.moveDown(GameGrid) == 0) {
					GameGrid.setScore(0);
					GameGrid.Reset();
				}

				// Check End of Game
			}

			// Draw Grid
			repaint();

			// Sleep
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);

	}

	@Override
	public void paint(Graphics g) {
		// Paint Current Block
		int type = block.getType();
		blockPos = block.getCurrPos();
		spacePos = block.getSpacePos();

		// Paint Space Block
		for (int i = 0; i < 8; i += 2)
			g.drawImage(emptyBlock, numPixels * spacePos[i] + 2, numPixels
					* (maxRow - 1 - spacePos[i + 1]) + 3, this);

		// Paint Current Block
		for (int i = 0; i < 8; i += 2)
			g.drawImage(blockImage[type - 1], numPixels * blockPos[i] + 2,
					numPixels * (maxRow - 1 - blockPos[i + 1]) + 3, this);

		// Paint GameGrid
		int[][] tempGrid = GameGrid.getGrid();
		for (int i = 0; i < GameGrid.getNumCols(); i++)
			for (int j = 0; j < GameGrid.getNumRows(); j++)
				if (tempGrid[i][j] != 0)
					g.drawImage(blockImage[tempGrid[i][j] - 1], numPixels * i
							+ 2, numPixels * (maxRow - 1 - j) + 3, this);

		// Paint Game Border
		g.drawRect(0, 1, numPixels * GameGrid.getNumCols() + 2, numPixels
				* GameGrid.getNumRows() + 2);
		g.drawRect(328, 40, 120, 120);

		// Draw Next Block
		int nextType = block.getNextType();
		int[] nextBlock = new int[8];
		if (nextType == 1)
			nextBlock = block.getPos(true, 0, 0, 1, nextType);
		else
			nextBlock = block.getPos(true, 0, 0, 0, nextType);

		// Paint NextBlock

		for (int i = 0; i < 8; i += 2) {
			if (nextType == 2)
				g.drawImage(blockImage[nextType - 1], numPixels * nextBlock[i]
						+ 2 + 388, numPixels * (nextBlock[i + 1]) + 3 + 100,
						this);
			else
				g.drawImage(blockImage[nextType - 1], numPixels * nextBlock[i]
						+ 2 + 375, numPixels * (nextBlock[i + 1]) + 3 + 100,
						this);
		}

		// Paint Score
		score = GameGrid.getScore();
		g.setFont(scoreFont);
		// g.drawString("Score", 330, 200);
		g.drawString("" + score, 360, 300);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			block.rotate(GameGrid);

			break;

		case KeyEvent.VK_DOWN:
			if (block.moveDown(GameGrid) == 0) {
				GameGrid.setScore(0);
				GameGrid.Reset();
			}
			timer = 0;
			break;

		case KeyEvent.VK_LEFT:
			block.moveLeft(GameGrid);

			break;

		case KeyEvent.VK_RIGHT:
			block.moveRight(GameGrid);
			break;

		case KeyEvent.VK_SPACE:
			while (block.moveDown(GameGrid) == 1) {

			}
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
