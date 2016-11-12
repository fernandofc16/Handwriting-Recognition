package neural;

import java.util.ArrayList;

import data.ReadWriteFile;

public class Train {

    private static final int NEURON_COUNT = 26;

    private Network network;
    private ArrayList<ArrayList<TrainingSet>> trainingSets;
    private String[] arrayABC = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String arrayIndexs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public Train() {
        this.network = new Network();
        this.network.addNeurons(NEURON_COUNT);
        this.trainingSets = ReadWriteFile.readTrainingSets();

        ReadWriteFile.restoreWeightsToNeurons(network);
        ReadWriteFile.showTextsABCSize();
    }

    public void trainAll() {  	
    	for(int i = 0; i < arrayABC.length; i++) {   		    		
    		train(10000, arrayABC[i]);
    	}
    }
    
    public void train(long count, String letter) {
        for (long i = 0; i < count; i++) {
            int index = ((int) (Math.random() * trainingSets.get(arrayIndexs.indexOf(letter)).size()));       
            TrainingSet set = trainingSets.get(arrayIndexs.indexOf(letter)).get(index);
            network.setInputs(set.getInputs());
            network.adjustWeights(set.getGoodOutput());
        }
        ReadWriteFile.saveWeigths(network.getNeurons());
        ReadWriteFile.saveBiasWeight(network.getNeurons());
    }
    
    public ArrayList<Integer> getRandomInputFromLetter(String letter) {
        int index = ((int) (Math.random() * trainingSets.get(arrayIndexs.indexOf(letter)).size()));       
        if(index == 0) { index++; }
        TrainingSet set = trainingSets.get(arrayIndexs.indexOf(letter)).get(index);
    	return set.getInputs();
    }

    public void setInputs(ArrayList<Integer> inputs) {
        network.setInputs(inputs);
    }

    public void addTrainingSet(TrainingSet newSet, String letter) {
        trainingSets.get(arrayIndexs.indexOf(letter)).add(newSet);
    }

    public ArrayList<Double> getOutputs() {
        return network.getOutputs();
    }
    
    public ArrayList<ArrayList<TrainingSet>> getTrainingSets() {
    	return trainingSets;
    }

}
