package com.example.friendly.fragments.match;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friendly.utils.MatchingUtils;
import com.example.friendly.R;
import com.parse.ParseUser;

import java.util.Collection;

public class LongMatchFragment extends Fragment {
    private static final String TAG = "LongMatchFragment";

    public LongMatchFragment() {

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
    }
}
