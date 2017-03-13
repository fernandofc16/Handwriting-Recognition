package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.GoodOutputs;
import data.ReadWriteFile;
import neural.Train;
import neural.TrainingSet;

public class MainGui extends JFrame {

	private static final long serialVersionUID = 1163083359000321124L;

	private final int RESOLUTION = 50;

    private Train networkTrainer;

    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private ResultPanel resultPanel;

    private JButton clearButton;
    private JButton recognizeResultButton;
    private JButton trainNetworkButton;
    private JButton trainAllLettersButton;
    private JButton testAllLettersButton;
    private JButton testLetterButton;
    
    private JTextField trainingAmountTextField;
    private JComboBox<String> trainAsComboBox;
    private JComboBox<String> testAllComboBox;
    private JComboBox<String> testLetterComboBox;
    private JTextArea outputTextArea;
    
    private JLabel inputLabel;
    private JLabel outputLabel;
    private JLabel delayTest;
    
	private String[] arrayABC = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    
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

        resultPanel = new ResultPanel(400, 400);

        panel.add(outputLabel);
        panel.add(resultPanel);
        
        mainPanel.add(panel);
    }

    private void setCenterArea() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(200, 450));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        testLetterButton = new JButton("Test Letter:");
        centerPanel.add(testLetterButton, gbc);
        
        testLetterComboBox = new JComboBox<>(arrayABC);
        testLetterComboBox.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(testLetterComboBox, gbc);
        
        centerPanel.add(Box.createVerticalStrut(30), gbc);
        
        testAllLettersButton = new JButton("Test All Letters");
        centerPanel.add(testAllLettersButton, gbc);

        centerPanel.add(Box.createVerticalStrut(10), gbc);
        
        delayTest = new JLabel("Delay (ms)");
        centerPanel.add(delayTest, gbc);
        
        testAllComboBox = new JComboBox<>(new String[]{"0", "10", "50", "100"});
        testAllComboBox.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(testAllComboBox, gbc);
        
        centerPanel.add(new JSeparator(SwingConstants.VERTICAL), gbc);
        centerPanel.add(Box.createVerticalStrut(20), gbc);
        
        trainAllLettersButton = new JButton("Train All Letters");
        centerPanel.add(trainAllLettersButton, gbc);
        
        centerPanel.add(Box.createVerticalStrut(20), gbc);
                
        centerPanel.add(new JLabel("Train as:", SwingConstants.CENTER), gbc);

        trainAsComboBox = new JComboBox<>(arrayABC);
        trainAsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        trainAsComboBox.setMaximumSize(new Dimension((int) trainAsComboBox.getPreferredSize().getWidth(), 30));
        centerPanel.add(trainAsComboBox, gbc);

        centerPanel.add(Box.createVerticalStrut(30));

        trainNetworkButton = new JButton("Train X times:");

        trainingAmountTextField = new JFormattedTextField("1000");
        trainingAmountTextField.setMaximumSize(new Dimension(100, 30));
        trainingAmountTextField.setPreferredSize(new Dimension(100, 30));
        trainingAmountTextField.setHorizontalAlignment(JTextField.CENTER);
        trainingAmountTextField.setFont(new Font("Serif", 20, 20));
        
        centerPanel.add(trainNetworkButton, gbc);
        centerPanel.add(trainingAmountTextField, gbc);

        centerPanel.add(Box.createVerticalStrut(70));

        recognizeResultButton = new JButton(">>>");
        centerPanel.add(recognizeResultButton, gbc);

        centerPanel.add(Box.createVerticalStrut(30));

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
        	resultPanel.setBackgroundResult(arrayABC[index]);
        } else {
        	resultPanel.setBackgroundResult("blank");
        }
        return index;
    }
    
    private void setOnClicks() {
    	
    	testLetterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						deactivateJFrame();
						int correctRecognitions = 0;
	    				int totalTrys = 0;
	    				
	    				boolean wrong = false;
	    	    			
    	    			testLetterButton.setText("Testing Letter [".concat(testLetterComboBox.getSelectedItem().toString()).concat("]"));
    	    			testLetterButton.setForeground(Color.BLUE);
    	    			
    	    			int size = networkTrainer.getNumbOfInputsLetter(testLetterComboBox.getSelectedItem().toString());
    	    			for(int j = 0; j < size; j++) {
    	    				
    	    				drawingPanel.drawLetter(networkTrainer.getInputFromLetter(testLetterComboBox.getSelectedItem().toString(), j));
    	    				
	    	                try {
	    	                	Thread.sleep(300);
	    					} catch (Exception e2) {
	    					}
	    	                
	    	                int index = transformFunction();
	    	                
	    	                try {
	    	                	Thread.sleep(300);
	    					} catch (Exception e2) {
	    					}

	    	                if(index == testLetterComboBox.getSelectedIndex()) {
	    	                	//resultPanel.setCustomizedColor(Color.GREEN);
	    	                	resultPanel.setBackground(Color.GREEN);
	    	                	correctRecognitions++;
	    	                	wrong = false;
	    	                } else {
	    	                	//resultPanel.setCustomizedColor(Color.RED);
	    	                	resultPanel.setBackground(Color.RED);
	    	                	wrong = true;
	    	                }
	    	                //resultPanel.setBackgroundResult(arrayABC[i]);
	    	                totalTrys++;
	    	                try {
	    	                	Thread.sleep(500);
	    					} catch (Exception e2) {
	    					}
	    	                if(wrong) {
	    	                	testLetterButton.setText("Training Wrong Letter");
	    	                	testLetterButton.setForeground(Color.RED);
	    	                	networkTrainer.train(1000, testLetterComboBox.getSelectedItem().toString());
	    	                	testLetterButton.setForeground(Color.BLACK);
	    	                	testLetterButton.setText("Test Letter:");
	    	                }
	    	                //resultPanel.setCustomizedColor(Color.BLACK);
	    	                resultPanel.setBackground(Color.WHITE);
	    	                resultPanel.setBackgroundResult("blank");
    	    			}
	    	        	  	    		
	    	    		//JOptionPane.showMessageDialog(null, "The program recognized ".concat(correctRecognitions + " of 78 letters"));
	    	    		drawingPanel.clear();
	    	    		resultPanel.setBackgroundResult("blank");
	    				//}
	    	    		activateJFrame();
	    	    		testLetterButton.setText("Test Letter:");
	    	    		testLetterButton.setForeground(Color.BLACK);
	    				JOptionPane.showMessageDialog(null, "The program recognized ".concat(correctRecognitions + " of " + totalTrys + " letters"));	    	    		
	    			
					}
				}).start();
				
			}
		});
    	
    	testAllLettersButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				int speedTest = Integer.parseInt(testAllComboBox.getSelectedItem().toString());

	    		new Thread(new Runnable(){
	    			public void run() {
	    				deactivateJFrame();
	    				int correctRecognitions = 0;
	    				int totalTrys = 0;
	    				//for(int k = 0; k < 1000; k++) {
	    	    		
	    	    		boolean wrong = false;
	    	    		for(int i = 0; i < 26; i++) {
	    	    			
	    	    			testAllLettersButton.setText("Testing Letter [".concat(arrayABC[i]).concat("]"));
	    	    			testAllLettersButton.setForeground(Color.BLUE);
	    	    			
	    	    			int size = networkTrainer.getNumbOfInputsLetter(arrayABC[i]);
	    	    			for(int j = 0; j < size; j++) {
		    	                drawingPanel.drawLetter(networkTrainer.getInputFromLetter(arrayABC[i], j));
		    	                int index = transformFunction();
		    	                try {
		    	                	Thread.sleep(speedTest);
		    					} catch (Exception e2) {
		    					}
		    	                if(index == i) {
		    	                	//resultPanel.setCustomizedColor(Color.GREEN);
		    	                	resultPanel.setBackground(Color.GREEN);
		    	                	correctRecognitions++;
		    	                	wrong = false;
		    	                } else {
		    	                	//resultPanel.setCustomizedColor(Color.RED);
		    	                	resultPanel.setBackground(Color.RED);
		    	                	wrong = true;
		    	                }
		    	                //resultPanel.setBackgroundResult(arrayABC[i]);
		    	                totalTrys++;
		    	                try {
		    	                	Thread.sleep(speedTest);
		    					} catch (Exception e2) {
		    					}
		    	                if(wrong) {
		    	                	testAllLettersButton.setText("Training Wrong Letter");
		    	                	testAllLettersButton.setForeground(Color.RED);
		    	                	networkTrainer.train(1000, arrayABC[i]);
		    	                	testAllLettersButton.setForeground(Color.BLACK);
		    	                	testAllLettersButton.setText("Test All Letters");
		    	                }
		    	                //resultPanel.setCustomizedColor(Color.BLACK);
		    	                resultPanel.setBackground(Color.WHITE);
	    	    			}
	    	        	}
	    	    		
	    	    		//JOptionPane.showMessageDialog(null, "The program recognized ".concat(correctRecognitions + " of 78 letters"));
	    	    		drawingPanel.clear();
	    	    		resultPanel.setBackgroundResult("blank");
	    				//}
	    	    		activateJFrame();
	    	    		testAllLettersButton.setText("Test All Letters");
    	    			testAllLettersButton.setForeground(Color.BLACK);
	    				JOptionPane.showMessageDialog(null, "The program recognized ".concat(correctRecognitions + " of " + totalTrys + " letters"));
	    	    		
	    			}
	    		}).start();
			}
		});
    	
    	trainAllLettersButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						deactivateJFrame();
						trainAllLettersButton.setText("Training All...");
			    		trainAllLettersButton.setForeground(Color.RED);
			    		setEnabled(false);
			    		networkTrainer.trainAll();
			    		setEnabled(true);
			    		trainAllLettersButton.setForeground(Color.BLACK);
			    		trainAllLettersButton.setText("Train All Letters");
			    		activateJFrame();
					}
				}).start();
			}
		});
    	
        clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub		
	        	drawingPanel.clear();
	        	resultPanel.setBackgroundResult("blank");
			}
		});

        trainNetworkButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

	        	new Thread(new Runnable() {
					
					@Override
					public void run() {
						setEnabled(false);

						trainNetworkButton.setText("Training...");
			        	trainNetworkButton.setForeground(Color.RED);
			            String letter = (String) trainAsComboBox.getSelectedItem();
			            networkTrainer.addTrainingSet(new TrainingSet(drawingPanel.getPixels(), GoodOutputs.getInstance().getGoodOutput(letter)), letter);
			            ReadWriteFile.saveToFile(drawingPanel.getPixels(), letter);
			            
			            int number = 0;
			            try {
			                number = Integer.parseInt(trainingAmountTextField.getText());
			            } catch (Exception x) {
			            }

			            networkTrainer.train(number, letter);
			            resultPanel.setBackgroundResult(arrayABC[transformFunction()]);
			            trainNetworkButton.setText("Train X times:");
			            trainNetworkButton.setForeground(Color.BLACK);	        
			            setEnabled(true);
					}
				
	        	}).start();

			}
		});

        recognizeResultButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				transformFunction();
			}
		});

    }

    private void updateTextArea() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> outputs = networkTrainer.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            int letterValue = i + 65;
            sb.append((char) letterValue);
            double value = outputs.get(i);
            //if (value < 0.01) { value = 0; }
            if (value > 0.99) { value = 1; }

            value *= 1000;
            int x = (int) (value);
            value = x / 1000.0;

            sb.append("\t " + value);
            sb.append("\n");
        }
        outputTextArea.setText(sb.toString());
    }
    
    private void deactivateJFrame() {
    	this.setEnabled(false);
    }

    private void activateJFrame() {
    	this.setEnabled(true);
    }
    
}
