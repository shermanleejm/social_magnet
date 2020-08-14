import org.junit.jupiter.api.*;

import main.java.social.*;

import java.util.*;

public class PostTest {
    @Test 
    public void testgetMessage() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals("THIS IS FUN", temp_post.getMessage());
    }

    @Test 
    public void testgetPoster() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(1, temp_post.getPoster());
    }

    // @Test 
    // public void testgetPosterName() {
    //     ArrayList<Comment> comments = new ArrayList<>();

    //     Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
    //     Comment c2 = new Comment(69, 4, "get a life", 100002);
    //     Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

    //     comments.add(c1);
    //     comments.add(c2);
    //     comments.add(c3);

    //     Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

    //     Assertions.assertEquals("admin", temp_post.getPosterName());
    // }

    @Test 
    public void testgetLikes() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(0, temp_post.getLikes());
    }

    @Test 
    public void testgetDislikes() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(0, temp_post.getDislikes());
    }

    @Test 
    public void testgetWall() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(2, temp_post.getWall());
    }

    @Test 
    public void testgetPostTime() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(100000, temp_post.getPostTime());
    }

    @Test 
    public void testgetPostID() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(69, temp_post.getPostID());
    }

    @Test 
    public void testgetGiftID() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment c1 = new Comment(69, 3, "i wanna be a part of this", 100001);
        Comment c2 = new Comment(69, 4, "get a life", 100002);
        Comment c3 = new Comment(69, 5, "i like the other social network more", 100003);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);

        Post temp_post = new Post(69, 1, 2, "THIS IS FUN", 0, 0, comments, 100000, 0);

        Assertions.assertEquals(0, temp_post.getGiftID());
    }
}