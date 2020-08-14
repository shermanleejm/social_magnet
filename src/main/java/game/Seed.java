package main.java.game;

import java.util.*;

public class Seed {
    private int seedID;
    private int userID;
    private String seedType;
    private int amount;

    public Seed (int seedID, int userID, String seedType, int amount){
        this.seedID = seedID;
        this.userID = userID;
        this.seedType = seedType;
        this.amount = amount;
    }

    public int getID(){
        return seedID;
    }
    
    public int getUserID(){
        return userID;
    }

    public String getSeedType(){
        return seedType;
    }
    
    public int getSeedAmount(){
        return amount;
    }

    public void addSeed(int addition){
        amount += addition;
    }
    public void reduceSeed(int subtraction){
        amount -= subtraction;
    }
}

