package com.example.friendly.query;

import android.content.Context;
import android.util.Log;

import com.example.friendly.R;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.fragments.match.MatchFragment;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HangoutQuery {
    private static final String TAG = "HangoutQuery";
    private static final int POSTS_TO_LOAD = 5;
    private int scrollCounter = 0;
    protected List<Hangout> allHangouts = new ArrayList<>();
    private Context mContext;
    private static String KEY_QUERY_PAST;
    private static String KEY_QUERY_FUTURE;
    private static String KEY_USER_NEXT_HANGOUT;
    private static String KEY_HANGOUT_DATE;
    private static String KEY_QUERY_QUICK;
    public HangoutQuery() {
    }

    public HangoutQuery(Context mContext) {
        this.mContext = mContext;
        KEY_QUERY_PAST = mContext.getResources().getString(R.string.KEY_QUERY_PAST);
        KEY_QUERY_FUTURE = mContext.getResources().getString(R.string.KEY_QUERY_FUTURE);
        KEY_QUERY_QUICK = mContext.getResources().getString(R.string.KEY_QUERY_QUICK);
        KEY_USER_NEXT_HANGOUT = mContext.getString(R.string.KEY_USER_NEXT_HANGOUT);
        KEY_HANGOUT_DATE = mContext.getString(R.string.KEY_HANGOUT_DATE);
    }

    // TODO: maybe change queryConditions into separate booleans
    public void queryHangouts(HangoutsAdapter adapter, ArrayList<String> queryConditions) {
        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
        // display quick hangouts (all hangouts with null User2)
        if (queryConditions.contains(KEY_QUERY_QUICK)) {
            query.include(Hangout.KEY_USER1);
            query.include(Hangout.KEY_USER2);
            query.include(Hangout.KEY_DATE);
            query.include(Hangout.KEY_LOCATION);
            query.setLimit(POSTS_TO_LOAD);
            query.whereEqualTo(Hangout.KEY_USER2, null);
        }
        else{
            query.whereEqualTo(Hangout.KEY_USER1, ParseUser.getCurrentUser());
            ParseQuery<Hangout> queryUser2 = ParseQuery.getQuery(Hangout.class);
            queryUser2.whereEqualTo(Hangout.KEY_USER2, ParseUser.getCurrentUser());
            List<ParseQuery<Hangout>> orQuery = Arrays.asList(queryUser2, query);
            query = ParseQuery.or(orQuery);

            query.include(Hangout.KEY_USER1);
            query.include(Hangout.KEY_USER2);
            query.include(Hangout.KEY_DATE);
            query.include(Hangout.KEY_LOCATION);
            query.setLimit(POSTS_TO_LOAD);
            // display past hangouts
            if (queryConditions.contains(KEY_QUERY_PAST)) {
                query.whereLessThan(Hangout.KEY_DATE, new Date());
                query.addDescendingOrder(Hangout.KEY_DATE);
            }
            // display future hangouts
            else if (queryConditions.contains(KEY_QUERY_FUTURE)) {
                query.whereGreaterThanOrEqualTo(Hangout.KEY_DATE, new Date());
                query.addAscendingOrder(Hangout.KEY_DATE);
            }
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
                if (!allHangouts.isEmpty() && queryConditions.contains(KEY_QUERY_FUTURE)) {
                    try {
                        Hangout upcomingHangout = allHangouts.get(0);
                        updateNextUpcomingHangout(upcomingHangout);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        scrollCounter = scrollCounter + POSTS_TO_LOAD;
    }

    private void updateNextUpcomingHangout(Hangout newestHangout) throws ParseException {
        Hangout nextUpcomingHangout = (Hangout) ParseUser.getCurrentUser().fetchIfNeeded().getParseObject("nextUpcomingHangout");
        if (nextUpcomingHangout == null) {
            ParseUser.getCurrentUser().put(KEY_USER_NEXT_HANGOUT, newestHangout);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                }
            });
        } else if (!nextUpcomingHangout.equals(newestHangout)) {
            Log.d(TAG, "updating previous " + nextUpcomingHangout.getObjectId() + " to " + newestHangout.getObjectId());
            // if previously stored hangout has already happened
            if (nextUpcomingHangout.fetchIfNeeded().getDate(KEY_HANGOUT_DATE).compareTo(new Date()) < 0) {
                // get other User that's not current user
                MatchFragment.showFeedbackDialog(nextUpcomingHangout);
            }
            ParseUser.getCurrentUser().put(KEY_USER_NEXT_HANGOUT, newestHangout);
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                }
            });
        }
    }

    public List<Hangout> getAllHangouts() {
        return allHangouts;
    }

    public int getScrollCounter() {
        return scrollCounter;
    }

    public void setScrollCounter(int i) {
        scrollCounter = i;
    }

}
