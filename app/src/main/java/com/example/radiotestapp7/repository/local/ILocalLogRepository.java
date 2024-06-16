package com.example.radiotestapp7.repository.local;

import com.example.radiotestapp7.model.Event;
import com.example.radiotestapp7.model.Log;

public interface ILocalLogRepository {
    void saveEvent(Event event);
    void saveLog(Log log);
    void createLogFile(String logId);
    void closeLogFile();
}
