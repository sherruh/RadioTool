package com.example.radiotestapp.repository.local;

import com.example.radiotestapp.enums.EEvents;
import com.example.radiotestapp.model.Event;
import com.example.radiotestapp.model.Log;

public interface ILocalLogRepository {
    void saveEvent(Event event);
    void saveLog(Log log);
    void createLogFile(String logId);
    void closeLogFile();
}
