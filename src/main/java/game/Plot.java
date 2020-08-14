package main.java.game;

import java.util.*;

public class Plot {
    private int plotID;
    private int userID;
    private int plotPosition;
    private String name;
    private int timeToMaturity;
    private long startTime;
    private String status;
    private int maxYield;
    private int stolenYield;

    public Plot (int plotID, int userID, int plotPosition, String name, int timeToMaturity, long startTime, int maxYield, int stolenYield) {
        this.plotID = plotID;
        this.userID = userID;
        this.plotPosition = plotPosition;
        this.name = name;
        this.timeToMaturity = timeToMaturity;
        this.startTime = startTime;
        status = "growing";
        this.maxYield = maxYield;
        this.stolenYield = stolenYield;
    }

    public String getName() {
        return name;
    }
    
    public int getID(){
        return plotID;
    }

    public int getUserID(){
        return userID;
    }

    public int getPlotPosition(){
        return plotPosition;
    }

    public long getStartTime(){
        return startTime;
    }

    public String getStatus() {
        return status;
    }

    public int getMaxYield(){
        return maxYield;
    }

    public int getStolenYield(){
        return stolenYield;
    }

    public int updateStatus(int baseUnit) {
        long currTime = System.currentTimeMillis();
        float timeDiff = (((currTime - startTime) / (60000.0f)) / timeToMaturity) * baseUnit;
        int numUnits = (int)timeDiff;
        if (numUnits >= baseUnit) {
            status = "harvest";
            // harvestDetails = store.getHarvestValues(name);
        }
        if (numUnits >= baseUnit*2) {
            status = "wilted";
        }
        return numUnits;
    }

    public int getGrowthPercent() {
        int numUnits = updateStatus(100);
        return Math.min(numUnits, 100);
    }

    public String getGrowthProgress() {    
        int numUnits = updateStatus(10);
        if (status == "wilted") {
            return "  wilted  ";
        }

        String progressBar = "";
        for (int i = 1; i <= 10; i++) {
            if (numUnits >= i) {
                progressBar += "#";
            }
            else {
                progressBar += "-";
            }
        }
        return progressBar;
    }

}