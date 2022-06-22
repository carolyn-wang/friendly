package com.example.friendly;

import android.util.Log;

import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.fragments.HangoutsFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HangoutsQuery {
    private static final String TAG = "HangoutsQuery";
    protected static final int POSTS_TO_LOAD = 5;
    protected int scrollCounter = 0;
    protected List<Hangout> allHangouts = new ArrayList<>();

    // TODO: move progress bar out
    public void queryHangouts(HangoutsAdapter adapter) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);
        query.setLimit(POSTS_TO_LOAD);
        query.addDescendingOrder(Hangout.KEY_CREATED_AT);
        query.setSkip(scrollCounter);
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Hangout>() {
            @Override
            public void done(List<Hangout> hangouts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                allHangouts.addAll(hangouts);
                adapter.notifyDataSetChanged();
                HangoutsFragment.hideProgressBar();
            }
        });

        // move function to postQuerier.java to query posts
        // query class passes info into both adapters
        // Repository (stores data) -- tells adapter update --> adapter
        scrollCounter = scrollCounter + POSTS_TO_LOAD;
    }

    public List<Hangout> getAllHangouts(){
        return allHangouts;
    }

    public int getScrollCounter(){
        return scrollCounter;
    }

    public void setScrollCounter(int i){
        scrollCounter = i;
    }

}
