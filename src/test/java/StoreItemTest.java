import org.junit.jupiter.api.*;
import java.util.*;

import main.java.game.*;

public class StoreItemTest {

    StoreItem testItem = new StoreItem("TestItem", 1, 2, 3, 4, 5, 6);

    @Test
    public void testGetName() {
        Assertions.assertEquals("TestItem", testItem.getName());
    }

    @Test
    public void testGetCost() {
        Assertions.assertEquals(1, testItem.getCost());
    }

    @Test
    public void testGetTime() {
        Assertions.assertEquals(2, testItem.getTime());
    }

    @Test
    public void testGetExp() {
        Assertions.assertEquals(3, testItem.getExp());
    }

    @Test
    public void testGetMaxYield() {
        Assertions.assertEquals(5, testItem.getMaxYield());
    }

    @Test
    public void testGetPrice() {
        Assertions.assertEquals(6, testItem.getPrice());
    }

    @Test
    public void testGetRandomYield() {
        int minYield = 4;
        int maxYield = testItem.getMaxYield();

        int randomYield = testItem.getRandomYield();

        Assertions.assertTrue(maxYield >= randomYield);
        Assertions.assertTrue(minYield <= randomYield);
    }

    @Test
    public void testGetItemHarvestDetails() {
        ArrayList<Integer> itemDetails = testItem.getItemHarvestDetails();

        int cropXP = itemDetails.get(0);
        Assertions.assertEquals(3, cropXP);

        int time = itemDetails.get(4);
        Assertions.assertEquals(2, time);
    }

}