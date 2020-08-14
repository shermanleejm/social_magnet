package main.java.social;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;

import main.java.social.*; 
import main.java.connection.*;
import main.java.game.*;

/**
 * Newsfeed
 */
public class Newsfeed {

    private User user;
    private ArrayList<Post> posts;
    private PostDAO pdao = new PostDAO();
    private UserDAO udao = new UserDAO();
    private int currentThread = -1;

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

        System.out.printf("[M]ain | [T]hread > ");
    }

    public void getPosts() {

        try { 
            int wall = user.getUserID();
            
            ArrayList<Post> posts = pdao.getNewsfeed(wall);
            this.posts = posts;

            printPostsAndComments(posts);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void viewThreadByNum(int i) {
        Post thread = this.posts.get(i - 1);

        int post_id = thread.getPostID();

        thread = pdao.getPostByPostID(post_id);

        System.out.println("== Social Magnet :: View a Thread ==");

        try {

            System.out.printf("%d %s: %s\n", i, thread.getPosterName(), thread.getMessage());    

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
        }
    }

    public void reply() {
        Scanner sc = new Scanner(System.in);
    
        int currentPostID = this.posts.get(this.currentThread - 1).getPostID();

        System.out.printf("Enter your reply > ");
        String msg = sc.nextLine();

        int userID = this.user.getUserID();

        pdao.addComment(currentPostID, userID, msg);

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
        int user_id = user.getUserID();

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
                    // System.out.println("Newsfeed: breaking from readOptionThread");
                    break;
            
                case "K":
                    deletePost();
                    Newsfeed nf = new Newsfeed(this.user);
                    // choice = "K";
                    break;

                case "R":
                    reply();

                    int wall = user.getUserID();
            
                    ArrayList<Post> posts = pdao.getNewsfeed(wall);
                    this.posts = posts;

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
            
        } while (choice != "M" & choice != "K" & choice != "R" & choice != "L" & choice != "D");
        // System.out.println("end of readOptionsThread");
    }

    public void readOptionMain() {
        String choice = null;

        Scanner userInput = new Scanner(System.in);

        do {
            choice = userInput.nextLine();
            System.out.println();
            // System.out.println("Newsfeed - readOptionMain current choice: " + choice);
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
            }
        } while ( choice != "M" );
    }

    public void deletePost() {
        int currentPostID = this.posts.get(this.currentThread - 1).getPostID();

        PostDAO pdao = new PostDAO();

        pdao.deletePostByPostID(currentPostID);
    }

    public Newsfeed(User user) {
        this.user = user;

        System.out.println("== Social Magnet :: News Feed ==");
        getPosts();
        readOptionMain();
    }


}