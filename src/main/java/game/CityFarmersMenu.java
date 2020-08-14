package main.java.game;

import main.java.game.*;
import main.java.social.*;
import main.java.connection.*;

import java.util.*;
import java.io.*;

public class CityFarmersMenu {
    private CityFarmersGameCtrl ctrl;

    // initialise the cityFarmersGameCtrl
    public CityFarmersMenu(CityFarmersGameCtrl ctrl) {
        this.ctrl = ctrl;
    }

    // menuHeader: prints out header for each page
    // user goes to, depending on user object and 
    // page user is at.

    // takes in the name of the header you wish to
    // output, and the user object
    public void menuHeader(String menu, User currUser) {
        System.out.print("\n== Social Magnet :: City Farmers");

        if (menu == "main") {
            System.out.println(" ==");
        }
        else {
            System.out.printf(" :: %s ==\n", menu);
        }

        String name = currUser.getName();
        System.out.printf("Welcome, %s!\n", name);

        Farmer farmer = currUser.getFarmer();
        String currRank = ctrl.getRankofFarmer(farmer);
        int gold = farmer.getGold();

        System.out.printf("Title: %s \t\t Gold: %d gold \n\n", currRank, gold);
    }


    // displayMainMenu: prints out menu options
    // for users to navigate

    // takes in user object
    public void displayMainMenu(User currUser) {
        menuHeader("main", currUser);
        // find user for now
        System.out.println("1. My Farmland");
        System.out.println("2. My Store");
        System.out.println("3. My Inventory");
        System.out.println("4. Visit Friend");
        System.out.println("5. Send Gift");
        System.out.print("[M]ain | Enter your choice > ");
    }

    // checkChoiceOption: checks if user chooses
    // a valid position for farmland related functions,
    // and outputs the correct error messages if needed

    // return true if user inputs a number after the char choice
    // returns false if no options are given
    public boolean checkChoiceOption(char choiceOption, String function) {
        if (choiceOption == ' ') {
            System.out.printf("Please indicate a valid plot you wish to %s your crop.\n", function);
            System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
            return false;
        }
        return true;
    }

    // giftFriends: calls the sendGift function for
    // each friend and outputs if sending is successful/not
    
    // takes in a string array of friends
    // and userID of user, as well as gift option
    public void giftFriends(String[] friendsChosen, int userID, String seedType) {
        for (String friendUsername : friendsChosen) {
            if (ctrl.sendGifttoFriend(friendUsername, userID, seedType)) {
                System.out.printf("Gift posted to your %s's wall.\n", friendUsername);
            }
            else {
                System.out.printf("Error sending to %s!\n", friendUsername);
            }
        }
    }

    // printCropPlantOptions: prints available seeds in
    // user's inventory for planting function

    // takes in the farmer object and an List of Seed objects
    public void printCropPlantOptions(Farmer farmer, List<Seed> userSeeds) {
        // List<Seed> userSeeds = ctrl.getAvailableSeeds(farmer);
        System.out.println("Select the crop:");

        for (int i = 0; i < userSeeds.size(); i++) {
            System.out.printf("%d. %s\n", i+1, userSeeds.get(i).getSeedType());
        }
        
        System.out.print("[M]ain | City [F]armers | Select choice > ");
    }

    // takes in a farmer object, the chosen plot number
    // and seedname of chosen crop. calls plantCrop in ctrl
    // and outputs if planting is successful / not.
    public void callCtrlPlant(Farmer farmer, int plotNum, String seedName) {
        if (ctrl.plantCrop(farmer, plotNum, seedName)) {
            System.out.printf("%s planted on plot %d.\n\n", seedName, plotNum);
        }
        else {
            System.out.println("An error occured while planting!");
        }
    }

    // mainMenu: main function for cityFarmersMenu
    // takes in an User object
    public void mainMenu(User currUser) {
        Scanner sc = new Scanner(System.in);
        char choice;
        char choiceOption = ' ';
        Farmer friendFarmer = null;
        displayMainMenu(currUser);
        Farmer farmer = currUser.getFarmer();

        try {
            String input = sc.nextLine();
            choice = input.charAt(0);
            if (input.length() > 1) {
                choiceOption = input.charAt(1);
            }
            
            while (choice != 'M') {
                if (choice == '1') { // enters user's farmland
                    menuHeader("My Farmland", currUser);
                    ctrl.getFarmlandInfo(farmer, "self");
                    System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
                }
                else if (choice == '2') { // enters store
                    menuHeader("My Store", currUser);
                    System.out.println("Seeds Available:");
                    ctrl.getStoreInfo();
                    
                    System.out.print("[M]ain | City [F]armers | Select choice > ");
                    choice = sc.nextLine().charAt(0);

                    if (choice == 'F' || choice == 'M') {
                        continue;
                    }
                    else {
                        int purchaseOption = Character.getNumericValue(choice);
                        System.out.print("Enter quantity > ");
                        int purchaseAmt = sc.nextInt();
                        sc.nextLine();

                        System.out.println(ctrl.purchaseSeeds(farmer, purchaseOption, purchaseAmt));

                        ctrl.getStoreInfo();
                        System.out.print("[M]ain | City [F]armers | Select choice > ");
                    }
                }

                else if (choice == '3') { // enters my inventory
                    menuHeader("My Inventory", currUser);
                    System.out.println("My Seeds: ");
                    ctrl.getInventoryInfo(farmer);
                    System.out.println();
                    System.out.print("[M]ain | City [F]armers | Select choice > ");
                }

                else if (choice == '4') { // enters visit friends
                    menuHeader("Visit Friend", currUser);
                    System.out.println("My Friends: ");
                    List<Integer> friendsList = ctrl.getFriendsInfo(farmer);
                    System.out.println();
                    System.out.print("[M]ain | City [F]armers | Select choice > ");
                    choice = sc.nextLine().charAt(0);
                    if (!(choice == 'F' || choice == 'M')) {
                        int friendChosen = Character.getNumericValue(choice);
                        if (friendChosen <= 0  || friendChosen > friendsList.size()) {
                            System.out.println("Invalid friend chosen, try again!");
                            choice = '4';
                            continue;
                        }
                        friendFarmer = ctrl.getFriendFarm(friendsList, friendChosen);
                        if (friendFarmer == null) {
                            System.out.println("Friend hasn't started playing CityFarmers. Please psycho them to play :D");
                            System.out.print("[M]ain | City [F]armers > ");
                        }
                        else {
                            System.out.print("[M]ain | City [F]armers | [S]teal > ");
                        }
                    }
                    else {
                        continue;
                    }
                }

                else if (choice == '5') { // enters send gift
                    menuHeader("Send a Gift", currUser);
                    System.out.println("Gifts Available:");
                    ArrayList<StoreItem> allGifts = ctrl.getGifts();

                    System.out.print("[M]ain | City [F]armers | Select choice > ");
                    choice = sc.nextLine().charAt(0);

                    if (!(choice == 'F' || choice == 'M')) {
                        int giftOption = Character.getNumericValue(choice);
                        if (giftOption > 0 && giftOption <= allGifts.size()) {
                            String seedType = allGifts.get(giftOption - 1).getName();
                            System.out.print("Send to > ");
                            String[] friendsChosen = sc.nextLine().split(",");
                            int userID = currUser.getUserID();

                            giftFriends(friendsChosen, userID, seedType);
                            
                            displayMainMenu(currUser);
                            // System.out.print("[M]ain | City [F]armers | Select choice > ");
                        }
                        else {
                            System.out.println("Input option is not valid.");
                            choice = '5';
                            continue;
                        }                    
                    }
                    else {
                        continue;
                    }
                }

                else if (choice == 'S') {
                    if (friendFarmer == null) {
                        System.out.println("Error retrieving friend info");
                    }
                    else {
                        ctrl.stealCrop(friendFarmer, farmer);
                    }
                    System.out.print("[M]ain | City [F]armers | Select choice > ");
                }

                else if (choice == 'P') {
                    if (checkChoiceOption(choiceOption, "plant")) {
                        int plotNum = Character.getNumericValue(choiceOption);

                        if (ctrl.isPlotValid(farmer, plotNum)) {
                            List<Seed> userSeeds = ctrl.getAvailableSeeds(farmer);

                            printCropPlantOptions(farmer, userSeeds);
                            choice = sc.nextLine().charAt(0);

                            if (!(choice == 'F' || choice == 'M')) {
                                int seedOption = Character.getNumericValue(choice);
                                if (seedOption <= 0 || seedOption > userSeeds.size()) {
                                    System.out.println("Invalid crop option! ");
                                    choice = 'P';
                                    continue;
                                }
                                else {
                                    String seedName = userSeeds.get(seedOption - 1).getSeedType();
                                    callCtrlPlant(farmer, plotNum, seedName);
                                    ctrl.getFarmlandInfo(farmer, "self");
                                    System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
                                }   
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            System.out.println("Cannot plant at chosen plot as there is an existing crop at chosen position / chosen position does not exist.");
                            System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
                        }
                    }
                }

                else if (choice == 'C') {
                    if (checkChoiceOption(choiceOption, "clear")) {
                        int plotNum = Character.getNumericValue(choiceOption);
                        if (ctrl.clearPlot(plotNum, farmer, "wilted")) {
                            System.out.println("Plot cleared.");
                        }
                        else {
                            System.out.println("Not able to clear chosen plot.");
                        }
                        choiceOption = ' ';
                        System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
                    }
                }

                else if (choice == 'H') {
                    if (ctrl.harvestPlots(farmer) == null) {
                        System.out.println("Nothing to harvest at the moment.");
                    }
                    System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
                }

                else if (choice == 'F') {
                    displayMainMenu(currUser);
                }
                
                else {
                    System.out.print("Please enter a choice between 1 to 5, or 'M' to go back to Social Magnet! > ");
                }

                input = sc.nextLine();
                choice = input.charAt(0);
                if (input.length() > 1) {
                    choiceOption = input.charAt(1);
                }
            }

            // MainMenu mm = new MainMenu(currUser); 

        } catch (StringIndexOutOfBoundsException se) {
            System.out.println("No valid input, exiting game...");
        }
    }
}