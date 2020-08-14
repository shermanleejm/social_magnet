package main.java.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.social.*;
import main.java.game.*;

/**
 * UserDAO
 */
public class UserDAO {

    public User getUserByID(int id) {
        User user = null;

        ConnManager cm = new ConnManager();
        
        try ( 

            Connection conn = ConnManager.connect();

            Statement stmt = conn.createStatement();
        
        ) {  

            String sql = "SELECT * FROM users WHERE user_id=" + id;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("name"));
            }

            stmt.close();

        } catch (Exception e) {

            System.out.println("Check SQL query");

        } finally {
            return user;
        }
    }

    public void addFriendsByUserId(int uid, int fid){
        
        ConnManager cm = new ConnManager();

        List<Integer> friendList = new ArrayList<>();

        try(
            Connection conn = cm.connect();

            Statement stmt = conn.createStatement();
        ) {
            
            String sql = "select fid from friends where uid =" + uid ;
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next()){
                String insert;
                insert= "INSERT INTO friends " 
                + "(uid,fid)" 
                + "VALUES (?,?)";
                System.out.println("New friend added!");
                try (PreparedStatement pstmt = conn.prepareStatement(insert)){
                    pstmt.setInt(1, uid);
                    pstmt.setInt(2, fid);
                    pstmt.executeUpdate();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }else{
                while(rs.next()){
                    int id = rs.getInt("fid");
                    friendList.add(id);
                }
                if(friendList.contains(fid)){
                    System.out.println("He is already your friend!");  
                }else{
                    String insert1;
                    insert1= "INSERT INTO friends " 
                    + "(uid, fid)" 
                    + "VALUES (?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insert1)) {
                        pstmt.setInt(1, uid);
                        pstmt.setInt(2, fid);
                        pstmt.executeUpdate();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("New friend added!");
                }
                
            }

        } catch (Exception e) {

            System.out.println(e.getStackTrace());

        }
}
        
    public List<Integer> getUserFriendsById(int uid){
        List<Integer> friendList = new ArrayList<>();
        Connection conn = ConnManager.connect();

        ConnManager cm = new ConnManager();

        try (

            // Connection conn = cm.connect();

            Statement stmt = conn.createStatement();

        ) {

            String sql = "select fid from friends where uid=" + uid ;

            ResultSet rs = stmt.executeQuery(sql);

            if ( rs == null ) {
                System.out.println("You have no friends!");
            }

            while (rs.next()) {
                int fid = rs.getInt("fid");
                friendList.add(fid);
            }

            
            
        } catch (Exception e) {

            System.out.println(e.getStackTrace());

        }

        // System.out.println(Arrays.toString(friendList.toArray()));
        return friendList;

    }

    public User getUserByUsername(String username) {
        User user = null;
        
        ConnManager cm = new ConnManager();

        try (
            Connection conn = cm.connect();
            Statement stmt = conn.createStatement();
        ) {  
            String sql = String.format("SELECT * FROM users WHERE username='%s'", username);

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("name"));
            }


        } catch (Exception e) {

            // System.out.println("Check SQL query");

        } finally {
            return user;
        }
    }

    public boolean areFriends(int u1, int u2) {
        boolean result = false;
        ResultSet rs = null;
        try (
            Connection conn = new ConnManager().connect();
            
            Statement stmt = conn.createStatement();
        ) {
            String sql = String.format("SELECT * FROM friends WHERE uid=%d AND fid=%d", u1, u2);

            rs = stmt.executeQuery(sql);

            if (!(rs.next())) {
                return true;
            }

            sql = String.format("SELECT * FROM friends WHERE uid=%d AND fid=%d", u2, u1);

            rs = stmt.executeQuery(sql);

            if (!(rs.next())) {
                return true;
            }
            
        } finally {
            return result;
        }
    }

}