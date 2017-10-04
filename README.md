##Handwriting Recognition using Machine Learning

#A single Fully-Connected Neural Network with softmax activation function implemented in Java

The images' pixels are simulated by squares in which the user draws the letter for prediction as seen below:

![Letter Recognition Program](https://github.com/fernandofc16/Handwriting-Recognition/blob/master/ScreenShot/Letter%20Recognition.png)

- **Test Letter:** Uses the dataset of all trained examples of the specified letter to test if the software predicts correctly otherwise it train the network again to fit the sample.

- **Test All Letters:** Do the same thing as 'Test Letter' above, but using all the dataset.

- **Train All Letters:** Train the network with all the dataset independently of correct or wrong prediction for all samples.

- **Train X times:** Train the network with the new input. Choose the label the new input has (A, B or C...) in 'Train as' and how many times it will adjust the network for the input (epochs), recommended 1000 times.

- **Button '>>>':** Predict the letter according to the input.

- **Clear:** Clean input and output data.
