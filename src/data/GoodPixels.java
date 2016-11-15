package data;

import java.util.ArrayList;

public class GoodPixels {

    private static GoodPixels instance;

    private ArrayList<ArrayList<Integer>> goodValues;

    public static GoodPixels getInstance() {
        if (instance == null)
            instance = new GoodPixels();

        return instance;
    }

    public GoodPixels() {
        this.goodValues = new ArrayList<>();
    }
    
    public void setGoodPixels(ArrayList<ArrayList<Integer>> goodPixels) {
    	this.goodValues = goodPixels;
    }

    public ArrayList<Integer> getGoodPixels(int index) {
        return goodValues.get(index);
    }

    public ArrayList<Integer> getGoodPixels(String letter) {
        char charLetter = letter.charAt(0);
        int index = ((int) charLetter) - 65;

        return goodValues.get(index);
    }
}