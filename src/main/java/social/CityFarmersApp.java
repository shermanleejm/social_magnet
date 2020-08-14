package main.java.social;

import main.java.game.*;
import main.java.social.*;

public class CityFarmersApp {

    public CityFarmersApp(User user) {
        CityFarmersGameCtrl ctrl = new CityFarmersGameCtrl();
        CityFarmersMenu menu = new CityFarmersMenu(ctrl);

        // check for farmerChar
        Farmer farmerChar;
        if (ctrl.getFarmerFromUser(user) == null) {
            // assign new farmer char before starting game
            int userID = user.getUserID();
            farmerChar = ctrl.createFarmer(userID);
        } else {
            farmerChar = ctrl.getFarmerFromUser(user);
        }
        user.setFarmer(farmerChar);
        
        // start game from CityFarmersMenu
        menu.mainMenu(user);

    }
}
