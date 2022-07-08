package com.example.friendly.fragments.match;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.fragments.HangoutsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickMatchFragment extends Fragment {
    private static final String TAG = "QuickMatchFragment";
    private FloatingActionButton btnCreateQuickHangout;
    private Activity mActivity;
    public QuickMatchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        btnCreateQuickHangout = view.findViewById(R.id.btnCreateQuickHangout);

        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fm.beginTransaction();
        ArrayList<String> conditions = new ArrayList<>(Arrays.asList("future", "quick"));
        Fragment fragTwo = HangoutsFragment.newInstance(conditions);
        ft.add(R.id.quickMatchHangouts, fragTwo);
        ft.commit();

        btnCreateQuickHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flContainer, new CreateQuickMatchFragment()).commit();
            }
        });
    }

}
