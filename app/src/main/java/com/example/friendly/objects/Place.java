package com.example.friendly.objects;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Place")
public class Place extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_CATEGORY = "category";

    public String getName() {
        return getString(KEY_NAME);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

}
