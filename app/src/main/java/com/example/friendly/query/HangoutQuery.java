package com.example.friendly.query;

import android.content.Context;
import android.util.Log;

import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.fragments.match.MatchFragment;
import com.example.friendly.objects.Hangout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HangoutQuery{
    private static final String TAG = "HangoutQuery";
    private static final int POSTS_TO_LOAD = 10;
    private int scrollCounter = 0;
    protected List<Hangout> allHangouts =  new ArrayList<>();
    private Context mContext;

    public HangoutQuery(){
    }
    public HangoutQuery(Context mContext) {
        this.mContext = mContext;
    }

    // TODO: maybe change queryConditions into separate booleans
    public void queryHangouts(HangoutsAdapter adapter, ArrayList<String> queryConditions) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        query.include(Hangout.KEY_USER1);
        query.include(Hangout.KEY_USER2);
        query.include(Hangout.KEY_DATE);
        query.include(Hangout.KEY_LOCATION);
        query.setLimit(POSTS_TO_LOAD);
        // display past hangouts
        if (queryConditions.contains(mContext.getResources().getString(R.string.query_key_quick))){
            query.whereLessThan(Hangout.KEY_DATE, new Date());
            query.addDescendingOrder(Hangout.KEY_DATE);
        }
        // display future hangouts
        if (queryConditions.contains(mContext.getResources().getString(R.string.query_key_future))){
            query.whereGreaterThanOrEqualTo(Hangout.KEY_DATE, new Date());
            query.addAscendingOrder(Hangout.KEY_DATE);
        }
        // display only current User's hangouts
        if (queryConditions.contains(mContext.getResources().getString(R.string.query_key_current_user))) {
            // TODO: add "or" condition where KEY_USER2 equals current user; check first that user2 not null
            query.whereEqualTo(Hangout.KEY_USER1, ParseUser.getCurrentUser());
        }

        // display quick hangouts (only hangouts with null User2)
        if (queryConditions.contains(mContext.getResources().getString(R.string.query_key_quick))) {
            query.whereEqualTo(Hangout.KEY_USER2, null);
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
                if (!allHangouts.isEmpty() && queryConditions.contains(mContext.getResources().getString(R.string.query_key_future))) { //TODO: CHANGE KEYS
                    updateNextUpcomingHangout(allHangouts.get(0));
                }
            }
        });
        scrollCounter = scrollCounter + POSTS_TO_LOAD;
    }

    private void updateNextUpcomingHangout(Hangout newestHangout) {
        Hangout nextUpcomingHangout = (Hangout) ParseUser.getCurrentUser().get("nextUpcomingHangout");
        if (nextUpcomingHangout == null){
            ParseUser.getCurrentUser().put("nextUpcomingHangout", newestHangout);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                }
            });
        } else if (!nextUpcomingHangout.equals(newestHangout)){
            // if previously stored hangout has already passed
            if (nextUpcomingHangout.getDate().compareTo(new Date()) < 0){
                Log.i(TAG, "old hangout");
                MatchFragment.showFeedbackDialog(newestHangout);
            }
            Log.i(TAG, "need to update hangout & notify change");
            ParseUser.getCurrentUser().put("nextUpcomingHangout", newestHangout);
        }
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
