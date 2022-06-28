package com.example.friendly.objects;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@ParseClassName("Hangout")
public class Hangout extends ParseObject {

    public static final String TAG = "Hangout";
    public static final String KEY_USER1 = "user1";
    public static final String KEY_USER2 = "user2";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "place";


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

    public Place getLocation() {
        return (Place) getParseObject(KEY_LOCATION);
    }

    public String getLocationName() {
        return getLocation().getName();
    }

}
