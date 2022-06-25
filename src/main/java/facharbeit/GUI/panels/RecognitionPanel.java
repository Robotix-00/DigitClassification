package facharbeit.GUI.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;

import facharbeit.GUI.Grid;
import facharbeit.GUI.KnnGUI;

/*
 * Panel zur Eingabe einer 28x28 Pixel-Matrix und deren Auswertung zu einer Zahl
 */
public class RecognitionPanel extends JPanel {
    private Grid panel;
    private JProgressBar[] bars;
    private JLabel[] labels;

    public RecognitionPanel() {
	setLayout(null);

	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "rec");
	this.getActionMap().put("rec", new AbstractAction() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		predict();
	    }
	});

	// Grid Panel
	{
	    panel = new Grid();
	    panel.setBackground(Color.WHITE);
	    panel.setBounds(10, 10, 237, 237);
	    add(panel);
	}

	// Erkennungs-Button
	{
	    JButton btnReg = new JButton("Erkennen");
	    btnReg.setBounds(20, 262, 100, 25);
	    this.add(btnReg);
	    btnReg.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    predict();
		}
	    });
	}

	// Clear-Button
	{
	    JButton btnClear = new JButton("Clear");
	    btnClear.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    panel.clear();

		    for (int i = 0; i < 10; i++) {
			bars[i].setValue(0);
			bars[i].setForeground(Color.BLUE);
			bars[i].setString("");

			labels[i].setForeground(Color.BLACK);
		    }
		}
	    });
	    btnClear.setBounds(144, 262, 100, 25);
	    this.add(btnClear);
	}

	// Progressbars
	{
	    bars = new JProgressBar[10];
	    labels = new JLabel[10];
	    for (int i = 0; i < 10; i++) {
		JProgressBar bar = new JProgressBar();
		bar.setValue(0);
		bar.setStringPainted(true);
		bar.setString("");
		bar.setBounds(320, 20 + (i * 30), 200, 25);

		bars[i] = bar;
		this.add(bars[i]);

		JLabel label = new JLabel("" + i);
		label.setBounds(300, 20 + (i * 30), 200, 25);
		labels[i] = label;
		this.add(labels[i]);
	    }
	}
    }

    // Sagt die Zahl vorher
    private void predict() {
	int[] intGrid = panel.get1dGrid();
	double[] grid = new double[intGrid.length];
	for (int i = 0; i < intGrid.length; i++)
	    grid[i] = (double) intGrid[i] / 255;

	// Vorhersage und Darstellung
	if (KnnGUI.hasNeuralNetwork())
	    displayData(KnnGUI.getNeuralNetwork().query(grid));

	// Fehlerbehandlung
	else
	    JOptionPane.showMessageDialog(null,
		    "Das Standard-Netz konnte nicht geladen werden, bitte wähle ein anders aus", null, 2);
    }

    // Stellt die übergebenen Daten auf den Progressbars dar
    public void displayData(double[] values) {
	int[] barValues = new int[10];

	int lable = 0;
	double higest = 0.0;
	for (int i = 0; i < 10; i++) {
	    barValues[i] = (int) (values[i] * 100);
	    if (higest < values[i]) {
		higest = values[i];
		lable = i;
	    }
	}
	Color green = new Color(0, 200, 0);
	Color red = new Color(255, 0, 0);

	for (int i = 0; i < 10; i++) {
	    bars[i].setValue(barValues[i]);
	    bars[i].setString("" + round(values[i], 4));
	    if (i == lable) {
		bars[i].setForeground(green);
		labels[i].setForeground(green);
	    } else {
		bars[i].setForeground(red);
		labels[i].setForeground(red);
	    }
	}
    }
    
    // Rundet eine Fließkommazahl
    private static double round(double value, int places) {
	if (places < 0)
	    throw new IllegalArgumentException();

	long factor = (long) Math.pow(10, places);
	value *= factor;
	long tmp = Math.round(value);
	return (double) tmp / factor;
    }
}
