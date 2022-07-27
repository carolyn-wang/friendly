package com.example.friendly.fragments.match;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.friendly.utils.DisplayUtils;
import com.example.friendly.utils.MatchingUtils;
import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LongMatchFragment extends Fragment {
    private static final String TAG = "LongMatchFragment";
    private static final int INDEX_PLACE = 0;
    private Context mContext;

    private static final String KEY_MATCHED_USER = "matchedUser";

    private Hangout hangout;
    private ParseUser matchedUser;
    private TextView tvHangoutUser2;
    private TextView tvMatchedPlace;
    private TextView tvHangoutDate;
    private TextView tvDate1;
    private TextView tvDate2;
    private TextView tvDate3;
    private CardView cdCoupon;
    private Button btnCreateHangout;

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
        tvDate1 = view.findViewById(R.id.tvDate1);
        tvDate2 = view.findViewById(R.id.tvDate2);
        tvDate3 = view.findViewById(R.id.tvDate3);
        cdCoupon = view.findViewById(R.id.cdCoupon);
        btnCreateHangout = view.findViewById(R.id.btnCreateHangoutAtPlace);

        while (matchedUser == null) {
            matchedUser = topMatchesIter.next();
        }

        tvHangoutUser2.setText(matchedUser.getUsername());

        List<Place> placeList = ((MainActivity) mContext).getPlaceList();
        List<Object> matchDetails = MatchingUtils.getMatchDetails(mContext, matchedUser, placeList);
        Place matchPlace = (Place) matchDetails.get(INDEX_PLACE);

        cdCoupon.setBackgroundColor(DisplayUtils.getCardColor(mContext, matchPlace));

        tvMatchedPlace.setText(matchPlace.getName());
        tvDate1.setText((String) matchDetails.get(1));
        tvDate2.setText((String) matchDetails.get(2));
        tvDate3.setText((String) matchDetails.get(3));

        setEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.slide_transition));

        btnCreateHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHangout(matchedUser, matchPlace);
            }
        });
    }

    //TODO: add checks here (cannot create hangout in past)
    private void createHangout(ParseUser user2, Place place) {
        Hangout hangout = new Hangout();
        hangout.setUser1(ParseUser.getCurrentUser());
        hangout.setUser2(user2);
        hangout.setLocation(place);
        hangout.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(mContext, ((MainActivity) mContext).getResources().getString(R.string.create_hangout_error), Toast.LENGTH_LONG).show();
                    Log.i(TAG, e.getMessage());
                } else {
                    NavigationUtils.goMainActivity(getActivity());
                }
            }
        });

    }
}
