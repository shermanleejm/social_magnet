package main.java.social;

import java.util.* ;

import main.java.connection.*;

/**
 * Post
 */
public class Post {
    private int post_id;
    private String message;
    private int poster;
    private int wall;
    private int likes;
    private int dislikes;
    private ArrayList<Comment> comments;
    private long postTime ;
    private int gift_id;

    public Post(int post_id, int poster, int wall, String msg, int likes, int dislikes, ArrayList<Comment> comments, long postTime, int gift_id) {
        this.post_id = post_id;
        this.wall = wall;
        this.message = msg;
        this.poster = poster;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.postTime = postTime;
        this.gift_id = gift_id;
    }

    public String getMessage() {
        return this.message;
    }

    public int getPoster() {
        return this.poster;
    }

    public String getPosterName() {
        UserDAO udao = new UserDAO();
        return udao.getUserByID(this.poster).getName();
    }

    public int getLikes() {
        return this.likes;
    }

    public int getDislikes() {
        return this.dislikes;
    }

    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    public int getWall() {
        return this.wall;
    }

    public long getPostTime() {
        return this.postTime;
    }

    public int getPostID() {
        return this.post_id;
    }

    public int getGiftID() {
        return this.gift_id;
    }
}