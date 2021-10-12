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
    private long prevousRecievedBytesYouTube = 0;
    private long prevousRecievedBytesDownload = 0;
    private long prevousTransmittedBytes = 0;
    private long prevTimeYoutube = 0;
    private long prevTimeUpload = 0;
    private long prevTimeDownload = 0;

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
            if (App.logRepository.getLogState() == EState.YOUTUBE_TEST ) App.logRepository.setYoutubeThroughput(getYouTubeThroughput());
            if (App.logRepository.getLogState() == EState.UPLOAD_TEST) App.logRepository.setUlThroughput(getUlThroughput());
            if (App.logRepository.getLogState() == EState.DOWNLOAD_TEST) App.logRepository.setDownloadTestThroughput(getDLThroughput());
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
        if (prevousRecievedBytesYouTube == 0 || System.currentTimeMillis() - prevTimeYoutube > 700){
            prevousRecievedBytesYouTube = TrafficStats.getTotalRxBytes();
            prevTimeYoutube = System.currentTimeMillis();
            return 0;
        }
        long downSpeed = ((TrafficStats.getTotalRxBytes() - prevousRecievedBytesYouTube) / 1024 * 8) * 2;
        prevousRecievedBytesYouTube = TrafficStats.getTotalRxBytes();
        prevTimeYoutube = System.currentTimeMillis();
        resetRecievedBytesDownload();
        resetTransmittedBytesUpload();
        return downSpeed;
    }

    private long getUlThroughput(){
        if (prevousTransmittedBytes == 0 || System.currentTimeMillis() - prevTimeUpload > 700){
            prevousTransmittedBytes = TrafficStats.getTotalTxBytes();
            prevTimeUpload = System.currentTimeMillis();
            return 0;
        }
        long upSpeed = ((TrafficStats.getTotalTxBytes() - prevousTransmittedBytes) / 1024 * 8) * 2;
        prevousTransmittedBytes = TrafficStats.getTotalTxBytes();
        prevTimeUpload = System.currentTimeMillis();
        resetRecievedBytesYoutube();
        resetRecievedBytesDownload();
        return upSpeed;
    }

    private long getDLThroughput(){
        if (prevousRecievedBytesDownload == 0 || System.currentTimeMillis() - prevTimeDownload > 700){
            prevousRecievedBytesDownload = TrafficStats.getTotalRxBytes();
            prevTimeDownload = System.currentTimeMillis();
            return 0;
        }
        long downSpeed = ((TrafficStats.getTotalRxBytes() - prevousRecievedBytesDownload) / 1024 * 8) * 2;
        prevousRecievedBytesDownload = TrafficStats.getTotalRxBytes();
        prevTimeDownload = System.currentTimeMillis();
        resetRecievedBytesYoutube();
        resetTransmittedBytesUpload();
        return downSpeed;
    }

    private void resetRecievedBytesDownload(){
        prevousRecievedBytesDownload = 0;
    }

    private void resetRecievedBytesYoutube(){
        prevousRecievedBytesYouTube = 0;
    }

    private void resetTransmittedBytesUpload(){
        prevousTransmittedBytes = 0;
    }

    public List<Log> getLogs() {
        return mLogs;
    }
}
