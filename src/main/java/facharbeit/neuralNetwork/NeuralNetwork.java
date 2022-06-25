package facharbeit.neuralNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import Jama.Matrix;

/*
 * Beinhaltet alle Methoden zur Erstellung, Trainierung, Speicherung eines Neuronalen Netzes
 */
public class NeuralNetwork {
    private int inodes, hnodes, onodes;
    private Matrix weights_ih, weights_ho;

    // Erstellt ein neues KNN mit entsprechender Knotenanzahl
    public NeuralNetwork(int inodes, int hnodes, int onodes) {
	this.inodes = inodes;
	this.hnodes = hnodes;
	this.onodes = onodes;

	// Generiert zufällige Gewichte zwischen -0.5 und 0.5
	weights_ih = Matrix.random(hnodes, inodes).minus(new Matrix(hnodes, inodes, 0.5));
	weights_ho = Matrix.random(onodes, hnodes).minus(new Matrix(onodes, hnodes, 0.5));
    }

    // Gibt eine Vorhersage des Netzes für die übergebenen Werte aus
    public double[] query(double[] in) {
	Matrix inputs, hiddenValues, outputValues;

	inputs = new Matrix(new double[][] { in }).transpose();
	hiddenValues = sigmoid(weights_ih.times(inputs));
	outputValues = sigmoid(weights_ho.times(hiddenValues)).transpose();

	return outputValues.getArray()[0];
    }

    // Trainiert das Netz mit dem übergebenen Datensatz
    public void train(double[] in, double[] target, double learnrate) {
	Matrix inputs, targets, hiddenValues, outputValues, outputError, hiddenError, weightAdaptation;

	inputs = new Matrix(new double[][] { in }).transpose();
	targets = new Matrix(new double[][] { target }).transpose();

	hiddenValues = sigmoid(weights_ih.times(inputs));
	outputValues = sigmoid(weights_ho.times(hiddenValues));

	// Berechnen der Fehler
	outputError = targets.minus(outputValues);
	hiddenError = weights_ho.transpose().times(outputError);

	weightAdaptation = outputError.arrayTimes(desigmoid(outputValues)).times(hiddenValues.transpose());
	weights_ho.plusEquals(weightAdaptation.times(learnrate));

	weightAdaptation = hiddenError.arrayTimes(desigmoid(hiddenValues)).times(inputs.transpose());
	weights_ih.plusEquals(weightAdaptation.times(learnrate));
    }

    // Lässt das Netz alle übergebenen Datensätze mehrfach lernen
    public void fit(double[][] in, double[][] target, int epochs, double learnrate) {
	for (int i = 0; i < epochs; i++) {
	    for (int j = 0; j < in.length; j++) {
		train(in[j], target[j], learnrate);
	    }
	    System.out.println("Epoche " + (i + 1) + "/" + epochs);
	}
	System.out.println("Training beendet");
    }

    // Läd ein Netz aus der angegebenen Datei
    public static NeuralNetwork load(File file) {
	try {
	    return load(new Scanner(file));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static NeuralNetwork load(InputStream stream) {
	return load(new Scanner(stream));
    }
    
    public static NeuralNetwork load(Scanner scanner) {
	NeuralNetwork net = null;
	String[] sizes = scanner.nextLine().split(",");
	net = new NeuralNetwork(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]), Integer.parseInt(sizes[2]));

	int i = 0;
	double[] weights = new double[net.inodes * net.hnodes];
	for (String now : scanner.nextLine().split(",")) {
	    weights[i] = Double.parseDouble(now);
	    i++;
	}
	net.weights_ih = MatrixManager.createMatrix(weights, net.hnodes, net.inodes);

	i = 0;
	weights = new double[net.hnodes * net.onodes];
	for (String now : scanner.nextLine().split(",")) {
	    weights[i] = Double.parseDouble(now);
	    i++;
	}
	net.weights_ho = MatrixManager.createMatrix(weights, net.onodes, net.hnodes);

	scanner.close();

	return net;
    }

    // Speichert das Netz in der angegebenen Datei
    public void save(File file) {
	try {
	    file.createNewFile();
	    FileWriter writer = new FileWriter(file);

	    writer.write(inodes + "," + hnodes + "," + onodes + "\n");

	    for (double[] arr : weights_ih.getArray()) {
		for (double now : arr) {
		    writer.write(now + ",");
		}
	    }
	    writer.write("\n");
	    for (double[] arr : weights_ho.getArray()) {
		for (double now : arr) {
		    writer.write(now + ",");
		}
	    }
	    writer.write("\n");

	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // Wendet die Sigmoid-Funktion auf alle Elemente der Matrix an
    private static Matrix sigmoid(Matrix matrix) {
	double[][] matrixArray = matrix.getArray();
	for (int i = 0; i < matrixArray.length; i++) {
	    for (int j = 0; j < matrixArray[0].length; j++) {
		matrixArray[i][j] = 1 / (1 + Math.pow(Math.E, -matrixArray[i][j]));
	    }
	}

	return matrix;
    }

    // Wendet die Umkehroperation der Sigmoid-Funktion auf alle Elemente der Matrix
    // an
    private static Matrix desigmoid(Matrix matrix) {
	double[][] matrixArray = matrix.getArray();
	for (int i = 0; i < matrixArray.length; i++) {
	    for (int j = 0; j < matrixArray[0].length; j++) {
		matrixArray[i][j] = matrixArray[i][j] * (1 - matrixArray[i][j]);
	    }
	}

	return matrix;
    }

    // Gibt die Größe des KNNs zurück
    public int[] getSize() {
	return new int[] { inodes, hnodes, onodes };
    }
}