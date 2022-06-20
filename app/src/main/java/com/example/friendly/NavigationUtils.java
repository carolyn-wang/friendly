package com.example.friendly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendly.fragments.HangoutHistoryFragment;
import com.parse.ParseUser;

public class NavigationUtils {

    public static void goLoginActivity(Activity activity){
        Context context = activity.getBaseContext();
        Intent i = new Intent(context, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void goMainActivity(Activity activity) {
        Context context = activity.getBaseContext();
        Intent i = new Intent(context, MainActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void goSignupActivity(Activity activity) {
        Context context = activity.getBaseContext();
        Intent i = new Intent(context, SignUpActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void displayFragmentHangoutHistory(ParseUser user, FragmentManager fragmentManager){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment hangoutHistoryFragment = new HangoutHistoryFragment().newInstance(user);
        ft.replace(R.id.flContainer, hangoutHistoryFragment);
        ft.commit();
    }
}
