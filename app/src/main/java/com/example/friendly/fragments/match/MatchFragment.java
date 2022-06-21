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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.Hangout;
import com.example.friendly.HangoutsAdapter;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
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

        rvHangouts = view.findViewById(R.id.rvHangouts);

        // TODO: code copied and pasted, find way to optimize
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
