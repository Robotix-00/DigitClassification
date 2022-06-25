package facharbeit.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Panel to draw/display a 28x28-pixel image
 */
public class Grid extends JPanel implements MouseListener, MouseMotionListener {
    private int[][] grid;
    private boolean readOnly;
    private Point mousePos = new Point(0, 0);

    public Grid() {
	this.addMouseMotionListener(this);
	clearGrid();
    }

    public Grid(boolean readOnly) {
	if (!readOnly)
	    this.addMouseMotionListener(this);
	this.readOnly = readOnly;
	clearGrid();
    }

    public void paint(Graphics g) {
	super.paint(g);
	drawGrid(g);

	if (!readOnly)
	    g.drawOval(mousePos.x - 30, mousePos.y - 30, 60, 60);
    }

    // draws a single pixel
    private void drawGrid(Graphics g) {
	for (int row = 0; row < 28; row++) {
	    for (int col = 0; col < 28; col++) {
		g.setColor(getGrayscale(grid[row][col]));
		for (int i = 0; i < 8; i++) {
		    g.drawLine(5 + row * 8, 5 + col * 8 + i, 13 + row * 8, 5 + col * 8 + i);

		}
	    }
	}

	// draws a divider
	g.setColor(Color.black);
	for (int row = 0; row < 29; row++) {
	    for (int col = 0; col < 29; col++) {
		g.drawLine((5), (col * 8 + 5), (28 * 8 + 5), (col * 8 + 5));
		g.drawLine((row * 8 + 5), (5), (row * 8 + 5), (28 * 8 + 5));
	    }
	}
    }

    // returns a crayscale color from a single number (0-255)
    private Color getGrayscale(int colNum) {
	int greyScale = 255 - colNum;
	return new Color(greyScale, greyScale, greyScale);
    }

    // what do you think?
    private void clearGrid() {
	grid = new int[28][28];
    }

    // clears the grid and rerenders the GUI
    public void clear() {
	clearGrid();
	this.repaint();
    }

    //
    public void mouseDragged(MouseEvent e) {
	mousePos = e.getPoint();
	int x = e.getX();
	int y = e.getY();
	int dx = (x - 5) / 8;
	int dy = (y - 5) / 8;
	if (dx < 0 || dx >= 28 || dy < 0 || dy >= 28) {
	    return;
	}

	drawCircle(dx, dy, 255);

	this.repaint();
    }

    /*
     * draws a circle.
     */
    private void drawCircle(int x, int y, int value) {
	if (x < 0 || x >= 28 || y < 0 || y >= 28)
	    return;

	// relative positions of all colored pixels
	int[][][] locs = { { { 0, 0 } }, { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } },
		{ { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 }, { 1, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 } } };

	for (int[] pos : locs[0]) {
	    int dx = x + pos[0], dy = y + pos[1];
	    if (dx < 28 && dx >= 0 && dy < 28 && dy >= 0) {
		grid[dx][dy] = value;
	    }
	}

	for (int[] pos : locs[1]) {
	    int dx = x + pos[0], dy = y + pos[1];
	    if (dx < 28 && dx >= 0 && dy < 28 && dy >= 0) {
		if (grid[dx][dy] < value / 2)
		    grid[dx][dy] += value / 2;
	    }
	}

	for (int[] pos : locs[2]) {
	    int dx = x + pos[0], dy = y + pos[1];
	    if (dx < 28 && dx >= 0 && dy < 28 && dy >= 0) {
		if (grid[dx][dy] < value / 4)
		    grid[dx][dy] += value / 4;
	    }
	}

    }

    public void mouseMoved(MouseEvent e) {
	mousePos = e.getPoint();
	this.repaint();
    }

    public void draw(int[][] g) {
	this.clearGrid();
	this.grid = g;
	this.repaint();
    }

    public int[][] getGrid() {
	return this.grid;
    }

    public int[] get1dGrid() {
	int[] grid2d = new int[28 * 28];

	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid[0].length; j++) {
		grid2d[(i * 28) + j] = grid[j][i];
	    }
	}

	return grid2d;
    }

    public boolean isReadOnly() {
	return readOnly;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
