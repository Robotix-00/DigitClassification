package facharbeit.GUI.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JPanel;

import facharbeit.GUI.Grid;

/*
 * Verwendet zum Ansehen der Beispiele in einer Datei
 * Unnötig, deshalb entfernt
 */
public class DisplayPanel extends JPanel {
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
		    // Letzten Datensatz erreicht
		    exampleIndex--;
		}
	    }
	});
	JButton btnReg = new JButton("<<");
	btnReg.setBounds(20, 262, 63, 23);
	add(btnReg);
	btnReg.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (exampleIndex > 1)
		    loadExample(exampleIndex -= 1);
	    }
	});

	//loadExample(0);
    }

    public void loadExample(int lineIndex) {
	String line = null;
	BufferedReader lines = new BufferedReader(
		new InputStreamReader(getClass().getResourceAsStream("dataset/trainset.csv")));

	try {
	    lines.skip(lineIndex);
	    line = lines.readLine();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	String[] values = line.split(",");
	int[][] grid = new int[28][28];

	for (int y = 0; y < 28; y++) {
	    for (int x = 0; x < 28; x++) {
		grid[x][y] = Integer.parseInt(values[1 + x + (y * 28)]);
	    }
	}

	panel.draw(grid);
    }
}
