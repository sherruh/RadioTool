package com.example.radiotestapp;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.radiotestapp.repository.LogRepository;
import com.example.radiotestapp.repository.local.ILocalStorage;
import com.example.radiotestapp.repository.local.LocalStorage;
import com.example.radiotestapp.repository.local.db.DataBase;

public class App extends Application {
    public static LogRepository logRepository;
    public static ILocalStorage localStorage;
    public static Context context;
    public static DataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        logRepository = new LogRepository();
        context = getApplicationContext();
        dataBase = Room
                .databaseBuilder(this, DataBase.class,"app_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        localStorage = new LocalStorage(dataBase.dao());
    }
}
