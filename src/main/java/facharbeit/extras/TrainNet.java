package facharbeit.extras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import facharbeit.neuralNetwork.NeuralNetwork;

public class TrainNet {

    public static void main(String[] args) {
	Scanner scanner = new Scanner(System.in);
	try {
	    System.out.println("Amount of hidden neurons?: ");
	    int hidden = scanner.nextInt();

	    System.out.println("training set: ");
	    String file = scanner.nextLine();

	    System.out.println("learning rate (default: 0.0005): ");
	    double learnrate = scanner.nextDouble();

	    System.out.println("epochs: ");
	    int epochen = scanner.nextInt();

	    System.out.println("File to save to: ");
	    String saveFile = scanner.nextLine();

	    System.out.println("starting training.....");

	    NeuralNetwork net = new NeuralNetwork(784, hidden, 10);
	    train(net, epochen, new File(file), learnrate);
	    net.save(new File(saveFile));
	} catch (Exception e) {
	    System.out.println("An error occured, please try again.");
	    main(args);
	}
    }
    
    private static int i = 0;
    public static void train(NeuralNetwork net, int epochs, File file, double learnrate) {
	// Reads all lines from the training file
	int elements = 0;
	List<String> lines = new LinkedList<>();
	
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(file));
	    String line;
	    while ((line = reader.readLine()) != null) {
		elements++;
		lines.add(line);
	    }
	    reader.close();
	} catch (Exception e) {

	}

	double[][] targets = new double[elements][10], inputs = new double[elements][784];
	int[] labels = new int[elements];

	
	lines.forEach(line -> {
	    String[] values = line.split(",");
	    labels[i] = Integer.parseInt(values[0]);

	    if (labels[i] != -1)
		targets[i][labels[i]] = 1.0;
	    for (int j = 0; j < 784; j++) {
		inputs[i][j] = (double) Integer.parseInt(values[j + 1]) / 255;
	    }
	    i++;
	});

	// training
	net.fit(inputs, targets, epochs, learnrate);

	/*
	 * fitness evaluation
	 */
	int yeyCounter = 0;
	for (i = 0; i < inputs.length; i++) {
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
	System.out.println("\nSuccesfull predictions: " + yeyCounter + " out of " + elements);
	System.out.println("Accuracy: " + ((double) yeyCounter / elements) * 100 + "%");
    }
}