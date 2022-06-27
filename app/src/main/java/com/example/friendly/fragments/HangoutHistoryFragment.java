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

@Deprecated

// TODO: child fragment that only queries specific user hangouts
public class HangoutHistoryFragment extends HangoutsFragment {

    private static final String TAG = "HangoutHistoryFragment";
    private Context mContext;

    private RecyclerView rvHangouts;
    protected HangoutsAdapter adapter;
    private HangoutQuery query;


//    public HangoutHistoryFragment() {
//    }

    public static HangoutHistoryFragment newInstance(ParseUser user) {
        Bundle args = new Bundle();
        HangoutHistoryFragment fragment = new HangoutHistoryFragment();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = view.getContext();
        rvHangouts = view.findViewById(R.id.rvHangouts);
        query = new HangoutQuery();
        List<Hangout> allHangouts = query.getAllHangouts();
        if (allHangouts.size() == 0){
            showProgressBar();
        }

        adapter = new HangoutsAdapter(mContext, allHangouts);
        rvHangouts.setAdapter(adapter);
        query.queryHangouts(adapter, "past");
        rvHangouts.setLayoutManager(new LinearLayoutManager(mContext));

        Log.i(TAG, "working");

        setPullToRefresh();
        setScrollListener();
    }

}