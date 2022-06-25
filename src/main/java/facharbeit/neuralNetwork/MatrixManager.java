package facharbeit.neuralNetwork;

import Jama.Matrix;

/*
 * Beinhaltet Methoden zur Verarbeitung und Erstellung von Matritzen
 */
public class MatrixManager {
    // Gibt den kleinsten Wert eines Arrays zurück
    public static double getMin(double[][] values) {
	double min = 100;

	for (int i = 0; i < values.length; i++) {
	    for (int j = 0; j < values[0].length; j++) {
		if (min > values[i][j])
		    min = values[i][j];
	    }
	}

	return min;
    }

    // Gibt den größten Wert eines Arrays zurück
    public static double getMax(double[][] values) {
	double max = -100;

	for (int i = 0; i < values.length; i++) {
	    for (int j = 0; j < values[0].length; j++) {
		if (max < values[i][j])
		    max = values[i][j];
	    }
	}

	return max;
    }

    // Limitiert die übergebene Matrix auf den Bereich 0.01 bis 0.99
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

    // Erzeugt eine Matrix mit den Maßen x*y, wobei jeder Wert value entspricht
    public static Matrix createMatrix(double value, int x, int y) {
	Matrix out = new Matrix(x, y);

	for (int i = 0; i < x; i++) {
	    for (int j = 0; j < y; j++) {
		out.set(x, y, value);
	    }
	}
	return out;
    }

    // Erstellt aus einem 1d-Array eine 2d-Matrix mit den Maßen x*y
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
