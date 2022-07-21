package com.example.friendly.fragments.match;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.friendly.utils.MatchingUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.fragments.HangoutDetailFragment;
import com.example.friendly.objects.Hangout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickMatchDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickMatchDetailFragment extends Fragment {
    private static Context mContext;
    private static final String KEY_HANGOUT = "hangout";
    private Hangout hangout;
    private Button btnJoinQuickHangout;

    public QuickMatchDetailFragment() {
    }

    public static QuickMatchDetailFragment newInstance(Hangout hangout) {
        QuickMatchDetailFragment fragment = new QuickMatchDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_HANGOUT, hangout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hangout = getArguments().getParcelable(KEY_HANGOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_match_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        btnJoinQuickHangout = view.findViewById(R.id.btnJoinQuickHangout);

        btnJoinQuickHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangout.setUser2(ParseUser.getCurrentUser());
                hangout.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // TODO: move to navUtils
                        ((MainActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new MatchFragment()).commit();
                        try {
                            MatchingUtils.adjustWeights(hangout.getUser1());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment hangoutDetailFragment = HangoutDetailFragment.newInstance(hangout);
        ft.add(R.id.hangoutDetail, hangoutDetailFragment).commit();

        setEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.slide_transition));
    }
}