package facharbeit.GUI.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import facharbeit.GUI.KnnGUI;

public class TestPanel extends JPanel {
    private static final long serialVersionUID = 2766442209479077609L;
    
    private JProgressBar successBar;
    private JLabel successLabel;

    private File selectedFile;

    public TestPanel() {
	setLayout(null);

	// Beschreibung
	{
	    JLabel label = new JLabel(
		    "Select a trainset to test the accuracy of the loaded nnet");
	    label.setBounds(25, 10, 500, 20);

	    successLabel = label;
	    this.add(label);

	}

	// File-Label
	{
	    JLabel label = new JLabel("");
	    label.setBounds(25, 150, 200, 20);

	    successLabel = label;
	    this.add(label);
	}

	// Start Button
	{
	    JButton btn = new JButton("start");
	    btn.setBounds(20, 125, 100, 25);
	    this.add(btn);
	    btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    startTest();
		}
	    });

	    // SuccessBar
	    {
		successBar = new JProgressBar();
		successBar.setStringPainted(true);
		successBar.setString("");
		successBar.setBounds(20, 70, 550, 50);

		this.add(successBar);
	    }
	}
    }

    private void startTest() {
	JFileChooser chooser = new JFileChooser();
	chooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
	chooser.setCurrentDirectory(new File("."));

	chooser.showOpenDialog(null);
	selectedFile = chooser.getSelectedFile();
	
	if (selectedFile == null)
	    return;
	
	if (!KnnGUI.hasNeuralNetwork()) {
	    JOptionPane.showMessageDialog(null,
		    "The default nnet failed to load, please select another one", null, 2);
	    return;
	}

	new Thread(new Runnable() {
	    public void run() {
		try {
		    Scanner scanner = new Scanner(selectedFile);

		    int counter = 0, successCounter = 0;

		    while (scanner.hasNextLine()) {
			String[] values = scanner.nextLine().split(",");
			counter++;

			int label = Integer.parseInt(values[0]);
			double[] inputs = new double[28 * 28];
			for (int i = 1; i < inputs.length && i < values.length; i++)
			    inputs[i - 1] = (double) Integer.parseInt(values[i]) / 255;

			double[] outputs = KnnGUI.getNeuralNetwork().query(inputs);

			double highestValue = 0.0;
			int predictedLabel = 0;
			for (int i = 0; i < outputs.length; i++) {
			    if (outputs[i] > highestValue) {
				highestValue = outputs[i];
				predictedLabel = i;
			    }
			}
			if (label == predictedLabel)
			    successCounter++;

			successBar.setValue((int) ((double) successCounter / counter * 100));
			successBar.setString(successCounter + " von " + counter);
		    }
		    scanner.close();
		    successLabel.setText("Accuracy: " + ((double) successCounter / counter * 100) + "%");
		} catch (FileNotFoundException | NumberFormatException e1) {
		    e1.printStackTrace();
		}
	    }
	}).start();
    }
}
