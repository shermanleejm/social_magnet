package main.java.game;

import java.io.*;
import java.util.*;

import main.java.game.*;
import main.java.social.*;
import main.java.connection.*;


public class CityFarmersGameCtrl {
    private FarmerDAO fDAO;
    private UserDAO uDAO;
    private PlotDAO pDAO;
    private SeedDAO sDAO;
    private GiftDAO gDAO;
    private PostDAO postDAO;
    private Rank rank;
    private Store Store;

    public CityFarmersGameCtrl() {
        fDAO = new FarmerDAO();
        uDAO = new UserDAO();
        pDAO = new PlotDAO();
        sDAO = new SeedDAO();
        gDAO = new GiftDAO();
        postDAO = new PostDAO();
        rank = new Rank();
        Store = new Store();
    }

    public User getUser(int userID) {
        return uDAO.getUserByID(userID);
    }

    public Farmer getFarmerFromUser(User user) {
        int userID = user.getUserID();
        Farmer farmer = fDAO.getFarmerFromUserID(userID);
        return farmer;
    }

    public Farmer createFarmer(int userID) {
        return fDAO.createNewFarmer(userID);
    }

    public String getRankofFarmer(Farmer farmer) {
        return rank.getRank(farmer.getExperience());
    }

    public void getStoreInfo() {
        ArrayList<StoreItem> storeItems = Store.getStore();
        int totalAvailableCrops = Store.getTotalAvailableCrops();

        for (int i = 0; i < totalAvailableCrops; i++) {
            StoreItem item = storeItems.get(i);
            System.out.printf("%d. %s\t- %s gold\n", i+1, item.getName(), item.getCost());
            System.out.printf("   Harvest in: %s mins\n", item.getTime());
            System.out.printf("   XP Gained: %s\n", item.getExp());
        }
    }

    public ArrayList<StoreItem> getGifts() {
        ArrayList<StoreItem> storeItems = Store.getStore();
        for (int i = 0; i < storeItems.size(); i++) {
            System.out.printf("%d. 1 Bag of %s Seeds\n", i+1, storeItems.get(i).getName());
        }
        return storeItems;
    }

    public String purchaseSeeds(Farmer farmer, int purchaseOption, int purchaseAmt) {
        String message = "";
        int userID = farmer.getUserID();
        StoreItem item = Store.getItemFromStore(purchaseOption - 1);
        int totalAmount = item.getCost() * purchaseAmt;
        String seedName = item.getName();

        if (farmer.purchase(totalAmount) == -1) {
            message += "You do not have enough money to purchase specified seed and quantity.";
            return message;
        }
        
        boolean isUpdated;
        Seed toModSeed = sDAO.getSeedbyUserID(userID, seedName);
        if (toModSeed == null) {
            isUpdated = sDAO.createSeed(userID, seedName, purchaseAmt);
        }
        else {
            toModSeed.addSeed(1);
            isUpdated = sDAO.updateSeed(toModSeed);
        }

        if (!(isUpdated)) {
            return "Error purchasing seeds, please try again later!";
        }

        // update farmer details
        fDAO.updateFarmer(farmer);
        message += String.format("%d bags of seeds purchased for %d gold.\nYou now have %d gold left.\n", purchaseAmt, totalAmount, farmer.getGold());

        return message;
    }

    // returns true if plot can be cleared
    public boolean clearPlot(int plotNum, Farmer farmer, String mode) {
        List<Plot> farmPlots = farmer.getFarm();

        if (farmer.getGold() < 5) {
            System.out.println("You need at least 5 gold to clear a plot.");
            return false;
        }

        for (int i = 0; i < farmPlots.size(); i++) {
            Plot currPlot = farmPlots.get(i);
            if (currPlot.getPlotPosition() == plotNum) {
                if (currPlot.getStatus().equals(mode)) {
                    int plotID = currPlot.getID();
                    if (pDAO.deletePlotByID(plotID)) {
                        farmPlots.remove(i);
                        farmer.purchase(5);
                        fDAO.updateFarmer(farmer);
                        return true;
                    }
                }
                else {
                    System.out.println("Unable to clear crop that hasn't wilted! ");
                    return false;
                }

            }
        }
        return false;
    }

    public List harvestPlots(Farmer farmer) {
        // System.out.println(System.currentTimeMillis());
        List<Plot> farmPlots = farmer.getFarm();

        // creating arrays and maps to store values
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        HashMap<String, ArrayList<Integer>> harvestDetails = new  HashMap<String, ArrayList<Integer>>();

        // get crops details
        HashMap<String, ArrayList<Integer>> allCrops = Store.getAllItemsHarvestDetails();
        // get harvestable plots
        List<Plot> toHarvest = farmer.getHarverstablePlots();
       
        // get yield and exp etc
        for (Plot eachPlot : toHarvest) {
            ArrayList<Integer> plotDetails = new ArrayList<Integer>();
            String cropName = eachPlot.getName();
            System.out.println(cropName);
            toRemove.add(eachPlot.getPlotPosition());

            int harvestYield = eachPlot.getMaxYield() - eachPlot.getStolenYield();
            int gold = allCrops.get(cropName).get(3) * harvestYield;
            int exp = allCrops.get(cropName).get(0) * harvestYield;
            // System.out.println("harvest: " + harvestYield);
            // System.out.println("gold: " + gold);
            // System.out.println("exp: " + exp);
            plotDetails.add(harvestYield);
            plotDetails.add(gold);
            plotDetails.add(exp);

            if (harvestDetails.containsKey(cropName)) {    
                ArrayList<Integer> currDetails = harvestDetails.get(cropName);
                for (int i = 0; i < plotDetails.size(); i++) { 
                    currDetails.set(i, currDetails.get(i) + plotDetails.get(i));
                }
            }
            else {
                harvestDetails.put(cropName, plotDetails);
            }
        } 
        // returns null if nothing to remove
        if (toRemove.size() == 0) {
            System.out.println("No. of plots to harvest: 0");
            return null; 
        }
        // remove the harvested plots
        for (int i = 0; i < toRemove.size(); i++) {
            if (!(clearPlot(toRemove.get(i), farmer, "harvest"))) {
                System.out.println("plot cannot be harvested properly, error!");
                return null;
            }
        }

        int totalGold = 0;
        int totalExp = 0;
        // print values obtained
        for (Map.Entry<String, ArrayList<Integer>> eachCrop : harvestDetails.entrySet()) {
            // name, amt harvested, xp, gold
            ArrayList<Integer> currCrop = eachCrop.getValue();
            int yield = currCrop.get(0);
            int gold = currCrop.get(1);
            int exp = currCrop.get(2);
            System.out.printf("You have harvested %d %s for %d XP and %d gold.\n", yield, eachCrop.getKey(), exp, gold);
            totalGold += gold;
            totalExp += exp;
        }

        // add values to farmer
        farmer.updateFarm(farmPlots);
        farmer.updateGains(totalGold, totalExp);
        fDAO.updateFarmer(farmer);
        return farmPlots;
    }

    public boolean isPlotValid(Farmer farmer, int plotNum) {
        List<Plot> farmPlots = farmer.getFarm();
        int totalPlots = rank.getAllocatedPlots(farmer.getExperience());

        for (Plot eachPlot : farmPlots) {
            if (eachPlot.getPlotPosition() == plotNum) {
                return false;
            }
        }
        return (!(plotNum > totalPlots || plotNum < 0));
    }

    public boolean plantCrop(Farmer farmer, int plotNum, String seedName) {
        HashMap<String, ArrayList<Integer>> allItems = Store.getAllItemsHarvestDetails();
        int timeToMaturity = allItems.get(seedName).get(4);
        int userID = farmer.getUserID();
        boolean plotCreation = pDAO.createPlot(userID, plotNum, seedName, timeToMaturity);

        // deduct inventory seed
        Seed toModSeed = sDAO.getSeedbyUserID(userID, seedName);
        if (toModSeed == null) {
            return false;
        }
        toModSeed.reduceSeed(1);
        boolean updateSeed = sDAO.updateSeed(toModSeed);

        if (plotCreation && updateSeed) {
            List<Plot> newPlots = fDAO.getPlotsForFarmer(userID);
            List<Seed> updatedInventory = fDAO.getInventoryForFarmer(userID);

            if ((newPlots == null) || (updatedInventory == null)) {
                System.out.println("Error planting new crops.");
                return false;
            }
            farmer.updateFarm(newPlots);
            farmer.updateInventory(updatedInventory);
            return true;
        }
        return false;
    }

    public boolean stealCrop(Farmer friendFarmer, Farmer userFarmer) {
        List<Plot> harvestablePlots = friendFarmer.getHarverstablePlots();
        int totalGold = 0;
        int totalExp = 0;

        for (Plot eachPlot : harvestablePlots) {
            int id = eachPlot.getID();
            List<Integer> lootDetails = pDAO.stealPlotByID(id);
            String name = eachPlot.getName();
            int steal = lootDetails.get(0);
            int exp = lootDetails.get(1);
            int gold = lootDetails.get(2);
            totalExp += exp;
            totalGold += gold;
            System.out.printf("You have stolen %d %s for %d XP and %d gold\n", steal, name, exp, gold);
        }
        userFarmer.updateGains(totalGold, totalExp);
        fDAO.updateFarmer(userFarmer);
        return true;
    }

    public List<Seed> getAvailableSeeds(Farmer farmer) {
        List<Seed> toReturn = new ArrayList<Seed>();

        List<Seed> allSeeds = sDAO.getAllSeedsByUserID(farmer.getUserID());
        for (Seed eachSeed : allSeeds) {
            if (eachSeed.getSeedAmount() > 0) {
                toReturn.add(eachSeed);
            }
        }
        return toReturn;
    }

    public void getInventoryInfo(Farmer farmer) {
        List<Seed> userSeeds = getAvailableSeeds(farmer);
        for (int i = 0; i < userSeeds.size(); i++) {
            Seed currSeed = userSeeds.get(i);
            int qty = currSeed.getSeedAmount();
            String seedName = currSeed.getSeedType();
            System.out.printf("%d. %d Bags of %s\n", i+1, qty, seedName);
        }
    }

    public void getFarmlandInfo(Farmer farmer, String mode) {
        // System.out.println(System.currentTimeMillis());
        List<Plot> farmPlots = farmer.getFarm();
        int numFarmerPlots = farmPlots.size();
        int totalPlots = rank.getAllocatedPlots(farmer.getExperience());
        if (mode.equals("self")) {
            System.out.printf("You have %d plots of land.\n", totalPlots);
        }

        for (int i = 0; i < totalPlots; i++) {
            // either print the plot, or print empty
            boolean toPrint = true;

            for (int j = 0; j < farmPlots.size(); j++) {
                Plot currPlot = farmPlots.get(j);

                if (currPlot.getPlotPosition() == i+1) {
                    String plotType = currPlot.getName();
                    String progressBar = currPlot.getGrowthProgress();
                    String percentageStr = "";
                    if (!(progressBar == "  wilted  ")) {
                        int percentage = currPlot.getGrowthPercent();
                        percentageStr = percentage + "%";
                    }
                    System.out.printf("%d. %s\t[%s] %s\n", i+1, plotType, progressBar, percentageStr);
                    toPrint = false;
                }
            }
            if (toPrint) {
                System.out.printf("%d. <empty>\t[----------]\n", i+1);
            }
        }
    }

    public List<Integer> getFriendsInfo(Farmer farmer) {
        List<Integer> friendsList = uDAO.getUserFriendsById(farmer.getUserID());
        for (int i = 0; i < friendsList.size(); i++) {
            User currFriend = uDAO.getUserByID(friendsList.get(i));
            String friendName = currFriend.getName();
            String friendUsername = currFriend.getUsername();
            System.out.printf("%d. %s (%s)\n", i+1, friendName, friendUsername);
        }
        return friendsList;
    }

    public Farmer getFriendFarm(List<Integer> friendsList, int friendChosen) {
        int friendID = friendsList.get(friendChosen-1);
        Farmer friendFarmer = fDAO.getFarmerByID(friendID);
        if (friendFarmer == null) {
            return null;
        }
        String friendName = uDAO.getUserByID(friendID).getName();
        String currRank = getRankofFarmer(friendFarmer);
        int gold = friendFarmer.getGold();

        System.out.printf("\nName: %s \nTitle: %s \nGold: %d gold \n", friendName, currRank, gold);
        getFarmlandInfo(friendFarmer, "friend");
        return friendFarmer;
    }

    public boolean sendGifttoFriend(String friendUsername, int userID, String seedType) {
        // gift friends
        long currTime = System.currentTimeMillis();
        User friendUser = uDAO.getUserByUsername(friendUsername);
        if (friendUser == null) {
            System.out.println("Username does not exist, check for typos!");
            return false;
        }
        int friendUserID = friendUser.getUserID();
        List<Gift> allGifts = gDAO.getGiftSentByUserID(userID);

        // System.out.println("Checking if user is a friend...");
        List<Integer> friendList = uDAO.getUserFriendsById(userID);
        if (!(friendList.contains(friendUserID))) {
            System.out.println("Specified username is not a friend of yours.");
            return false;
        }

        // System.out.println("check if five gifts are sent");
        int count = 0;
        for (Gift eachGift : allGifts) {
            if (currTime - eachGift.getTimeStamp() < 86400000) {
                count += 1;
                if (eachGift.getFriendID() == friendUserID) {
                    System.out.println("You've already sent a gift to this friend today. Try again tomorrow!");
                    return false;
                }
            }
            if (count >= 5) {
                System.out.println("You have already sent five gifts today!");
                return false;
            }
        }

        int giftID = gDAO.createGift(userID, friendUserID, seedType);
        if (giftID != 0) {
            String msg = String.format("Here is a bag of %s for you. - City Farmers", seedType);
            int time = (int)currTime;

            return (postDAO.createGiftPost(userID, friendUserID, msg, time, giftID));
        }
        else {
            System.out.println("Error creating gift!");
            return false;
        }
        
    }

}

