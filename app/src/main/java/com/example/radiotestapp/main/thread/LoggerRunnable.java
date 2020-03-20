package com.example.radiotestapp.main.thread;

import com.example.radiotestapp.model.Log;
import com.example.radiotestapp.utils.Logger;

import java.util.List;

public class LoggerRunnable implements Runnable {

    private volatile boolean isRunning = true;

    public void stopLog(){
        isRunning = false;
    }

    public LoggerRunnable(List<Log> logs){

    }

    @Override
    public void run() {
        while (isRunning){
            Logger.d("Loger Thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
