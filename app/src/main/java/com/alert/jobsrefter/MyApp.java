package com.alert.jobsrefter;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
     //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // Initialize the Mobile Ads SDK.


    }
}