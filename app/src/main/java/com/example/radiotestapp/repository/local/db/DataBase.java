package com.example.radiotestapp.repository.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.SettingsParameter;

@Database(entities = {SettingsParameter.class, Log.class, Event.class}, exportSchema = true, version = 5)
public abstract class DataBase extends RoomDatabase {
    public abstract Dao dao();
}
