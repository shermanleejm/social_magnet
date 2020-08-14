package main.java.connection;

import java.sql.*; 
import java.util.*;
import main.java.game.*;
import main.java.social.*;

public class PlotDAO {

    // Helper function to parse Results, return Plot Obj
    private Plot rsToPlot(ResultSet rs){
        try{
            int id = rs.getInt(1);
            int userID = rs.getInt(2);
            int plotPosition = rs.getInt(3);
            String name = rs.getString(4);
            int timeToMaturity = rs.getInt(5);
            long startingTime = rs.getLong(6);
            int maxYield = rs.getInt(7);
            int stolenYield = rs.getInt(8);
            return new Plot(id,userID, plotPosition, name, timeToMaturity, startingTime, maxYield, stolenYield);
            
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
            Plot plot = null;
            return plot;
        }
        
    }
    // Get all Plots
    // Returns empty ArrayList if no plot is found

    public List<Plot> getAllPlots() {
        List<Plot> output = new ArrayList<>();
        
        Connection con = ConnManager.connect();
        if (con == null ){
            return output;
        }
        
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from plot");  
            while(rs.next()){
                Plot parsedPlot = rsToPlot(rs);
                if (parsedPlot != null){
                    output.add(parsedPlot);
                }
            }  

            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
    
        return output;
    }

    // Get Plot by userID
    // Returns empty ArrayList if no plot is found

    public List<Plot> getPlotByUserID(int userID) {
        List<Plot> output = new ArrayList<>();
        
        Connection con = ConnManager.connect();
        if (con == null ){
            return output;
        }
        
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from plot where user_id = " + userID);  

            while(rs.next()){
                Plot parsedPlot = rsToPlot(rs);
                if (parsedPlot != null){
                    output.add(parsedPlot);
                }
            }  

            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
    
        return output;
    }

    // Get plot by PlotID
    // Returns null plot if no such entry is found

    public Plot getPlotByID(int plotID){
        Plot plot = null;
        
        Connection con = ConnManager.connect();

        if (con == null){
            return plot;
        }
       
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from plot where plot_id = " + plotID);  
            while(rs.next()){
                plot = rsToPlot(rs);
            }  
            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
        
        return plot;
    }

    // Deletes plot by PlotID
    // Return true if successful, false if failed to delete
    public boolean deletePlotByID(int plotID){

        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }
       
        try{
            Statement stmt=con.createStatement();  
            int rowsDeleted = stmt.executeUpdate("delete from plot where plot_id = " + plotID);  
            con.close();  
            
            return rowsDeleted == 1;
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();

            return false;
        }
        
    }
    // Create plot
    // Return true if successful
    // False if failed to create (might be input mismatch) or id+Plot combination exists 
    public boolean createPlot(int userID, int plotPosition, String name, int timeToMaturity ){
    //    System.out.println("IN Create plot");
        Connection con = ConnManager.connect();

        if (con == null){
            return false;
        }
        try{
            // Check if user_id and plot_position combination exits
            Statement queryStmt=con.createStatement();  
            ResultSet rs=queryStmt.executeQuery("select * from plot where user_id = " 
                    + userID + " and plot_position = "+  plotPosition);

            while (rs.next()){
                return false;
            }
            long time = System.currentTimeMillis();

            String sql = "insert into plot(" 
            + "plot_id," + "user_id," + "plot_position," + "crop_name," + "time_to_maturity," 
            + "start_time, " + "max_yield," + "stolen_yield"
            + " ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) "; 
            
            PreparedStatement st = con.prepareStatement(sql);

            Store ns = new Store();
            StoreItem item = ns.getStoreItembySeedName(name);

            // Exist function is store doesn't contain this item
            if (item == null){
                return false;
            }

            // System.out.println("Store item success" + " " + item.getName() + " " +item.getRandomYield());
            int generateYield = item.getRandomYield();
            
            st.setInt(1, userID);
            st.setInt(2, plotPosition);
            st.setString(3, name);
            st.setInt(4, timeToMaturity);
            st.setLong(5, time);
            st.setInt(6, generateYield);
            // Initialise with 0 stolen
            st.setInt(7, 0);
            int rowsAdded = st.executeUpdate();
            
            con.close();  

            return rowsAdded == 1;
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed / Item already exists");
            // ex.printStackTrace();

            return false;
        }
    }
    
    // Steal plot by PlotID
    // This assumes the person stealing has already been checked to be a friend 
    // Edits stolenYield in database
    // Returns exp, gold of stolen amount in String
    // Returns null if failed

    public ArrayList<Integer> stealPlotByID(int plotID){
        // String output = null;
        Plot plot = null;
        ArrayList<Integer> results = new ArrayList<Integer>();

        Connection con = ConnManager.connect();

        if (con == null){
            return results;
        }
       
        try{
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from plot where plot_id = " + plotID);  
            while(rs.next()){
                plot = rsToPlot(rs);
                
                int maxAllowedForTheft = (int) Math.round(plot.getMaxYield() * 0.2);
                int amountStolen = plot.getStolenYield();

                if (maxAllowedForTheft <= amountStolen){
                    return results;
                }
                
                Random r = new Random();
                // get 5% of maxYield 
                int toSteal = (int) Math.round((r.nextInt(5) + 1) * maxAllowedForTheft);

                if (maxAllowedForTheft - amountStolen < toSteal){
                    toSteal = maxAllowedForTheft - amountStolen;
                }
                System.out.println("to Steal" + toSteal);
                String sql = "UPDATE plot set stolen_yield = ? where plot_id = ?" ; 
                PreparedStatement st = con.prepareStatement(sql);

                st.setInt(1, amountStolen + toSteal);
                st.setInt(2, plotID);

                int rowsAdded = st.executeUpdate();

                if (rowsAdded != 1){
                    return results;
                }

                Store ns = new Store();
                StoreItem item = ns.getStoreItembySeedName(plot.getName());

                // Exit function if store doesn't contain this item
                if (item == null){
                    return results;
                }

                // output = "" + item.getExp() * toSteal + "," + item.getPrice() * toSteal;
                results.add(toSteal);
                results.add(item.getExp() * toSteal);
                results.add(item.getPrice() * toSteal);            
            }  
            con.close();  
        }catch (SQLException ex) {
            System.out.println("Reading from DB failed");
            ex.printStackTrace();
        }
        
        return results;
    }
}