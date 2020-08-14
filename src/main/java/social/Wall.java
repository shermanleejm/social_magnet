package main.java.social;

import java.lang.*;
import java.util.*;
import java.sql.*; 

import main.java.connection.*;
import main.java.social.*;
import main.java.game.*;
/**
 * Wall
 */
public class Wall {
    private User user;
    private ArrayList<Post> posts;
    private int currentThread;
    private PostDAO pDAO;
    private UserDAO uDAO;
    private FarmerDAO fDAO;
    private GiftDAO gDAO;
    private SeedDAO sDAO;


    public void viewThreadByNum(int i) {
        Post thread = this.posts.get(i - 1);

        int post_id = thread.getPostID();

        thread = pDAO.getPostByPostID(post_id);

        System.out.println("== Social Magnet :: View a Thread ==");

        try {

            System.out.printf("%d %s: %s\n", i, thread.getPosterName(), thread.getMessage());    

            int commentCount = 1;
            
            for (Comment c : thread.getComments()) {
                System.out.printf( "  %d.%d %s: %s\n", i, commentCount, c.getPosterName(), c.getMessage() );
                commentCount ++;
            }

            ArrayList< ArrayList<User> > likersAndDislikers = pDAO.getLikesAndDislikes(thread.getPostID());

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
        }
    }

    public void like() {
        /*
        INSERT INTO likes (post_id, user_id, status)
        VALUES (1, 1, 0)
        ON DUPLICATE KEY UPDATE status=0
        */

        int post_id = posts.get(this.currentThread - 1).getPostID();
        int user_id = user.getUserID();

        try ( 
            Connection conn = ConnManager.connect();
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
        int user_id = user.getUserID();

        try ( 
            Connection conn = ConnManager.connect();
            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("INSERT INTO likes (post_id, user_id, status) VALUES (%d, %d, 0) ON DUPLICATE KEY UPDATE status=0;", post_id, user_id);

            stmt.execute(sql);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void readOptionThread() {
        String choice = null;

        Scanner sc = new Scanner(System.in);

        do {
            System.out.printf("      [M]ain | [K]ill | [R]eply | [L]ike | [D]islike > ");
            choice = sc.nextLine();
            System.out.println();
            switch (choice) {
                case "M":
                    choice = "M";
                    break;
            
                case "K":
                    deletePost();
                    // Wall wall = new Wall(user);
                    break;

                case "R":
                    reply();
                    viewThreadByNum(this.currentThread);
                    // readOptionThread();
                    break;

                case "L":
                    like();
                    viewThreadByNum(this.currentThread);
                    // readOptionThread();
                    break;
                
                case "D":
                    dislike();
                    viewThreadByNum(this.currentThread);
                    // readOptionThread();
                    break;
                
                default:
                    System.out.println("Please only input the correct string.");
                    break;
            }
            
        // } while (choice != "M" & choice != "K" & choice != "R" & choice != "L" & choice != "D");
        } while (choice != "M");
        // System.out.println("end of Wall:readOptionThread");
    }

    public void readOptionMain() {

        String choice = null;

        Scanner userInput = new Scanner(System.in);

        getWallPosts();

        do {
            choice = userInput.nextLine();
            System.out.println();
            switch (choice) {
                case "M":
                    choice = "M";
                    break;
                
                case "T1":
                    viewThreadByNum(1);
                    this.currentThread = 1;
                    readOptionThread();
                    choice = "M";
                    break;

                case "T2":
                    viewThreadByNum(2);
                    this.currentThread = 2;
                    readOptionThread();
                    choice = "M";
                    break;

                case "T3":
                    viewThreadByNum(3);
                    this.currentThread = 3;
                    readOptionThread();
                    choice = "M";
                    break;
                
                case "A":
                    acceptGift();
                    // Wall wall = new Wall(user);
                    break;
                
                case "P":
                    makePost();
                    // wall = new Wall(user);
                    break;
            }
        // } while ( choice != "M" & choice != "T1" & choice != "T2" & choice != "T3" );
        } while ( choice != "M" );

    }

    public void makePost() {
        Scanner sc = new Scanner(System.in);

        UserDAO udaom = new UserDAO();

        String doNothing = "";

        int userID = this.user.getUserID();

        System.out.printf("Enter message > ");
        String msg = sc.nextLine();

        if (msg.contains("@")) {
            for (String m : msg.split(" ")) {
                if (m.contains("@")) {
                    try {
                        User temp_user = udaom.getUserByUsername(m.substring(1));
                        pDAO.createNormalPost(userID, temp_user.getUserID(), msg);
                    } catch (Exception e) {
                        doNothing = "";
                    }
                    
                }
            }
        }

        pDAO.createNormalPost(userID, userID, msg);

        System.out.println();

    }

    public void deletePost() {
        int currentPostID = this.posts.get(this.currentThread - 1).getPostID();

        pDAO.deletePostByPostID(currentPostID);
    }
    
    public void getWallPosts() {
        ArrayList<Post> posts = pDAO.getWall(this.user);

        this.posts = posts;

        printPostsAndComments(posts);
    }

    public void printPostsAndComments(ArrayList<Post> posts) {

        int postCount = 1;

        for (Post p : posts) {
            System.out.printf("%d", postCount);

            int commentCount = 1;
            String likeString = "likes";
            String dislikeString = "dislikes";

            String poster = uDAO.getUserByID( p.getPoster() ).getName();
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
            // System.out.printf("[ %d %s, %d %s ]\n", likes, likeString, dislikes, dislikeString);

            for (Comment c : p.getComments()) {
                System.out.printf( "  %d.%d %s: %s\n", postCount, commentCount, uDAO.getUserByID(c.getPoster()).getName(), c.getMessage() );
                commentCount ++;
            }

            System.out.println();
            postCount ++;

        }
        System.out.printf("[M]ain | [T]hread | [A]ccept Gift | [P]ost > ");
    }

    public void reply() {
        Scanner sc = new Scanner(System.in);
    
        int currentPostID = this.posts.get(this.currentThread - 1).getPostID();

        System.out.printf("Enter your reply > ");
        String msg = sc.nextLine();

        int userID = this.user.getUserID();

        pDAO.addComment(currentPostID, userID, msg);

    }

    public void acceptGift() {
        for (Post p : this.posts) {
            if (p.getGiftID() != 0) {
                int post_id = p.getPostID();

                int user_id = this.user.getUserID();

                Gift g = gDAO.getGiftByID(p.getGiftID());

                String seedType = g.getSeedType();

                gDAO.acceptGift(g);

                sDAO.updateSeedByUserIDTypeAmount(user_id, seedType, 1);

                pDAO.deletePostByPostID(post_id);
            }
        }
        System.out.println("All new gifts requests accepted.");
    }


    public Wall(User user) {
        this.user = user;
        fDAO = new FarmerDAO();
        pDAO = new PostDAO();
        uDAO = new UserDAO();
        gDAO = new GiftDAO();
        sDAO = new SeedDAO();
        
        String username = user.getUsername();
        String fullName = user.getName();
        int userID = user.getUserID();

        Farmer farmer = fDAO.getFarmerFromUserID(userID);

        // printing basic stuff
        System.out.println("== Social Magnet :: My Wall ==");
        System.out.println("About " + username);
        System.out.println("Full Name: " + fullName);

        try {
            System.out.println("" + farmer.getExperience() + " EXP " + farmer.getGold() + " gold\n");
        } catch (Exception e) {
            System.out.println("User has no farmer character\n");
        }
    
        readOptionMain();

    }
}