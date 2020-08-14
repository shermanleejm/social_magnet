package main.java.social;

import java.util.*;
import java.sql.*;

import main.java.connection.*;
import main.java.game.*;
import main.java.social.*;

/**
 * FriendWall
 */
public class FriendWall {
    public UserDAO udao = new UserDAO();
    private User owner;
    private User visitor;
    private ArrayList<Post> posts;
    private int currentThread;

    public void getWallPosts() {
        PostDAO pdao = new PostDAO();

        this.posts = pdao.getWall(this.owner);

        printPostsAndComments(posts);
    }

    public void printPostsAndComments(ArrayList<Post> posts) {

        int postCount = 1;

        for (Post p : posts) {

            System.out.printf("%d", postCount);

            int commentCount = 1;
            String likeString = "likes";
            String dislikeString = "dislikes";

            String poster = udao.getUserByID( p.getPoster() ).getName();
            String message = p.getMessage();
            int likes = p.getLikes();
            int dislikes = p.getDislikes();

            if (p.getLikes() == 1) {
                likeString = "like";
            }

            if (p.getDislikes() == 1) {
                dislikeString = "dislike";
            }

            System.out.printf(" " + poster + ": " + message + "\n");
            System.out.printf("[ %d %s, %d %s ]\n", likes, likeString, dislikes, dislikeString);

            for (Comment c : p.getComments()) {
                System.out.printf( "  %d.%d %s: %s\n", postCount, commentCount, udao.getUserByID(c.getPoster()).getName(), c.getMessage() );
                commentCount ++;
            }

            System.out.println();
            postCount ++;

        }
        // System.out.printf("[M]ain | [T]hread > ");
    }

    public void printFriends() {
        FriendsDAO fdao = new FriendsDAO();
        UserDAO udao = new UserDAO();

        System.out.println(this.owner.getName() + "'s Friend");

        ArrayList<User> owner_friends = fdao.getFriends(owner.getUserID());
        int count = 1;
        for (User f : owner_friends) {
            System.out.printf("%d. %s ", count, f.getName());

            if (udao.areFriends(owner.getUserID(), visitor.getUserID())) {
                System.out.println("(Common Friend)");
            } else {
                System.out.println();
            }
            
            count ++;
            
        }
    }

    public void viewThreadByNum(int i) {
        Post thread = this.posts.get(i - 1);

        int post_id = thread.getPostID();

        PostDAO pdao = new PostDAO();

        thread = pdao.getPostByPostID(post_id);

        System.out.println("== Social Magnet :: View a Thread ==");

        try {

            System.out.printf("%d %s: %s\n", i, "admin", thread.getMessage());    

            int commentCount = 1;
            
            for (Comment c : thread.getComments()) {
                System.out.printf( "  %d.%d %s: %s\n", i, commentCount, c.getPosterName(), c.getMessage() );
                commentCount ++;
            }

            ArrayList< ArrayList<User> > likersAndDislikers = pdao.getLikesAndDislikes(thread.getPostID());

            // Prints likers
            System.out.println();
            System.out.println("Who likes this post:");

            try {
                int likeCount = 1;

                for (User u : likersAndDislikers.get(0)) {
                    System.out.printf("  %d. %s (%s)\n", likeCount, u.getName(), u.getUsername());

                    likeCount ++;
                }
            } catch (Exception e) {

            }

            // prints dislikers
            System.out.println();
            System.out.println("Who dislikes this post:");

            try {
                int dislikeCount = 1;

                for (User u : likersAndDislikers.get(1)) {
                    System.out.printf("  %d. %s (%s)\n", dislikeCount, u.getName(), u.getUsername());

                    dislikeCount ++;
                }
            } catch (Exception e) {

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Who likes this post:\n");
            System.out.println("Who dislikes this post:\n");
        }
    }

    public void like() {
        /*
        INSERT INTO likes (post_id, user_id, status)
        VALUES (1, 1, 0)
        ON DUPLICATE KEY UPDATE status=0
        */

        int post_id = posts.get(this.currentThread - 1).getPostID();
        int user_id = visitor.getUserID();

        try ( 
            Connection conn = new ConnManager().connect();
            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("INSERT INTO likes (post_id, user_id, status) VALUES (%d, %d, 1) ON DUPLICATE KEY UPDATE status=1;", post_id, user_id);

            stmt.execute(sql);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void dislike() {
        int post_id = posts.get(this.currentThread - 1).getPostID();
        int user_id = visitor.getUserID();

        try ( 
            Connection conn = new ConnManager().connect();
            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("INSERT INTO likes (post_id, user_id, status) VALUES (%d, %d, 0) ON DUPLICATE KEY UPDATE status=0;", post_id, user_id);

            stmt.execute(sql);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void reply() {
        Scanner sc = new Scanner(System.in);
        PostDAO pdao = new PostDAO();
    
        int currentPostID = this.posts.get(this.currentThread - 1).getPostID();

        System.out.printf("Enter your reply > ");
        String msg = sc.nextLine();

        int userID = this.visitor.getUserID();

        pdao.addComment(currentPostID, userID, msg);

    }

    public void readOptionThread() {
        String choice = null;

        Scanner sc = new Scanner(System.in);

        System.out.printf("      [M]ain | [R]eply | [L]ike | [D]islike > ");

        choice = sc.nextLine();

        do {
            
            switch (choice) {
                case "M":
                    MainMenu mm = new MainMenu(this.visitor);
                    break;
            
                // case "K":
                //     deletePost();
                //     // Newsfeed nf = new Newsfeed(this.user);
                //     Wall wall = new Wall(user);
                //     break;

                case "R":
                    // TODO: enable replies
                    reply();
                    viewThreadByNum(this.currentThread);
                    readOptionThread();
                    break;

                case "L":
                    like();
                    viewThreadByNum(this.currentThread);
                    readOptionThread();
                    break;
                
                case "D":
                    dislike();
                    viewThreadByNum(this.currentThread);
                    readOptionThread();
                    break;
                
                default:
                    System.out.println("Please only input the correct string.");
                    break;
            }
            
        } while (choice != "M" & choice != "R" & choice != "L" & choice != "D");
    }

    public void readOptionFriendWallMain() {
        String choice = null;

        FriendMenu fm = null;

        PostDAO pdao = new PostDAO();
        FriendsDAO fdao = new FriendsDAO();

        int num = 0;

        Scanner sc = new Scanner(System.in);

        System.out.printf("[M]ain | [T]hread | [P]ost > ");

        String raw_input = sc.nextLine();

        do {
            
            choice = raw_input.substring(0, 1);

            // System.out.println();

            switch (choice) {
                case "M":
                    MainMenu mm = new MainMenu(visitor);
                    break;
                
                case "T":
                    try { 
                        num = Integer.parseInt(raw_input.substring(1, raw_input.length()));
                    } catch (Exception e) {
                        System.out.println("Invalid input.\n");
                        FriendWall fw2 = new FriendWall(visitor, owner);
                    }
                    this.currentThread = num;
                    viewThreadByNum(num);
                    readOptionThread();
                    break;

                case "P":
                    System.out.printf("Enter your message > ");
                    String msg = sc.nextLine();
                    String doNothing = "";
                    UserDAO udaom = new UserDAO();
                    if (msg.contains("@")) {
                        for (String m : msg.split(" ")) {
                            if (m.contains("@")) {
                                try {
                                    User temp_user = udaom.getUserByUsername(m.substring(1));
                                    pdao.createNormalPost(visitor.getUserID(), temp_user.getUserID(), msg);
                                } catch (Exception e) {
                                    doNothing = "";
                                }
                                
                            }
                        }
                    }

                    pdao.createNormalPost(visitor.getUserID(), owner.getUserID(), msg);

                    FriendWall fw1 = new FriendWall(visitor, owner);

                    break;
            }
        } while ( choice != "M" & choice != "T" & choice != "P" );

    }

    public FriendWall(User visitor, User owner) {
        this.owner = owner;
        this.visitor = visitor;
        
        Farmer farmer = owner.getFarmer();

        System.out.printf("== Social Magnet :: %s's Wall ==\n", owner.getUsername());
        System.out.printf("Welcome, %s!\n\n", visitor.getName());

        System.out.printf("About %s\n", owner.getUsername());
        System.out.printf("Full Name: %s\n", owner.getName());

        try {
            System.out.println("" + farmer.getExperience() + " EXP " + farmer.getGold() + " gold\n");
        } catch (Exception e) {
            System.out.println("User has no farmer character\n");
        }

        getWallPosts();

        printFriends();
        System.out.println();

        readOptionFriendWallMain();

    }
}