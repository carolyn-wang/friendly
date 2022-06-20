package com.example.friendly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
}
