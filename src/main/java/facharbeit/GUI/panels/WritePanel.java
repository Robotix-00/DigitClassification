package facharbeit.GUI.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import facharbeit.GUI.Grid;

public class WritePanel extends JPanel {
    private static final long serialVersionUID = -942692246944071484L;
    
    private Grid panel;
    private BufferedWriter writer;

    private JLabel selectedFileLabel, nextNumberLabel;
    private File selectedFile;
    
    private int countPos = 0;
    
    public WritePanel() {
	this.setLayout(null);
	this.setFocusable(true);
	
	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "save");
	this.getActionMap().put("save", new AbstractAction() {
	    private static final long serialVersionUID = 8468388914366796047L;
	    @Override
	    public void actionPerformed(ActionEvent e) {
		saveNumber();
	    }
	});
	
	
	{
	    panel = new Grid();
	    panel.setBackground(Color.WHITE);
	    panel.setBounds(10, 10, 237, 237);
	    add(panel);
	}

	// File-Label
	{
	    JLabel label = new JLabel();
	    label.setBounds(25, 300, 200, 20);

	    selectedFileLabel = label;
	    this.add(label);
	}

	// Select-Button
	{
	    JButton btn = new JButton("Select file");
	    btn.setBounds(10, 262, 130, 25);
	    this.add(btn);
	    btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JFileChooser chooser = new JFileChooser();
		    chooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		    chooser.setCurrentDirectory(new File("."));

		    chooser.showOpenDialog(null);
		    selectedFile = chooser.getSelectedFile();

		    if (selectedFile == null)
			return;

		    selectedFileLabel.setText("File: " + selectedFile.getName());

		    try {
			writer = new BufferedWriter(new FileWriter(selectedFile, true));
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
	    });
	}
	
	// Clear-Button
	{
	    JButton btnClear = new JButton("Clear");
	    btnClear.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    panel.clear();
		}
	    });
	    btnClear.setBounds(154, 262, 100, 25);
	    this.add(btnClear);
	}

	// Next-Value Label
	{
	    JLabel label = new JLabel();
	    label.setBounds(340, 20, 200, 200);
	    label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 200));
	    nextNumberLabel = label;
	    reloadLabel();
	    this.add(label);
	}
	
	// Next-Value Label
	{
	    JLabel label = new JLabel("press ENTER to save the number");
	    label.setBounds(300, 10, 200, 20);
	    
	    this.add(label);
	}
    }

    public void saveNumber() {
	if (selectedFile == null || !selectedFile.exists()) {
	    JOptionPane.showMessageDialog(null, "Please select a file to write to", null, 2);
	    return;
	}

	int[] grid = panel.get1dGrid();
	double[] grid1d = new double[28 * 28];

	for (int i = 0; i < grid.length; i++) {
	    grid1d[i] = (double) grid[i] / 255;
	}

	try {	    
	    String text = nextNumberLabel.getText();
	    writer.write(text);
	    
	    for (double now : grid1d)
		writer.write("," + (int) (now * 255));

	    writer.newLine();
	    writer.flush();

	    panel.clear();
	    reloadLabel();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void reloadLabel() {
	if(countPos >= 9)
	    countPos = 0;
	else countPos++;
	
	nextNumberLabel.setText(""+countPos);
    }
}
