package com.example.radiotestapp.main.thread;

import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.util.List;

public class LoggerRunnable implements Runnable {

    private volatile boolean isRunning = true;

    private List<Log> mLogs;
    private Log mLog;

    public void stopLog(){
        isRunning = false;
    }

    public LoggerRunnable(List<Log> logs, Log log){
        mLogs = logs;
        mLogs.add(log);
        mLog = log;
    }

    public void setLog(Log log){
        mLog = log;
    }

    @Override
    public void run() {
        while (isRunning){
            mLogs.add(mLog);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Log> getLogs() {
        return mLogs;
    }
}
