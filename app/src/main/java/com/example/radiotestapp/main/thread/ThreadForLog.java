package com.example.radiotestapp.main.thread;

import com.example.radiotestapp.utils.Logger;

public class ThreadForLog implements Runnable {

    private volatile boolean isRunning = true;

    public void finishThread(){
        isRunning = false;
    }

    public ThreadForLog(String object){

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
