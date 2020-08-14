package main.java.social;

import java.util.*;

import main.java.connection.*;
import main.java.social.*; 
import main.java.game.*;

/**
 * MainMenu
 */
public class MainMenu {

    static User user;

    public void displayMainMenu() {
        System.out.println("== Social Magnet :: Main Menu ==");
        System.out.println("Welcome, " + user.getName() + "!");
        System.out.println("1. News Feed");
        System.out.println("2. My Wall");
        System.out.println("3. My Friends");
        System.out.println("4. City Farmers");
        System.out.println("5. Logout");
        System.out.printf("Enter your choice > ");
    }

    public MainMenu(User mainUser) {
        user = mainUser;
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        // displayMainMenu();
        do {
            displayMainMenu();
            String input = sc.nextLine();
            System.out.println();
            choice = Integer.parseInt(input);
            // System.out.println("mainmenu: current choice: " + choice);
            switch (choice) {
                case 1:
                    goNewsFeed();
                    break;
                case 2:
                    goWall();
                    break;
                case 3:
                    displayFriends();
                    break;
                case 4:
                    goFarmGame();
                    break;
                case 5:
                    // choice = 5;
                    SocialMagnetMenu smm = new SocialMagnetMenu();
                    break;
                default:
                    System.out.println("Enter a choice between 1 to 5");
            }
        } while (choice != 5);
    }

    public static void goNewsFeed() {
        // System.out.println("--> this is newsfeed <--");
        Newsfeed nf = new Newsfeed(user);
    }

    public static void goWall() {
        Wall wall = new Wall(user);
    }

    public static void displayFriends() {
        // System.out.println("--> these are your friends <--");
        FriendMenu fm = new FriendMenu(user);
    }

    public static void goFarmGame() {
        // System.out.println("--> going to farmgame... <--");
        CityFarmersApp farmgame = new CityFarmersApp(user);
    }
}