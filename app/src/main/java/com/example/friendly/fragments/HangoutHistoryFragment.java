package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.friendly.Hangout;
import com.example.friendly.HangoutsAdapter;
import com.example.friendly.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
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
        Hangout hangout0 = new Hangout("sam", "carolyn", new Date(12321213));
        Hangout hangout1 = new Hangout("ola", "carolyn", new Date(12313));
        Hangout hangout2 = new Hangout("stephanie", "carolyn", new Date(12321213));
        allHangouts.add(hangout0);
        allHangouts.add(hangout1);
        allHangouts.add(hangout2);
        adapter = new HangoutsAdapter(mContext, allHangouts);
        rvHangouts.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        rvHangouts.setLayoutManager(new LinearLayoutManager(mContext));

    }
}