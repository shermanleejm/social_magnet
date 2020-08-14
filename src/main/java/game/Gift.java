package main.java.game;

import java.util.*;

public class Gift {
    private int giftID;
    private int userID;
    private int friendID;
    private String seedType;
    // 0 means pending acceptance, 1 means accepted
    private int status;
    private long timestamp;

    public Gift (int giftID, int userID, int friendID, String seedType, int status, long timestamp){
        this.giftID = giftID;
        this.userID = userID;
        this.friendID = friendID;
        this.seedType = seedType;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getID(){
        return giftID;
    }
    
    public int getUserID(){
        return userID;
    }
    
    public int getFriendID(){
        return friendID;
    }
    
    public String getSeedType(){
        return seedType;
    }

    public int getStatus(){
        return status;
    }

    public long getTimeStamp(){
        return timestamp;
    }

    public void acceptGift(){
        status = 1;
    }
}

