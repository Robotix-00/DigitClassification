package facharbeit.extras;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import facharbeit.neuralNetwork.NeuralNetwork;

public class TrainNet {

    public static void main(String[] args) {
	try {
	    int hidden = InOut.readInt("Anzahl der Hidden-Neurons: ");
	    String file = InOut.readString("Trainingsdatei: ");
	    double learnrate = InOut.readDouble("Lernrate (default: 0.0005): ");
	    String saveFile = InOut.readString("Speicherdatei: ");
	    int epochen = InOut.readInt("Epochen: ");

	    System.out.println("Start Training");

	    NeuralNetwork net = new NeuralNetwork(784, hidden, 10);
	    train(net, epochen, new File(file), learnrate);
	    net.save(new File(saveFile));
	} catch (Exception e) {
	    System.out.println("Ein Fehler ist aufgetreten, bitte versuchen Sie es erneut.");
	    main(args);
	}
    }

    public static void train(NeuralNetwork net, int epochs, File file, double learnrate) {
	// Anzahl der Elemente, die aus der Datei ausgelesen werden sollen
	int elements = 1002;

	Scanner scanner;
	double[][] targets = new double[elements][10], inputs = new double[elements][784];
	int[] labels = new int[elements];

	/*
	 * Liest die Datensätze aus der Datei und speichert sie in den Variablen
	 * 'inputs' und 'targets'
	 */
	try {
	    scanner = new Scanner(file);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return;
	}

	for (int i = 0; i < elements; i++) {
	    if (!scanner.hasNext())
		break;
	    String line = scanner.nextLine();
	    String[] values = line.split(",");
	    labels[i] = Integer.parseInt(values[0]);

	    if (labels[i] != -1)
		targets[i][labels[i]] = 1.0;
	    for (int j = 0; j < 784; j++) {
		inputs[i][j] = (double) Integer.parseInt(values[j + 1]) / 255;
	    }
	}
	scanner.close();

	// Training

	net.fit(inputs, targets, epochs, learnrate);

	/*
	 * Fittnes-Test - Testet auf wieviele der gegebenen Zahlen das Netz das richtige
	 * Ergebniss erziehlt
	 */
	int yeyCounter = 0;
	for (int i = 0; i < inputs.length; i++) {
	    double highestValue = 0.0;
	    int lable = 0, j = 0;
	    for (double now : net.query(inputs[i])) {
		if (now > highestValue) {
		    highestValue = now;
		    lable = j;
		}
		j++;
	    }
	    if (lable == labels[i])
		yeyCounter++;
	}
	System.out.println("\nErfolgreiche Vorhersagen: " + yeyCounter + " von " + elements);
	System.out.println("Treffsicherheit: " + ((double) yeyCounter / elements) * 100 + "%");
    }
}