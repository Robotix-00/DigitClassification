package facharbeit.GUI;

import java.io.File;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import facharbeit.GUI.panels.DisplayPanel;
import facharbeit.GUI.panels.RecognitionPanel;
import facharbeit.GUI.panels.SettingPanel;
import facharbeit.GUI.panels.TestPanel;
import facharbeit.GUI.panels.WritePanel;
import facharbeit.neuralNetwork.NeuralNetwork;

public class KnnGUI extends JFrame {
    private static final long serialVersionUID = -2491554068914103949L;
    
    public static NeuralNetwork neuralNetwork;
    public static File networkFile;

    private JPanel contentPanel;
    private JTabbedPane tabbedPane;

    public KnnGUI() {

	// Laden des Standart-Netzes
	{
	    InputStream stream = getClass().getClassLoader().getResourceAsStream("KNNs/NeuralNetwork.nnet");
	    networkFile = null;
	    neuralNetwork = NeuralNetwork.load(stream);

	}

	// Fenstereinstellungen
	this.setBounds(100, 100, 633, 462);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.getContentPane().setLayout(null);
	this.setResizable(false);
	this.setTitle("digit recognition");

	//
	contentPanel = new JPanel();
	contentPanel.setBounds(0, 0, 617, 391);
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPanel.setLayout(null);

	getContentPane().add(contentPanel);

	// MenüAuswahl-Einstellungen
	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(10, 10, 597, 371);

	// Hinzufügen der einzelnen Fenster
	{
	    tabbedPane.addTab("Classification", new RecognitionPanel());
	    tabbedPane.addTab("Tests", new TestPanel());
	    tabbedPane.addTab("Display", new DisplayPanel());
	    tabbedPane.addTab("Writing", new WritePanel());
	    tabbedPane.addTab("Settings", new SettingPanel());
	}
	contentPanel.add(tabbedPane);
    }

    public void start() {
	this.setVisible(true);
    }

    /*
     * Getter und Setter
     */
    public static NeuralNetwork getNeuralNetwork() {
	return neuralNetwork;
    }

    public static void setNeuralNetwork(NeuralNetwork neuralNetwork) {
	if (neuralNetwork != null)
	    KnnGUI.neuralNetwork = neuralNetwork;
    }

    public static boolean hasNeuralNetwork() {
	return neuralNetwork != null;
    }
}
