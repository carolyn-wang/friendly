package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendly.R;

import java.util.ArrayList;
import java.util.Arrays;

public class HangoutHistoryFragment extends Fragment {
    private static final String TAG = "HangoutHistoryFragment";
    private Context mContext;

    public HangoutHistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hangout_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = view.getContext();

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // TODO: put "past", "user", "first name" all in a strings constant file
        ArrayList<String> conditions = new ArrayList<>(Arrays.asList("past", "user"));
        ft.replace(R.id.rvHangoutHistory, HangoutsFragment.newInstance(conditions))
                .addToBackStack(null)
                .commit();
    }


}
