package facharbeit.extras;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Scanner;

import facharbeit.neuralNetwork.NeuralNetwork;

/*
 * Berechnet die Treffsicherheit aller Netze in einem Ordner, die dem Filter entsprechen.
 */
public class GenerateTable {
    private static final int trainElements = 1381;

    public static void main(String[] args) {
	double[][] inputs = new double[trainElements][784];
	int[] labels = new int[trainElements];

	Scanner scanner;
	try {
	    scanner = new Scanner(new File("src/main/resources/dataset/trainset.csv"));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return;
	}
	for (int i = 0; i < trainElements; i++) {
	    if (!scanner.hasNext())
		break;
	    String line = scanner.nextLine();
	    String[] values = line.split(",");
	    labels[i] = Integer.parseInt(values[0]);
	    
	    for (int j = 0; j < 784; j++) {
		inputs[i][j] = (double) Integer.parseInt(values[j + 1]) / 255;
	    }
	}
	scanner.close();

	for (File file : new File("NNet").listFiles(new FilenameFilter() {
	    public boolean accept(File dir, String name) {
		return name.startsWith("net-") && name.endsWith(".nnet");
	    }
	})) {
	    NeuralNetwork net = NeuralNetwork.load(file);

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
	    System.out.println(file.getName() + ": " + yeyCounter + " out of " + trainElements + " ("
		    + ((double) yeyCounter / trainElements) * 100 + "%)");

	}

    }

}
