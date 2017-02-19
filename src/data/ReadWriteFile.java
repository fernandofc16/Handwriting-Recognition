package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import neural.Network;
import neural.Neuron;
import neural.TrainingSet;

public class ReadWriteFile {

	//private static File directory = new File("C:/Hand_Reco/");
	private static File directory = new File(System.getProperty("user.dir").replace("\\", "/") + "/Hand_Reco/");
	private static File temporaryFileABC;
	private static File biasValues = new File(directory, "biasValues.txt");
	private static File weightValues = new File(directory, "weightValues.txt");
	private static File goodPixels = new File(directory, "goodPixels.txt");
	private static String[] arrayABC = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	public static void createDirectoryAndFiles() throws IOException {
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		if(!biasValues.exists()) {
			biasValues.createNewFile();
		}
		
		if(!weightValues.exists()) {
			weightValues.createNewFile();
		}
		
		if(!goodPixels.exists()) {
			goodPixels.createNewFile();
		}
		
		for(int i = 0; i < arrayABC.length; i++) {
			temporaryFileABC = new File(directory, arrayABC[i] + ".txt");
			if(!temporaryFileABC.exists()) {
				temporaryFileABC.createNewFile();
			}
		}
	}
	
    public static ArrayList<ArrayList<TrainingSet>> readTrainingSets() {
    	ArrayList<ArrayList<TrainingSet>> trainingSets = new ArrayList<>();
        
        for (int i = 0; i < 26; i++) {
            char letterValue = (char) (i + 65);
            String letter = String.valueOf(letterValue);
            String fileABCPath = directory + "/" + letter + ".txt";
            ArrayList<TrainingSet> listSets = new ArrayList<>();
            for (ArrayList<Integer> list : readFromFile(fileABCPath)) {
            	listSets.add(new TrainingSet(list, GoodOutputs.getInstance().getGoodOutput(letter)));
            }
            trainingSets.add(listSets);
        }

        return trainingSets;
    }

    private static ArrayList<ArrayList<Integer>> readFromFile(String filename) {
        ArrayList<ArrayList<Integer>> inputs = new ArrayList<>();

        try {
            InputStream in = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<Integer> input = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    int value = 0;
                    try {
                        value = Integer.parseInt(String.valueOf(line.charAt(i)));
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }
                    input.add(value);
                }
                inputs.add(input);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputs;
    }
    
    public static void showTextsABCSize() {
    	ArrayList<String> results = new ArrayList<>();
    	String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    	for(int i = 0; i < 26; i++) {
	    	try {
	            InputStream in = new FileInputStream(directory + "/" + letters[i] + ".txt");
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            
	            int numbLines = 0;
	            while ((reader.readLine()) != null) {
	                numbLines++;
	            }
	            results.add(letters[i] + ":" + numbLines + " results");
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	
    }

    public static void saveToFile(ArrayList<Integer> input, String filename) {
        try {
            File file = new File(directory + "/" + filename + ".txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            for (Integer i : input) {
                pw.write(Integer.toString(i));
            }
            pw.write("\n");
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveWeigths(ArrayList<Neuron> neurons) {
        try {
            File file = new File(directory + "/weightValues.txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            for (Neuron neuron : neurons) {
            	for (Double weight : neuron.getWeights()) {
            		pw.write(weight + "&");
            	}
            	pw.write("\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveBiasWeight(ArrayList<Neuron> neurons) {
        try {
            File file = new File(directory + "/biasValues.txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            for (Neuron neuron : neurons) {
            	pw.write(neuron.getBiasWeight() + "\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void restoreWeightsToNeurons(Network network) {
    	
    	ArrayList<Neuron> neuronsRestored = new ArrayList<>();
    	ArrayList<Double> biasWeights = new ArrayList<>();
    	try {
            InputStream in = new FileInputStream(new File(directory + "/biasValues.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
            	biasWeights.add(Double.parseDouble(line));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	try {
            InputStream in = new FileInputStream(new File(directory + "/weightValues.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
            	String[] arrayWeights = line.split("&");
            	ArrayList<Double> weightsNeurons = new ArrayList<>();
            	for(int i = 0; i < arrayWeights.length; i++) {
            		weightsNeurons.add(Double.parseDouble(arrayWeights[i]));
            	}
            	neuronsRestored.add(new Neuron(weightsNeurons, biasWeights.get(index)));
            	index++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	if(neuronsRestored.size() != 0) {
    		network.setNeurons(neuronsRestored);
    	}

    }
    
    public static void cleanDocument(String filename) {
    	PrintWriter writer;
		try {
			File file = new File("src/resources/" + filename + ".txt");
			writer = new PrintWriter(new FileOutputStream(file));
	    	writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

}
