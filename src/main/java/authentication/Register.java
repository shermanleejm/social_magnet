package main.java.authentication;

import java.sql.*;
import java.util.*;

import main.java.connection.*;
import main.java.social.*;
import main.java.game.*;

public class Register{
    private Register reg;

    public Register(){
    }

    static ConnManager cm = new ConnManager();
    static Connection conn = cm.connect();

    public static void Registration(){

        Scanner input = new Scanner(System.in);

        System.out.println("NOTE: your username is a unique one so it cannot be changed.");

        System.out.printf("Username: ");
        String username = input.next();
        boolean checkname = false;

        try{
            PreparedStatement st = conn.prepareStatement("select * from users where username = ?");
            st.setString(1, username);
            ResultSet r1=st.executeQuery();
            if(r1.next()) {
              checkname = true;  
             }else{
              checkname = false;
             }
    }catch(Exception e ){
            //do something;
        }
        try{
        while(checkname == true){

            System.out.println("Username is already exists and used, please type another one");
            System.out.printf("Username: ");
            username = input.next();

            PreparedStatement st = conn.prepareStatement("select * from users where username = ?");
            st.setString(1, username);
            ResultSet r1=st.executeQuery();
            if(r1.next()) {
              checkname = true;  
             }else{
              checkname = false;
             }
        }
    }catch( Exception e){

    }
        System.out.println("Username is not in use!");
        System.out.printf("Password: ");
        String password = input.next();
        System.out.println("Now please enter your password again!");
        System.out.printf("Confirm Password: ");
        String confirmPass = input.next();

        // int passInt = Integer.parseInt(password);
        // int confInt = Integer.parseInt(confirmPass);
       

        while( !(password.equals(confirmPass)) ){
            System.out.println("Password mismatch, please try again!");

            System.out.printf("Password: ");
            password = input.next();
            System.out.println("Now please enter your password again!");
            

            System.out.printf("Confirm Password: ");
            confirmPass = input.next();

            // passInt = Integer.parseInt(password);
            // confInt = Integer.parseInt(confirmPass);

        }
            System.out.println("Please enter your profile name: ");
            String name = input.next();
            while(name == null || name == ""){
                System.out.println("Your name shouldn't be empty, Please enter again!");
                System.out.println("Please enter your profile name: ");
                name = input.next();
            }
            String insert;

            insert = "INSERT INTO users "
            + "(username, password,name) "
            + "VALUES (?, ?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insert)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
            System.out.println("Your account has been created!");
        }catch(Exception e) {
            e.printStackTrace();
        }
  }
}




