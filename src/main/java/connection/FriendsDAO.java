package main.java.connection;

import java.sql.*; 
import java.util.*;
import main.java.game.*;
import main.java.social.*;

/**
 * FriendsDAO
 */
public class FriendsDAO {

    public ArrayList<User> getFriends(int userID) {

        ArrayList<User> result = new ArrayList<>();

        UserDAO udao = new UserDAO();

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {
            
            String sql = String.format("SELECT * FROM friends WHERE uid=%d", userID);

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User temp = udao.getUserByID( rs.getInt("fid") );

                result.add(temp);
            }
            
        } catch (Exception e) {

            //if no friends return empty arrlist
            // System.err.println("Something went wrong with FriendsDAO.getFriends");

        } finally {

            return result;

        }
    }

    public ArrayList<User> getRequests(int userID) {
        ArrayList<User> result = new ArrayList<>();

        UserDAO udao = new UserDAO();

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {
            
            String sql = String.format("SELECT * FROM requests WHERE friend_id=%d", userID);

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User temp = udao.getUserByID( rs.getInt("user_id") );

                result.add(temp);
            }
            
        } catch (Exception e) {

            //if no requests return empty arrlist
            // System.err.println("Something went wrong with FriendsDAO.getFriends");

        } finally {

            return result;

        }
    }

    public void deleteFriend(int uid, int fid) {

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("DELETE FROM friends WHERE uid=%d AND fid=%d", uid, fid);

            stmt.execute(sql);
            
        } catch (Exception e) {
            System.out.println("Error at FriendsDAO.deleteFriend");
        }
    }

    public void rejectRequest(int uid, int fid) {

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("DELETE FROM requests WHERE user_id=%d AND friend_id=%d", uid, fid);

            stmt.execute(sql);
            
        } catch (Exception e) {
            System.out.println("Error at FriendsDAO.deleteFriend");
        }

    }

    public void acceptRequest(int uid, int fid) {

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {

            String sql = String.format("DELETE FROM requests WHERE user_id=%d AND friend_id=%d", fid, uid);

            stmt.execute(sql);

            sql = String.format("INSERT INTO friends VALUES (%d, %d);", uid, fid);

            stmt.execute(sql);
            
        } catch (Exception e) {
            System.out.println("Error at FriendsDAO.acceptRequest");
        }

    }

    public boolean sendRequest(int uid, String username) {
        boolean result = false;

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {
            UserDAO udao = new UserDAO();


            User request = udao.getUserByUsername(username);

            if (request != null) {
                String sql = String.format("INSERT INTO requests VALUES (%d, %d)", uid, request.getUserID());

                stmt.execute(sql);

                result = true;
            }


        } finally {

            return result;

        }
    }
}