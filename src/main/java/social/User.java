package main.java.social;

import main.java.game.*;
import main.java.connection.*;

public class User {
    private int userID;
    private String username;
    private String password; 
    private String name;
    private Farmer farmerChar;

    public User(int userID, String username, String password, String name) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.farmerChar = null;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getUserID() {
        return this.userID;
    }

    public Farmer getFarmer() {
        return this.farmerChar;
    }

    public void setFarmer(Farmer farmer) {
        this.farmerChar = farmer;
    }
}