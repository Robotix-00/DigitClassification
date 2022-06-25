package facharbeit.extras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import facharbeit.neuralNetwork.NeuralNetwork;

/*
 * Diese Klasse wird verwendet um mehrere Netze gleichzeitig zu trainieren.
 * Es sollte in einem IDE ausgeführt werden, da das Verändern der Parameter direkt im Code getätigt werden muss.
 */
public class TrainNets {
    private static int threadCount = 4;

    private static List<TrainRequest> trainRequests = new ArrayList<>();
    private static List<Thread> threads = new ArrayList<>();

    private static Runnable trainingsRun = new Runnable() {
	public void run() {
	    while (trainRequests.size() > 0) {
		TrainRequest request = trainRequests.get(0);
		trainRequests.remove(0);

		request.train();
		request.save();
	    }
	}
    };

    public static void main(String[] args) throws IOException, InterruptedException {
	for (int neurons : new int[] { 300, 400, 500, 600 })
	    trainRequests
		    .add(new TrainRequest(new NeuralNetwork(784, neurons, 10), 500, "test-net-" + neurons + "_500"));

	for (int i = 0; i < threadCount; i++) {
	    Thread thread = new Thread(trainingsRun);
	    threads.add(thread);
	    thread.start();

	    // Verzögerung, damit die TrainRequests nicht doppelt vergeben werden
	    Thread.sleep(500);
	}
    }
}