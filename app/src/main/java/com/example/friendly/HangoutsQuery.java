package com.example.friendly;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HangoutsQuery {
    private static final String TAG = "HangoutsQuery";
    protected static final int POSTS_TO_LOAD = 5;
    protected int scrollCounter = 0;
    static List<Hangout> allHangouts;
    public static List<Hangout> queryHangouts(List<Hangout> allHangoutsIn, int offset) {
        List<Hangout> newHangouts = new ArrayList<>();
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.setLimit(POSTS_TO_LOAD);
        query.addDescendingOrder(Hangout.KEY_CREATED_AT);
        query.setSkip(offset);
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Hangout>() {
            @Override
            public void done(List<Hangout> hangouts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Hangout hangout : hangouts) {
//                    Log.i(TAG, "User 1: " + hangout.getUser1().getUsername() + ", User 2: " + hangout.getUser2().getUsername());
                }
//                adapter.notifyDataSetChanged();
//                allHangouts.addAll(hangouts);
//                newHangouts = hangouts;
            }
        });

        // move function to postQuerier.java to query posts
        // query class passes info into both adapters
        // Repository (stores data) -- tells adapter update --> adapter
//        scrollCounter = scrollCounter + POSTS_TO_LOAD;
        return allHangouts;
    }
}
