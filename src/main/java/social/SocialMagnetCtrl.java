package main.java.social;

import java.io.*;
import java.sql.*;
import java.util.*;

import main.java.connection.*;
import main.java.authentication.*;
import main.java.game.*;
import main.java.social.*;


public class SocialMagnetCtrl{
        
        public SocialMagnetCtrl(){
        }

        public void display(){
            System.out.println("== Welcome to Social Magnet! ==");
            System.out.println("1. Login ");
            System.out.println("2. Register ");
            //for testing only
            /* System.out.println("3. AddFriend");
            System.out.println("4. GetFriends"); */
            System.out.println("3. Exit ");
            /* System.out.println("4. DeletePost"); */
            System.out.print("Please enter your choice: ");
        }
    
        public void readOption() {
            Scanner sc = new Scanner(System.in);
            int choice;
           
            do {
                display();
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        User user = Authentication();
                        MainMenu mm = new MainMenu(user);
                        break;
                    case 2:
                        Registration();
                        break;
                    case 3:
                        System.out.println("Bye Bye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter a choice between 1 to 3");
                }
            } while (choice != 3);
        }

        public User Authentication(){
            System.out.println("\n== Authentication Page ==");
            User user = Authentication.Authenticate();
            return user;
        }

        public void Registration(){
            System.out.println("\n== Registration Page ==");
            Register.Registration();
            
        }

    }



