import org.junit.jupiter.api.*;
import java.util.*;

import main.java.game.*;

public class FarmerTest {
    
    List<Plot> listP1 = new ArrayList<Plot>();
    List<Seed> listS1 = new ArrayList<Seed>();
    Plot p1 = new Plot(1, 1, 1, "TestName", 1, 1L, 1, 1);
    Seed s1 = new Seed(1, 1, "SeedTest", 1);

    @Test
    public void testGetFarmerExp() {
        listP1.add(p1);
        listS1.add(s1);
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);

        Assertions.assertEquals(1, f1.getExperience());
    }

    @Test
    public void testGetFarmerGold() {
        listP1.add(p1);
        listS1.add(s1);
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);

        Assertions.assertEquals(1, f1.getGold());
    }

    @Test
    public void testGetFarmerFarm() {
        listP1.add(p1);
        listS1.add(s1);
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);

        Assertions.assertEquals(listP1, f1.getFarm());
    }

    @Test
    public void testGetFarmerInv() {
        listP1.add(p1);
        listS1.add(s1);
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);

        Assertions.assertEquals(listS1, f1.getInventory());
    }

    @Test
    public void testUpdateGains() {
        listP1.add(p1);
        listS1.add(s1);
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);
        f1.updateGains(1, 1); // new gold should be 1+1

        Assertions.assertEquals(2, f1.getGold());
    }

    @Test
    public void testUpdateFarm() {
        listP1.add(p1);
        listS1.add(s1);

        List<Plot> listP2 = new ArrayList<Plot>();
        Plot p2 = new Plot(1, 1, 1, "TestName2", 1, 1L, 1, 1);
        listP2.add(p2);
        
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);

        f1.updateFarm(listP2);

        Assertions.assertEquals(listP2, f1.getFarm());
    }

    public void testUpdateInventory() {
        listP1.add(p1);
        listS1.add(s1);

        List<Seed> listS2 = new ArrayList<Seed>();
        Seed s2 = new Seed(1, 1, "SeedTest2", 1);
        listS2.add(s2);
        
        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1);

        f1.updateInventory(listS2);

        Assertions.assertEquals(listS2, f1.getFarm());
    }

    @Test
    public void testPurchaseSuccess() {
        listP1.add(p1);
        listS1.add(s1);

        Farmer f1 = new Farmer(1, listP1, listS1, 1, 10); // farmer orig has 10gold

        f1.purchase(1); // result should be 10 - 1 = 9

        Assertions.assertEquals(9, f1.getGold());
    }

    @Test
    public void testPurchaseFail() {
        listP1.add(p1);
        listS1.add(s1);

        Farmer f1 = new Farmer(1, listP1, listS1, 1, 1); // farmer orig has 10gold

        int results = f1.purchase(10); // result should be -1 since cannot deduct

        Assertions.assertEquals(-1, results);
        Assertions.assertEquals(1, f1.getGold());
    }
    
    
}