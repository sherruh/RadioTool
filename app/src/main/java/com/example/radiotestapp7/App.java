package com.example.radiotestapp7;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.radiotestapp7.repository.LogRepository;
import com.example.radiotestapp7.repository.local.ILocalStorage;
import com.example.radiotestapp7.repository.local.LocalStorage;
import com.example.radiotestapp7.repository.local.db.DataBase;
import com.example.radiotestapp7.repository.remote.RemoteRepository;

public class App extends Application {
    public static LogRepository logRepository;
    public static ILocalStorage localStorage;
    public static RemoteRepository remoteRepository;
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
        remoteRepository = new RemoteRepository();
    }
}
