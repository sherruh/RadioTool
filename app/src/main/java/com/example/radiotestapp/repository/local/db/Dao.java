package com.example.radiotestapp.repository.local.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.radiotestapp.model.SettingsParameter;

@androidx.room.Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveSettingsParameter(SettingsParameter settingsParameter);

    @Query("SELECT * FROM settings_parameter WHERE name = :name")
    SettingsParameter getSettingParameter(String name);
}
