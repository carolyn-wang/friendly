package com.example.friendly.objects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Hangout")
public class Hangout extends ParseObject {

    public static final String TAG = "Hangout";
    public static final String KEY_USER1 = "user1";
    public static final String KEY_USER2 = "user2";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "place";
    public static final String KEY_USER_FIRST_NAME = "firstName";


    public ParseUser getUser1() {
        return getParseUser(KEY_USER1);
    }

    public String getUser1Name() {
        if (getParseUser(KEY_USER1) != null){
            return getParseUser(KEY_USER1).getString(KEY_USER_FIRST_NAME);
        }
        return "";    }

    public void setUser1(ParseUser user) {
        put(KEY_USER1, user);
    }

    public ParseUser getUser2() {
        return getParseUser(KEY_USER2);
    }

    public String getUser2Name() {
        if (getParseUser(KEY_USER2) != null){
            return getParseUser(KEY_USER2).getString(KEY_USER_FIRST_NAME);
        }
        return "";
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

    public Place getPlace() {
        return (Place) getParseObject(KEY_LOCATION);
    }

    public String getLocationName() {
        if (getPlace() != null){
            return getPlace().getName();
        }
        else return "";
    }

    public void setLocation(Place location) {
        put(KEY_LOCATION, location);
    }

    public boolean equals(Hangout hangout) {
        return hangout.getObjectId().equals(this.getObjectId());
    }
}
