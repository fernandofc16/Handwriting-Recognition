package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.GoodOutputs;
import data.GoodPixels;
import data.ReadWriteFile;
import neural.Train;
import neural.TrainingSet;

public class MainGui extends JFrame {

	private static final long serialVersionUID = 1163083359000321124L;

	private final int RESOLUTION = 20;

    private Train networkTrainer;

    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private CustomPanel resultPanel;

    private JButton clearButton;
    private JButton recognizeResultButton;
    private JButton trainNetworkButton;
    private JButton trainAllLettersButton;
    private JButton testAllLettersButton;
    
    private JTextField trainingAmountTextField;
    private JComboBox<String> trainAsComboBox;
    private JTextArea outputTextArea;
    
    private JLabel inputLabel;
    private JLabel outputLabel;
    
    public static void main(String[] args) {
    	new MainGui();
    }

    public MainGui() {
        super("Letters recognition using neural networks");

        try {
			ReadWriteFile.createDirectoryAndFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        networkTrainer = new Train();

        setMainPanel();
        setLeftSide();
        setCenterArea();
        setRightSide();
        setOutputPanel();

        setOnClicks();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(1260, 500));
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        setContentPane(mainPanel);
    }

    private void setLeftSide() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(410, 450));
        
        inputLabel = new JLabel("INPUT:");
        inputLabel.setFont(new Font("Arial", 20, 30));
        
        drawingPanel = new DrawingPanel(400, 400, RESOLUTION);

        panel.add(inputLabel);
        panel.add(drawingPanel);

        mainPanel.add(panel);
    }
    
    private void setRightSide() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(410, 450));
        
        outputLabel = new JLabel("OUTPUT:");
        outputLabel.setFont(new Font("Arial", 20, 30));

        resultPanel = new CustomPanel(400, 400, RESOLUTION);

        panel.add(outputLabel);
        panel.add(resultPanel);
        
        mainPanel.add(panel);
    }

    private void setCenterArea() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(200, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        testAllLettersButton = new JButton("Test All Letters");
        //testAllLetters.setEnabled(false);
        centerPanel.add(testAllLettersButton, gbc);

        centerPanel.add(Box.createVerticalStrut(30), gbc);
        
        trainAllLettersButton = new JButton("Train All Letters");
        centerPanel.add(trainAllLettersButton, gbc);
        
        centerPanel.add(Box.createVerticalStrut(30), gbc);
                
        centerPanel.add(new JLabel("Train as:", SwingConstants.CENTER), gbc);

        trainAsComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"});
        trainAsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        trainAsComboBox.setMaximumSize(new Dimension((int) trainAsComboBox.getPreferredSize().getWidth(), 30));
        centerPanel.add(trainAsComboBox, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        trainNetworkButton = new JButton("Train X times:");

        trainingAmountTextField = new JFormattedTextField("5000");
        trainingAmountTextField.setMaximumSize(new Dimension(100, 30));
        trainingAmountTextField.setPreferredSize(new Dimension(100, 30));
        trainingAmountTextField.setHorizontalAlignment(JTextField.CENTER);
        trainingAmountTextField.setFont(new Font("Serif", 20, 20));
        
        centerPanel.add(trainNetworkButton, gbc);
        centerPanel.add(trainingAmountTextField, gbc);

        centerPanel.add(Box.createVerticalStrut(100));

        recognizeResultButton = new JButton(">>>");
        centerPanel.add(recognizeResultButton, gbc);

        centerPanel.add(Box.createVerticalStrut(50));

        clearButton = new JButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(clearButton, gbc);

        mainPanel.add(centerPanel);
    }

    private void setOutputPanel() {
        JPanel outputPanel = new JPanel();
        outputPanel.setPreferredSize(new Dimension(200, 430));

        outputTextArea = new JTextArea();
        outputTextArea.setPreferredSize(new Dimension(200, 430));
        outputPanel.add(outputTextArea);

        mainPanel.add(outputPanel);
    }
    
    private int transformFunction() {
        networkTrainer.setInputs(drawingPanel.getPixels());

        ArrayList<Double> outputs = networkTrainer.getOutputs();
        int index = 0;
        for (int i = 0; i < outputs.size(); i++) {
            if (outputs.get(i) != null && outputs.get(i) > outputs.get(index)) {
                index = i;
            }
        }

        updateTextArea();

        if(index <= 25) {
        	trainAsComboBox.setSelectedIndex(index);
            resultPanel.drawLetter(GoodPixels.getInstance().getGoodPixels(index));
        } else {
        	resultPanel.clear();
        }
        return index;
    }
    
    private void setOnClicks() {      	
    	
    	testAllLettersButton.addActionListener(e -> {
    		new Thread(new Runnable(){
    			public void run() {
    				int correctRecognitions = 0;
    				for(int k = 0; k < 1000; k++) {
    				String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    	    		
    	    		boolean wrong = false;
    	    		for(int i = 0; i < 26; i++) {   
    	    			//for(int j = 0; j < 3; j++) {
	    	                drawingPanel.drawLetter(networkTrainer.getRandomInputFromLetter(letters[i]));
	    	                int index = transformFunction();
	    	                try {
	    	                	//Thread.sleep(500);
	    					} catch (Exception e2) {
	    					}
	    	                if(index == i) {
	    	                	resultPanel.setCustomizedColor(Color.GREEN);
	    	                	correctRecognitions++;
	    	                	wrong = false;
	    	                } else {
	    	                	resultPanel.setCustomizedColor(Color.RED);
	    	                	wrong = true;
	    	                }
	    	                resultPanel.drawLetter(GoodPixels.getInstance().getGoodPixels(index));
	    	                try {
	    	                	//Thread.sleep(500);
	    					} catch (Exception e2) {
	    					}
	    	                if(wrong) {
	    	                	testAllLettersButton.setText("Training Wrong Letter");
	    	                	testAllLettersButton.setForeground(Color.RED);
	    	                	networkTrainer.train(5000, letters[i]);
	    	                	testAllLettersButton.setForeground(Color.BLACK);
	    	                	testAllLettersButton.setText("Test All Letters");
	    	                }
	    	                resultPanel.setCustomizedColor(Color.BLACK);
    	    			}
    	        	}
    	    		
    	    		//JOptionPane.showMessageDialog(null, "The program recognized ".concat(correctRecognitions + " of 78 letters"));
    	    		drawingPanel.clear();
    	    		resultPanel.clear();
    				//}
    				JOptionPane.showMessageDialog(null, "The program recognized ".concat(correctRecognitions + " of 26000 letters"));
    			}
    		}).start();
    		
    	});
    	
    	trainAllLettersButton.addActionListener(e -> {
    		trainAllLettersButton.setText("Training...");
    		trainAllLettersButton.setForeground(Color.RED);
    		this.setEnabled(false);
    		JOptionPane.showMessageDialog(this, "The training of all letters will start\n" + "This might take a while, please wait...");
    		networkTrainer.trainAll();
    		this.setEnabled(true);
    		trainAllLettersButton.setForeground(Color.BLACK);
    		trainAllLettersButton.setText("Train All Letters");
    		JOptionPane.showMessageDialog(this, "Train completed");
    	});
    	
        clearButton.addActionListener(e -> {
        	drawingPanel.clear();
        	resultPanel.clear();
        });

        trainNetworkButton.addActionListener(e -> {
            String letter = (String) trainAsComboBox.getSelectedItem();
            networkTrainer.addTrainingSet(new TrainingSet(drawingPanel.getPixels(), GoodOutputs.getInstance().getGoodOutput(letter)), letter);
            ReadWriteFile.saveToFile(drawingPanel.getPixels(), letter);
            
            int number = 0;
            try {
                number = Integer.parseInt(trainingAmountTextField.getText());
            } catch (Exception x) {
                JOptionPane.showMessageDialog(this, "Wrong input", "ERROR", JOptionPane.PLAIN_MESSAGE);
            }

            networkTrainer.train(number, letter);
        });

        recognizeResultButton.addActionListener(e -> {
        	transformFunction();
        });

    }

    private void updateTextArea() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> outputs = networkTrainer.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            int letterValue = i + 65;
            sb.append((char) letterValue);
            double value = outputs.get(i);
            if (value < 0.01)
                value = 0;
            if (value > 0.99)
                value = 1;

            value *= 1000;
            int x = (int) (value);
            value = x / 1000.0;

            sb.append("\t " + value);
            sb.append("\n");
        }
        outputTextArea.setText(sb.toString());
    }

}
