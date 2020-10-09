package com.example.radiotestapp.main.thread;

import android.net.ConnectivityManager;

import com.example.radiotestapp.App;
import com.example.radiotestapp.enums.ESTATE;
import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import android.net.TrafficStats;

import android.content.Context;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.util.List;

public class LoggerRunnable implements Runnable {

    private volatile boolean isRunning = true;
    private List<Log> mLogs;
    private Context mContext;
    private long prevousRecievedBytes = 0;
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

    public void setLog(Log log){
        App.logRepository.setLog(log);
    }

    @Override
    public void run() {
        while (isRunning){
            if (App.logRepository.getLogState() == ESTATE.YOUTUBE_TEST) App.logRepository.setDlThroughput(getYouTubeThroughput());
            App.logRepository.setDate(System.currentTimeMillis());
            mLogs.add(App.logRepository.getLog());
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
        long downSpeed = ((TrafficStats.getTotalRxBytes() - prevousRecievedBytes) / 1000 * 8) * 2;
        Logger.d("YoutubeThroughput " + downSpeed  + " BYTES " + TrafficStats.getTotalRxBytes()  + " PREV " + prevousRecievedBytes);
        prevousRecievedBytes = TrafficStats.getTotalRxBytes();
        prevTime = System.currentTimeMillis();
        return downSpeed;
    }


    public List<Log> getLogs() {
        return mLogs;
    }
}
