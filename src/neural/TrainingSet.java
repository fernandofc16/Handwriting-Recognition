package neural;

import java.util.ArrayList;

public class TrainingSet {

	// The inputs you want to train your software to know
    private ArrayList<Integer> inputs;
    
    // The outputs you want you program to answer for the inputs given
    private ArrayList<Double> goodOutput;

    public TrainingSet(ArrayList<Integer> inputs, ArrayList<Double> goodOutput) {
        this.inputs = inputs;
        this.goodOutput = goodOutput;
    }

    public ArrayList<Integer> getInputs() {
        return inputs;
    }

    public ArrayList<Double> getGoodOutput() {
        return goodOutput;
    }
}
