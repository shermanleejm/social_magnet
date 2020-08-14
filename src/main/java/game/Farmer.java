package main.java.game;

import java.util.*;

public class Farmer {
    private int userID;
    private List<Plot> farm;
    private List<Seed> inventory;
    private int experience;
    private int gold;

    public Farmer(int userID, List<Plot> farm, List<Seed> inventory, int experience, int gold) {
        this.userID = userID;
        this.farm = farm;
        this.inventory = inventory;
        this.experience = experience;
        this.gold = gold;
    }

    public int getExperience() {
        return experience;
    }

    public int getGold() {
        return gold;
    }

    public int getUserID(){
        return userID;
    }

    public List<Plot> getFarm() {
        return farm;
    }

    public List<Seed> getInventory() {
        return inventory;
    }

    public int purchase(int amount) {
        if (gold < amount) {
            return -1;
        }
        gold -= amount;
        return gold;
    }

    // public void harvestGains(int totalGold, int totalExp, List<Plot> farmPlots) {
    //     gold += totalGold;
    //     experience += totalExp;
    //     farm = farmPlots;
    // }

    public void updateGains(int totalGold, int totalExp) {
        gold += totalGold;
        experience += totalExp;
    }

    public List<Plot> getHarverstablePlots() {
        List<Plot> toReturn = new ArrayList<Plot>();
        for (Plot eachPlot: farm) {
            if (eachPlot.getStatus() == "harvest") {
                // System.out.println(eachPlot.getName());
                toReturn.add(eachPlot);
            }
        }
        return toReturn;
    }
    
    // to update the farmer object itself
    public void updateFarm(List<Plot> newPlots) {
        farm = newPlots;
    }

    public void updateInventory(List<Seed> updatedInventory) {
        inventory = updatedInventory;
    }

}