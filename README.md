# digit classification

The goal was to create a nerual network from scratch that could classify a digit from an image. It is based on a paper I wrote in in 10th grade. I do not intend to maintain this project further.

## Development
This project uses the build-tool Maven to manage the one dependency. I tested it on Java 17 although it should work on all versions > 1.7.
By default, the program includes a default network that worked quite well for me.

## Dataset
The program uses the csv format for its datasets, the first value represents the label of the following 784 grayscale pixels. In order to train the current nerual network I used the [MNIST-Dataset](http://yann.lecun.com/exdb/mnist/) as well as a self created one which turned out to work better.
This might be caused by the fact, that the handwritten digits look quite diffrent from the mouse-drawn ones that this program creates.
