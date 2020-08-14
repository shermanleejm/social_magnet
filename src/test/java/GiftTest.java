import org.junit.jupiter.api.*;

import main.java.game.*;

public class GiftTest {
    @Test 
    public void testgetID() {
        Gift temp_gift = new Gift(1, 1, 2, "Sunflower", 0, 100000);

        Assertions.assertEquals(1, temp_gift.getID());
    }

    @Test 
    public void testgetUserID() {
        Gift temp_gift = new Gift(1, 1, 2, "Sunflower", 0, 100000);

        Assertions.assertEquals(1, temp_gift.getUserID());
    }

    @Test 
    public void testgetFriendID() {
        Gift temp_gift = new Gift(1, 1, 2, "Sunflower", 0, 100000);

        Assertions.assertEquals(2, temp_gift.getFriendID());
    }

    @Test 
    public void testgetSeedType() {
        Gift temp_gift = new Gift(1, 1, 2, "Sunflower", 0, 100000);

        Assertions.assertEquals("Sunflower", temp_gift.getSeedType());
    }

    @Test 
    public void testgetStatus() {
        Gift temp_gift = new Gift(1, 1, 2, "Sunflower", 0, 100000);

        Assertions.assertEquals(0, temp_gift.getStatus());
    }

    @Test 
    public void testgetTimeStamp() {
        Gift temp_gift = new Gift(1, 1, 2, "Sunflower", 0, 100000);

        Assertions.assertEquals(100000, temp_gift.getTimeStamp());
    }

}