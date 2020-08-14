import org.junit.jupiter.api.*;

import main.java.social.*;

public class CommentTest {
    @Test 
    public void testgetPID() {
        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);

        Assertions.assertEquals(69, c1.getPID());
    }

    @Test 
    public void testgetPoster() {
        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);

        Assertions.assertEquals(3, c1.getPoster());
    }

    @Test 
    public void testgetMessage() {
        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);

        Assertions.assertEquals("i wanna be a part of this", c1.getMessage());
    }

    @Test 
    public void testgetCommentTime() {
        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);

        Assertions.assertEquals(100001, c1.getCommentTime());
    }
}