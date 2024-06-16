package com.example.radiotestapp7.repository.local;

import com.example.radiotestapp7.model.Event;
import com.example.radiotestapp7.model.Log;
import com.example.radiotestapp7.model.LogResult;
import com.example.radiotestapp7.model.SettingsParameter;
import com.example.radiotestapp7.repository.Callback;

import java.util.List;

public interface ILocalStorage {
    SettingsParameter getSettingsParameter(String name);
    Long saveSettingsParameter(SettingsParameter settingsParameter);
    void saveLogs(List<Log> logs, Callback<List<Long>> callback);
    void saveEvents(List<Event> events, Callback<List<Long>> callback);
    Log getLogById(Long id);
    List<Log> getLogsByLogId(String logId);
    List<Event> getEventsByLogId(String logId);
    Long saveLogResult(LogResult logResult);
    LogResult getLogResultById(String id);
}
