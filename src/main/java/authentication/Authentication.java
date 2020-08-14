package main.java.authentication;

import java.sql.*;
import java.util.*;

import main.java.connection.*;
import main.java.social.*;
import main.java.game.*;

public class Authentication {

    public Authentication(){
        
    }

    private static Scanner sc;
    private static boolean verified = false;

    public static User Authenticate(){
        verified = false;
        User user = null;

        Scanner input = new Scanner(System.in);
        String username = "";
        String password = "";
        
        while(verified == false){

            System.out.print("Enter your username: ");
            username = input.nextLine();
    
            System.out.print("Enter your password: ");
            password = input.nextLine();  
    
            verifyLogin(username,password);
        
        }

        try {
            UserDAO udao = new UserDAO();

            user = udao.getUserByUsername(username);

        } catch (Exception e) {
            System.out.println("Problem with creating user object in Authentication.java");
        } finally {
            return user;
        }
        
    }

    public static void verifyLogin(String username,String password){
        String passData;
        ResultSet res;
        boolean found = false;
        String queryString ="SELECT password FROM users WHERE username=?";
        res = null;
        try (
            Connection conn = ConnManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(queryString);
        ) {
           /*  sc.useDelimiter(",\n"); */
            pstmt.setString(1,username);
            res = pstmt.executeQuery();

            res.next();
            passData = res.getString(1);

            if (passData.equals(password)) {
                    found = true;/* 
                    System.out.print(found); */
                    verified = true;
                    System.out.println("\nWelcome back, " + username + ".\n");



                } else {
                    found = false;
                    System.out.println("Invalid Username / Password, try again");
                }
               
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println("Invalid Username / Password, try again");
            }
    }


}