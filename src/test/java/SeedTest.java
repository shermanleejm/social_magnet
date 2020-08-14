import org.junit.jupiter.api.*;

import main.java.game.*;

public class SeedTest {
    @Test
    public void testgetID() {
        Seed test_seed = new Seed(1, 1, "Sunflower", 10);

        Assertions.assertEquals(1, test_seed.getID());
    }

    @Test
    public void testgetUserID() {
        Seed test_seed = new Seed(1, 1, "Sunflower", 10);

        Assertions.assertEquals(1, test_seed.getUserID());
    }

    @Test
    public void testgetSeedType() {
        Seed test_seed = new Seed(1, 1, "Sunflower", 10);

        Assertions.assertEquals("Sunflower", test_seed.getSeedType());
    }

    @Test
    public void testgetSeedAmount() {
        Seed test_seed = new Seed(1, 1, "Sunflower", 10);

        Assertions.assertEquals(10, test_seed.getSeedAmount());
    }

    @Test
    public void testAddSeed() {
        Seed test_seed = new Seed(1, 1, "Sunflower", 10);
        test_seed.addSeed(1);
        Assertions.assertEquals(11, test_seed.getSeedAmount());
    }

    @Test
    public void testReduceSeed() {
        Seed test_seed = new Seed(1, 1, "Sunflower", 10);
        test_seed.reduceSeed(1);
        Assertions.assertEquals(9, test_seed.getSeedAmount());
    }
}