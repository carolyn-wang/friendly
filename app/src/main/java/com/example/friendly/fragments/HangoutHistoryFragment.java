package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.friendly.Hangout;
import com.example.friendly.HangoutsAdapter;
import com.example.friendly.HangoutsQuery;
import com.example.friendly.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HangoutHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HangoutHistoryFragment extends Fragment {

    private static final String TAG = "HangoutHistoryFragment";
    private Context mContext;

    protected static final int POSTS_TO_LOAD = 5;
    private RecyclerView rvHangouts;
    protected HangoutsAdapter adapter;
    protected List<Hangout> allHangouts;
//    private SwipeRefreshLayout swipeContainer;
//    private EndlessRecyclerViewScrollListener scrollListener;
    protected int scrollCounter;

    public HangoutHistoryFragment() {
        // Required empty public constructor
    }

    public static HangoutHistoryFragment newInstance(ParseUser user) {

        Bundle args = new Bundle();

        HangoutHistoryFragment fragment = new HangoutHistoryFragment();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hangout_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();

        rvHangouts = view.findViewById(R.id.rvHangouts);
        scrollCounter = 0;

        allHangouts = new ArrayList<>();



        rvHangouts.setLayoutManager(new LinearLayoutManager(mContext));

//        allHangouts = HangoutsQuery.queryHangouts(allHangouts, 0);
        adapter = new HangoutsAdapter(mContext, allHangouts);
        rvHangouts.setAdapter(adapter);

        queryHangouts(0);
//        adapter.notifyDataSetChanged();

    }

        public void queryHangouts(int offset) {
            ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
            query.include(Hangout.KEY_USER1);
            query.include(Hangout.KEY_USER2);
            query.include(Hangout.KEY_DATE);
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
                    allHangouts.addAll(hangouts);
                    adapter.notifyDataSetChanged();
                }
            });

            // move function to postQuerier.java to query posts
            // query class passes info into both adapters
            // Repository (stores data) -- tells adapter update --> adapter
//        scrollCounter = scrollCounter + POSTS_TO_LOAD;
        }


}