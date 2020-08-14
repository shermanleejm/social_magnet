package main.java.game;

import java.io.*;
import java.util.*;

public class Store{
    private ArrayList<StoreItem> storeInventory = new ArrayList<StoreItem>();

    public Store() {
        try (Scanner scFile = new Scanner(new FileInputStream("./data/crop.csv"))) {
            int count = 0;
            while (scFile.hasNext()) {
                if (count == 0) {
                    scFile.nextLine();
                }
                String line = scFile.nextLine();
                // System.out.println(line);
                String[] lineArr = line.split(",");

                StoreItem newItem = new StoreItem(
                    lineArr[0],
                    Integer.parseInt(lineArr[1]),
                    Integer.parseInt(lineArr[2]),
                    Integer.parseInt(lineArr[3]),
                    Integer.parseInt(lineArr[4]),
                    Integer.parseInt(lineArr[5]),
                    Integer.parseInt(lineArr[6])
                );
                storeInventory.add(newItem);   
                // System.out.println((lineArr[0]));
                count += 1;
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("crop details not found!");
        }
        
    }

    public ArrayList<StoreItem> getStore(){
        return storeInventory;
    }

    public int getTotalAvailableCrops() {
        return storeInventory.size();
    }

    public StoreItem getItemFromStore(int position) {
        return storeInventory.get(position);
    }
    public StoreItem getStoreItembySeedName(String name){
        StoreItem output = null;
        for (StoreItem item: storeInventory){
            if (item.getName().equals(name)){
                output = item;
            }
        }
        return output;
    }
    public HashMap<String, ArrayList<Integer>> getAllItemsHarvestDetails() {
        HashMap<String, ArrayList<Integer>> allItems = new HashMap<String, ArrayList<Integer>>();
 
        for (StoreItem item : storeInventory) {
            ArrayList<Integer> eachItemDetail = item.getItemHarvestDetails();
            allItems.put(item.getName(), eachItemDetail);
        }

        return allItems;
    }
}
