package main.java.social;

import java.util.*;

import main.java.connection.*;
import main.java.social.*;
import main.java.game.*;

/**
 * FriendMenu
 */
public class FriendMenu {

    private User user;
    private ArrayList<User> friends;
    private ArrayList<User> requests;
    public FriendsDAO fdao;

    public void readOptionMain() {
        FriendMenu fm = null;

        String choice = null;

        int num = 0;

        int fid = 0;

        Scanner sc = new Scanner(System.in);

        System.out.printf("[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject | [V]iew > ");

        do {
            
            String raw_input = sc.nextLine();

            choice = raw_input.substring(0, 1);
                        
            System.out.println();
            switch (choice) {
                case "M":
                    choice = "M";
                    break;
                
                case "U":

                    try { 
                        num = Integer.parseInt(raw_input.substring(1, raw_input.length()));
                    } catch (Exception e) {
                        System.out.println("Invalid input.\n");
                        fm = new FriendMenu(user);
                    }
                    
                    fid = friends.get(num - 1).getUserID();

                    fdao.deleteFriend(user.getUserID(), fid);

                    fm = new FriendMenu(user);

                    break;

                case "Q":
                    System.out.printf("Enter the username > ");
                    String request_username = sc.nextLine();

                    if (fdao.sendRequest(user.getUserID(), request_username) == false) {
                        System.out.println("Username does not exist.");
                    } else {
                        System.out.println("A friend request is sent to " + request_username);
                    }

                    fm = new FriendMenu(user);

                    break;

                case "A":
                    try { 
                        num = Integer.parseInt(raw_input.substring(1, raw_input.length())) - friends.size();
                    } catch (Exception e) {
                        System.out.println("Invalid input.\n");
                        fm = new FriendMenu(user);
                    }

                    fid = requests.get(num - 1).getUserID();

                    fdao.acceptRequest(user.getUserID(), fid);

                    fm = new FriendMenu(user);

                    break;
                
                case "R":
                    try { 
                        num = Integer.parseInt(raw_input.substring(1, raw_input.length())) - friends.size();
                    } catch (Exception e) {
                        System.out.println("Invalid input.\n");
                        fm = new FriendMenu(user);
                    }

                    fid = requests.get(num - 1).getUserID();

                    fdao.rejectRequest(user.getUserID(), fid);

                    fm = new FriendMenu(user);

                    break;
                
                case "V":
                    try { 
                        num = Integer.parseInt(raw_input.substring(1, raw_input.length()));
                    } catch (Exception e) {
                        System.out.println("Invalid input.\n");
                        fm = new FriendMenu(user);
                    }

                    User friend = friends.get(num - 1);

                    FriendWall fw = new FriendWall(user, friend);

                    break;

            }
        } while ( choice != "M" & choice != "T1" & choice != "T2" & choice != "T3" );

    }
    
    public FriendMenu(User user) {
        fdao = new FriendsDAO();

        this.user = user;
        
        System.out.println("== Social Magnet :: My Friends ==");
        System.out.println("Welcome, " + user.getName() + "\n");
        System.out.println("My Friends:");

        this.friends = fdao.getFriends(user.getUserID());

        for (int i = 0; i < friends.size(); i ++) {
            User fren = friends.get(i);

            int display_num = i + 1;

            System.out.println("" + display_num + ". " + fren.getName() );
        }

        System.out.println();

        System.out.println("My Requests:");

        this.requests = fdao.getRequests(user.getUserID());

        for (int i = 0; i < requests.size(); i ++) {
            User fren = requests.get(i);

            int display_num = friends.size() + i + 1;

            System.out.println("" + display_num + ". " + fren.getName() );
        }

        System.out.println();

        readOptionMain();

    }
    
}