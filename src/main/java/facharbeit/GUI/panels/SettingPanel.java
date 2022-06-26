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

public class SettingPanel extends JPanel {
    private static final long serialVersionUID = -9068568466794132027L;
    
    private Label networkLabel;

    public SettingPanel() {
	setLayout(null);

	// Netzwerk
	{
	    // Label
	    {
		if (KnnGUI.networkFile != null && KnnGUI.networkFile.exists())
		    networkLabel = new Label("selected network: " + KnnGUI.networkFile.getName());
		else
		    networkLabel = new Label("selected network: standard");
		networkLabel.setBounds(150, 20, 500, 20);

		this.add(networkLabel);
	    }
	    // File-Select
	    {
		JButton btn = new JButton("select nnet");
		btn.setBounds(10, 15, 130, 25);
		this.add(btn);
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("NNET", "nnet"));
			chooser.setCurrentDirectory(new File("."));
			
			chooser.showOpenDialog(null);
			KnnGUI.networkFile = chooser.getSelectedFile();
			
			if (KnnGUI.networkFile == null || !KnnGUI.networkFile.exists())
			    return;
			
			NeuralNetwork net = NeuralNetwork.load(KnnGUI.networkFile);
			if (net != null) {
			    networkLabel.setText("network: " + KnnGUI.networkFile.getName());
			    KnnGUI.setNeuralNetwork(net);
			}
		    }
		});
	    }
	}
    }
}
