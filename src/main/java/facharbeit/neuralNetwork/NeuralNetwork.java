package facharbeit.neuralNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import Jama.Matrix;

public class NeuralNetwork {
    private int inodes, hnodes, onodes;
    private Matrix weights_ih, weights_ho;

    public NeuralNetwork(int inodes, int hnodes, int onodes) {
	this.inodes = inodes;
	this.hnodes = hnodes;
	this.onodes = onodes;

	// generates random weights between -0.5 and 0.5
	weights_ih = Matrix.random(hnodes, inodes).minus(new Matrix(hnodes, inodes, 0.5));
	weights_ho = Matrix.random(onodes, hnodes).minus(new Matrix(onodes, hnodes, 0.5));
    }

    // returns a prediction
    public double[] query(double[] in) {
	Matrix inputs, hiddenValues, outputValues;

	inputs = new Matrix(new double[][] { in }).transpose();
	hiddenValues = sigmoid(weights_ih.times(inputs));
	outputValues = sigmoid(weights_ho.times(hiddenValues)).transpose();

	return outputValues.getArray()[0];
    }

    // trains the net with the passed data
    public void train(double[] in, double[] target, double learnrate) {
	Matrix inputs, targets, hiddenValues, outputValues, outputError, hiddenError, weightAdaptation;

	inputs = new Matrix(new double[][] { in }).transpose();
	targets = new Matrix(new double[][] { target }).transpose();

	hiddenValues = sigmoid(weights_ih.times(inputs));
	outputValues = sigmoid(weights_ho.times(hiddenValues));

	// calculating the error
	outputError = targets.minus(outputValues);
	hiddenError = weights_ho.transpose().times(outputError);

	weightAdaptation = outputError.arrayTimes(desigmoid(outputValues)).times(hiddenValues.transpose());
	weights_ho.plusEquals(weightAdaptation.times(learnrate));

	weightAdaptation = hiddenError.arrayTimes(desigmoid(hiddenValues)).times(inputs.transpose());
	weights_ih.plusEquals(weightAdaptation.times(learnrate));
    }

    // trains the net for multiple epochs
    public void fit(double[][] in, double[][] target, int epochs, double learnrate) {
	System.out.println("starting training...");
	for (int i = 0; i < epochs; i++) {
	    for (int j = 0; j < in.length; j++) {
		train(in[j], target[j], learnrate);
	    }
	    System.out.println("epoch " + (i + 1) + "/" + epochs);
	}
	System.out.println("training finished");
    }

    // loads a neural network from the specified file
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

    // saves the neural network to the specified file
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

    // applies the sigmoid function to all values of a matrix
    private static Matrix sigmoid(Matrix matrix) {
	double[][] matrixArray = matrix.getArray();
	for (int i = 0; i < matrixArray.length; i++) {
	    for (int j = 0; j < matrixArray[0].length; j++) {
		matrixArray[i][j] = 1 / (1 + Math.pow(Math.E, -matrixArray[i][j]));
	    }
	}

	return matrix;
    }

    // applies the reverse sigmoid function to all valies of a matrix
    private static Matrix desigmoid(Matrix matrix) {
	double[][] matrixArray = matrix.getArray();
	for (int i = 0; i < matrixArray.length; i++) {
	    for (int j = 0; j < matrixArray[0].length; j++) {
		matrixArray[i][j] = matrixArray[i][j] * (1 - matrixArray[i][j]);
	    }
	}

	return matrix;
    }

    // returns the size of all three layers of the neural network
    public int[] getSize() {
	return new int[] { inodes, hnodes, onodes };
    }
}