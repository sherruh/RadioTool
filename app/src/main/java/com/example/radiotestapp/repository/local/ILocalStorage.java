package com.example.radiotestapp.repository.local;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.SettingsParameter;
import com.example.radiotestapp.repository.Callback;

import java.util.List;

public interface ILocalStorage {
    SettingsParameter getSettingsParameter(String name);
    Long saveSettingsParameter(SettingsParameter settingsParameter);
    void saveLogs(List<Log> logs, Callback<List<Long>> callback);
    void saveEvents(List<Event> events, Callback<List<Long>> callback);
    Log getLogById(Long id);
    List<Log> getLogsByLogId(String logId);
    List<Event> getEventsByLogId(String logId);
}