package com.example.friendly.fragments.match;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.objects.Hangout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchFragment extends Fragment {
    private static final String TAG = "MatchFragment";
    private Context mContext;

    private Button btnQuickHangout;
    private Button btnLongHangout;
    private Button btnMessage;

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
        ArrayList<String> conditions = new ArrayList<>(Arrays.asList(getString(R.string.query_key_future), getString(R.string.query_key_current_user)));
        ft.add(R.id.upcomingHangouts, HangoutsFragment.newInstance(conditions));
        ft.commit();

    }

    private void showFeedbackDialog(Hangout hangout) {
        LayoutInflater inflater = ((MainActivity) mContext).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.item_dialog_feedback,
                null);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mContext);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialogLayout);
        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);

        TextView tvDialogUser1 = dialogLayout.findViewById(R.id.tvDialogUser1);
        TextView tvDialogUser2 = dialogLayout.findViewById(R.id.tvDialogUser2);
        TextView tvDialogPlace = dialogLayout.findViewById(R.id.tvDialogPlace);
        ImageButton ibClose = (ImageButton) dialogLayout.findViewById(R.id.ibClose);
        ImageButton ibPositive = (ImageButton) dialogLayout.findViewById(R.id.ibPositive);
        ImageButton ibNeutral = (ImageButton) dialogLayout.findViewById(R.id.ibNegative);

        tvDialogUser1.setText(hangout.getUser1().getString(getString(R.string.KEY_USER_FIRST_NAME)));
        tvDialogUser2.setText(hangout.getUser2().getString(getString(R.string.KEY_USER_FIRST_NAME)));
        tvDialogPlace.setText(hangout.getLocationName());


        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);

        ibPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });


        ibNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
}
