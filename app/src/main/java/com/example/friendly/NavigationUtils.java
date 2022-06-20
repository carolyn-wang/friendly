package com.example.friendly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.friendly.activities.LoginActivity;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.activities.SignUpActivity;
import com.example.friendly.fragments.HangoutHistoryFragment;
import com.example.friendly.fragments.match.LongMatchFragment;
import com.example.friendly.fragments.match.QuickMatchFragment;
import com.parse.ParseUser;

public class NavigationUtils {

    public static void goActivity(Activity activity, Class targetClass){
        Context context = activity.getBaseContext();
        Intent i = new Intent(context, targetClass);
        activity.startActivity(i);
        activity.finish();
    }

    public static void goLoginActivity(Activity activity){
        goActivity(activity, LoginActivity.class);

    }

    public static void goMainActivity(Activity activity) {
        goActivity(activity, MainActivity.class);
    }

    public static void goSignupActivity(Activity activity) {
        goActivity(activity, SignUpActivity.class);
    }
    public static void displayFragmentQuickMatch(ParseUser user, FragmentManager fragmentManager){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment quickMatchFragment = new QuickMatchFragment();
        ft.replace(R.id.flContainer, quickMatchFragment);
        ft.commit();
    }

    public static void displayFragmentLongMatch(ParseUser currentUser, FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment longMatchFragment = new LongMatchFragment();
        ft.replace(R.id.flContainer, longMatchFragment);
        ft.commit();
    }

    public static void displayFragmentHangoutHistory(ParseUser user, FragmentManager fragmentManager){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment hangoutHistoryFragment = new HangoutHistoryFragment().newInstance(user);
        ft.replace(R.id.flContainer, hangoutHistoryFragment);
        ft.commit();
    }
}
