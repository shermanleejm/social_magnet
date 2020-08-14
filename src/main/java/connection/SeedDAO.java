package main.java.connection;

import java.sql.*; 
import java.util.*;

import main.java.game.*;
import main.java.social.*;

public class SeedDAO {

    // get specific seed given usedID and seedName
    public Seed getSeedbyUserID(int userID, String seedName) {
        List<Seed> allSeeds = getAllSeedsByUserID(userID);
        for (Seed eachSeed : allSeeds) {
            if (eachSeed.getSeedType().equals(seedName)) {
                return eachSeed;
            }
        }
        return null;
    }

    // Get Seeds by userID
    // Returns empty ArrayList if no seed is found in inventory

    public List<Seed> getAllSeedsByUserID(int userID) {
        List<Seed> output = new ArrayList<>();
        
        Connection con = ConnManager.connect();
        if (con == null ){
            return output;
        }
        
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from seed where user_id = " + userID);  
           
            while(rs.next()){
                
                int id = rs.getInt(1);
                String seedType = rs.getString(3);
                int amount = rs.getInt(4);
                Seed newSeed = new Seed(id, userID, seedType, amount);

                if (newSeed != null){
                    output.add(newSeed);
                }
            }  

            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
    
        return output;
    }


    // Get seed by SeedID
    // Returns null if no such entry is found

    public Seed getSeedByID(int seedID){
        Seed seed = null;
        
        Connection con = ConnManager.connect();

        if (con == null){
            return seed;
        }
       
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from seed where seed_id = " + seedID);  
            while(rs.next()){
                int id = rs.getInt(1);
                int userID = rs.getInt(2);
                String seedType = rs.getString(3);
                int amount = rs.getInt(4);
                seed = new Seed(id, userID, seedType, amount);

            }  
            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
        
        return seed;
    }

    // Deletes seed by SeedID
    // Return true if successful, false if failed to delete
    public boolean deleteSeedByID(int seedID){

        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }
       
        try{
            Statement stmt=con.createStatement();  
            int rowsDeleted = stmt.executeUpdate("delete from seed where seed_id = " + seedID);  
            con.close();  
            
            return rowsDeleted == 1;
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();

            return false;
        }
        
    }
    // Create Seed
    // Return true if successful
    // False if failed to create (might be input mismatch) or id+Plot combination exists 

    public boolean createSeed(int userID, String seedType, int amount){
        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }

        try {
            Statement queryStmt=con.createStatement();  
            ResultSet rs=queryStmt.executeQuery("select * from seed where user_id = " 
            + userID + " and seed_type = \"" + seedType + "\"");

            while (rs.next()){
                return false;
            }

            String sql = "insert into seed(" 
            + "seed_id," + "user_id," + "seed_type," + "amount) VALUES (DEFAULT, ?, ?, ?)"  ; 
            
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, userID);
            st.setString(2, seedType);
            st.setInt(3, amount);
           
            int rowsAdded = st.executeUpdate();
            
            con.close();  

            return rowsAdded == 1;

        }catch (SQLException ex) {
            System.out.println("Reading from DB failed / Item already exists");
            ex.printStackTrace();
            return false;
        }

    }

    // Update Seed 
    // ** Takes in an updated Seed object. Use addSeed and reduceSeed 
    // in Seed Class to modify seed amount before passing in **
    // Return true if successful
    // False if failed to update

    public boolean updateSeed(Seed seed){
        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }

        try {
            String sql = "UPDATE seed set amount = ? where seed_id = ?" ; 
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, seed.getSeedAmount());
            st.setInt(2, seed.getID());
           
            int rowsAdded = st.executeUpdate();
            
            con.close();  
            return rowsAdded == 1;

        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
            return false;
        }

    }

    public void updateSeedByUserIDTypeAmount(int userID, String seedType, int amount) {
        String sql = "error with sql";

        try (
            Connection conn = new ConnManager().connect();

            Statement stmt = conn.createStatement();
        ) {

            try { 
                // try to update if seed exists in inventory
                sql = String.format("SELECT * FROM SEED WHERE user_id=%d AND seed_type='%s'", userID, seedType);
                
                ResultSet rs = stmt.executeQuery(sql);

                amount += rs.getInt(3);

                sql = String.format("UPDATE seed SET amount=%d WHERE user_id=%d AND seed_type='%s' ", amount, userID, seedType );

                stmt.execute(sql);
            } catch (Exception e) {
                // create a new row
                createSeed(userID, seedType, amount);
            }

        } catch (Exception e) {
            System.out.println("There is a problem with SeedDAO.updateSeedByUserIDTypeAmount");
            e.printStackTrace();
        }
    }
}