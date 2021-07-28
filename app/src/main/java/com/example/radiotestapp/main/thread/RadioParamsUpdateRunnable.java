package com.example.radiotestapp.main.thread;

public class RadioParamsUpdateRunnable implements Runnable {

    private volatile boolean isRunning = true;

    @Override
    public void run() {
        while(isRunning) {
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
