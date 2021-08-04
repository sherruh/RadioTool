package com.example.radiotestapp.main.thread;

import android.content.Context;
import android.net.TrafficStats;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.EState;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.util.List;

public class LoggerRunnable implements Runnable {

    private volatile boolean isRunning = true;
    private List<Log> mLogs;
    private Context mContext;
    private long prevousRecievedBytes = 0;
    private long prevousTransmittedBytes = 0;
    private long prevTime = 0;

    public void stopLog(){
        isRunning = false;
    }

    public LoggerRunnable(List<Log> logs, Log log, Context context){
        mLogs = logs;
        mLogs.add(log);
        mContext = context;
        App.logRepository.setLog(log);
    }

    @Override
    public void run() {
        while (isRunning){
            if (App.logRepository.getLogState() == EState.YOUTUBE_TEST) App.logRepository.setDlThroughput(getYouTubeThroughput());
            if (App.logRepository.getLogState() == EState.UPLOAD_TEST) App.logRepository.setUlThroughput(getUlThroughput());
            App.logRepository.setDate(System.currentTimeMillis());
            Logger.d("Logging " + App.logRepository.getLog());
            mLogs.add(App.logRepository.getLog());
            App.logRepository.saveLog(App.logRepository.getLog());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getYouTubeThroughput(){
        if (prevousRecievedBytes == 0 || System.currentTimeMillis() - prevTime > 700){
            prevousRecievedBytes = TrafficStats.getTotalRxBytes();
            prevTime = System.currentTimeMillis();
            return 0;
        }
        long downSpeed = ((TrafficStats.getTotalRxBytes() - prevousRecievedBytes) / 1024 * 8) * 2;
        prevousRecievedBytes = TrafficStats.getTotalRxBytes();
        prevTime = System.currentTimeMillis();
        return downSpeed;
    }

    private long getUlThroughput(){
        if (prevousTransmittedBytes == 0 || System.currentTimeMillis() - prevTime > 700){
            prevousTransmittedBytes = TrafficStats.getTotalTxBytes();
            prevTime = System.currentTimeMillis();
            return 0;
        }
        long upSpeed = ((TrafficStats.getTotalTxBytes() - prevousTransmittedBytes) / 1024 * 8) * 2;
        prevousTransmittedBytes = TrafficStats.getTotalTxBytes();
        prevTime = System.currentTimeMillis();
        return upSpeed;
    }


    public List<Log> getLogs() {
        return mLogs;
    }
}
