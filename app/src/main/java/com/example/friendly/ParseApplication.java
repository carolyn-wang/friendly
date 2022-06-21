package com.example.friendly;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Hangout.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("IDsIeYawTbR7j0y43iGILuUWJgAM1muaNGzSPWw8")
                .clientKey("3bk3ZJ7qiVoOWjd0y72cKCmWAMGLz6RSfIgXMJm8")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
