package main.java.connection;

import java.sql.*;  
 
public class ConnManager {

    public static Connection connect() {
        Connection con = null;
        try {
            // Make sure to change port, login credentials
            // jianwei's
            // String url = "jdbc:mysql://localhost:3306/magnet?serverTimezone=UTC";
            // String user = "ArronLi";
            // String password = null;
            
            // jeremy's version
           /*  String url = "jdbc:mysql://localhost:8886/magnet?serverTimezone=UTC";
            String user = "root";
            String password = "root";  */

            // sherman's?
            // String url = "jdbc:mysql://root:root@localhost:3306/magnet?serverTimezone=UTC";
            // String user = "root";
            // String password = "root"; 

            // sam's
            String url = "jdbc:mysql://localhost:3306/magnet?serverTimezone=UTC";
            String user = "root";
            String password = null; 

            Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection(url, user, password);

            if (con != null) {
                // System.out.println("You are online!");
            }
            return con;
            
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid. Make sure to uncomment the correct login credentials!");
            ex.printStackTrace();
        } catch(Exception e){ System.out.println(e);} 
        
        return con;
        
    }
    
}
// Sample code for reference

  // Statement stmt=con.createStatement();  
    // ResultSet rs=stmt.executeQuery("select * from user");  
    // while(rs.next())  
    // // System.out.println("" + rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
    // System.out.println("" + rs.getString(1));
    // con.close();  


    // // connect way #2
    // String url2 = "jdbc:mysql://localhost:3306/test2?user=root&password=secret";
    // conn2 = DriverManager.getConnection(url2);
    // if (conn2 != null) {
    //     System.out.println("Connected to the database test2");
    // }

    // // connect way #3
    // String url3 = "jdbc:mysql://localhost:3306/test3";
    // Properties info = new Properties();
    // info.put("user", "root");
    // info.put("password", "secret");

    // conn3 = DriverManager.getConnection(url3, info);
    // if (conn3 != null) {
    //     System.out.println("Connected to the database test3");
    // }
            