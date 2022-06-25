package facharbeit.neuralNetwork;

import Jama.Matrix;

public class MatrixManager {
    // returns the minimum value of a 2d array
    public static double getMin(double[][] values) {
	double min = values[0][1];
	
	int j = 1;
	for (int i = 0; i < values.length; i++) {
	    for (; j < values[0].length; j++) {
		if (min > values[i][j])
		    min = values[i][j];
	    }
	    j = 0;
	}

	return min;
    }

    // returns the maximum value of a 2d array
    public static double getMax(double[][] values) {
	double max = values[0][1];
	
	int j = 1;
	for (int i = 0; i < values.length; i++) {
	    for (; j < values[0].length; j++) {
		if (max < values[i][j])
		    max = values[i][j];
	    }
	    j = 0;
	}

	return max;
    }

    // limits all values of the array to be 0<x<1
    public static Matrix limit(Matrix matrix) {
	double[][] out = matrix.getArray();

	double max = getMax(matrix.getArrayCopy());
	double min = getMin(matrix.getArrayCopy());

	for (int i = 0; i < out.length; i++) {
	    for (int j = 0; j < out[0].length; j++) {
		out[i][j] -= min;
		out[i][j] /= max;
		out[i][j] *= 0.98;
		out[i][j] += 0.01;
	    }
	}

	return matrix;
    }

    // creates a matrix of size x*y where each field is set to value
    public static Matrix createMatrix(double value, int x, int y) {
	Matrix out = new Matrix(x, y);

	for (int i = 0; i < x; i++) {
	    for (int j = 0; j < y; j++) {
		out.set(x, y, value);
	    }
	}
	return out;
    }

    // maps a 1d array to a 2d array of size x*y
    public static Matrix createMatrix(double[] values, int x, int y) {
	Matrix tmp = new Matrix(x, y);

	for (int i = 0; i < x; i++) {
	    for (int j = 0; j < y; j++) {
		tmp.getArray()[i][j] = values[(i * y) + j];
	    }
	}
	return tmp;
    }
}
