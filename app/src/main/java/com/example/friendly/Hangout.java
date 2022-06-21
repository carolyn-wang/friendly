package com.example.friendly;

import com.parse.ParseUser;

import java.util.Date;

public class Hangout {

    //todo: change to ParseUser
    private String user1;
    private String user2;
    private Date date;

    public Hangout(String user1, String user2, Date date) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public Date getDate() {
        return date;
    }
}
