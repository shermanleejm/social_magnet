package main.java.game;

import java.io.*;
import java.util.*;

public class StoreItem {
    private String name;
    private int cost;
    private int time;
    private int cropXP;
    private int minYield;
    private int maxYield;
    private int salePrice;


    public StoreItem(String name, int cost, int time, int cropXP, int minYield, int maxYield, int salePrice){
        this.name = name;
        this.cost = cost;
        this.time = time;
        this.cropXP = cropXP;
        this.minYield = minYield;
        this.maxYield = maxYield;
        this.salePrice = salePrice;
    }

    public String getName(){
        return name;
    }

    public int getCost(){
        return cost;
    }

    public int getExp(){
        return cropXP;
    }

    public int getTime(){
        return time;
    }

    public int getPrice(){
        return salePrice;
    }
    // initalise random yield for create plot between min and max yield
    public int getRandomYield(){
        Random r = new Random();
		return r.nextInt((maxYield - minYield) + 1) + minYield;
    }
    public int getMaxYield(){
        return maxYield;
    }

    public ArrayList<Integer> getItemHarvestDetails() {
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        toReturn.add(cropXP);
        toReturn.add(minYield);
        toReturn.add(maxYield);
        toReturn.add(salePrice);
        toReturn.add(time); // used in plantCrop
        return toReturn;
    }
   
}
