package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.friendly.Hangout;
import com.example.friendly.HangoutsAdapter;
import com.example.friendly.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HangoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HangoutsFragment extends Fragment {

    private static final String TAG = "HangoutsFragment";
    private Context mContext;

    protected static final int POSTS_TO_LOAD = 5;
    private RecyclerView rvHangouts;
    protected HangoutsAdapter adapter;
    protected List<Hangout> allHangouts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected int scrollCounter;
    private ProgressBar pb;

    public HangoutsFragment() {
        // Required empty public constructor
    }

    public static HangoutsFragment newInstance(ParseUser user) {

        Bundle args = new Bundle();

        HangoutsFragment fragment = new HangoutsFragment();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hangouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);
        rvHangouts = view.findViewById(R.id.rvHangouts);
        scrollCounter = 0;
        allHangouts = new ArrayList<>();
        rvHangouts.setLayoutManager(new LinearLayoutManager(mContext));
//        allHangouts = HangoutsQuery.queryHangouts(allHangouts, 0);
        adapter = new HangoutsAdapter(mContext, allHangouts);
        rvHangouts.setAdapter(adapter);
        pb.setVisibility(ProgressBar.VISIBLE);
        queryHangouts(scrollCounter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /**
             * Refreshes feed
             * Sets refreshing to false once network request has completed successfully
             */
            @Override
            public void onRefresh() {
                adapter.clear();
                scrollCounter = 0;
                queryHangouts(scrollCounter);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvHangouts.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryHangouts(scrollCounter);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvHangouts.addOnScrollListener(scrollListener);

    }

    // TODO: move to separate file
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
                pb.setVisibility(ProgressBar.INVISIBLE);
            }
        });

        // move function to postQuerier.java to query posts
        // query class passes info into both adapters
        // Repository (stores data) -- tells adapter update --> adapter
        scrollCounter = scrollCounter + POSTS_TO_LOAD;
    }


}