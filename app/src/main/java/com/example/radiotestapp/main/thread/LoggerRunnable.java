package com.example.radiotestapp.main.thread;

import com.example.radiotestapp.App;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.util.List;

public class LoggerRunnable implements Runnable {

    private volatile boolean isRunning = true;

    private List<Log> mLogs;

    public void stopLog(){
        isRunning = false;
    }

    public LoggerRunnable(List<Log> logs, Log log){
        mLogs = logs;
        mLogs.add(log);
        App.logRepository.setLog(log);
    }

    public void setLog(Log log){
        App.logRepository.setLog(log);
    }

    @Override
    public void run() {
        while (isRunning){
            App.logRepository.setDate(System.currentTimeMillis());
            mLogs.add(App.logRepository.getLog());
            Logger.d(" Thread Log " + App.logRepository.getLog().getRscp() + " CELLID " + App.logRepository.getLog().getCellId() + " Event " + App.logRepository.getLog().geteEvent());
            App.logRepository.clearLastEvent();
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
