package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friendly.DisplayUtils;
import com.example.friendly.R;
import com.example.friendly.objects.Hangout;
import com.google.android.material.transition.MaterialContainerTransform;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HangoutDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HangoutDetailFragment extends Fragment {

    private static final String KEY_HANGOUT = "hangout";
    private Context mContext;

    private Hangout hangout;
    private View vHangoutDetail;
    private TextView tvHangoutUser1;
    private TextView tvHangoutUser2;
    private TextView tvHangoutDate;

    public HangoutDetailFragment() {
        // Required empty public constructor
    }

    public static HangoutDetailFragment newInstance(Hangout hangout) {
        HangoutDetailFragment fragment = new HangoutDetailFragment();
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
        return inflater.inflate(R.layout.fragment_hangout_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        vHangoutDetail = view.findViewById(R.id.vHangoutDetail);
        tvHangoutUser1 = view.findViewById(R.id.tvHangoutUser1);
        tvHangoutUser2 = view.findViewById(R.id.tvHangoutUser2);
        tvHangoutDate = view.findViewById(R.id.tvHangoutDate);

        vHangoutDetail.setBackgroundColor(DisplayUtils.getCardColor(mContext, hangout));

        tvHangoutUser1.setText(hangout.getUser1().getUsername());
        if (hangout.getUser2() != null){
            tvHangoutUser2.setText(hangout.getUser2().getUsername());
        }
        else
        {
            tvHangoutUser2.setText("");
        }
        tvHangoutDate.setText(hangout.getDate().toString());
        
        setEnterTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_expand_transition));
    }
}