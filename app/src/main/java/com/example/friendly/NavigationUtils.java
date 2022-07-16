package com.example.friendly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.example.friendly.activities.LoginActivity;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.activities.PreferencesActivity;
import com.example.friendly.activities.SignUpActivity;
import com.example.friendly.fragments.HangoutDetailFragment;
import com.example.friendly.fragments.HangoutHistoryFragment;
import com.example.friendly.fragments.match.QuickMatchDetailFragment;
import com.example.friendly.fragments.match.LongMatchFragment;
import com.example.friendly.fragments.match.QuickMatchFragment;
import com.example.friendly.objects.Hangout;
import com.google.android.material.transition.MaterialContainerTransform;

public class NavigationUtils {

    public static void goActivity(Activity activity, Class targetClass) {
        Context context = activity.getBaseContext();
        Intent i = new Intent(context, targetClass);
        activity.startActivity(i);
        activity.finish();
    }

    public static void goLoginActivity(Activity activity) {
        goActivity(activity, LoginActivity.class);
    }

    public static void goMainActivity(Activity activity) {
        goActivity(activity, MainActivity.class);
    }

    public static void goSignupActivity(Activity activity) {
        goActivity(activity, SignUpActivity.class);
    }

    public static void goPreferencesActivity(Activity activity) {
        goActivity(activity, PreferencesActivity.class);
    }

    public static void displayFragmentQuickMatch(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flContainer, new QuickMatchFragment())
                .addToBackStack(null)
                .commit();
    }

    public static void displayFragmentLongMatch(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flContainer, new LongMatchFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Show Hangout Detail fragment with sliding transition
     *
     * @param hangout
     * @param fragmentManager
     */
    public static void displayFragmentHangoutDetail(Hangout hangout, FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment hangoutDetailFragment = HangoutDetailFragment.newInstance(hangout);
        ft.replace(R.id.flContainer, hangoutDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Show Hangout Detail fragment for setting up quick match
     *
     * @param hangout
     * @param fragmentManager
     */
    public static void displayFragmentQuickMatchDetail(Context mContext, View view, Hangout hangout, FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment hangoutDetailFragment = QuickMatchDetailFragment.newInstance(hangout);
        setCardToDetailTransition(mContext, view, DisplayUtils.getCardColor(mContext, hangout), hangoutDetailFragment);
        ft.replace(R.id.flContainer, hangoutDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private static void setCardToDetailTransition(Context mContext, View view, int containerColor, Fragment fragment) {
        MaterialContainerTransform enterTransition = new MaterialContainerTransform();
        enterTransition.setStartView(view);
        enterTransition.setEndView(((MainActivity) mContext).findViewById(R.id.vHangoutDetail));
        enterTransition.setDuration(700);
        enterTransition.setScrimColor(Color.TRANSPARENT);
        enterTransition.setContainerColor(containerColor);
        fragment.setEnterTransition(enterTransition);
    }

    public static void displayFragmentHangoutHistory(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment hangoutDetailFragment = new HangoutHistoryFragment();
        ft.replace(R.id.flContainer, hangoutDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void onBackPressed(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }
}
