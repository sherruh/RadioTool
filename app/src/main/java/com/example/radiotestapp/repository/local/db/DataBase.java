package com.example.radiotestapp.repository.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.radiotestapp.model.SettingsParameter;

@Database(entities = {SettingsParameter.class}, exportSchema = true, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract Dao dao();
}
