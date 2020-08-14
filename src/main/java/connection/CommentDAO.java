package main.java.connection;

import java.io.*;
import java.util.*;
import java.sql.*;

import main.java.social.*;

/**
 * CommentDAO
 */
public class CommentDAO {

    public ArrayList<Comment> getCommentsByPID(int pid) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Comment c = null;

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("SELECT * FROM comments WHERE post_id=%s ORDER BY comment_time", pid);

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                c = new Comment(rs.getInt("post_id"), rs.getInt("user_id"), rs.getString("msg"), rs.getLong("comment_time"));
                
                comments.add(c);

            }

        } catch (Exception e) {
            System.out.println("Check your CommentDAO");
        } finally {
            return comments;
        }
    }
}