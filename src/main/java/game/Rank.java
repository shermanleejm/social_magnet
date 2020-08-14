package main.java.game;

import java.util.*;
import java.io.*;

public class Rank {
    private ArrayList<String> rankNames;
    private ArrayList<Integer> experience;
    private ArrayList<Integer> numPlots;

    public Rank() {
        ArrayList<String> rankNames = new ArrayList<String>();
        ArrayList<Integer> experience = new ArrayList<Integer>();
        ArrayList<Integer> numPlots = new ArrayList<Integer>();

        try (Scanner scFile = new Scanner(new FileInputStream("./data/rank.csv"))) {
            int count = 0;
            while (scFile.hasNext()) {
                if (count == 0) {
                    scFile.nextLine();
                }
                String line = scFile.nextLine();
                String[] arrLine = line.split(",");
                String rankTitle = arrLine[0];
                int minExp = Integer.parseInt(arrLine[1]);
                int numPlotsAllocated = Integer.parseInt(arrLine[2]);

                rankNames.add(rankTitle);
                experience.add(minExp);
                numPlots.add(numPlotsAllocated);   
                count += 1;      
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("rank details not found!");
        }
        this.rankNames = rankNames;
        this.experience = experience;
        this.numPlots = numPlots; 
    }

    public String getRank(int userExp) {
        for (int i = rankNames.size() -1; i >= 0; i--) {
            if (userExp >= experience.get(i)) {
                return rankNames.get(i);
            }
        }
        return "error";
    }

    public int getAllocatedPlots(int userExp) {
        for (int i = numPlots.size() -1; i >= 0; i--) {
            if (userExp >= experience.get(i)) {
                return numPlots.get(i);
            }
        }
        return 0;
    }

}