package com.example.friendly.fragments.match;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.friendly.utils.MatchingUtils;
import com.example.friendly.R;
import com.example.friendly.objects.Hangout;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.Iterator;

public class LongMatchFragment extends Fragment {
    private static final String TAG = "LongMatchFragment";
    private Context mContext;

    private static final String KEY_MATCHED_USER = "matchedUser";

    private Hangout hangout;
    private ParseUser matchedUser;
    private TextView tvHangoutUser2;
    private TextView tvMatchedPlace;
    private TextView tvHangoutDate;


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
        mContext = getContext();

        Collection<ParseUser> topMatches = MatchingUtils.getMatches();
        Iterator<ParseUser> topMatchesIter = topMatches.iterator();
        matchedUser = topMatchesIter.next();

        tvHangoutUser2 = view.findViewById(R.id.tvMatchedUser);
        tvMatchedPlace = view.findViewById(R.id.tvMatchedPlace);
        tvHangoutDate = view.findViewById(R.id.tvHangoutDate);

        while (matchedUser == null) {
            matchedUser = topMatchesIter.next();
        }

        MatchingUtils.getMatchDetails(matchedUser);

        tvHangoutUser2.setText(matchedUser.getUsername());
        tvMatchedPlace.setText("Place");

        setEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.slide_transition));
    }

}
