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

public class HangoutQuery {
    private static final String TAG = "HangoutQuery";
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
        scrollCounter = scrollCounter + POSTS_TO_LOAD;
    }

    /*
    public ParseQuery<Hangout> getDefaultParseQuery() {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);
        query.setLimit(POSTS_TO_LOAD);
        query.addDescendingOrder(Hangout.KEY_DATE);
        query.setSkip(scrollCounter);
        return query;
    }
     */

    public void callParseQuery(ParseQuery<Hangout> query, HangoutsAdapter adapter){
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
    }

    public void queryHangoutsByUser(HangoutsAdapter adapter, ParseUser user) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);
        query.setLimit(POSTS_TO_LOAD);
        query.addDescendingOrder(Hangout.KEY_DATE);
        query.setSkip(scrollCounter);

        /*
        ParseQuery<Hangout> queryUser1 = new ParseQuery<Hangout>(Hangout.class);
        queryUser1.whereEqualTo(Hangout.KEY_USER1, user);
        ParseQuery<Hangout> queryUser2 = new ParseQuery<Hangout>(Hangout.class);
        queryUser2.whereEqualTo(Hangout.KEY_USER2, user);

        List<ParseQuery<Hangout>> list = new ArrayList<ParseQuery<Hangout>>();
        list.add(query);
        list.add(queryUser1);
        list.add(queryUser2);
        ParseQuery<Hangout> queryOr = ParseQuery.or(list);
         */

        //TODO: user can also be User2
        query.whereEqualTo(Hangout.KEY_USER1, user);
        callParseQuery(query, adapter);
        scrollCounter = scrollCounter + POSTS_TO_LOAD;

    }

    public void queryPastHangouts(HangoutsAdapter adapter, ParseUser user) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);
        query.setLimit(POSTS_TO_LOAD);
        query.addDescendingOrder(Hangout.KEY_DATE);
        query.setSkip(scrollCounter);
        //TODO: user can also be User2
//        query.whereEqualTo(Hangout.KEY_USER1, user);
        query.whereLessThan(Hangout.KEY_DATE, new Date());
        callParseQuery(query, adapter);
        scrollCounter = scrollCounter + POSTS_TO_LOAD;
    }

    public void queryFutureHangouts(HangoutsAdapter adapter, ParseUser user) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);
        query.setLimit(POSTS_TO_LOAD);
        query.addAscendingOrder(Hangout.KEY_DATE);
        query.setSkip(scrollCounter);
        //TODO: user can also be User2
//        query.whereEqualTo(Hangout.KEY_USER1, user);
        query.whereGreaterThanOrEqualTo(Hangout.KEY_DATE, new Date());
        callParseQuery(query, adapter);
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
