package com.example.radiotestapp.repository.local.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.LogResult;
import com.example.radiotestapp.model.SettingsParameter;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveSettingsParameter(SettingsParameter settingsParameter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveLogResult(LogResult logResult);

    @Query("SELECT * FROM log_result WHERE id = :id")
    LogResult getLogResultById(String id);

    @Query("SELECT * FROM settings_parameter WHERE name = :name")
    SettingsParameter getSettingParameter(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> saveLogs(List<Log> logs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveLog(Log logs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> saveEvents(List<Event> events);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveEvent(Event event);

    @Query("SELECT * FROM log WHERE id = :id")
    Log getLogById(Long id);

    @Query("SELECT * FROM log WHERE logId = :logId")
    List<Log> getLogsByLogId(String logId);

    @Query("SELECT * FROM event WHERE logId = :logId")
    List<Event> getEventsByLogId(String logId);

    @Query("SELECT * FROM log WHERE isUploaded = 0")
    List<Log> getUnUploadedLogs();

    @Query("SELECT * FROM event WHERE isUploaded = 0")
    List<Event> getUnUploadedEvents();

    @Query("UPDATE log SET isUploaded = 1  WHERE id = :id")
    void setLogUploaded(long id);

    @Query("UPDATE event SET isUploaded = 1  WHERE id = :id")
    void setEventUploaded(long id);

    @Query("SELECT * FROM log_result WHERE isUploaded = 0")
    List<LogResult> getUnUploadedLogResults();

    @Query("UPDATE log_result SET isUploaded = 1  WHERE id = :id")
    void setLogResultUploaded(long id);
}
