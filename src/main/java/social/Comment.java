package main.java.social;

import main.java.connection.UserDAO;

/**
 * Comment
 */
public class Comment {

    private int pid;
    private int poster;
    private String message;
    private long commentTime;

    public Comment(int pid, int poster, String message, long commentTime) {
        this.pid = pid;
        this.poster = poster;
        this.message = message;
        this.commentTime = commentTime;
    }

    public int getPID() {
        return this.pid;
    }

    public int getPoster() {
        return this.poster;
    }

    public String getPosterName() {
        UserDAO udao = new UserDAO();
        return udao.getUserByID(this.poster).getName();
    }

    public User getPosterUser(){
        UserDAO udao = new UserDAO();
        return udao.getUserByID(this.poster);
    }

    public String getMessage() {
        return this.message;
    }

    public long getCommentTime() {
        return this.commentTime;
    }
}