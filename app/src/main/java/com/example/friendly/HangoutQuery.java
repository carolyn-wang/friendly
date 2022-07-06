package com.example.friendly;

import android.util.Log;

import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.objects.Hangout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HangoutQuery{
    private static final String TAG = "HangoutQuery";
    protected static final int POSTS_TO_LOAD = 10;
    protected int scrollCounter = 0;
    protected List<Hangout> allHangouts = new ArrayList<>();

    // TODO: maybe change queryConditions into separate booleans
    public void queryHangouts(HangoutsAdapter adapter, ArrayList<String> queryConditions) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);

        query.setLimit(POSTS_TO_LOAD);
        // display past hangouts
        if (queryConditions.contains("past")){
            query.whereLessThan(Hangout.KEY_DATE, new Date());
            query.addDescendingOrder(Hangout.KEY_DATE);
        }
        // display future hangouts
        if (queryConditions.contains("future")){
            query.whereGreaterThanOrEqualTo(Hangout.KEY_DATE, new Date());
            query.addAscendingOrder(Hangout.KEY_DATE);
        }
        // display only current User's hangouts
        if (queryConditions.contains("user")) {
            // TODO: add "or" condition where KEY_USER2 equals current user
            query.whereEqualTo(Hangout.KEY_USER1, ParseUser.getCurrentUser());
        }

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
