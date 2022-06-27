package com.example.friendly.objects;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

@ParseClassName("Hangout")
public class Hangout extends ParseObject {

    public static final String KEY_USER1 = "user1";
    public static final String KEY_USER2 = "user2";
    public static final String KEY_DATE = "date";

    public ParseUser getUser1() {
        return getParseUser(KEY_USER1);
    }

    public void setUser1(ParseUser user) {
        put(KEY_USER1, user);
    }

    public ParseUser getUser2() {
        return getParseUser(KEY_USER2);
    }

    public void setUser2(ParseUser user) {
        put(KEY_USER2, user);
    }


    public Date getDate() {
        return getDate(KEY_DATE);
    }

    public void setDate(Date date) {
        put(KEY_DATE, date);
    }


}