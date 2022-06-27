package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.widget.ProgressBar;

import com.example.friendly.objects.Hangout;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.HangoutQuery;
import com.example.friendly.R;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HangoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HangoutHistoryFragment extends HangoutsFragment {

    private static final String TAG = "HangoutHistoryFragment";
    private Context mContext;

    private RecyclerView rvHangouts;
    protected HangoutsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static ProgressBar pb;
    private HangoutQuery query;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = view.getContext();
        pb = view.findViewById(R.id.pbLoading);
        rvHangouts = view.findViewById(R.id.rvHangouts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        query = new HangoutQuery();
        List<Hangout> allHangouts = query.getAllHangouts();
        if (allHangouts.size() == 0){
            showProgressBar();
        }

        adapter = new HangoutsAdapter(mContext, allHangouts);
        rvHangouts.setAdapter(adapter);
        query.queryPastHangouts(adapter, ParseUser.getCurrentUser());
        rvHangouts.setLayoutManager(new LinearLayoutManager(mContext));

        setPullToRefresh();
        setScrollListener();

    }

}