package main.java.connection;

import java.sql.*;
import java.util.*;

import main.java.game.*;

public class FarmerDAO {

    public List<Plot> getPlotsForFarmer(int userID) {
        PlotDAO pDAO = new PlotDAO();
        return pDAO.getPlotByUserID(userID);
    }

    public List<Seed> getInventoryForFarmer(int userID) {
        SeedDAO sDAO = new SeedDAO();
        return sDAO.getAllSeedsByUserID(userID);
    }

    public Farmer getFarmerByID(int userID){
        Farmer farmer = null;
        Connection con = ConnManager.connect();

        if (con == null ){
            return farmer;
        }

        List<Plot> plotList = getPlotsForFarmer(userID);
        List<Seed> inventory = getInventoryForFarmer(userID);

        if ((plotList == null) || (inventory == null)) {
            return farmer;
        }

        try {
            Statement stmt = con.createStatement();  
            ResultSet rs = stmt.executeQuery("select * from farmer where user_id = " + userID); 
            
            while(rs.next()){
                int id = rs.getInt(1);
                int exp = rs.getInt(2);
                int gold = rs.getInt(3);
                
                farmer = new Farmer(id, plotList, inventory, exp, gold);
            }  
            con.close();  
        } catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
        
        return farmer;
    }

    public Farmer getFarmerFromUserID(int userID){
        Farmer farmer = null;
        // int userID = 0;
        Connection con = ConnManager.connect();

        if (con == null ){
            return farmer;
        }
        try {
            String sql = "select user_id from farmer where user_id = ?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, userID);

            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                userID = rs.getInt(1);
                farmer = getFarmerByID(userID);
            }  
            con.close();  
        } catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }

        List<Plot> plotList = getPlotsForFarmer(userID);
        List<Seed> inventory = getInventoryForFarmer(userID);
        
        if ((plotList == null) || (inventory == null)) {
            return farmer;
        }
        
        return farmer;
    }

    public Farmer createNewFarmer(int userID) {
        Farmer farmer = null;
        Connection con = ConnManager.connect();

        if (con == null ){
            return farmer;
        }

        try {
            String sql = "INSERT into farmer(user_id, experience, gold) VALUES (?, ?, ?)";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, userID);
            st.setInt(2, 0);
            st.setInt(3, 50);

            int rowsAdded = st.executeUpdate();

            if (rowsAdded == 1) {
                Statement queryStmt = con.createStatement();  
                ResultSet rs = queryStmt.executeQuery("select MAX(user_id) from farmer");

                if (rs.next()) {
                    userID = rs.getInt(1);
                    ArrayList<Plot> farm = new ArrayList<Plot>();
                    ArrayList<Seed> inventory = new ArrayList<Seed>();
                    farmer = new Farmer(userID, farm, inventory, 0, 50);
                }
            }
            // System.out.println(farmer);
            con.close();  
        
        }  catch (SQLException ex) {
            System.out.println("Insert into DB failed");
            // ex.printStackTrace();
        }     
        return farmer;
    }

    public boolean updateFarmer(Farmer farmer) {
        Connection con = ConnManager.connect();

        if (con == null ){
            return false;
        }

        try {
            int experience = farmer.getExperience();
            int gold = farmer.getGold();
            int id = farmer.getUserID();
            String sql = "UPDATE farmer set experience = ?, gold = ? where user_id = ?";

            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, experience);
            st.setInt(2, gold);
            st.setInt(3, id);

            return (st.executeUpdate() == 1);

        } catch (SQLException se) {
            System.out.println("failed to update farmer");
            return false;
        }

    }




}