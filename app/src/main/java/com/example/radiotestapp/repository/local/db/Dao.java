package com.example.radiotestapp.repository.local.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.SettingsParameter;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveSettingsParameter(SettingsParameter settingsParameter);

    @Query("SELECT * FROM settings_parameter WHERE name = :name")
    SettingsParameter getSettingParameter(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> saveLogs(List<Log> logs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> saveEvents(List<Event> events);

    @Query("SELECT * FROM log WHERE id = :id")
    Log getLogById(Long id);

    @Query("SELECT * FROM log WHERE logId = :logId")
    List<Log> getLogsByLogId(String logId);

    @Query("SELECT * FROM event WHERE logId = :logId")
    List<Event> getEventsByLogId(String logId);
}
