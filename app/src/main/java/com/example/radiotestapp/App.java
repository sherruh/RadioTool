package com.example.radiotestapp;

import android.app.Application;

import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.repository.LogRepository;

public class App extends Application {
    public static LogRepository logRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        logRepository = new LogRepository();
    }
}
