package com.example.friendly.fragments.match;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.example.friendly.query.HangoutQuery;
import com.example.friendly.utils.MatchingUtils;
import com.example.friendly.utils.NavigationUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchFragment extends Fragment {
    private static final String TAG = "MatchFragment";
    private static final String KEY_USER_FIRST_NAME = "String";
    private static Context mContext;

    private static Handler mHandler = new Handler();

    private Button btnQuickHangout;
    private Button btnLongHangout;

    private View dialogFeedback;

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
        dialogFeedback = view.findViewById(R.id.dialogFeedback);

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
        ArrayList<String> conditions = new ArrayList<>(Arrays.asList(getString(R.string.KEY_QUERY_FUTURE)));
        ft.add(R.id.upcomingHangouts, HangoutsFragment.newInstance(conditions));
        ft.commit();
    }

    public static void showFeedbackDialog(Hangout hangout) throws ParseException {

        LayoutInflater inflater = ((MainActivity) mContext).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.item_dialog_feedback,
                null);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mContext);
        dialogBuilder.setView(dialogLayout);
        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);

        TextView tvDialogUser = dialogLayout.findViewById(R.id.tvDialogUser);
        TextView tvDialogPlace = dialogLayout.findViewById(R.id.tvDialogPlace);
        ImageButton ibClose = (ImageButton) dialogLayout.findViewById(R.id.ibClose);
        ImageButton ibPositive = (ImageButton) dialogLayout.findViewById(R.id.ibPositive);
        ImageButton ibNegative = (ImageButton) dialogLayout.findViewById(R.id.ibNegative);

        ParseUser matchedUser = getOtherUser(hangout);
        tvDialogUser.setText(matchedUser.fetchIfNeeded().getString("firstName"));
        Place location = ((Place) hangout.fetchIfNeeded().getParseObject("location"));
        if (location != null) {
            String locationName = location.fetchIfNeeded().getString("name");
            if (locationName != null) {
                tvDialogPlace.setText(locationName);
            }
        }
        else{
            if (!hangout.getLocationName().isEmpty()){
                tvDialogPlace.setText(hangout.getLocationName());
            }
        }

        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);

        ibPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                try {
                    MatchingUtils.adjustWeightsPositive(matchedUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ibNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                try {
                    MatchingUtils.adjustWeightsNegative(matchedUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

//        Close dialog automatically after a few seconds
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.cancel();
            }}, 10000);
    }

    private static ParseUser getOtherUser(Hangout hangout) throws ParseException {
        // Set text to other user (not current user)
        if (hangout.getUser1().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            return hangout.fetchIfNeeded().getParseUser("user2");
        }
        return hangout.getUser1();
    }
}
