package com.example.radiotestapp.repository.local;

import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.model.LogResult;
import com.example.radiotestapp.model.SettingsParameter;
import com.example.radiotestapp.repository.Callback;
import com.example.radiotestapp.repository.local.db.Dao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalStorage implements ILocalStorage {

    private Dao dao;

    public LocalStorage(Dao dao){
        this.dao = dao;
    }

    @Override
    public SettingsParameter getSettingsParameter(String name) {
         return dao.getSettingParameter(name);
    }

    @Override
    public Long saveSettingsParameter(SettingsParameter settingsParameter) {
        return dao.saveSettingsParameter(settingsParameter);
    }

    @Override
    public void saveLogs(List<Log> logs, Callback<List<Long>> callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Long> l = dao.saveLogs(logs);
            if (l.size() > 0) callback.onSuccess(l);
            else callback.onFailure("Didn't save to database");
        });
    }

    @Override
    public void saveEvents(List<Event> events, Callback<List<Long>> callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Long> l = dao.saveEvents(events);
            if (l.size() > 0) callback.onSuccess(l);
            else callback.onFailure("Didn't save events");
        });
    }

    @Override
    public Log getLogById(Long id) {
        return dao.getLogById(id);
    }

    @Override
    public List<Log> getLogsByLogId(String logId) {
        return dao.getLogsByLogId(logId);
    }

    @Override
    public List<Event> getEventsByLogId(String logId) {
        return dao.getEventsByLogId(logId);
    }

    @Override
    public Long saveLogResult(LogResult logResult) {
        return dao.saveLogResult(logResult);
    }

    @Override
    public LogResult getLogResultById(String id) {
        return dao.getLogResultById(id);
    }

    @Override
    public void getUnUploadedLogs(Callback<List<Log>> callback) {
        callback.onSuccess(dao.getUnUploadedLogs());
    }

    @Override
    public void getUnUploadedEvents(Callback<List<Event>> callback) {
        callback.onSuccess(dao.getUnUploadedEvents());
    }
}
