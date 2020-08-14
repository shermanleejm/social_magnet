package main.java.connection;

import java.sql.*; 
import java.util.*;
import main.java.game.*;
import main.java.social.*;

public class GiftDAO {

    // Helper function to parse Results, return Gift Obj
    private Gift rsToGift(ResultSet rs){
        try{
            int giftID = rs.getInt(1);
            int userID = rs.getInt(2);
            int friendID = rs.getInt(3);
            String seedType = rs.getString(4);
            int status = rs.getInt(5);
            long timestamp = rs.getLong(6);

            return new Gift(giftID, userID, friendID, seedType, status, timestamp);

        }catch (SQLException ex) {
            System.out.println("Reading from DB failed: GiftDAO");
            ex.printStackTrace();
            Gift gift = null;
            return gift;
        }
        
    }
    // Get All Gifts Sent by userID
    // Returns empty ArrayList if no gift sent
    public List<Gift> getGiftSentByUserID(int userID) {
        List<Gift> output = new ArrayList<>();
        
        Connection con = ConnManager.connect();
        if (con == null ){
            return output;
        }
        
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from gift where user_id = " + userID);  
           
            while(rs.next()){
                Gift newGift = rsToGift(rs);
                if (newGift != null){
                    output.add(newGift);
                }
            }  

            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed: GiftDAO");
            ex.printStackTrace();
        }
    
        return output;
    }

    // Get All Gifts Received by userID
    // Returns empty ArrayList if no gift received

    public List<Gift> getGiftRecievedByUserID(int userID) {
        List<Gift> output = new ArrayList<>();
        
        Connection con = ConnManager.connect();
        if (con == null ){
            return output;
        }
        
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from gift where friend_id = " + userID);  
           
            while(rs.next()){
                Gift newGift = rsToGift(rs);
                if (newGift != null){
                    output.add(newGift);
                }
            }  

            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed: GiftDAO");
            ex.printStackTrace();
        }
    
        return output;
    }

    // Get Gift by GiftID
    // Returns empty ArrayList if no gift received

    public Gift getGiftByID(int giftID) {
        Gift output = null;

        Connection con = ConnManager.connect();
        if (con == null ){
            return output;
        }
        
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from gift where gift_id = " + giftID);  
           
            while(rs.next()){
                output = rsToGift(rs);
                
            }  

            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed: GiftDAO");
            ex.printStackTrace();
        }
    
        return output;
    }

    // Deletes gift by giftID
    // Return true if successful, false if failed to delete
    public boolean deleteGiftByID(int giftID){

        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }
       
        try{
            Statement stmt=con.createStatement();  
            int rowsDeleted = stmt.executeUpdate("delete from gift where gift_id = " + giftID);  
            con.close();  
            
            return rowsDeleted == 1;
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed: GiftDAO");
            ex.printStackTrace();

            return false;
        }
        
    }

    // Create Gift
    // update 27/03/20: return ID of gift if successful
    // else return 0 (no gift created)

    // Return true if successful
    // False if failed to create (might be input mismatch)

    // There are no constraints -- Must implement 5 per day check in CTRL

    public int createGift(int userID, int friendID, String seedType){
        Connection con = ConnManager.connect();
        int giftID = 0;

        if (con == null){
            return giftID;
        }

        try {
            long time = System.currentTimeMillis();

            String sql = "insert into gift(" 
            + "gift_id," + "user_id," + "friend_id," + "seed_type," + "status," 
            + "timestamp) VALUES (DEFAULT, ?, ?, ?, ?, ?) "; 
            
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, userID);
            st.setInt(2, friendID);
            st.setString(3, seedType);
            st.setInt(4, 0);
            st.setLong(5, time);
            int rowsAdded = st.executeUpdate();
            
            // return rowsAdded == 1;
            if (rowsAdded == 1) {
                Statement queryStmt = con.createStatement();  
                ResultSet rs = queryStmt.executeQuery("select MAX(gift_id) from gift");
                if (rs.next()) {
                    giftID = rs.getInt(1);
                    // return giftID;
                }
            }
            con.close();
            return giftID;

        } catch (SQLException ex) {
            System.out.println("Reading from DB failed: GiftDAO");
            ex.printStackTrace();
            return giftID;
        }

    }

    // Accept Gift
    // ** Takes in an updated Gift object. Use accpetGift method
    // in Gift Class to modify status before passing in **
    // Return true if successful
    // False if failed to update

    public boolean acceptGift(Gift gift){
        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }

        try {
            String sql = "UPDATE gift set status = ? where gift_id = ?" ; 
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, gift.getStatus());
            st.setInt(2, gift.getID());
           
            int rowsAdded = st.executeUpdate();
            
            con.close();  
            return rowsAdded == 1;

        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
            return false;
        }

    }
}