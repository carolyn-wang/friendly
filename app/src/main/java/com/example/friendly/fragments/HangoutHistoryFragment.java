package com.example.friendly.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.friendly.R;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HangoutHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HangoutHistoryFragment extends Fragment {

    private static final String TAG = "HangoutHistoryFragment";
    private Context mContext;

    private Button btnLogout;
    private Button btnHangoutHistory;
    private TextView tvUsername;

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
        mContext = getContext();

    }
}