package com.example.friendly.fragments.match;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.fragments.HangoutHistoryFragment;
import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.objects.Hangout;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.parse.ParseUser;

import java.util.List;

public class MatchFragment extends Fragment {
    private static final String TAG = "MatchFragment";
    private Context mContext;

    private Button btnQuickHangout;
    private Button btnLongHangout;

    private RecyclerView rvHangouts;
    protected HangoutsAdapter adapter;
    protected List<Hangout> allHangouts;


    public MatchFragment(){

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
                Log.i(TAG, "onClick Hangout History button");
                NavigationUtils.displayFragmentQuickMatch(ParseUser.getCurrentUser(), ((MainActivity)mContext).getSupportFragmentManager());
            }
        });

        btnLongHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick LongTerm History button");
                NavigationUtils.displayFragmentLongMatch(ParseUser.getCurrentUser(), ((MainActivity)mContext).getSupportFragmentManager());
            }
        });

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fm.beginTransaction();
        new HangoutsFragment();
//        TODO: can remove getCurrUser
        Fragment fragTwo = HangoutsFragment.newInstance(ParseUser.getCurrentUser(), "future");
        ft.add(R.id.matchHangoutHistory, fragTwo);
        ft.commit();
    }
}
