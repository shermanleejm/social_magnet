package main.java.social;

import main.java.connection.*;
import main.java.game.*;
import main.java.social.*;


/**
 * SocialMagnetMenu
 */
public class SocialMagnetMenu {
    public static void main(String[] args) {
        SocialMagnetMenu smm = new SocialMagnetMenu();
    }
    
    public SocialMagnetMenu() {
        SocialMagnetCtrl menu = new SocialMagnetCtrl();
        menu.readOption();
    }
}