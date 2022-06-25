package facharbeit.GUI.panels;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import facharbeit.GUI.KnnGUI;
import facharbeit.neuralNetwork.NeuralNetwork;

/*
 * Zur Einstellung von allen Variablen (KNN-Pfad)
 */
public class SettingPanel extends JPanel {
    private Label networkLabel;

    public SettingPanel() {
	setLayout(null);

	// Netzwerk
	{
	    // Label
	    {
		Label label;
		if (KnnGUI.networkFile != null && KnnGUI.networkFile.exists())
		    label = new Label("Netzwerk: " + KnnGUI.networkFile.getName());
		else
		    label = new Label("Netzwerk: standard");
		label.setBounds(150, 20, 500, 20);

		networkLabel = label;
		this.add(networkLabel);
	    }
	    // File-Select
	    {
		JButton btn = new JButton("KNN auswählen");
		btn.setBounds(10, 15, 130, 25);
		this.add(btn);
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("KNN", "knn"));
			chooser.setCurrentDirectory(new File("."));
			
			chooser.showOpenDialog(null);
			KnnGUI.networkFile = chooser.getSelectedFile();
			
			if (KnnGUI.networkFile == null || !KnnGUI.networkFile.exists())
			    return;
			
			NeuralNetwork net = NeuralNetwork.load(KnnGUI.networkFile);
			if (net != null) {
			    networkLabel.setText("Netzwerk: " + KnnGUI.networkFile.getName());
			    KnnGUI.setNeuralNetwork(net);
			}
		    }
		});
	    }
	}
    }
}
