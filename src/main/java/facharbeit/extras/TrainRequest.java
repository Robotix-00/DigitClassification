package facharbeit.extras;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import facharbeit.neuralNetwork.NeuralNetwork;

public class TrainRequest {
    private static BufferedWriter log;
    
    private Date startTime;
    private Date endTime;
    
    static {
	try {
	    log = new BufferedWriter(new FileWriter("KNNs/Iteration3/log.txt", true));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    
    private NeuralNetwork net;
    private int epochs;
    
    private double[][] targets, inputs;
    private int[] labels;
    
    private int trainElements = 1381;
    private int testElements = 80;
    
    private String name;


    public TrainRequest(NeuralNetwork net, int epochs, String name) {
	this.net = net;
	this.epochs = epochs;
	this.name = name;
    }

    public void train() {
	startTime = new Date();
	Scanner scanner;
	targets = new double[trainElements][10];
	inputs = new double[trainElements][784];
	labels = new int[trainElements];

	/*
	 * Liest die Datensätze aus der Datei und speichert sie in den Variablen
	 * 'inputs' und 'targets'
	 */
	try {
	    scanner = new Scanner(new File("dataset/trainset.csv"));
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

	    if (labels[i] != -1)
		targets[i][labels[i]] = 1.0;
	    for (int j = 0; j < 784; j++) {
		inputs[i][j] = (double) Integer.parseInt(values[j + 1]) / 255;
	    }
	}
	scanner.close();

	/*
	 * Training
	 */
	net.fit(inputs, targets, epochs, 0.0005);
	
	endTime = new Date();

    }

    public void save() {
	net.save(new File("KNNs/KNNs/Iteration3/" + name + ".knn"));
	
	
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
	try {
	    log.write(name + ": " + yeyCounter + " von " + trainElements + " ("
		    + ((double) yeyCounter / trainElements) * 100 + "%) bein Trainingssatz");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	targets = new double[testElements][10];
	inputs = new double[testElements][784];
	labels = new int[testElements];
	
	
	Scanner scanner;
	try {
	    scanner = new Scanner(new File("dataset/testset.csv"));
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

	    if (labels[i] != -1)
		targets[i][labels[i]] = 1.0;
	    for (int j = 0; j < 784; j++) {
		inputs[i][j] = (double) Integer.parseInt(values[j + 1]) / 255;
	    }
	}
	scanner.close();
	
	yeyCounter = 0;
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

	try {
	    log.write(" | " + yeyCounter + " von " + testElements + " (" + ((double) yeyCounter / testElements) * 100
		    + "%) bein Testsatz");
	    
	    long diff = endTime.getTime() - startTime.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
	    
	    log.write(" | Zeit: " + diffHours +"h " + diffMinutes + "min " + diffSeconds + "sec");
	} catch (IOException e1) {
	    e1.printStackTrace();
	}

	try {
	    log.newLine();
	    log.flush();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
