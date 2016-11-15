package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ResultPanel extends JPanel{

	private static final long serialVersionUID = -4318333228000514760L;
	
	private Image image;
	
    public ResultPanel(int w, int h) {
        super();

        setPreferredSize(new Dimension(w, h));
        setBackground(Color.WHITE);

    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
        	g.drawImage(image, 0, 0, this);
        }
    }
    
    public Image getImageFromResource(String letter) {
    	try {
			return ImageIO.read((ResultPanel.class.getResourceAsStream(letter + ".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }

    public void setBackgroundResult(String letter) {
    	this.image = getImageFromResource(letter);
    	repaint();
    }

}
