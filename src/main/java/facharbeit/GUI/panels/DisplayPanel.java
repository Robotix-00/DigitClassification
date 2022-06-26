package facharbeit.GUI.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JPanel;

import facharbeit.GUI.Grid;

public class DisplayPanel extends JPanel {
    private static final long serialVersionUID = 7936452373116638725L;

    private Grid panel;
    private int exampleIndex = 0;

    public DisplayPanel() {
	setLayout(null);

	panel = new Grid(true);
	panel.setBackground(Color.WHITE);
	panel.setBounds(10, 10, 237, 237);
	add(panel);

	JButton nextButton = new JButton(">>");
	nextButton.setBounds(100, 262, 63, 23);
	add(nextButton);
	nextButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    loadExample(exampleIndex += 1);
		} catch (NoSuchElementException e) {
		    exampleIndex--;
		}
	    }
	});
	JButton btnReg = new JButton("<<");
	btnReg.setBounds(20, 262, 63, 23);
	add(btnReg);
	btnReg.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (exampleIndex > 0)
		    loadExample(exampleIndex -= 1);
	    }
	});

	loadExample(0);
    }

    private Scanner scanner = new Scanner(
	    new InputStreamReader(getClass().getResourceAsStream("/dataset/testset.csv")));
    private List<int[][]> grids = new LinkedList<>();

    public void loadExample(int lineIndex) {
	if (lineIndex >= grids.size()) {
	    String[] values = scanner.nextLine().split(",");
	    int[][] grid = new int[28][28];

	    for (int y = 0; y < 28; y++) {
		for (int x = 0; x < 28; x++) {
		    grid[x][y] = Integer.parseInt(values[1 + x + (y * 28)]);
		}
	    }
	    grids.add(grid);
	}

	panel.draw(grids.get(lineIndex));
    }
}
