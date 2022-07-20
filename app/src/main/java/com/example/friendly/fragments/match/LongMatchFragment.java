package com.example.friendly.fragments.match;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.friendly.utils.MatchingUtils;
import com.example.friendly.R;
import com.example.friendly.fragments.HangoutDetailFragment;
import com.example.friendly.objects.Hangout;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;

public class LongMatchFragment extends Fragment {
    private static final String TAG = "LongMatchFragment";
    private Context mContext;

    private static final String KEY_MATCHED_USER = "matchedUser";

    private Hangout hangout;
    private ParseUser matchedUser;
    private TextView tvHangoutUser1;
    private TextView tvHangoutUser2;
    private TextView tvHangoutDate;


    public LongMatchFragment() {
    }

    public static HangoutDetailFragment newInstance(ParseUser matchedUser) {
        HangoutDetailFragment fragment = new HangoutDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MATCHED_USER, matchedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchedUser = getArguments().getParcelable(KEY_MATCHED_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_long_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Collection<ParseUser> topMatches = MatchingUtils.getMatches();
        for (ParseUser user : topMatches) {
            Log.i(TAG, user.getUsername());
        }

        mContext = getContext();
        tvHangoutUser1 = view.findViewById(R.id.tvHangoutUser1);
        tvHangoutUser2 = view.findViewById(R.id.tvHangoutUser2);
        tvHangoutDate = view.findViewById(R.id.tvHangoutDate);

        if (hangout.getUser1() != null) {
            tvHangoutUser1.setText(hangout.getUser1().getUsername());
        } else {
            tvHangoutUser1.setText("");
        }
        if (hangout.getUser2() != null) {
            tvHangoutUser2.setText(hangout.getUser2().getUsername());
        } else {
            tvHangoutUser2.setText("");
        }
        tvHangoutDate.setText(hangout.getDate().toString());

    }

}
