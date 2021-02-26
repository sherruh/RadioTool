package com.example.radiotestapp;

import android.app.Application;
import android.content.Context;

import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.repository.LogRepository;

public class App extends Application {
    public static LogRepository logRepository;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        logRepository = new LogRepository();
        context = getApplicationContext();
    }
}
