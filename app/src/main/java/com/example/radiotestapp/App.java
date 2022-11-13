package com.example.radiotestapp;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.radiotestapp.repository.LogRepository;
import com.example.radiotestapp.repository.local.ILocalStorage;
import com.example.radiotestapp.repository.local.LocalStorage;
import com.example.radiotestapp.repository.local.db.DataBase;
import com.example.radiotestapp.repository.remote.ApiClient;
import com.example.radiotestapp.repository.remote.IApiClient;

public class App extends Application {
    public static LogRepository logRepository;
    public static ILocalStorage localStorage;
    public static IApiClient apiClient;
    public static Context context;
    public static DataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        logRepository = new LogRepository();
        dataBase = Room
                .databaseBuilder(this, DataBase.class,"app_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        localStorage = new LocalStorage(dataBase.dao());
        apiClient = new ApiClient();
    }
}
