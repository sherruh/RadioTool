package com.example.radiotestapp.download_test;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.example.radiotestapp.App;
import com.example.radiotestapp.core.Constants;
import com.example.radiotestapp.model.SettingsParameter;
import com.example.radiotestapp.utils.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class Downloader {

    private long downloadDuration = 30000L;
    private Context mContext;
    private DownloadListener downloadListener;
    private Timer timerForDuration;
    private DownloadManager manager;
    private String url;
    private BroadcastReceiver downloadReceiver;
    private boolean isNeedStart = true;

    public void download(String url, Context context, DownloadListener downloadListener ) {
        if (downloadReceiver != null) mContext.unregisterReceiver(downloadReceiver);
        mContext = context;
        timerForDuration = null;
        this.downloadListener = downloadListener;
        manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        this.url = url;
        if (!url.contains("http://") && !url.contains("https://")) {
            downloadListener.onFailure("Error url");
            isNeedStart = false;
            return;
        }
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        long downloadId = manager.enqueue(request);
        Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(downloadId));
        downloadDuration = getDownloadDuration();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int status = -1;

                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor c = manager.query(q);
                int downloadedBytes = 0;
                if (c.moveToFirst()) {
                   downloadedBytes = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                }
                if (downloadedBytes == 0) {
                    if (timerForDuration != null) timerForDuration.cancel();
                    downloadListener.onFailure("Time out");
                    isNeedStart = false;
                    timer.cancel();
                } else {
                    startTimerForDownloading(timer.purge());
                    registrOnCompleteReceiver(downloadId);
                }
                timer.cancel();
            }
        }, downloadDuration);
    }

    private void startTimerForDownloading(int purge) {
        if (timerForDuration != null) return;
        timerForDuration = new Timer();
        timerForDuration.schedule(new TimerTask() {
            @Override
            public void run() {
                downloadListener.onComplete();
                isNeedStart = false;
                timerForDuration.cancel();
            }
        }, purge);
    }

    private long getDownloadDuration() {
        long l = 30000L;
        SettingsParameter downloadDurationSettings = App.localStorage.getSettingsParameter(Constants.DOWNLOAD_DURATION);
        if (downloadDurationSettings != null){
            try{
                l = Long.parseLong(downloadDurationSettings.getValue()) * 1000L;
            }catch (Exception e){}
        }
        return l;
    }

    private void registrOnCompleteReceiver(long downloadId ){
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (downloadId == -1)
                    return;

                Cursor cursor = manager.query(new DownloadManager.Query().setFilterById(downloadId));
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if(status == DownloadManager.STATUS_SUCCESSFUL){
                        if (isNeedStart)download(url,mContext,downloadListener);
                    }
                    else {
                        // download is cancelled
                    }
                }
                else {
                    // download is cancelled
                }
            }
        };

        mContext.registerReceiver(downloadReceiver, filter);
    }

    public void stopDownload(){
        if (timerForDuration != null) timerForDuration.cancel();
        isNeedStart = false;
    }


    public interface DownloadListener{
        void onFailure(String message);
        void onComplete();
    }

}
