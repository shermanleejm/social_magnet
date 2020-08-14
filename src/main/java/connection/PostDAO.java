package main.java.connection;

import main.java.social.*;

import java.util.*;
import java.sql.*;
import java.lang.*;

/**
 * PostDAO
 */
public class PostDAO {

    public ArrayList< ArrayList<User> > getLikesAndDislikes(int post_id) {
        String sql = null;
        ResultSet rs = null;
        ArrayList< ArrayList<User> > result = new ArrayList<>();
        ArrayList<User> likers = new ArrayList<>();
        ArrayList<User>  dislikers = new ArrayList<>();

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {
            UserDAO udao = new UserDAO();

            sql = "SELECT * FROM likes WHERE post_id=" + post_id + " AND status=1";

            rs = stmt.executeQuery(sql);

            User liker = null;
            while (rs.next()) { 
                liker = udao.getUserByID(rs.getInt("user_id"));
                likers.add(liker);
            }

            sql = "SELECT * FROM likes WHERE post_id=" + post_id + " AND status=0";

            rs = stmt.executeQuery(sql);

            User disliker = null;
            while (rs.next()) { 
                disliker = udao.getUserByID(rs.getInt("user_id"));
                dislikers.add(disliker);
            }

        } catch (SQLException e) {
            System.out.println("Something happened at PostDAO.getLikesAndDislikes");
            // e.printStackTrace();

        } finally {
            result.add(likers);
            result.add(dislikers);
            return result;

        }

    }

    public Post getPostByPostID(int post_id) {
        Post post = null;
        ArrayList<Comment> comments = null;
        String sql = null;
        ResultSet rs = null;

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {

            CommentDAO cdao = new CommentDAO();

            comments = cdao.getCommentsByPID(post_id);

            sql = "SELECT * FROM posts WHERE post_id=" + post_id;

            rs = stmt.executeQuery(sql);

            int likes = getLikesAndDislikes(post_id).get(0).size();
            int dislikes = getLikesAndDislikes(post_id).get(1).size();

            while (rs.next()) {
                post = new Post( post_id, rs.getInt("post_id"), rs.getInt("wall"), rs.getString("msg"), likes, dislikes, comments, rs.getLong("post_time"), rs.getInt("gift_id") );
            }

        } catch (SQLException e) {

            System.out.println("something went wrong with PostDAO.getPostByPostID");

        } finally {
            rs = null;
            return post;

        }
    }

    public void deletePostByPostID(int post_id) {

        try (
            Connection conn = new ConnManager().connect();
            Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM posts WHERE post_id=" + post_id ;
            
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            System.out.println("Error at POSTDAO.deletePostByPostID. Check DB?");
            e.printStackTrace();

        }
    }


    public ArrayList<Post> getNewsfeed(int wall) {
        ArrayList<Post> postArray = new ArrayList<>();
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = null; 
        ResultSet rs = null;
        ResultSet rs1 = null;

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {
            CommentDAO cdao = new CommentDAO();

            int likes = 0;
            int dislikes = 0;

            sql = String.format("SELECT * FROM posts WHERE wall=%d AND gift_id=0 ORDER BY post_time DESC LIMIT 3;", wall);

            rs = stmt.executeQuery(sql);

            int post_id = 0;
            int poster = 0;
            // dont need a wall variable
            String msg = "";
            Post post = null;
            long postTime = 0;
            int gift_id = 0;

            while (rs.next()) {

                post_id = rs.getInt("post_id");
                poster = rs.getInt("poster");
                // dont need a wall variable
                msg = rs.getString("msg");
                comments = cdao.getCommentsByPID(post_id);
                postTime = rs.getLong("post_time");
                gift_id = rs.getInt("gift_id");

                try { 
                    likes = getLikesAndDislikes(post_id).get(0).size();
                    dislikes = getLikesAndDislikes(post_id).get(1).size();
                } catch (NullPointerException e) {
                    System.out.println("***");
                    likes = 0;
                    dislikes = 0;
                }

                post = new Post( post_id, poster, wall, msg, likes, dislikes, comments, postTime, gift_id );
                
                postArray.add(post);
            }

        } catch (SQLException e) {

            System.out.println("something went wrong with PostDAO.getNewsfeed");
            e.printStackTrace();

        } finally {
            rs = null;
            return postArray;

        }
    }

    public ArrayList<Post> getWall(User user) {
        int user_id = user.getUserID();

        ArrayList<Post> postArray = new ArrayList<>();
        ArrayList<Comment> comments = new ArrayList<>();
        String sql = null; 
        ResultSet rs = null;
        ResultSet rs1 = null;

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {
            CommentDAO cdao = new CommentDAO();

            int likes = 0;
            int dislikes = 0;

            sql = String.format("SELECT * FROM posts WHERE wall=%d ORDER BY post_time DESC LIMIT 5;", user_id);

            rs = stmt.executeQuery(sql);

            int post_id = 0;
            int poster = 0;
            int wall = 0;
            String msg = "";
            Post post = null;
            long postTime = 0;
            int gift_id = 0;

            while (rs.next()) {

                post_id = rs.getInt("post_id");
                poster = rs.getInt("poster");
                wall = rs.getInt("wall");
                msg = rs.getString("msg");
                comments = cdao.getCommentsByPID(post_id);
                postTime = rs.getLong("post_time");
                gift_id = rs.getInt("gift_id");

                try { 
                    likes = getLikesAndDislikes(post_id).get(0).size();
                    dislikes = getLikesAndDislikes(post_id).get(1).size();
                } catch (NullPointerException e) {
                    System.out.println("***");
                    likes = 0;
                    dislikes = 0;
                }

                post = new Post( post_id, poster, wall, msg, likes, dislikes, comments, postTime, gift_id );
                
                postArray.add(post);
            }

        } catch (SQLException e) {

            System.out.println("something went wrong with PostDAO.getNewsfeed");
            e.printStackTrace();

        } finally {
            rs = null;
            return postArray;

        }
    }

    public void createNormalPost(int poster, int wall, String message) {

        try (
            Connection conn = new ConnManager().connect();
            Statement stmt = conn.createStatement();
        ) {
            String sql = String.format( "INSERT INTO posts (poster, wall, msg, likes, dislikes, post_time, gift_id) VALUES (%d, %d, '%s', 0, 0, %d, 0)", poster, wall, message, System.currentTimeMillis() );
            stmt.execute(sql);

        } catch (SQLException e) {  
            System.out.println("Error at PostDAO.createNormalPost");
            e.printStackTrace();
        }
    }


    // Create a post specifically to send gift
    // ** Takes in details of sender and receiver of gift
    // as well as the giftID, which should be created before 
    // creation of post. **
    // returns true if sucessful, else false if errors within
    // the DB happen.
    public boolean createGiftPost(int userID, int friendID, String msg, int time, int giftID) {

        try (
            Connection conn = ConnManager.connect();
        ) {
            String sql = "INSERT into posts(poster, wall, msg, likes, dislikes, post_time, gift_id) VALUES (?, ?, ?, 0, 0, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, userID);
            stmt.setInt(2, friendID);
            stmt.setString(3, msg);
            stmt.setInt(4, time);
            stmt.setInt(5, giftID);

            return (stmt.executeUpdate() == 1);

        } catch (SQLException e) {
            System.out.println("Error at PostDAO.createGiftPost");
            e.printStackTrace();
            return false;
        }
    }

    public void addComment(int postID, int userID, String msg) {
        String sql = "default";

        try (
            Connection conn = ConnManager.connect();

            Statement stmt = conn.createStatement();
        ) { 

            sql = String.format( "INSERT INTO comments (post_id, user_id, msg, comment_time) VALUES (%d, %d, '%s', %d)", postID, userID, msg, System.currentTimeMillis() ) ;
    
            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong when trying to add comment");
            System.out.println("This is your sql --> " + sql);
        }

    }
 
    // public static void main(String[] args) {
    //     Post p = getPostByPostID(1);

    //      createPost(p);



    // }
}