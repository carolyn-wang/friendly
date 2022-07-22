package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.friendly.fragments.match.MatchFragment;
import com.example.friendly.query.HangoutQuery;
import com.example.friendly.R;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.objects.Hangout;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HangoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HangoutsFragment extends Fragment {

    private static final String TAG = "HangoutsFragment";
    private static final String KEY_CONDITION = "condition";
    private Context mContext;

    private RecyclerView rvHangouts;
    protected HangoutsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static ProgressBar pb;
    private HangoutQuery query;
    private ArrayList<String> queryConditions;

    public HangoutsFragment() {
        // Required empty public constructor
    }

    public static HangoutsFragment newInstance(ArrayList<String> queryConditions) {

        Bundle args = new Bundle();
        HangoutsFragment fragment = new HangoutsFragment();
        args.putStringArrayList(KEY_CONDITION, queryConditions);
        fragment.setArguments(args);
        return fragment;
    }

    public static HangoutsFragment newInstance(String queryCondition) {
        ArrayList<String> conditionsArray = new ArrayList<>();
        conditionsArray.add(queryCondition);
        return HangoutsFragment.newInstance(conditionsArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hangouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryConditions = (ArrayList<String>) getArguments().getStringArrayList(KEY_CONDITION);

        if (queryConditions != null) {
            queryConditions = (ArrayList<String>) getArguments().getStringArrayList(KEY_CONDITION);
        }
        mContext = view.getContext();
        pb = view.findViewById(R.id.pbLoading);
        rvHangouts = view.findViewById(R.id.rvHangouts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        query = new HangoutQuery(mContext);
        List<Hangout> allHangouts = query.getAllHangouts();
        showProgressBar();

        adapter = new HangoutsAdapter(mContext, allHangouts);
        rvHangouts.setAdapter(adapter);
        query.queryHangouts(adapter, queryConditions);

        rvHangouts.setLayoutManager(new LinearLayoutManager(mContext));

        setPullToRefresh();
        setScrollListener();
    }

    public static void hideProgressBar() {
        pb.setVisibility(ProgressBar.INVISIBLE);
    }

    public static void showProgressBar() {
        pb.setVisibility(ProgressBar.VISIBLE);
    }


    /**
     * Displays animation when user refreshes feed
     * Sets refreshing to false once network request has completed successfully
     */
    public void setPullToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                adapter.clear();
                query.setScrollCounter(0);
                query.queryHangouts(adapter, queryConditions);

                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * Adds a scroll listener to Hangouts RecyclerView
     */
    public void setScrollListener() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvHangouts.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                query.queryHangouts(adapter, queryConditions);

            }
        };
        rvHangouts.addOnScrollListener(scrollListener);
    }

}