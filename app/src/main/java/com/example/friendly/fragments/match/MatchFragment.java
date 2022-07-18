package com.example.friendly.fragments.match;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.fragments.MapFragment;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchFragment extends Fragment {
    private static final String TAG = "MatchFragment";
    private Context mContext;

    private Button btnQuickHangout;
    private Button btnLongHangout;
    private Button btnMap;

    public MatchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = view.getContext();

        btnQuickHangout = view.findViewById(R.id.btnQuickHangout);
        btnLongHangout = view.findViewById(R.id.btnLongHangout);

        btnQuickHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.displayFragmentQuickMatch(((MainActivity) mContext).getSupportFragmentManager());
            }
        });

        btnLongHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.displayFragmentLongMatch(((MainActivity) mContext).getSupportFragmentManager());
            }
        });

        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ArrayList<String> conditions = new ArrayList<>(Arrays.asList("future", "user"));
        ft.add(R.id.upcomingHangouts, HangoutsFragment.newInstance(conditions));
        ft.commit();

    }
}
