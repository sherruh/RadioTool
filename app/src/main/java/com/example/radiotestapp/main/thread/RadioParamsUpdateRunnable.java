package com.example.radiotestapp.main.thread;

import com.example.radiotestapp.App;

public class RadioParamsUpdateRunnable implements Runnable {

    private volatile boolean isRunning = true;

    @Override
    public void run() {
        while(isRunning) {
            App.logRepository.addToLevelList(App.logRepository.levelLiveData.getValue());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        isRunning = false;
    }
}
