import org.junit.jupiter.api.*;

import main.java.social.User;

public class UserTest {

    @Test 
    public void testGetUserID() {
        User test_user = new User(69, "shermanROX", "password123", "LEE Sherman");

        Assertions.assertEquals(69, test_user.getUserID());
    }

    @Test 
    public void testGetUsername() {
        User test_user = new User(69, "shermanROX", "password123", "LEE Sherman");

        Assertions.assertEquals("shermanROX", test_user.getUsername());
    }

    @Test 
    public void testGetPassword() {
        User test_user = new User(69, "shermanROX", "password123", "LEE Sherman");

        Assertions.assertEquals("password123", test_user.getPassword());
    }

    @Test 
    public void testGetName() {
        User test_user = new User(69, "shermanROX", "password123", "LEE Sherman");

        Assertions.assertEquals("LEE Sherman", test_user.getName());
    }

    @Test 
    public void testFarmer() {
        User test_user = new User(69, "shermanROX", "password123", "LEE Sherman");

        Assertions.assertEquals(null, test_user.getFarmer());
    }

}